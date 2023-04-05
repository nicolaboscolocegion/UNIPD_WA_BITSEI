package it.unipd.dei.bitsei.dao;


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
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Product\" WHERE product_id = ?;";

    /**
     * The product_id of the product
     */
    private final int product_id;

    /**
     * Creates a new object for searching product by id.
     *
     * @param con    the connection to the database.
     * @param product_id the salary of the employee.
     */
    public LoadProductForUpdateDAO(final Connection con, final int product_id) {
        super(con);
        this.product_id = product_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        Product p = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, product_id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                p = new Product(rs.getInt("product_id"), rs.getInt("company_id"), rs.getString("title"), rs.getInt("default_price"), rs.getString("logo"), rs.getString("measurement_unit"), rs.getString("description"));
            }

            LOGGER.info("Product with product_id above %d successfully listed.", product_id);
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