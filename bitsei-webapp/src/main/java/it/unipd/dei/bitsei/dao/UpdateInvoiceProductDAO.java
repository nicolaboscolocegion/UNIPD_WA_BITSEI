package it.unipd.dei.bitsei.dao;


import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates an invoice product present in the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateInvoiceProductDAO extends AbstractDAO {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "UPDATE bitsei_schema.\"Invoice_Product\" SET quantity = ?, unit_price = ?, related_price = ?, related_price_description = ?, purchase_date = ? WHERE invoice_id = ? AND product_id = ?";

    /**
     * The invoice product to be updated.
     */
    private final InvoiceProduct invoiceProduct;

    /**
     * Creates a new object for updating an invoice product present in the database.
     *
     * @param con the connection to the database.
     * @param invoiceProduct the invoice product to be updated.
     */
    public UpdateInvoiceProductDAO(final Connection con, final InvoiceProduct invoiceProduct) {
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
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
