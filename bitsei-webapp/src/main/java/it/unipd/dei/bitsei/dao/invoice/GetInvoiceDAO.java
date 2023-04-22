package it.unipd.dei.bitsei.dao.invoice;



import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Searches invoice by id.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GetInvoiceDAO extends AbstractDAO<Invoice> {
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Customer\".customer_id = bitsei_schema.\"Invoice\".customer_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Customer\".customer_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ?;";


    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";

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
     * Creates a new object for searching invoice by id.
     *
     * @param con    the connection to the database.
     * @param invoice_id the id of the invoice.
     */
    public GetInvoiceDAO(final Connection con, final int invoice_id, final int owner_id, final int company_id) {
        super(con);
        this.invoice_id = invoice_id;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        Invoice i = null;

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

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice_id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                i = new Invoice(rs.getInt("invoice_id"), rs.getInt("customer_id"), rs.getInt("status"), rs.getInt("warning_number"), rs.getDate("warning_date"), rs.getString("warning_pdf_file"), rs.getString("invoice_number"), rs.getDate("invoice_date"), rs.getString("invoice_pdf_file"), rs.getString("invoice_xml_file"), rs.getDouble("total"), rs.getDouble("discount"), rs.getDouble("pension_fund_refund"), rs.getBoolean("has_stamp"));
            }

            LOGGER.info("Invoice with invoice_id %d successfully listed.", invoice_id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (rs_check != null) {
                rs_check.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = i;
    }
}