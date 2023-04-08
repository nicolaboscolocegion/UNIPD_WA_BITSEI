package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.InvoiceProduct;
import it.unipd.dei.bitsei.resources.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


/**
 * Searches employees by their salary.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CloseInvoiceDAO extends AbstractDAO<List<Object>> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT_UPDATE = "UPDATE bitsei_schema.\"Invoice\" SET status = ? WHERE invoice_id = ?;";
    private static final String STATEMENT_SELECT_INVOICE = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";
    private static final String STATEMENT_SELECT_CUSTOMER = "SELECT * FROM bitsei_schema.\"Customer\" WHERE customer_id = ?;";
    private static final String STATEMENT_SELECT_INVOICE_PRODUCT = "SELECT * FROM bitsei_schema.\"Invoice_Product\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Product\".product_id = bitsei_schema.\"Invoice_Product\".product_id WHERE bitsei_schema.\"Invoice_Product\".invoice_id = ?;";

    private int invoice_id;

    private Customer c;
    private Invoice i;
    private List<Product> lp = new ArrayList<>();
    private List<InvoiceProduct> lip = new ArrayList<>();
    private List<Object> output = new ArrayList<>();

    /**
     * Closes the invoice.
     *
     * @param con
     *        the connection to the database.
     * @param invoice_id
     *        the id of the invoice to be closed.
     */
    public CloseInvoiceDAO(final Connection con, int invoice_id) {
        super(con);
        this.invoice_id = invoice_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search

        try {
            pstmt = con.prepareStatement(STATEMENT_UPDATE);
            pstmt.setInt(1, 1);
            pstmt.setInt(2, this.invoice_id);
            rs = pstmt.executeQuery();
            LOGGER.info("Invoice status successfully set to 1.");

            pstmt = con.prepareStatement(STATEMENT_SELECT_INVOICE);
            pstmt.setInt(1, this.invoice_id);
            rs = pstmt.executeQuery();
            int customer_id = 0;
            while (rs.next()) {
                i = new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("status"),
                        rs.getInt("warning_number"),
                        rs.getDate("warning_date"),
                        rs.getString("warning_pdf_file"),
                        rs.getString("invoice_number"),
                        rs.getDate("invoice_date"),
                        rs.getString("invoice_pdf_file"),
                        rs.getString("invoice_xml_file"),
                        rs.getDouble("total"),
                        rs.getDouble("discount"),
                        rs.getDouble("pension_fund_refund"),
                        rs.getBoolean("has_stamp")
                );

                customer_id =  rs.getInt("customer_id");
            }
            LOGGER.info("Invoice data successfully fetched.");


            pstmt = con.prepareStatement(STATEMENT_SELECT_CUSTOMER);
            pstmt.setInt(1, customer_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("business_name"),
                        rs.getString("vat_number"),
                        rs.getString("tax_code"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("province"),
                        rs.getString("postal_code"),
                        rs.getString("email"),
                        rs.getString("pec"),
                        rs.getString("unique_code"),
                        rs.getInt("company_id")
                        );
            }
            LOGGER.info("Customer data successfully fetched.");

            pstmt = con.prepareStatement(STATEMENT_SELECT_INVOICE_PRODUCT);
            pstmt.setInt(1, this.invoice_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lp.add(new Product(rs.getInt("product_id"), rs.getInt("company_id"), rs.getString("title"), rs.getInt("default_price"), rs.getString("logo"), rs.getString("measurement_unit"), rs.getString("description")));
                lip.add(new InvoiceProduct(rs.getInt("invoice_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getFloat("unit_price"), rs.getFloat("related_price"), rs.getString("related_price_description")));
            }


            output.add(c);
            output.add(i);
            output.add(lip);
            output.add(lp);

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = output;
    }
}