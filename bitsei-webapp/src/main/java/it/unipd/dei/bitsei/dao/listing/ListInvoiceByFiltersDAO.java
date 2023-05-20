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
import it.unipd.dei.bitsei.resources.InvoiceContainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * List invoices associated to a company.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 */
public class ListInvoiceByFiltersDAO extends AbstractDAO<List<InvoiceContainer>> {

    /**
     * The SQL statement to be executed in order to check user authorization for this resource
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" as cmp WHERE cmp.company_id = ? AND cmp.owner_id = ?";

    private final int ownerId;
    private final int companyId;

    private final boolean filterByTotal;
    /**
     * Start total for the interval to be considered
     */
    private final double fromTotal;

    /**
     * End total for the interval to be considered
     */
    private final double toTotal;

    private final boolean filterByDiscount;
    /**
     * Start discount for the interval to be considered
     */
    private final double fromDiscount;

    /**
     * End discount for the interval to be considered
     */
    private final double toDiscount;

    private final boolean filterByPfr;
    /**
     * Start pension fund refund for the interval to be considered
     */
    private final double fromPfr;

    /**
     * End pension fund refund for the interval to be considered
     */
    private final double toPfr;

    private final boolean filterByInvoiceDate;
    /**
     * Start Invoice Date for the period of time to be considered
     */
    private final Date fromInvoiceDate;


    /**
     * End Invoice Date for the period of time to be considered
     */
    private final Date toInvoiceDate;

    private final boolean filterByWarningDate;
    /**
     * Start Warning Date for the period of time to be considered
     */
    private final Date fromWarningDate;

    /**
     * End Warning Date for the period of time to be considered
     */
    private final Date toWarningDate;

    private final boolean filterByCustomerId;

    /**
     * List of the business names of the customers associated to the invoices to list
     */
    private final List<Integer> fromCustomerId;

    private final boolean filterByProductId;

    /**
     * List of the product titles of the products associated to the invoices to list
     */
    private final List<Integer> fromProductId;


    private String FilterBetween(String field_name, boolean enableNull) {
        enableNull = false;
        StringBuilder ret = new StringBuilder();
        ret.append(" AND ((" + field_name + " BETWEEN ? AND ?)");
        if (enableNull)
            ret.append(" OR (" + field_name + " IS NULL)");
        ret.append(")");
        return ret.toString();
    }

    private String FilterByStringList(String field_name, int list_size) {
        StringBuilder ret = new StringBuilder();
        ret.append(" AND (");
        for (int i = 0; i < list_size; i++)
            ret.append(" (" + field_name + " = ?) OR");
        String ret_string = ret.substring(0, ret.lastIndexOf("OR"));
        return ret_string + ")";
    }


    /**
     * Creates a new object for searching the invoices from filters
     *
     * @param con                  the connection to the database.
     * @param ownerId              the id of the owner of the company
     * @param companyId            the id of the company
     * @param fromTotal            the total from which to start the filtering
     * @param toTotal              the total from which to end the filtering
     * @param fromDiscount         the discount from which to start the filtering
     * @param toDiscount           the discount from which to end the filtering
     * @param fromPfr              the pension fund refund from which to start the filtering
     * @param toPfr                the pension fund refund from which to end the filtering
     * @param fromInvoiceDate      the invoice date from which to start the filtering
     * @param toInvoiceDate        the invoice date from which to end the filtering
     * @param fromWarningDate      the warning date from which to start the filtering
     * @param toWarningDate        the warning date from which to end the filtering
     * @param fromCustomerId       the customer id from which to start the filtering
     * @param fromProductId        the product id from which to start the filtering
     * @param filterByDiscount     true if the discount filter is enabled, false otherwise
     * @param filterByTotal        true if the total filter is enabled, false otherwise
     * @param filterByPfr          true if the pension fund refund filter is enabled, false otherwise
     * @param filterByInvoiceDate  true if the invoice date filter is enabled, false otherwise
     * @param filterByWarningDate  true if the warning date filter is enabled, false otherwise
     * @param filterByCustomerId true if the customer id filter is enabled, false otherwise
     * @param filterByProductId true if the product id filter is enabled, false otherwise
     */
    public ListInvoiceByFiltersDAO(final Connection con, int ownerId, int companyId,
                                   final boolean filterByTotal, final double fromTotal, final double toTotal,
                                   final boolean filterByDiscount, final double fromDiscount, final double toDiscount,
                                   final boolean filterByPfr, final double fromPfr, final double toPfr,
                                   final boolean filterByInvoiceDate, final Date fromInvoiceDate, final Date toInvoiceDate,
                                   final boolean filterByWarningDate, final Date fromWarningDate, final Date toWarningDate,
                                   final boolean filterByCustomerId, final List<Integer> fromCustomerId,
                                   final boolean filterByProductId, final List<Integer> fromProductId) {
        super(con);

        this.ownerId = ownerId;
        this.companyId = companyId;

        this.filterByTotal = filterByTotal;
        this.fromTotal = fromTotal;
        this.toTotal = toTotal;

        this.filterByDiscount = filterByDiscount;
        this.fromDiscount = fromDiscount;
        this.toDiscount = toDiscount;

        this.filterByPfr = filterByPfr;
        this.fromPfr = fromPfr;
        this.toPfr = toPfr;

        this.filterByInvoiceDate = filterByInvoiceDate;
        this.fromInvoiceDate = fromInvoiceDate;
        this.toInvoiceDate = toInvoiceDate;

        this.filterByWarningDate = filterByWarningDate;
        this.fromWarningDate = fromWarningDate;
        this.toWarningDate = toWarningDate;

        this.filterByCustomerId = filterByCustomerId;
        this.fromCustomerId = fromCustomerId;

        this.filterByProductId = filterByProductId;
        this.fromProductId = fromProductId;
    }

