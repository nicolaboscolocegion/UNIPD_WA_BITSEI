package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Deletes an invoice product from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteInvoiceProductDAO extends AbstractDAO {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "DELETE FROM bitsei_schema.\"Invoice_Product\" WHERE invoice_id = ? AND product_id = ?;";

    /**
     * The invoice  product to be deleted from the database.
     */
    private final InvoiceProduct invoiceProduct;

    /**
     * Creates a new object for deleting an invoice product from the database.
     *
     * @param con the connection to the database.
     * @param invoiceProduct the invoice product to be deleted from the database.
     */
    public DeleteInvoiceProductDAO(final Connection con, final InvoiceProduct invoiceProduct) {
        super(con);

        if (invoiceProduct == null) {
            LOGGER.error("The invoice product cannot be null.");
            throw new NullPointerException("The invoice product cannot be null.");
        }

        this.invoiceProduct = invoiceProduct;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoiceProduct.getInvoice_id());
            pstmt.setInt(2, invoiceProduct.getProduct_id());


            pstmt.execute();

            LOGGER.info("Invoice product successfully deleted from the database.");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
