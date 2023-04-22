package it.unipd.dei.bitsei.dao.product;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "DELETE FROM bitsei_schema.\"Product\" WHERE product_id = ?;";

    /**
     * The Product to be deleted from the database.
     */
    private final Product product;

    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * Creates a new object for deleting a product from the database.
     *
     * @param con the connection to the database.
     * @param product the product to be deleted from the database.
     */
    public DeleteProductDAO(final Connection con, final Product product, final int owner_id) {
        super(con);

        if (product == null) {
            LOGGER.error("The product cannot be null.");
            throw new NullPointerException("The product cannot be null.");
        }
        this.owner_id = owner_id;

        this.product = product;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, product.getCompany_id());
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
            pstmt.setInt(1, product.getProduct_id());


            pstmt.execute();

            LOGGER.info("Product %s successfully deleted from the database.", product.getTitle());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
