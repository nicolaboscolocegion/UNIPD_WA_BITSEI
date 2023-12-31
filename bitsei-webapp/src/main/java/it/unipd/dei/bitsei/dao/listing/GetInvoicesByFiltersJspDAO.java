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
public class GetInvoicesByFiltersJspDAO extends AbstractDAO<List<Invoice>> {

    /**
     * The SQL statement to be executed
     */
    // Query with all the filters
    //private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE (total BETWEEN ? AND ?) AND (discount BETWEEN ? AND ?) AND (pension_fund_refund BETWEEN ? AND ?) AND (invoice_date BETWEEN ? AND ?) AND (warning_date BETWEEN ? AND ?) AND ()";

    // Query to show results with null fields (just for debug purposes)
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE ((total BETWEEN ? AND ?) OR (total IS NULL)) AND ((discount BETWEEN ? AND ?) OR (discount IS NULL)) AND ((pension_fund_refund BETWEEN ? AND ?) OR (pension_fund_refund IS NULL))  AND ((invoice_date BETWEEN ? AND ?) OR (invoice_date IS NULL)) AND ((warning_date BETWEEN ? AND ?) OR (warning_date IS NULL))";

    private final boolean filterByTotal;
    /**
     * Start total for the interval to be considered
     */
    private final double startTotal;

    /**
     * End total for the interval to be considered
     */
    private final double endTotal;

    private final boolean filterByDiscount;
    /**
     * Start discount for the interval to be considered
     */
    private final double startDiscount;

    /**
     * End discount for the interval to be considered
     */
    private final double endDiscount;

    private final boolean filterByPfr;
    /**
     * Start pension fund refund for the interval to be considered
     */
    private final double startPfr;

    /**
     * End pension fund refund for the interval to be considered
     */
    private final double endPfr;

    private final boolean filterByInvoiceDate;
    /**
     * Start Invoice Date for the period of time to be considered
     */
    private final Date startInvoiceDate;


    /**
     * End Invoice Date for the period of time to be considered
     */
    private final Date endInvoiceDate;

    private final boolean filterByWarningDate;
    /**
     * Start Warning Date for the period of time to be considered
     */
    private final Date startWarningDate;

    /**
     * End Warning Date for the period of time to be considered
     */
    private final Date endWarningDate;

    private String FilterBetween(String field_name, boolean firstFilter, boolean enableNull) {
        StringBuilder ret = new StringBuilder();
        if (firstFilter)
            ret.append(" WHERE ");
        else
            ret.append(" AND ");
        ret.append("((" + field_name + " BETWEEN ? AND ?)");
        if (enableNull)
            ret.append(" OR (" + field_name + " IS NULL)");
        ret.append(")");
        return ret.toString();
    }


