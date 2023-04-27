/*
 * Copyright 2022-2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.bitsei.dao.user;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Retrieves a user from the database with the given email.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
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
