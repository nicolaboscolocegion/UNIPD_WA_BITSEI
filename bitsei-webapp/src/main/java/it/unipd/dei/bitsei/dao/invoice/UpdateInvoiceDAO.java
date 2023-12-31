package it.unipd.dei.bitsei.dao.invoice;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Updates an invoice present in the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateInvoiceDAO extends AbstractDAO<Invoice> {
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Customer\".customer_id = bitsei_schema.\"Invoice\".customer_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ?;";

    private static final String FETCH = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "UPDATE bitsei_schema.\"Invoice\" SET customer_id = ?, discount = ?, pension_fund_refund = ?, has_stamp = ? WHERE invoice_id = ?";

    /**
     * The invoice to be updated.
     */
    private final Invoice invoice;
    private final int owner_id;
    private final int company_id;

    /**
     * Creates a new object for updating an invoice present in the database.
     *
     * @param con the connection to the database.
     * @param invoice the invoice to be updated.
     * @param owner_id the id of the owner of the session.
     * @param company_id the id of the company of the owner of the session.
     */
    public UpdateInvoiceDAO(final Connection con, final Invoice invoice, final int owner_id, final int company_id) {
        super(con);

        if (invoice == null) {
            LOGGER.error("The invoice cannot be null.");
            throw new NullPointerException("The invoice cannot be null.");
        }

        this.invoice = invoice;
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
            pstmt.setInt(3, invoice.getInvoice_id());
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs.getInt("c") == 0) {
                LOGGER.error("Data access violation");
                throw new IllegalAccessException();
            }


            pstmt = con.prepareStatement(FETCH);
            pstmt.setInt(1, invoice.getInvoice_id());
            rs = pstmt.executeQuery();
            Invoice i = null;

            while (rs.next()) {
                i = new Invoice(rs.getInt("invoice_id"), rs.getInt("customer_id"), rs.getInt("status"), rs.getInt("warning_number"), rs.getDate("warning_date"), rs.getString("warning_pdf_file"), rs.getString("invoice_number"), rs.getDate("invoice_date"), rs.getString("invoice_pdf_file"), rs.getString("invoice_xml_file"), rs.getDouble("total"), rs.getDouble("discount"), rs.getDouble("pension_fund_refund"), rs.getBoolean("has_stamp"));
            }

            if (i.getStatus() != 0) {
                LOGGER.error("Update after closure not allowed.");
                throw new IllegalCallerException();
            }

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice.getCustomer_id());
            pstmt.setDouble(2, invoice.getDiscount());
            pstmt.setDouble(3, invoice.getPension_fund_refund());
            pstmt.setBoolean(4, invoice.hasStamp());
            pstmt.setInt(5, invoice.getInvoice_id());

            pstmt.execute();

            LOGGER.info("query: " + pstmt.toString());

            LOGGER.info("Invoice successfully updated in the database.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
