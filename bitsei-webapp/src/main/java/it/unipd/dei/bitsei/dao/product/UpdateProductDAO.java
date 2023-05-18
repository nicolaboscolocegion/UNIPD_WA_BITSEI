package it.unipd.dei.bitsei.dao.product;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Updates a product present in the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateProductDAO extends AbstractDAO<Product> {
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Product\".company_id = bitsei_schema.\"Company\".company_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Product\".product_id = ?;";

    private static final String FETCH = "SELECT * FROM bitsei_schema.\"Product\" WHERE product_id = ?;";
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "UPDATE bitsei_schema.\"Product\" SET company_id = ?, title = ?, default_price = ?, logo = ?, measurement_unit = ?, description = ? WHERE product_id = ?";

    /**
     * The product to be updated.
     */
    private final Product product;

    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id currently used, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for updating a product present in the database.
     *
     * @param con        the connection to the database.
     * @param product    the product to be updated.
     * @param owner_id   the id of the owner of the session.
     * @param company_id the id of the company of the owner of the session.
     */
    public UpdateProductDAO(final Connection con, final Product product, final int owner_id, final int company_id) {
        super(con);

        if (product == null) {
            LOGGER.error("The product cannot be null.");
            throw new NullPointerException("The product cannot be null.");
        }
        this.owner_id = owner_id;
        this.company_id = company_id;
        this.product = product;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, product.getProduct_id());
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs.getInt("c") == 0) {
                LOGGER.error("Data access violation");
                throw new IllegalAccessException();
            }

            pstmt = con.prepareStatement(FETCH);
            pstmt.setInt(1, product.getProduct_id());
            rs = pstmt.executeQuery();
            Product p = null;

            while (rs.next()) {
                p = new Product(rs.getInt("product_id"), rs.getInt("company_id"), rs.getString("title"), rs.getInt("default_price"), rs.getString("logo"), rs.getString("measurement_unit"), rs.getString("description"));
            }

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
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
