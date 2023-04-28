package it.unipd.dei.bitsei.dao.documentation;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Fetches all the customers for pdf listing.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class FetchCustomersDAO extends AbstractDAO<List<Customer>> {

    /**
     * The SQL statement to be executed
     */

    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Customer\";";

    private final int owner_id;
    private final int company_id;


    /**
     * Creates a new object fetching all Customers.
     *
     * @param con        the connection to the database.
     * @param owner_id   the owner id of the company
     * @param company_id the company id
     */
    public FetchCustomersDAO(final Connection con, int owner_id, int company_id) {
        super(con);
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search

        List<Customer> lc = new ArrayList<Customer>();

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
                lc.add(new Customer(rs.getInt("customer_id"), rs.getString("business_name"), rs.getString("vat_number"), rs.getString("tax_code"), rs.getString("address"), rs.getString("city"), rs.getString("province"), rs.getString("postal_code"), rs.getString("email"), rs.getString("pec"), rs.getString("unique_code"), rs.getInt("company_id")));
            }

            LOGGER.info("Customer(s) successfully listed.");
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

        this.outputParam = lc;
    }
}

