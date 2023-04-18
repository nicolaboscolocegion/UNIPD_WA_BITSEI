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
public final class LoadInvoiceForUpdateDAO extends AbstractDAO<Invoice> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";

    /**
     * The invoice_id of the invoice
     */
    private final int invoice_id;

    /**
     * Creates a new object for searching invoice by id.
     *
     * @param con    the connection to the database.
     * @param invoice_id the id of the invoice.
     */
    public LoadInvoiceForUpdateDAO(final Connection con, final int invoice_id) {
        super(con);
        this.invoice_id = invoice_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        Invoice i = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice_id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                i = new Invoice(rs.getInt("invoice_id"), rs.getInt("customer_id"), rs.getInt("status"), rs.getInt("warning_number"), rs.getDate("warning_date"), rs.getString("warning_pdf_file"), rs.getString("invoice_number"), rs.getDate("invoice_date"), rs.getString("invoice_pdf_file"), rs.getString("invoice_xml_file"), rs.getDouble("total"), rs.getDouble("discount"), rs.getDouble("pension_fund_refund"), rs.getBoolean("has_stamp"));
            }

            LOGGER.info("Invoice with invoice_id %d successfully listed.", invoice_id);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = i;
    }
}