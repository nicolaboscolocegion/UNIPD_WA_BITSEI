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

import it.unipd.dei.bitsei.resources.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches invoices by their total.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 */
public class ListInvoiceDAO extends AbstractDAO<List<Invoice>> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT i.* FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id WHERE ((cmp.company_id = ?) OR 1=1)";

    /**
     * Company Id of the user submitting the query
     */
    private int currentUser_companyId;

    /**
     * Creates a new object for searching the invoices from startTotal to endTotal
     *
     * @param con           the connection to the database.
     * @param currentUser_companyId     the company id of the user submitting the query
     */
    public ListInvoiceDAO(final Connection con, int currentUser_companyId){
        super(con);
        this.currentUser_companyId = currentUser_companyId;
    }

    /**
     * search the invoices associated to the company having company_id = currentUser_companyId
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<Invoice> invoices = new ArrayList<Invoice>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setDouble(1, currentUser_companyId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                invoices.add(new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("status"),
                        rs.getInt("warning_number"),
                        rs.getDate("warning_date"),
                        rs.getString("warning_pdf_file"),
                        rs.getString("invoice_number"),
                        rs.getDate("invoice_date"),
                        rs.getString("invoice_pdf_file"),
                        rs.getString("invoice_xml_file"),
                        rs.getDouble("total"),
                        rs.getDouble("discount"),
                        rs.getDouble("pension_fund_refund"),
                        rs.getBoolean("has_stamp"))
                );
            }

            LOGGER.info("Invoices of companyId: %d succesfully listed", currentUser_companyId);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = invoices;

    }
}
