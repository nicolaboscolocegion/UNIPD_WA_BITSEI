package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.PasswordRest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetPasswordRestDAO extends AbstractDAO<PasswordRest>{

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Password_Reset_Token\" WHERE token = ? and token_expiry > now()";

    private final String token;

    /**
     * Creates a new object for getting one user.
     *
     * @param con   the connection to the database.
     * @param token the token of the user
     */
    public GetPasswordRestDAO(final Connection con, String token) {
        super(con);
        this.token = token;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        PasswordRest passwordRest = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, token);

            rs = pstmt.executeQuery();

            if (rs.next()){
                passwordRest = new PasswordRest(
                        rs.getString("token"),
                        rs.getDate("expiry_date"),
                        rs.getInt("owner_id")
                );
            }

            LOGGER.info("Password Rest successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = passwordRest;
    }
}
