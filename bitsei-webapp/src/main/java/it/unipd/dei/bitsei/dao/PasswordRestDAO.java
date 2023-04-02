package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.PasswordRest;

import java.sql.*;
import java.util.concurrent.TimeUnit;


public class PasswordRestDAO extends AbstractDAO<PasswordRest> {
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Password_Reset_Token\" (owner_id, token, token_expiry) VALUES ((SELECT owner_id FROM bitsei_schema.\"Owner\" WHERE email=?), ?, ?) RETURNING *";

    private final String user_email;
    private final String token;

    /**
     * Creates a new object for getting one user.
     *
     * @param con   the connection to the database.
     * @param email the user_id of the user
     * @param token the token of the user
     */
    public PasswordRestDAO(final Connection con, String email, String token) {
        super(con);
        this.token = token;
        this.user_email = email;
    }


    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PasswordRest passwordRest = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, user_email);
            pstmt.setString(2, token);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                passwordRest = new PasswordRest(
                        rs.getString("token"),
                        rs.getDate("token_expiry"),
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
