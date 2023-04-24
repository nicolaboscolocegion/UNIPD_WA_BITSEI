package it.unipd.dei.bitsei.dao.invoiceproduct;


import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Updates an invoice product present in the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateInvoiceProductDAO extends AbstractDAO {

    private static final String FETCH_INVOICE = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";
    private static final String FETCH = "SELECT * FROM bitsei_schema.\"InvoiceProduct\" WHERE invoice_id = ? AND product_id;";
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Product\".company_id INNER JOIN bitsei_schema.\"Invoice_Product\" ON bitsei_schema.\"Product\".product_id = bitsei_schema.\"Invoice_Product\".product_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Invoice_Product\".invoice_id = bitsei_schema.\"Invoice\".invoice_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ? AND bitsei_schema.\"Product\".product_id = ?;";

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "UPDATE bitsei_schema.\"Invoice_Product\" SET quantity = ?, unit_price = ?, related_price = ?, related_price_description = ?, purchase_date = ? WHERE invoice_id = ? AND product_id = ?";

    /**
     * The invoice product to be updated.
     */
    private final InvoiceProduct invoiceProduct;

    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id of this session, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for updating an invoice product present in the database.
     *
     * @param con the connection to the database.
     * @param invoiceProduct the invoice product to be updated.
     */
    public UpdateInvoiceProductDAO(final Connection con, final InvoiceProduct invoiceProduct, final int owner_id, final int company_id) {
        super(con);

        if (invoiceProduct == null) {
            LOGGER.error("The invoice product cannot be null.");
            throw new NullPointerException("The invoice product cannot be null.");
        }

        this.invoiceProduct = invoiceProduct;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, invoiceProduct.getInvoice_id());
            pstmt.setInt(4, invoiceProduct.getProduct_id());
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs.getInt("c") == 0) {
                LOGGER.error("Data access violation");
                throw new IllegalAccessException();
            }

            pstmt = con.prepareStatement(FETCH_INVOICE);
            pstmt.setInt(1, invoiceProduct.getInvoice_id());
            rs = pstmt.executeQuery();
            Invoice i = null;

            while (rs.next()) {
                i = new Invoice(rs.getInt("invoice_id"), rs.getInt("customer_id"), rs.getInt("status"), rs.getInt("warning_number"), rs.getDate("warning_date"), rs.getString("warning_pdf_file"), rs.getString("invoice_number"), rs.getDate("invoice_date"), rs.getString("invoice_pdf_file"), rs.getString("invoice_xml_file"), rs.getDouble("total"), rs.getDouble("discount"), rs.getDouble("pension_fund_refund"), rs.getBoolean("has_stamp"));
            }

            if (i.getStatus() != 0) {
                LOGGER.error("Update after closure not allowed.");
                throw new IllegalCallerException();
            }

            pstmt = con.prepareStatement(FETCH);
            pstmt.setInt(1, invoiceProduct.getInvoice_id());
            pstmt.setInt(2, invoiceProduct.getProduct_id());
            rs = pstmt.executeQuery();
            InvoiceProduct ip = null;

            while (rs.next()) {
                ip = new InvoiceProduct(rs.getInt("invoice_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getDouble("unit_price"), rs.getDouble("related_price"), rs.getString("related_price_description"), rs.getDate("purchase_date"));
            }

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoiceProduct.getQuantity());
            pstmt.setDouble(2, invoiceProduct.getUnit_price());
            pstmt.setDouble(3, invoiceProduct.getRelated_price());
            pstmt.setString(4, invoiceProduct.getRelated_price_description());
            pstmt.setDate(5, invoiceProduct.getPurchase_date());
            pstmt.setInt(6, invoiceProduct.getInvoice_id());
            pstmt.setInt(7, invoiceProduct.getProduct_id());

            pstmt.execute();

            LOGGER.info("query: " + pstmt.toString());

            LOGGER.info("Invoice product successfully updated in the database.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
