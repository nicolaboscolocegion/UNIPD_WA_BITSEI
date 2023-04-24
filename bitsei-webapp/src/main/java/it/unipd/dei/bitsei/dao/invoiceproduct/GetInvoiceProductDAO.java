package it.unipd.dei.bitsei.dao.invoiceproduct;



import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Searches invoice product by ids.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GetInvoiceProductDAO extends AbstractDAO<InvoiceProduct> {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice_Product\" WHERE invoice_id = ? AND product_id = ?;";

    /**
     * The invoice_id of the invoice product.
     */
    private final int invoice_id;

    /**
     * The product_id of the invoice product.
     */
    private final int product_id;

    /**
     * Creates a new object for searching invoice product by ids.
     *
     * @param con    the connection to the database.
     * @param invoice_id the id of the invoice.
     * @param product_id the id of the product.
     */
    public GetInvoiceProductDAO(final Connection con, final int invoice_id, final int product_id) {
        super(con);
        this.invoice_id = invoice_id;
        this.product_id = product_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        InvoiceProduct i = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice_id);
            pstmt.setInt(2, product_id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                i = new InvoiceProduct(rs.getInt("invoice_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getDouble("unit_price"), rs.getDouble("related_price"), rs.getString("related_price_description"), rs.getDate("purchase_date"));
            }

            LOGGER.info("Invoice product with invoice_id %d and product_id &d successfully listed.", invoice_id, product_id);
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