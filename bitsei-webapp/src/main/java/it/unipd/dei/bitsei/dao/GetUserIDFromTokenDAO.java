package it.unipd.dei.bitsei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserIDFromTokenDAO extends AbstractDAO<Integer> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT user_id FROM public.\"RestToken\" WHERE token = ? and expiration_date > now()";

    private final String token;

    /**
     * Creates a new object for getting one user.
     *
     * @param con   the connection to the database.
     * @param token the reset_token of the change password request
     */
    public GetUserIDFromTokenDAO(final Connection con, String token) {
        super(con);
        this.token = token;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        int user_id = -1;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, token);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user_id = rs.getInt("user_id");
            }

            LOGGER.info("User successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = user_id;
    }
}
