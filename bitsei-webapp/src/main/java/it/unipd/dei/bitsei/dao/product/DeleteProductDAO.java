package it.unipd.dei.bitsei.dao.product;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Deletes a product from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteProductDAO extends AbstractDAO {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "DELETE FROM bitsei_schema.\"Product\" WHERE product_id = ?;";

    /**
     * The Product to be deleted from the database.
     */
    private final Product product;

    /**
     * Creates a new object for deleting a product from the database.
     *
     * @param con the connection to the database.
     * @param product the product to be deleted from the database.
     */
    public DeleteProductDAO(final Connection con, final Product product) {
        super(con);

        if (product == null) {
            LOGGER.error("The product cannot be null.");
            throw new NullPointerException("The product cannot be null.");
        }

        this.product = product;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, product.getProduct_id());


            pstmt.execute();

            LOGGER.info("Product %s successfully deleted from the database.", product.getTitle());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
