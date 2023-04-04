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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches invoices by their total.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 */
public class FilterInvoiceByTotalDAO extends AbstractDAO<List<Invoice>> {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE total BETWEEN ? AND ?";

    /**
     * Start total for the interval to be considered
     */
    private final double startTotal;

    /**
     * End total for the interval to be considered
     */
    private final double endTotal;

    /**
     * Parameter to order the query results by total, descending (1) or ascending(2) (default value=0)
     */
    private int orderedByTotal = 1;

    /**
     * Creates a new object for searching the invoices from startTotal to endTotal
     *
     * @param con           the connection to the database.
     * @param startTotal     the total from which to start the filtering
     * @param endTotal       the total from which to end the filtering
     */
    public FilterInvoiceByTotalDAO(final Connection con, final double startTotal, final double endTotal){
        super(con);
        this.startTotal = startTotal;
        this.endTotal = endTotal;
    }

    public FilterInvoiceByTotalDAO(final Connection con, final double startTotal, final double endTotal, final int orderedByTotal){
        super(con);
        this.startTotal = startTotal;
        this.endTotal = endTotal;
        if(orderedByTotal == 1 || orderedByTotal == 2)
            this.orderedByTotal = orderedByTotal;
    }

    /**
     * search the invoices in the period of time specified
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<Invoice> invoices = new ArrayList<Invoice>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setDouble(1, startTotal);
            pstmt.setDouble(2, endTotal);
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

            LOGGER.info("Invoices from %.2f to %.2f succesfully listed", startTotal, endTotal);
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
