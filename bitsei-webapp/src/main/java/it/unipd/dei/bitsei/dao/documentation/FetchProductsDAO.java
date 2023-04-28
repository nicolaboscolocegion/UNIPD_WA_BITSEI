package it.unipd.dei.bitsei.dao.documentation;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Fetches all products.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class FetchProductsDAO extends AbstractDAO<List<Product>> {

    /**
     * The SQL statement to be executed
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Product\";";


    private final int owner_id;
    private final int company_id;

    /**
     * Creates a new object for searching employees by salary.
     *
     * @param con        the connection to the database.
     * @param owner_id   the owner id of the company
     * @param company_id the company id
     */
    public FetchProductsDAO(final Connection con, int owner_id, int company_id) {
        super(con);
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search

        List<Product> lp = new ArrayList<Product>();

        try {

            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
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

            rs = pstmt.executeQuery();

            while (rs.next()) {
                lp.add(new Product(rs.getInt("product_id"), rs.getInt("company_id"), rs.getString("title"), rs.getInt("default_price"), rs.getString("logo"), rs.getString("measurement_unit"), rs.getString("description")));
            }

            LOGGER.info("Products(s) successfully listed.");
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

        this.outputParam = lp;
    }
}

