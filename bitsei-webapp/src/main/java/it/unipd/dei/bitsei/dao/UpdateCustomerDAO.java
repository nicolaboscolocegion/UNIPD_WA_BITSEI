package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateCustomerDAO extends AbstractDAO {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "UPDATE bitsei_schema.\"Customer\" SET business_name = ?, vat_number = ?, tax_code = ?, address = ?, city = ?, province = ?, postal_code = ?, email = ?, pec = ?, unique_code = ? WHERE customer_id = ?";

    /**
     /**
     * The customer to be stored into the database
     */
    private final Customer customer;

    /**
     * Creates a new object for storing a customer into the database.
     *
     * @param con
     *            the connection to the database.
     * @param customer
     *            the customer to be stored into the database.
     */
    public UpdateCustomerDAO(final Connection con, final Customer customer) {
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
            pstmt.setString(1, customer.getBusinessName());
            pstmt.setString(2, customer.getVatNumber());
            pstmt.setString(3, customer.getTaxCode());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getCity());
            pstmt.setString(6, customer.getProvince());
            pstmt.setString(7, customer.getPostalCode());
            pstmt.setString(8, customer.getEmailAddress());
            pstmt.setString(9, customer.getPec());
            pstmt.setString(10, customer.getUniqueCode());
            pstmt.setInt(11, customer.getCustomerID());

            pstmt.execute();

            LOGGER.info("query: " + pstmt.toString());

            LOGGER.info("Customer %s successfully updated in the database.", customer.getBusinessName());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
