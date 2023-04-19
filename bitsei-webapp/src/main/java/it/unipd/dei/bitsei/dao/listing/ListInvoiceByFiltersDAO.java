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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Filter {
    String field_name;
    Object from_value;
    Object to_value;

    String getField_name() {
        return field_name;
    }
    
    Object getFrom_value() {
        return from_value;
    }
    
    Object getTo_value() {
        return to_value;
    }
    
    void setField_name(String s) {
        field_name = s;
    }
    
    void setFrom_value(Object o) {
        from_value = o;
    }
    
    void setTo_value(Object o) {
        to_value = o;
    }
}

/**
 * List invoices associated to a company.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 */
public class ListInvoiceByFiltersDAO extends AbstractDAO<List<Invoice>> {

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

    private final boolean filterByBusinessName;

    /**
     * List of the business names of the customers associated to the invoices to list
     */
    private final List<String> fromBusinessName;

    private final boolean filterByProductTitle;

    /**
     * List of the product titles of the products associated to the invoices to list
     */
    private final List<String> fromProductTitle;

    private String FilterBetween(String field_name, boolean enableNull){
        enableNull = false;
        StringBuilder ret = new StringBuilder();
        ret.append(" AND ((" + field_name + " BETWEEN ? AND ?)");
        if(enableNull)
            ret.append(" OR (" + field_name + " IS NULL)");
        ret.append(")");
        return ret.toString();
    }

    private String FilterByStringList(String field_name, int list_size) {
        StringBuilder ret = new StringBuilder();
        ret.append(" AND (");
        for(int i=0; i<list_size; i++)
            ret.append(" (" + field_name + " = ?) OR");
        String ret_string = ret.substring(0, ret.lastIndexOf("OR"));
        return ret_string + ")";
    }


    /**
     * Creates a new object for searching the invoices from filters
     *
     * @param con           the connection to the database.
     * @param fromTotal     the total from which to start the filtering
     * @param toTotal       the total from which to end the filtering
     */
    public ListInvoiceByFiltersDAO(final Connection con, int companyId,
                                    final boolean filterByTotal, final double fromTotal, final double toTotal,
                                    final boolean filterByDiscount, final double fromDiscount, final double toDiscount,
                                    final boolean filterByPfr, final double fromPfr, final double toPfr,
                                    final boolean filterByInvoiceDate, final Date fromInvoiceDate, final Date toInvoiceDate,
                                    final boolean filterByWarningDate, final Date fromWarningDate, final Date toWarningDate,
                                    final boolean filterByBusinessName, final List<String> fromBusinessName,
                                    final boolean filterByProductTitle, final List<String> fromProductTitle){
        super(con);

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

        this.filterByBusinessName = filterByBusinessName;
        this.fromBusinessName = fromBusinessName;

        this.filterByProductTitle = filterByProductTitle;
        this.fromProductTitle = fromProductTitle;
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

            String init_query = "SELECT i.* FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id JOIN bitsei_schema.\"Product\" AS p ON cmp.company_id = p.company_id WHERE ((cmp.company_id = ?) OR 1=1)";
            query.append(init_query);

            if(filterByTotal)
                query.append(FilterBetween("i.total", true));

            if(filterByDiscount)
                query.append(FilterBetween("i.discount", true));

            if(filterByPfr)
                query.append(FilterBetween("i.pension_fund_refund", true));

            if(filterByInvoiceDate)
                query.append(FilterBetween("i.invoice_date", true));

            if(filterByWarningDate)
                query.append(FilterBetween("i.warning_date", true));

            if(filterByBusinessName)
                query.append(FilterByStringList("c.business_name", fromBusinessName.size()));
            
            if(filterByProductTitle)
                query.append(FilterByStringList("p.title", fromProductTitle.size()));

            pstmt = con.prepareStatement(query.toString());
            String param = "";
            pstmt.setInt(1, companyId);
            int i = 2;
            if(filterByTotal) {
                pstmt.setDouble(i++, fromTotal);
                pstmt.setDouble(i++, toTotal);
                param += "fromTotal: " + fromTotal + " toTotal: " + toTotal + " ";
            }
            if(filterByDiscount) {
                pstmt.setDouble(i++, fromDiscount);
                pstmt.setDouble(i++, toDiscount);
                param += "fromDiscount: " + fromDiscount + " toDiscount: " + toDiscount + " ";
            }
            if(filterByPfr) {
                pstmt.setDouble(i++, fromPfr);
                pstmt.setDouble(i++, toPfr);
                param += "fromPfr: " + fromPfr + " toPfr: " + toPfr + " ";
            }
            if(filterByInvoiceDate) {
                pstmt.setDate(i++, fromInvoiceDate);
                pstmt.setDate(i++, toInvoiceDate);
                param += "fromInvoiceDate: " + fromInvoiceDate + " toInvoiceDate: " + toInvoiceDate + " ";
            }
            if(filterByWarningDate) {
                pstmt.setDate(i++, fromWarningDate);
                pstmt.setDate(i++, toWarningDate);
                param += "fromWarningDate: " + fromWarningDate + " toWarningDate: " + toWarningDate + " ";
            }
            if(filterByBusinessName) {
                for(int j=0; j<fromBusinessName.size(); j++) {
                    param += "fromBusinessName(" + j + "): " + fromBusinessName.get(j) + " ";
                    pstmt.setString(i++, fromBusinessName.get(j));
                }
            }
            if(filterByProductTitle) {
                for (int j = 0; j < fromProductTitle.size(); j++) {
                    param += "fromProductTitle(" + j + "): " + fromProductTitle.get(j) + " ";
                    pstmt.setString(i++, fromProductTitle.get(j));
                }
            }


            LOGGER.info("## ListInvoiceByFiltersDAO: Submitted query: " + query + " ##");
            LOGGER.info("## ListInvoiceByFiltersDAO: Submitted parameters --> " + param + " ##");
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
