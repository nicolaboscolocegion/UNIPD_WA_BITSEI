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


import at.favre.lib.crypto.bcrypt.BCrypt;
import it.unipd.dei.bitsei.resources.LoginResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Searches the user from username and password, will return true if the user have right username and passoerd
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class UserAuthDAO extends AbstractDAO<Integer> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT password, owner_id FROM bitsei_schema.\"Owner\" WHERE email = ?";

    /**
     * username of the user
     */
    private final String email;

    /**
     * hashed
     */
    private final String pass;

    /**
     * Creates a new object for searching the user
     *
     * @param con           the connection to the database.
     * @param loginResource the username and password of the user
     */
    public UserAuthDAO(final Connection con, final LoginResource loginResource) {
        super(con);

        if (loginResource.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        this.email = loginResource.getEmail();

        if (loginResource.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        this.pass = loginResource.getPassword();

        this.outputParam = -1;
    }

    /**
     * search the user outputParam will be true if the user is authenticated
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final String hashPassword;
        final int owner_id;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if(!rs.next()) {
                return;
            }
            LOGGER.info("User successfully found." + email + rs.getString("password"));

            hashPassword = rs.getString("password");
            owner_id = rs.getInt("owner_id");

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), hashPassword);
        if (result.verified) {
            outputParam = owner_id;
        }

    }
}
