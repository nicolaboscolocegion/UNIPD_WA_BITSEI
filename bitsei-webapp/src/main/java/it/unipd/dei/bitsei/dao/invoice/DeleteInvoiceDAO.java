package it.unipd.dei.bitsei.dao.invoice;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.AbstractResource;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Deletes an invoice from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 *
 * @param <I> Parameter for invoice object.
 */
public final class DeleteInvoiceDAO<I extends AbstractResource> extends AbstractDAO<Invoice> {
    private static final String FETCH = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Customer\".customer_id = bitsei_schema.\"Invoice\".customer_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ?;";
    /**
     * The SQL statement to be executed.
     */
    private static final String DELETE = "DELETE FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";

    /**
     * The id of the invoice to be deleted from the database.
     */
    private final int invoice_id;
    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id of this session, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for deleting an invoice from the database.
     *
     * @param con the connection to the database.
     * @param invoice_id the invoice to be deleted from the database.
     * @param owner_id the owner_id of the session, to be checked for security reasons.
     * @param company_id the company_id of the session, to be checked for security reasons.
     */
    public DeleteInvoiceDAO(final Connection con, final int invoice_id, final int owner_id, final int company_id) {
        super(con);

        this.invoice_id = invoice_id;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    protected final void doAccess() throws SQLException {

        Invoice i = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, invoice_id);
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
            pstmt.setInt(1, invoice_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                i = new Invoice(rs.getInt("invoice_id"), rs.getInt("customer_id"), rs.getInt("status"), rs.getInt("warning_number"), rs.getDate("warning_date"), rs.getString("warning_pdf_file"), rs.getString("invoice_number"), rs.getDate("invoice_date"), rs.getString("invoice_pdf_file"), rs.getString("invoice_xml_file"), rs.getDouble("total"), rs.getDouble("discount"), rs.getDouble("pension_fund_refund"), rs.getBoolean("has_stamp"));
            }

            if (i.getStatus() != 0) {
                LOGGER.error("Deletion after closure not allowed.");
                throw new IllegalCallerException();
            }

           /* pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs.getInt("c") == 0) {
                LOGGER.error("Company selected does not belong to logged user.");
                throw new IllegalAccessException();
            }*/

            pstmt = con.prepareStatement(DELETE);
            pstmt.setInt(1, invoice_id);
            pstmt.executeUpdate();


            LOGGER.info("Invoice successfully deleted from the database.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = i;

    }
}
