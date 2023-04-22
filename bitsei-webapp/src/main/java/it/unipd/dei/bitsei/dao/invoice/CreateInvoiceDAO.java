package it.unipd.dei.bitsei.dao.invoice;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Invoice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a new invoice inside the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CreateInvoiceDAO extends AbstractDAO<Invoice> {
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";

    /**
     * SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Invoice\" (customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * The invoice to be stored into the database.
     */
    private final Invoice invoice;

    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id of this session, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for storing an invoice into the database.
     *
     * @param con the connection to the database.
     *
     * @param invoice the invoice to be stored into the database.
     */
    public CreateInvoiceDAO(final Connection con, final Invoice invoice, final int owner_id, final int company_id) {
        super(con);

        if (invoice == null) {
            LOGGER.error("The invoice cannot be null.");
            throw new NullPointerException("The invoice cannot be null.");
        }
        this.owner_id = owner_id;
        this.company_id = company_id;

        this.invoice = invoice;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
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
            }

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice.getCustomer_id());
            pstmt.setInt(2, invoice.getStatus());
            pstmt.setInt(3, invoice.getWarning_number());
            pstmt.setDate(4, invoice.getWarning_date());
            pstmt.setString(5, invoice.getWarning_pdf_file());
            pstmt.setString(6, invoice.getInvoice_number());
            pstmt.setDate(7, invoice.getInvoice_date());
            pstmt.setString(8, invoice.getInvoice_pdf_file());
            pstmt.setString(9, invoice.getInvoice_xml_file());
            pstmt.setDouble(10, invoice.getTotal());
            pstmt.setDouble(11, invoice.getDiscount());
            pstmt.setDouble(12, invoice.getPension_fund_refund());
            pstmt.setBoolean(13, invoice.hasStamp());
            pstmt.execute();

            LOGGER.info("Invoice successfully stored in the database.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
