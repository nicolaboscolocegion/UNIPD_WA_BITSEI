package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists all the users in the database.
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class ListUserDAO extends AbstractDAO<List<User>> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Owner\"";

    /**
     * Creates a new object for listing all the users.
     *
     * @param con the connection to the database.
     */
    public ListUserDAO(final Connection con) {
        super(con);
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<User> users = new ArrayList<User>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(
                        new User(
                                rs.getInt("owner_id"),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("telegram_chat_id")
                        )
                );
            }

            LOGGER.info("User(s) successfully listed.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = users;
    }
}
