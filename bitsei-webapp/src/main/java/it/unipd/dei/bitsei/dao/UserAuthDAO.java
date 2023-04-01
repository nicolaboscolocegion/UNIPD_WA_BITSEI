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

import org.apache.logging.log4j.message.StringFormattedMessage;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Searches the user from username and password, will return true if the user have right username and passoerd
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class UserAuthDAO extends AbstractDAO<Boolean> {

    /**
	 * The SQL statement to be executed
	 */
    private static final String STATEMENT = "SELECT username FROM \"bitsei_schema.Owner\" WHERE \"bitsei_schema.Owner.password\"='pass' AND \"bitsei_schema.Owner.user\"='usr'";

    /**
     * username of the user
     */
    private final String usr;
    /**
     * hashed
     */
    private final String pass;

    /**
	 * Creates a new object for searching the user
	 *
	 * @param con    the connection to the database.
	 * @param username username of the user
     * @param password the password of the user in clear
	 */
    public UserAuthDAO(final Connection con, final String username, final String password){
        super(con);
        this.usr=username;
        this.pass= BCrypt.withDefaults().hashToString(12, password.toCharArray()); 
    }

    /**
     * search the user outputParam will be true if the user is authenticated
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
		ResultSet rs = null;

        final String tablename;

        try {
			pstmt = con.prepareStatement(STATEMENT);

			pstmt.setString(1, usr);
            pstmt.setString(2, pass);

			rs = pstmt.executeQuery();
            tablename = rs.getString("user");

		} finally {
			if (rs != null) {
                rs.close();
			}

			if (pstmt != null) {
				pstmt.close();
			}
		}

        if(tablename==usr){
            this.outputParam=true;
        }

    }
}
