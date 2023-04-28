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

package it.unipd.dei.bitsei.dao.listing;

import it.unipd.dei.bitsei.dao.AbstractDAO;
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
public class ListInvoiceDAO extends AbstractDAO<List<Invoice>> {

    /**
     * The SQL statement to be executed in order to check user authorization for this resource
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" as cmp WHERE cmp.company_id = ? AND cmp.owner_id = ?";

    private final int ownerId;
    private final int companyId;

    /**
     * /**
     * Creates a new object for searching the invoices from startTotal to endTotal
     *
     * @param con       the connection to the database.
     * @param ownerId   the id of the owner of the company
     * @param companyId the id of the company
     */
    public ListInvoiceDAO(final Connection con, int ownerId, int companyId) {
        super(con);
        this.ownerId = ownerId;
        this.companyId = companyId;
    }

    /**
     * search the invoices associated to the company having company_id = currentUser_companyId
     */
    @Override
    protected void doAccess() throws SQLException {
        //final String STATEMENT = "SELECT i.* FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id JOIN bitsei_schema.\"Product\" AS p ON cmp.company_id = p.company_id WHERE ((cmp.owner_id = ?) OR 1=1) AND ((cmp.company_id = ?) OR 1=1)";
        final String STATEMENT = "SELECT i.* FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id JOIN bitsei_schema.\"Product\" AS p ON cmp.company_id = p.company_id WHERE cmp.owner_id = ? AND cmp.company_id = ?;";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        List<Invoice> invoices = new ArrayList<Invoice>();

        // check if the user is allowed for this resource
        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, companyId);
            pstmt.setInt(2, ownerId);
            rs_check = pstmt.executeQuery();

            if (!rs_check.next()) {
                LOGGER.error("## ListInvoiceDAO: Error on fetching data from database ##");
                throw new SQLException();
            }

            if (rs_check.getInt("c") == 0) {
                LOGGER.error("## ListInvoiceDAO: Data access violation ##");
                throw new IllegalAccessException();
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }

        // perform the actual search
        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, ownerId);
            pstmt.setInt(2, companyId);
            rs = pstmt.executeQuery();

            invoices = parseInvoiceRS(rs);

            LOGGER.info("## ListInvoiceDAO: Invoices of ownerId: %d and companyId: %d succesfully listed ##", ownerId, companyId);
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

    private List<Invoice> parseInvoiceRS(ResultSet rs) throws SQLException {
        final List<Invoice> invoices = new ArrayList<Invoice>();

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

        return invoices;
    }


}
