package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates a new invoice product inside the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CreateInvoiceProductDAO extends AbstractDAO {

    /**
     * SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Invoice_Product\" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description, purchase_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * The invoice product to be stored into the database.
     */
    private final InvoiceProduct invoiceProduct;

    /**
     * Creates a new object for storing an invoice product into the database.
     *
     * @param con the connection to the database.
     *
     * @param invoiceProduct the invoice product to be stored into the database.
     */
    public CreateInvoiceProductDAO(final Connection con, final InvoiceProduct invoiceProduct) {
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
            pstmt.setInt(3, invoiceProduct.getQuantity());
            pstmt.setDouble(4, invoiceProduct.getUnit_price());
            pstmt.setDouble(5, invoiceProduct.getRelated_price());
            pstmt.setString(6, invoiceProduct.getRelated_price_description());
            pstmt.setDate(7, invoiceProduct.getPurchase_date());
            pstmt.execute();

            LOGGER.info("Invoice product successfully stored in the database.");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
