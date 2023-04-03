package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.ChangePassword;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateUserPasswordDAO extends AbstractDAO<Boolean> {

    /**
     * The SQL statement to be executed
     */
    private static final String UPDATE_STATEMENT = "UPDATE bitsei_schema.\"Owner\" SET password = ? WHERE owner_id = (Select owner_id from bitsei_schema.\"Password_Reset_Token\" WHERE token = ? and token_expiry > now())";
    private static final String DELETE_STATEMENT = "DELETE FROM bitsei_schema.\"Password_Reset_Token\" WHERE owner_id IN (SELECT owner_id FROM bitsei_schema.\"Password_Reset_Token\" WHERE token = ?)";

    private final String token;
    private final String password;

    /**
     * Creates a new object for getting one user.
     *
     * @param con  the connection to the database.
     * @param data the data containing token and password of the user
     */
    public UpdateUserPasswordDAO(final Connection con, ChangePassword data) {
        super(con);
        if (data == null) {
            throw new IllegalArgumentException("The data cannot be null.");
        }
        this.token = data.getReset_token();
        this.password = data.getPassword();
    }

    @Override
    protected final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        int rs = 0;

        // the result of the search
        boolean is_done = false;

        try {

            con.setAutoCommit(false);
            pstmt = con.prepareStatement(UPDATE_STATEMENT);
            pstmt.setString(1, password);
            pstmt.setString(2, token);

            rs = pstmt.executeUpdate();
            if (rs != 0) {
                is_done = true;
                pstmt = con.prepareStatement(DELETE_STATEMENT);
                pstmt.setString(1, token);

                pstmt.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                throw e1;
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        LOGGER.info("Password Rest successfully fetched.");
        outputParam = is_done;
    }
}
