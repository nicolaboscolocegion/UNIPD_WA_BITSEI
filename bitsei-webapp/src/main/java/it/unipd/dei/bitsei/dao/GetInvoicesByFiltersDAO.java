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
import java.sql.Date;

/**
 * Searches invoices by their total.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 */
public class GetInvoicesByFiltersDAO extends AbstractDAO<List<Invoice>> {

    /**
     * The SQL statement to be executed
     */
    // Query with all the filters
    //private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE (total BETWEEN ? AND ?) AND (discount BETWEEN ? AND ?) AND (pension_fund_refund BETWEEN ? AND ?) AND (invoice_date BETWEEN ? AND ?) AND (warning_date BETWEEN ? AND ?) AND ()";

    // Query to show results with null fields (just for debug purposes)
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE ((total BETWEEN ? AND ?) OR (total IS NULL)) AND ((discount BETWEEN ? AND ?) AND ((pension_fund_refund BETWEEN ? AND ?) OR (pension_fund_refund IS NULL)) OR (discount IS NULL)) AND ((invoice_date BETWEEN ? AND ?) OR (invoice_date IS NULL)) AND ((warning_date BETWEEN ? AND ?) OR (warning_date IS NULL))";

    /**
     * Start total for the interval to be considered
     */
    private final double startTotal;

    /**
     * End total for the interval to be considered
     */
    private final double endTotal;

    /**
     * Start discount for the interval to be considered
     */
    private final double startDiscount;

    /**
     * End discount for the interval to be considered
     */
    private final double endDiscount;

    /**
     * Start pension fund refund for the interval to be considered
     */
    private final double startPfr;

    /**
     * End pension fund refund for the interval to be considered
     */
    private final double endPfr;

    /**
     * Start Invoice Date for the period of time to be considered
     */
    private final Date startInvoiceDate;


    /**
     * End Invoice Date for the period of time to be considered
     */
    private final Date endInvoiceDate;

    /**
     * Start Warning Date for the period of time to be considered
     */
    private final Date startWarningDate;


    /**
     * End Warning Date for the period of time to be considered
     */
    private final Date endWarningDate;

    /**
     * Creates a new object for searching the invoices from startTotal to endTotal
     *
     * @param con           the connection to the database.
     * @param startTotal     the total from which to start the filtering
     * @param endTotal       the total from which to end the filtering
     */
    public GetInvoicesByFiltersDAO(final Connection con,
                                   final double startTotal, final double endTotal,
                                   final double startDiscount, final double endDiscount,
                                   final double startPfr, final double endPfr,
                                   final Date startInvoiceDate, final Date endInvoiceDate,
                                   final Date startWarningDate, final Date endWarningDate){
        super(con);

        this.startTotal = startTotal;
        this.endTotal = endTotal;

        this.startDiscount = startDiscount;
        this.endDiscount = endDiscount;

        this.startPfr = startPfr;
        this.endPfr = endPfr;

        this.startInvoiceDate = startInvoiceDate;
        this.endInvoiceDate = endInvoiceDate;

        this.startWarningDate = startWarningDate;
        this.endWarningDate = endWarningDate;
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
            pstmt.setDouble(3, startDiscount);
            pstmt.setDouble(4, endDiscount);
            pstmt.setDouble(5, startPfr);
            pstmt.setDouble(6, endPfr);
            pstmt.setDate(7, startInvoiceDate);
            pstmt.setDate(8, endInvoiceDate);
            pstmt.setDate(9, startWarningDate);
            pstmt.setDate(10, endWarningDate);

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

            LOGGER.info("## DAO: Invoices succesfully listed ##");
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
