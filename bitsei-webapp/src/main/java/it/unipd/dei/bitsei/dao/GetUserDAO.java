package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserDAO extends AbstractDAO<User> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Owner\" WHERE email = ?";

    private final String email;

    /**
     * Creates a new object for getting one user.
     *
     * @param con   the connection to the database.
     * @param email the email of the user
     */
    public GetUserDAO(final Connection con, String email) {
        super(con);
        this.email = email;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        User user = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("owner_id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("telegram_chat_id")
                );
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

        outputParam = user;
    }
}
