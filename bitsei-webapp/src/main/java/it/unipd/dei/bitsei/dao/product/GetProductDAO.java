package it.unipd.dei.bitsei.dao.product;


import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Searches product by his id.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GetProductDAO extends AbstractDAO<Product> {
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Product\".company_id = bitsei_schema.\"Company\".company_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Product\".product_id = ?;";

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Product\" WHERE product_id = ?;";

    /**
     * The product_id of the product
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
     * Creates a new object for searching product by id.
     *
     * @param con    the connection to the database.
     * @param product_id the id of the product.
     */
    public GetProductDAO(final Connection con, final int product_id, final int owner_id, final int company_id) {
        super(con);
        this.owner_id = owner_id;
        this.product_id = product_id;
        this.company_id = company_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        Product p = null;

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

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, product_id);

            rs = pstmt.executeQuery();

            if (rs.getInt("c") == 0) {
                LOGGER.error("Product selected does not belong to logged user.");
                throw new IllegalAccessException();
            }

            p = new Product(rs.getInt("product_id"), rs.getInt("company_id"), rs.getString("title"), rs.getInt("default_price"), rs.getString("logo"), rs.getString("measurement_unit"), rs.getString("description"));


            LOGGER.info("Product with product_id above %d successfully listed.", product_id);
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = p;
    }
}