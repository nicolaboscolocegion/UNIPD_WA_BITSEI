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
public final class ListCompaniesDAO extends AbstractDAO<List<Company>> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Company\" WHERE owner_id = ?";

    // the id of the owner
    private final int owner_id;


    /**
     * Creates a new object for listing all the users.
     *
     * @param con the connection to the database.
     */
    public ListCompaniesDAO(final Connection con, final int owner_id) {
        super(con);
        this.owner_id = owner_id;
    }

    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<Company> companies = new ArrayList<Company>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, owner_id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                companies.add(
                        new Company(
                                rs.getInt("company_id"),
                                rs.getString("title"),
                                rs.getString("business_name"),
                                rs.getString("vat_number"),
                                rs.getString("tax_code"),
                                rs.getString("address"),
                                rs.getString("province"),
                                rs.getString("city"),
                                rs.getString("postal_code"),
                                rs.getString("unique_code"),
                                rs.getBoolean("has_mail_notifications"),
                                rs.getBoolean("has_telegram_notifications")
                        )
                );
            }

            LOGGER.info("Companies successfully listed.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = companies;
    }
}
