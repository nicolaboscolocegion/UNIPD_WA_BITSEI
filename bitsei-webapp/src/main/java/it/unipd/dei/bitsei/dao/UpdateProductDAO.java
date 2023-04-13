package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates a product present in the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateProductDAO extends AbstractDAO {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "UPDATE bitsei_schema.\"Product\" SET company_id = ?, title = ?, default_price = ?, logo = ?, measurement_unit = ?, description = ? WHERE product_id = ?";

    /**
     * The product to be updated.
     */
    private final Product product;

    /**
     * Creates a new object for updating a product present in the database.
     *
     * @param con the connection to the database.
     * @param product the product to be updated.
     */
    public UpdateProductDAO(final Connection con, final Product product) {
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
            pstmt.setInt(1, product.getCompany_id());
            pstmt.setString(2, product.getTitle());
            pstmt.setInt(3, product.getDefault_price());
            pstmt.setString(4, product.getLogo());
            pstmt.setString(5, product.getMeasurement_unit());
            pstmt.setString(6, product.getDescription());
            pstmt.setInt(7, product.getProduct_id());

            pstmt.execute();

            LOGGER.info("query: " + pstmt.toString());

            LOGGER.info("Product %s successfully updated in the database.", product.getTitle());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
