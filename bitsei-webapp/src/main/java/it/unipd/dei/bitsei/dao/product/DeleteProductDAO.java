package it.unipd.dei.bitsei.dao.product;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.AbstractResource;
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
 *
 *
 * @param <P> Parameter for product object.
 */
public final class DeleteProductDAO<P extends AbstractResource> extends AbstractDAO<Product> {
    private static final String FETCH = "SELECT * FROM bitsei_schema.\"Product\" WHERE product_id = ?;";

    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Product\".company_id = bitsei_schema.\"Company\".company_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Product\".product_id = ?;";

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "DELETE FROM bitsei_schema.\"Product\" WHERE product_id = ?;";

    /**
     * The id of the product to be deleted from the database.
     */
    private final int product_id;

    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id currently used, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for deleting a product from the database.
     *
     * @param con        the connection to the database.
     * @param product_id the id of the product to be deleted from the database.
     * @param owner_id   the id of the owner of the session.
     * @param company_id the id of the company of the owner of the session.
     */
    public DeleteProductDAO(final Connection con, final int product_id, final int owner_id, final int company_id) {
        super(con);

        this.product_id = product_id;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    protected final void doAccess() throws SQLException {

        Product p = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, product_id);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs.getInt("c") == 0) {
                LOGGER.error("Product selected does not belong to logged user.");
                throw new IllegalAccessException();
            }

            pstmt = con.prepareStatement(FETCH);
            pstmt.setInt(1, product_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                p = new Product(rs.getInt("product_id"), rs.getInt("company_id"), rs.getString("title"), rs.getInt("default_price"), rs.getString("logo"), rs.getString("measurement_unit"), rs.getString("description"));
            }

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, product_id);
            pstmt.executeUpdate();

            LOGGER.info("Product successfully deleted from the database.");
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = p;

    }
}