    /**
     * Creates a new object for searching the invoices from startTotal to endTotal
     *
     * @param con                 the connection to the database.
     * @param startTotal          the total from which to start the filtering
     * @param endTotal            the total from which to end the filtering
     * @param startDiscount       the discount from which to start the filtering
     * @param endDiscount         the discount from which to end the filtering
     * @param startPfr            the pension fund refund from which to start the filtering
     * @param endPfr              the pension fund refund from which to end the filtering
     * @param startInvoiceDate    the invoice date from which to start the filtering
     * @param endInvoiceDate      the invoice date from which to end the filtering
     * @param startWarningDate    the warning date from which to start the filtering
     * @param endWarningDate      the warning date from which to end the filtering
     * @param filterByDiscount    true if the discount filter is enabled, false otherwise
     * @param filterByPfr         true if the pension fund refund filter is enabled, false otherwise
     * @param filterByInvoiceDate true if the invoice date filter is enabled, false otherwise
     * @param filterByWarningDate true if the warning date filter is enabled, false otherwise
     * @param filterByTotal       true if the total filter is enabled, false otherwise
     */
    public GetInvoicesByFiltersJspDAO(final Connection con,
                                      final boolean filterByTotal, final double startTotal, final double endTotal,
                                      final boolean filterByDiscount, final double startDiscount, final double endDiscount,
                                      final boolean filterByPfr, final double startPfr, final double endPfr,
                                      final boolean filterByInvoiceDate, final Date startInvoiceDate, final Date endInvoiceDate,
                                      final boolean filterByWarningDate, final Date startWarningDate, final Date endWarningDate) {
        super(con);

        this.filterByTotal = filterByTotal;
        this.startTotal = startTotal;
        this.endTotal = endTotal;

        this.filterByDiscount = filterByDiscount;
        this.startDiscount = startDiscount;
        this.endDiscount = endDiscount;

        this.filterByPfr = filterByPfr;
        this.startPfr = startPfr;
        this.endPfr = endPfr;

        this.filterByInvoiceDate = filterByInvoiceDate;
        this.startInvoiceDate = startInvoiceDate;
        this.endInvoiceDate = endInvoiceDate;

        this.filterByWarningDate = filterByWarningDate;
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
            StringBuilder query = new StringBuilder();
            boolean firstFilter = true;

            String init_query = "SELECT * FROM bitsei_schema.\"Invoice\"";
            query.append(init_query);

            /*
            if(filterByTotal) {
                query.append(" ((total BETWEEN ? AND ?) OR (total IS NULL))");
                firstFilter = false;
            }

            if(filterByDiscount) {
                if(!firstFilter)
                    query.append(" AND");
                query.append(" ((discount BETWEEN ? AND ?) OR (discount IS NULL))");
                firstFilter = false;
            }

            if(filterByPfr){
                if(!firstFilter)
                    query.append(" AND");
                query.append(" ((pension_fund_refund BETWEEN ? AND ?) OR (pension_fund_refund IS NULL))");
                firstFilter = false;
            }

            if(filterByInvoiceDate){
                if(!firstFilter)
                    query.append(" AND");
                query.append(" ((invoice_date BETWEEN ? AND ?) OR (invoice_date IS NULL))");
                firstFilter = false;
            }


            if(filterByWarningDate){
                if(!firstFilter)
                    query.append(" AND");
                query.append(" ((warning_date BETWEEN ? AND ?) OR (warning_date IS NULL))");
                firstFilter = false;
            }
            */

            if (filterByTotal) {
                query.append(FilterBetween("total", firstFilter, true));
                firstFilter = false;
            }

            if (filterByDiscount) {
                query.append(FilterBetween("discount", firstFilter, true));
                firstFilter = false;
            }

            if (filterByPfr) {
                query.append(FilterBetween("pension_fund_refund", firstFilter, true));
                firstFilter = false;
            }

            if (filterByInvoiceDate) {
                query.append(FilterBetween("invoice_date", firstFilter, true));
                firstFilter = false;
            }

            if (filterByWarningDate) {
                query.append(FilterBetween("warning_date", firstFilter, true));
                firstFilter = false;
            }

            pstmt = con.prepareStatement(query.toString());
            String param = "";
            int i = 1;
            if (filterByTotal) {
                pstmt.setDouble(i++, startTotal);
                pstmt.setDouble(i++, endTotal);
                param += "startTotal: " + startTotal + " endTotal: " + endTotal + " ";
            }
            if (filterByDiscount) {
                pstmt.setDouble(i++, startDiscount);
                pstmt.setDouble(i++, endDiscount);
                param += "startDiscount: " + startDiscount + " endDiscount: " + endDiscount + " ";
            }
            if (filterByPfr) {
                pstmt.setDouble(i++, startPfr);
                pstmt.setDouble(i++, endPfr);
                param += "startPfr: " + startPfr + "endPfr: " + endPfr + " ";
            }
            if (filterByInvoiceDate) {
                pstmt.setDate(i++, startInvoiceDate);
                pstmt.setDate(i++, endInvoiceDate);
                param += "startInvoiceDate: " + startInvoiceDate + "endInvoiceDate: " + endInvoiceDate + " ";
            }
            if (filterByWarningDate) {
                pstmt.setDate(i++, startWarningDate);
                pstmt.setDate(i++, endWarningDate);
                param += "startWarningDate: " + startWarningDate + "endWarningDate: " + endWarningDate + " ";
            }

            LOGGER.info("## DAO: Submitted query: " + query + " ##");
            LOGGER.info("## DAO: Submitted parameters --> " + param + " ##");
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