    /**
     * search the invoices in the period of time specified
     */
    @Override
    protected void doAccess() throws SQLException {
        //String init_query = "SELECT i.* FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id JOIN bitsei_schema.\"Product\" AS p ON cmp.company_id = p.company_id WHERE ((cmp.owner_id = ?) OR 1=1) AND ((cmp.company_id = ?) OR 1=1)";
        String init_query = "SELECT i.*, c.business_name, p.product_id, p.title FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id JOIN bitsei_schema.\"Product\" AS p ON cmp.company_id = p.company_id WHERE cmp.owner_id = ? AND cmp.company_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        final List<InvoiceContainer> invoices = new ArrayList<InvoiceContainer>();

        // check if the user is allowed for this resource
        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, companyId);
            pstmt.setInt(2, ownerId);
            rs_check = pstmt.executeQuery();

            if (!rs_check.next()) {
                LOGGER.error("## ListInvoiceByFiltersDAO: Error on fetching data from database ##");
                throw new SQLException();
            }

            if (rs_check.getInt("c") == 0) {
                LOGGER.error("## ListInvoiceByFiltersDAO: Data access violation ##");
                throw new IllegalAccessException();
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }

        // perform the actual search
        try {
            StringBuilder query = new StringBuilder();
            query.append(init_query);

            if (filterByTotal)
                query.append(FilterBetween("i.total", true));

            if (filterByDiscount)
                query.append(FilterBetween("i.discount", true));

            if (filterByPfr)
                query.append(FilterBetween("i.pension_fund_refund", true));

            if (filterByInvoiceDate)
                query.append(FilterBetween("i.invoice_date", true));

            if (filterByWarningDate)
                query.append(FilterBetween("i.warning_date", true));

            if (filterByCustomerId)
                query.append(FilterByStringList("c.customer_id", fromCustomerId.size()));

            if (filterByProductId)
                query.append(FilterByStringList("p.product_id", fromProductId.size()));

            query.append(";");

            pstmt = con.prepareStatement(query.toString());
            String param = "";
            pstmt.setInt(1, ownerId);
            pstmt.setInt(2, companyId);
            param += "ownerId: " + ownerId + " companyId: " + companyId + " ";
            int i = 3;
            if (filterByTotal) {
                pstmt.setDouble(i++, fromTotal);
                pstmt.setDouble(i++, toTotal);
                param += "fromTotal: " + fromTotal + " toTotal: " + toTotal + " ";
            }
            if (filterByDiscount) {
                pstmt.setDouble(i++, fromDiscount);
                pstmt.setDouble(i++, toDiscount);
                param += "fromDiscount: " + fromDiscount + " toDiscount: " + toDiscount + " ";
            }
            if (filterByPfr) {
                pstmt.setDouble(i++, fromPfr);
                pstmt.setDouble(i++, toPfr);
                param += "fromPfr: " + fromPfr + " toPfr: " + toPfr + " ";
            }
            if (filterByInvoiceDate) {
                pstmt.setDate(i++, fromInvoiceDate);
                pstmt.setDate(i++, toInvoiceDate);
                param += "fromInvoiceDate: " + fromInvoiceDate + " toInvoiceDate: " + toInvoiceDate + " ";
            }
            if (filterByWarningDate) {
                pstmt.setDate(i++, fromWarningDate);
                pstmt.setDate(i++, toWarningDate);
                param += "fromWarningDate: " + fromWarningDate + " toWarningDate: " + toWarningDate + " ";
            }
            if (filterByCustomerId) {
                for (int j = 0; j < fromCustomerId.size(); j++) {
                    param += "fromCustomerId(" + j + "): " + fromCustomerId.get(j) + " ";
                    pstmt.setInt(i++, fromCustomerId.get(j));
                }
            }
            if (filterByProductId) {
                for (int j = 0; j < fromProductId.size(); j++) {
                    param += "fromProductId(" + j + "): " + fromProductId.get(j) + " ";
                    pstmt.setInt(i++, fromProductId.get(j));
                }
            }


            LOGGER.info("## ListInvoiceByFiltersDAO: Submitted query: " + query + " ##");
            LOGGER.info("## ListInvoiceByFiltersDAO: Submitted parameters --> " + param + " ##");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                invoices.add(new InvoiceContainer(
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
                        rs.getBoolean("has_stamp"),
                        rs.getString("business_name"),
                        rs.getInt("product_id"),
                        rs.getString("title"))
                );
            }

            LOGGER.info("## ListInvoiceByFiltersDAO: Invoices succesfully listed ##");
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
