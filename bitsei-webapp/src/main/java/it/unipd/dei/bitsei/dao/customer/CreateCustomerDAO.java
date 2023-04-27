package it.unipd.dei.bitsei.dao.customer;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Customer\" (business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     /**
     * The customer to be stored into the database
     */
    private final Customer customer;
    private final int owner_id;
    private final int company_id;

    /**
     * Creates a new object for storing a customer into the database.
     *
     * @param con
     *            the connection to the database.
     * @param customer
     *            the customer to be stored into the database.
     * @param owner_id
     *            the owner of the customer.
     */
    public CreateCustomerDAO(final Connection con, final Customer customer, final int owner_id, final int company_id) {
        super(con);

        if (customer == null) {
            LOGGER.error("The customer cannot be null.");
            throw new NullPointerException("The customer cannot be null.");
        }
        this.owner_id = owner_id;
        this.company_id = company_id;
        this.customer = customer;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

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
            pstmt.setInt(11, customer.getCompanyID());

            pstmt.execute();

            LOGGER.info("Customer %s successfully stored in the database.", customer.getBusinessName());



        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
