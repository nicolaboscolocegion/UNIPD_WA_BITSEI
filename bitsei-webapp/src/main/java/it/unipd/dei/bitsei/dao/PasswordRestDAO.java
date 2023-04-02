package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.PasswordRest;
import it.unipd.dei.bitsei.resources.User;

import java.sql.*;


public class PasswordRestDAO extends AbstractDAO<PasswordRest> {
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Password_Reset_Token\" (owner_id, token, token_expiry) VALUES (?, ?, ?) RETURNING *";

    private final int user_id;
    private final String token;

    /**
     * Creates a new object for getting one user.
     *
     * @param con     the connection to the database.
     * @param user_id the user_id of the user
     * @param token   the token of the user
     */
    public PasswordRestDAO(final Connection con, int user_id, String token) {
        super(con);
        this.token = token;
        this.user_id = user_id;
    }


    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PasswordRest passwordRest = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, user_id);
            pstmt.setString(2, token);
            pstmt.setDate(3, new Date(new java.util.Date().getTime() + 1000 * 60 * 15));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                passwordRest = new PasswordRest(
                        rs.getString("token"),
                        rs.getDate("expiry_date"),
                        rs.getInt("owner_id")
                );

                LOGGER.info("Rest Password Token successfully stored in the database.");
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        outputParam = passwordRest;

    }
}
