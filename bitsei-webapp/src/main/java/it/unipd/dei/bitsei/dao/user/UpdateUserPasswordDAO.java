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
import it.unipd.dei.bitsei.resources.ChangePassword;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Update the password of a user with the given token and new password
 * and delete the token from the database.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class UpdateUserPasswordDAO extends AbstractDAO<Boolean> {

    /**
     * The SQL statement to be executed
     */
    private static final String UPDATE_STATEMENT = "UPDATE bitsei_schema.\"Owner\" SET password = ? WHERE owner_id = (Select owner_id from bitsei_schema.\"Password_Reset_Token\" WHERE token = ? and token_expiry > now())";
    private static final String DELETE_STATEMENT = "DELETE FROM bitsei_schema.\"Password_Reset_Token\" WHERE owner_id IN (SELECT owner_id FROM bitsei_schema.\"Password_Reset_Token\" WHERE token = ?)";

    // the token provided to user in the email that he has to use to reset the password
    private final String token;

    // the new password
    private final String password;

    /**
     * Creates a new object for getting one user.
     *
     * @param con  the connection to the database.
     * @param data the data containing token and password of the user
     */
    public UpdateUserPasswordDAO(final Connection con, ChangePassword data) {
        super(con);
        if (data == null) {
            throw new IllegalArgumentException("The data cannot be null.");
        }
        this.token = data.getReset_token();
        this.password = data.getPassword();
        this.outputParam = false;
    }

    @Override
    protected final void doAccess() throws SQLException {
        int rs;
        PreparedStatement pstmt = null;

        // the result of the update
        boolean is_done = false;

        try {
            // set auto-commit to false to enable transaction
            con.setAutoCommit(false);

            // update the password if the token is valid
            pstmt = con.prepareStatement(UPDATE_STATEMENT);
            pstmt.setString(1, password);
            pstmt.setString(2, token);

            rs = pstmt.executeUpdate();

            if (rs != 0) {
                // delete the token from the database
                is_done = true;
                pstmt = con.prepareStatement(DELETE_STATEMENT);
                pstmt.setString(1, token);

                pstmt.executeUpdate();
            }

            // commit the transaction
            con.commit();
        } catch (SQLException e) {
            try {
                // rollback the transaction if the update or delete fails
                con.rollback();
            } catch (SQLException e1) {
                throw e1;
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        LOGGER.info("Password Rest successfully done.");
        outputParam = is_done;
    }
}
