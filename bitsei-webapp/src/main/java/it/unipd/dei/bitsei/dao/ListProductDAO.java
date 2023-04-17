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

import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * List invoices associated to a company.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 */
public class ListProductDAO extends AbstractDAO<List<Product>> {

    /**
     * The SQL statement to be executed
     */
    private static final String INIT_STATEMENT = "SELECT i.* FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id WHERE ((cmp.company_id = ?) OR 1=1)";

    private final String requestFor;
    private final int companyId;
    /**
    /**
     * Creates a new object for searching the invoices from startTotal to endTotal
     *
     * @param con           the connection to the database.
     */
    public ListProductDAO(final Connection con, String requestFor, int companyId){
        super(con);
        this.requestFor = requestFor;
        this.companyId = companyId;
    }

    /**
     * search the invoices associated to the company having company_id = currentUser_companyId
     */
    @Override
    protected void doAccess() throws SQLException {
        final String STATEMENT = "SELECT p.* FROM bitsei_schema.\"Product\" AS p JOIN bitsei_schema.\"Company\" AS c ON p.company_id = c.company_id WHERE ((c.company_id = ?) OR (1=1))";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        List<Product> products = new ArrayList<Product>();
        if(requestFor.equals("listAll")) {
            try {
                pstmt = con.prepareStatement(STATEMENT);
                pstmt.setInt(1, companyId);
                rs = pstmt.executeQuery();

                products = parseProductRS(rs);

                LOGGER.info("## ListProductDAO: Products of companyId: %d succesfully listed ##", companyId);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                }
            }
        }

        this.outputParam = products;

    }

    private List<Product> parseProductRS(ResultSet rs) throws SQLException {
        final List<Product> products = new ArrayList<Product>();

        while (rs.next()) {
            products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getInt("company_id"),
                    rs.getString("title"),
                    rs.getInt("default_price"),
                    rs.getString("logo"),
                    rs.getString("measurement_unit"),
                    rs.getString("description"))
            );
        }

        return products;
    }

}
