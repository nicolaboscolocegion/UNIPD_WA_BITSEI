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

import it.unipd.dei.bitsei.resources.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists all the companies in the database.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public final class DeleteCompanyDAO extends AbstractDAO<Boolean> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "DELETE FROM bitsei_schema.\"Company\" WHERE owner_id = ? AND company_id = ?";

    // the id of the owner
    private final int owner_id;
    private final int company_id;


    /**
     * Creates a new object for listing all the users.
     *
     * @param con the connection to the database.
     */
    public DeleteCompanyDAO(final Connection con, final int company_id, final int owner_id) {
        super(con);
        this.owner_id = owner_id;
        this.company_id = company_id;
        this.outputParam = false;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT)) {
            pstmt.setInt(1, owner_id);
            pstmt.setInt(2, company_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                outputParam = true;
            }
            LOGGER.info("Company successfully delete.");
        }
    }
}
