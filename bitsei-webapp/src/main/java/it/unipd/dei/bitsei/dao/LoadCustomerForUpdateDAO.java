package it.unipd.dei.bitsei.dao;


import it.unipd.dei.bitsei.resources.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Searches employees by their salary.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class LoadCustomerForUpdateDAO extends AbstractDAO<Customer> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Customer\" WHERE customer_id = ?;";

    /**
     * The customerID of the employee
     */
    private final int customerID;

    /**
     * Creates a new object for searching employees by salary.
     *
     * @param con    the connection to the database.
     * @param salary the salary of the employee.
     */
    public LoadCustomerForUpdateDAO(final Connection con, final int customerID) {
        super(con);
        this.customerID = customerID;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        Customer c = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, customerID);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                c = new Customer(rs.getInt("customer_id"), rs.getString("business_name"), rs.getString("vat_number"), rs.getString("tax_code"), rs.getString("address"), rs.getString("city"), rs.getString("province"), rs.getString("postal_code"), rs.getString("email"), rs.getString("pec"), rs.getString("unique_code"), rs.getInt("company_id"));
            }

            LOGGER.info("Customer with customerID above %d successfully listed.", customerID);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = c;
    }
}