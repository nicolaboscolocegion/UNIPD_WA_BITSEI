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
package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.PasswordRest;

import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 * Stores a password reset token in the database.
 * The token is valid for 15 minutes.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class PasswordRestDAO extends AbstractDAO<PasswordRest> {
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Password_Reset_Token\" (owner_id, token, token_expiry) VALUES ((SELECT owner_id FROM bitsei_schema.\"Owner\" WHERE email=?), ?, ?) RETURNING *";

    // the email of the user
    private final String user_email;

    // the token created for the user
    private final String token;

    /**
     * Creates a new object for getting one user.
     *
     * @param con   the connection to the database.
     * @param email the email of the user
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
        ResultSet rs;

        PasswordRest passwordRest = null;
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, user_email);
            pstmt.setString(2, token);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                passwordRest = new PasswordRest(rs.getString("token"), rs.getDate("token_expiry"), rs.getInt("owner_id"));

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
