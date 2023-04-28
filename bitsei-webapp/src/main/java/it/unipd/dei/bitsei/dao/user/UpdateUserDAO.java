package it.unipd.dei.bitsei.dao.user;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateUserDAO extends AbstractDAO<User> {

    private final int user_id;
    private final User user;

    /**
     * Creates a new object for getting one user.
     *
     * @param con     the connection to the database.
     * @param user_id the user id to be used for getting the user.
     * @param user    the user to be updated.
     */
    public UpdateUserDAO(final Connection con, int user_id, User user) {
        super(con);
        this.user = user;
        this.user_id = user_id;
    }

    private static String GetStatement(User user) {
        String sql = "UPDATE bitsei_schema.\"Owner\" SET ";

        List<String> setClauses = new ArrayList<>();
        for (Field f : user.getClass().getDeclaredFields()) {
            setClauses.add(f.getName() + " = ?");
        }

        String setClauseString = String.join(", ", setClauses);
        sql += setClauseString;

        sql += " WHERE id = ?";
        return sql;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
//        User user = null;

        try {
            int i = 1;
            pstmt = con.prepareStatement(GetStatement(user));
            for (Field f : user.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                pstmt.setObject(i++, f.get(this.user));
            }
            pstmt.setInt(i, user_id);
            rs = pstmt.executeQuery();

//            if (rs.next()){
//                user = new User(
//                        rs.getString("name"),
//                        rs.getString("surname")
//                );
//            }

            LOGGER.info("User successfully fetched.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
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
