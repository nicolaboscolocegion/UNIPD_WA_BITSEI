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
public final class CreateCustomerDAO extends AbstractDAO {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Customer\" (business_name, vat_number, tax_code) VALUES (?, ?, ?)";

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
    public CreateCustomerDAO(final Connection con, final Customer customer) {
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

            pstmt.execute();

            LOGGER.info("Customer %s successfully stored in the database.", customer.getBusinessName());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
