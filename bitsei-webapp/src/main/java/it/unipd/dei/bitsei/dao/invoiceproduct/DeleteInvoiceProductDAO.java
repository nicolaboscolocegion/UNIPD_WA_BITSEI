package it.unipd.dei.bitsei.dao.invoiceproduct;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.AbstractResource;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Deletes an invoice product from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteInvoiceProductDAO extends AbstractDAO<InvoiceProduct> {
    private static final String FETCH_INVOICE = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";
    private static final String FETCH = "SELECT * FROM bitsei_schema.\"Invoice_Product\" WHERE invoice_id = ? AND product_id = ?;";
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Product\".company_id INNER JOIN bitsei_schema.\"Invoice_Product\" ON bitsei_schema.\"Product\".product_id = bitsei_schema.\"Invoice_Product\".product_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Invoice_Product\".invoice_id = bitsei_schema.\"Invoice\".invoice_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ? AND bitsei_schema.\"Product\".product_id = ?;";

    /**
     * The SQL statement to be executed.
     */
    private static final String DELETE = "DELETE FROM bitsei_schema.\"Invoice_Product\" WHERE invoice_id = ? AND product_id = ?;";

    /**
     * The invoice_id of the invoice product to be deleted from the database.
     */
    private final int invoice_id;

    /**
     * The product_id of the invoice product to be deleted from the database.
     */
    private final int product_id;

    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id of this session, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for deleting an invoice product from the database.
     *
     * @param con the connection to the database.
     * @param invoice_id the id of the invoice linked to the invoice product to be deleted from the database.
     * @param product_id the id of the product linked to the invoice product to be deleted from the database.
     * @param owner_id the owner_id of the session, to be checked for security reasons.
     * @param company_id the company_id of the session, to be checked for security reasons.
     */
    public DeleteInvoiceProductDAO(final Connection con, final int invoice_id, final int product_id, final int owner_id, final int company_id) {
        super(con);

        this.invoice_id = invoice_id;
        this.product_id = product_id;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    protected final void doAccess() throws SQLException {

        InvoiceProduct ip = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, invoice_id);
            pstmt.setInt(4, product_id);
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
            pstmt.setInt(1, invoice_id);
            rs = pstmt.executeQuery();
            Invoice i = null;

            while (rs.next()) {
                i = new Invoice(rs.getInt("invoice_id"), rs.getInt("customer_id"), rs.getInt("status"), rs.getInt("warning_number"), rs.getDate("warning_date"), rs.getString("warning_pdf_file"), rs.getString("invoice_number"), rs.getDate("invoice_date"), rs.getString("invoice_pdf_file"), rs.getString("invoice_xml_file"), rs.getDouble("total"), rs.getDouble("discount"), rs.getDouble("pension_fund_refund"), rs.getBoolean("has_stamp"));
            }

            if (i.getStatus() != 0) {
                LOGGER.error("Deletion after closure not allowed.");
                throw new IllegalCallerException();
            }



            pstmt = con.prepareStatement(FETCH);
            pstmt.setInt(1, invoice_id);
            pstmt.setInt(2, product_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ip = new InvoiceProduct(rs.getInt("invoice_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getDouble("unit_price"), rs.getDouble("related_price"), rs.getString("related_price_description"), rs.getDate("purchase_date"));
            }


            pstmt = con.prepareStatement(DELETE);
            pstmt.setInt(1, invoice_id);
            pstmt.setInt(2, product_id);
            pstmt.executeUpdate();


            LOGGER.info("Invoice product successfully deleted from the database.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = ip;

    }
}
