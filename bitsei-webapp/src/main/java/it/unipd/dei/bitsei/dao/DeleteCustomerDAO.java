package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Deletes a customer from the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteCustomerDAO extends AbstractDAO {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM bitsei_schema.\"Customer\" WHERE customer_id = ?;";

    /**
     /**
     * The customer to be deleted from the database
     */
    private final Customer customer;

    /**
     * Creates a new object for deleting a customer from the database.
     *
     * @param con
     *            the connection to the database.
     * @param customer
     *            the customer to be deleted from the database.
     */
    public DeleteCustomerDAO(final Connection con, final Customer customer) {
        super(con);

        if (customer == null) {
            LOGGER.error("The customer cannot be null.");
            throw new NullPointerException("The customer cannot be null.");
        }

        this.customer = customer;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, customer.getCustomerID());


            pstmt.execute();

            LOGGER.info("Customer %s successfully deleted from the database.", customer.getBusinessName());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}