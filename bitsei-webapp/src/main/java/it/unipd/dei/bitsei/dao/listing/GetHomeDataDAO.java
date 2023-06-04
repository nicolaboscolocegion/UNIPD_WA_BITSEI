package it.unipd.dei.bitsei.dao.listing;


import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.HomeData;

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
public final class GetHomeDataDAO extends AbstractDAO<HomeData> {

    /**
     * The SQL statement to be executed
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ?;";
    private static final String STATEMENT_ONE = "SELECT SUM(bitsei_schema.\"Invoice\".total) AS t FROM bitsei_schema.\"Invoice\" inner join bitsei_schema.\"Customer\" on bitsei_schema.\"Invoice\".customer_id = bitsei_schema.\"Customer\".customer_id WHERE bitsei_schema.\"Customer\".company_id = ?;";
    private static final String STATEMENT_TWO = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Invoice\" inner join bitsei_schema.\"Customer\" on bitsei_schema.\"Invoice\".customer_id = bitsei_schema.\"Customer\".customer_id WHERE bitsei_schema.\"Customer\".company_id = ? and bitsei_schema.\"Invoice\".invoice_date is not null and bitsei_schema.\"Invoice\".invoice_date <= NOW() - interval '14 day' AND bitsei_schema.\"Invoice\".status = 1;";
    private static final String STATEMENT_THREE = "SELECT SUM(total) AS t, bitsei_schema.\"Customer\".business_name  FROM bitsei_schema.\"Invoice\" inner join bitsei_schema.\"Customer\" on bitsei_schema.\"Invoice\".customer_id = bitsei_schema.\"Customer\".customer_id WHERE bitsei_schema.\"Customer\".company_id = ? GROUP BY bitsei_schema.\"Customer\".business_name order by t DESC LIMIT 1;";
    private static final String STATEMENT_FOUR = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Customer\" WHERE company_id = ?;";

    /**
     * The customerID of the employee
     */
    private final int owner_id;
    private final int company_id;

    /**
     * Creates a new object for searching customers by ID.
     *
     * @param con        the connection to the database.
     * @param owner_id   the owner id of the company
     * @param company_id the company id
     */
    public GetHomeDataDAO(final Connection con,  final int owner_id, final int company_id) {
        super(con);
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        HomeData hd = null;
        double total = 0;
        int closed_inv = 0;
        String most_money_cust = "";
        double most_money_cust_val = 0;
        int active_cust = 0;

        try {

            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            rs_check = pstmt.executeQuery();
            if (!rs_check.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs_check.getInt("c") == 0) {
                LOGGER.error("Data access violation");
                throw new IllegalAccessException();
            }


            /*pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, customerID);

            rs = pstmt.executeQuery();


            while (rs.next()) {

                c = new Customer(rs.getInt("customer_id"), rs.getString("business_name"), rs.getString("vat_number"), rs.getString("tax_code"), rs.getString("address"), rs.getString("city"), rs.getString("province"), rs.getString("postal_code"), rs.getString("email"), rs.getString("pec"), rs.getString("unique_code"), rs.getInt("company_id"));
            }

            LOGGER.info("Customer with customerID above %d successfully listed.", customerID);*/

            pstmt = con.prepareStatement(STATEMENT_ONE);
            pstmt.setInt(1, company_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                total = rs.getDouble("t");
            }


            pstmt = con.prepareStatement(STATEMENT_TWO);
            pstmt.setInt(1, company_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                closed_inv = rs.getInt("c");
            }

            pstmt = con.prepareStatement(STATEMENT_THREE);
            pstmt.setInt(1, company_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                most_money_cust = rs.getString("business_name");
                most_money_cust_val = rs.getDouble("t");
            }

            pstmt = con.prepareStatement(STATEMENT_FOUR);
            pstmt.setInt(1, company_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                active_cust = rs.getInt("c");
            }

            hd = new HomeData(total, closed_inv, most_money_cust, most_money_cust_val, active_cust);



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

        this.outputParam = hd;
    }
}