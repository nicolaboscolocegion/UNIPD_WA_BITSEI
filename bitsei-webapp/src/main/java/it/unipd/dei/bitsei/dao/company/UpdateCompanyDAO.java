package it.unipd.dei.bitsei.dao.company;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Company;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Partial Update the company by id in the database.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class UpdateCompanyDAO extends AbstractDAO<Boolean> {

    private final int owner_id;
    private final Company company;

    /**
     * Creates a new object for getting one user.
     *
     * @param con      the connection to the database.
     * @param owner_id the owner_id of the company
     * @param company  the company to update
     */
    public UpdateCompanyDAO(final Connection con, final int owner_id, final Company company) {
        super(con);
        this.owner_id = owner_id;
        this.company = company;

        outputParam = false;
    }

    /**
     * Returns the SQL Statement based on given data.
     *
     * @param company the company to update
     * @return the SQL Statement.
     */
    private static String GetStatement(Company company) {
        // main query
        String sql = "UPDATE bitsei_schema.\"Company\" SET ";

        // set clause over all the fields
        List<String> setClauses = new ArrayList<>();
        for (Field f : company.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            Object value;
            try {
                value = f.get(company);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (value != null) {
                setClauses.add(f.getName() + " = ?");
            }
        }

        String setClauseString = String.join(", ", setClauses);
        sql += setClauseString;

        sql += " WHERE owner_id = ? AND company_id = ?";
        return sql;
    }

    @Override
    protected final void doAccess() throws SQLException {

        int rs;
        PreparedStatement pstmt = null;

        try {
            // set all the parameters
            int i = 1;
            pstmt = con.prepareStatement(GetStatement(company));
            for (Field f : company.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object value = f.get(company);
                if (value != null) {
                    pstmt.setObject(i++, value);
                }
            }
            pstmt.setInt(i++, owner_id);
            pstmt.setInt(i, company.getCompany_id());
            rs = pstmt.executeUpdate();

            if (rs == 0) {
                throw new SQLException("Updating company failed, no rows affected.");
            }

            LOGGER.info("Company successfully updated.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        outputParam = true;
    }
}
