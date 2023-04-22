package it.unipd.dei.bitsei.dao.customer;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.AbstractResource;
import it.unipd.dei.bitsei.resources.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Deletes a customer from the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteCustomerDAO<C extends AbstractResource> extends AbstractDAO {

    /**
     * The SQL statement to be executed
     */
    private static final String FETCH = "SELECT * FROM bitsei_schema.\"Customer\" WHERE customer_id = ?;";
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Customer\".customer_id = ?;";
    private static final String DELETE = "DELETE FROM bitsei_schema.\"Customer\" WHERE customer_id = ?;";

    /**
     /**
     * The customer to be deleted from the database
     */
    private final int customerID;
    private final int owner_id;
    private final int company_id;

    /**
     * Creates a new object for deleting a customer from the database.
     *
     * @param con
     *            the connection to the database.
     * @param customerID
     *            the customer to be deleted from the database.
     * @param owner_id
     *            the owner of the customer.
     */
    public DeleteCustomerDAO(final Connection con, final int customerID, final int owner_id, final int company_id) {
        super(con);

        this.customerID = customerID;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    protected final void doAccess() throws SQLException {
        Customer c = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, customerID);
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
            pstmt.setInt(1, customerID);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                c = new Customer(rs.getInt("customer_id"), rs.getString("business_name"), rs.getString("vat_number"), rs.getString("tax_code"), rs.getString("address"), rs.getString("city"), rs.getString("province"), rs.getString("postal_code"), rs.getString("email"), rs.getString("pec"), rs.getString("unique_code"), rs.getInt("company_id"));
            }


            pstmt = con.prepareStatement(DELETE);
            pstmt.setInt(1, customerID);
            pstmt.executeUpdate();

            LOGGER.info("Customer %s successfully deleted from the database.", c.getBusinessName());
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

        this.outputParam = c;

    }

}