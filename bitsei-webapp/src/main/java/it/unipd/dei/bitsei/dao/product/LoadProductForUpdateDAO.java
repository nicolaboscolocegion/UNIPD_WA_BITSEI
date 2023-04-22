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
public final class LoadProductForUpdateDAO extends AbstractDAO<Product> {
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";

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
     * Creates a new object for searching product by id.
     *
     * @param con    the connection to the database.
     * @param product_id the id of the product.
     */
    public LoadProductForUpdateDAO(final Connection con, final int product_id, final int owner_id) {
        super(con);
        this.owner_id = owner_id;
        this.product_id = product_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        Product p = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, product_id);

            rs = pstmt.executeQuery();


            while (rs.next()) {
                pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
                pstmt.setInt(1, rs.getInt("company_id"));
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

                p = new Product(rs.getInt("product_id"), rs.getInt("company_id"), rs.getString("title"), rs.getInt("default_price"), rs.getString("logo"), rs.getString("measurement_unit"), rs.getString("description"));
            }

            LOGGER.info("Product with product_id above %d successfully listed.", product_id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
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