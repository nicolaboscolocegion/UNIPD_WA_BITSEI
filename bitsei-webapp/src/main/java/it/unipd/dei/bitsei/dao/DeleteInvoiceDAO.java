package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Deletes an invoice from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteInvoiceDAO extends AbstractDAO {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "DELETE FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";

    /**
     * The invoice to be deleted from the database.
     */
    private final Invoice invoice;

    /**
     * Creates a new object for deleting an invoice from the database.
     *
     * @param con the connection to the database.
     * @param invoice the invoice to be deleted from the database.
     */
    public DeleteInvoiceDAO(final Connection con, final Invoice invoice) {
        super(con);

        if (invoice == null) {
            LOGGER.error("The invoice cannot be null.");
            throw new NullPointerException("The invoice cannot be null.");
        }

        this.invoice = invoice;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice.getInvoice_id());


            pstmt.execute();

            LOGGER.info("Invoice successfully deleted from the database.");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
