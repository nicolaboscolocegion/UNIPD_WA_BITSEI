package it.unipd.dei.bitsei.dao.customer;


import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Search customer by his id.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GetCustomerDAO extends AbstractDAO<Customer> {

    /**
     * The SQL statement to be executed
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Company\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Customer\".customer_id = ?;";
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Customer\" WHERE customer_id = ?;";

    /**
     * The customerID of the employee
     */
    private final int customerID;
    private final int owner_id;
    private final int company_id;

    /**
     * Creates a new object for searching customers by ID.
     *
     * @param con    the connection to the database.
     * @param customerID the salary of the employee.
     */
    public GetCustomerDAO(final Connection con, final int customerID, final int owner_id, final int company_id) {
        super(con);
        this.customerID = customerID;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        Customer c = null;

        try {

            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1,  company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(2, customerID);
            rs_check = pstmt.executeQuery();
            if (!rs_check.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs_check.getInt("c") == 0) {
                LOGGER.error("Data access violation");
                throw new IllegalAccessException();
            }


            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, customerID);

            rs = pstmt.executeQuery();


            while (rs.next()) {

                c = new Customer(rs.getInt("customer_id"), rs.getString("business_name"), rs.getString("vat_number"), rs.getString("tax_code"), rs.getString("address"), rs.getString("city"), rs.getString("province"), rs.getString("postal_code"), rs.getString("email"), rs.getString("pec"), rs.getString("unique_code"), rs.getInt("company_id"));
            }

            LOGGER.info("Customer with customerID above %d successfully listed.", customerID);
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (rs_check != null) {
                rs_check.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = c;
    }
}