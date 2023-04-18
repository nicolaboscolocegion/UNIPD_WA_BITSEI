/*
 * Copyright 2018-2023 University of Padua, Italy
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

package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.listing.FilterInvoiceByDateDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;
import java.text.SimpleDateFormat;

/**
 * Searches invoices by their period of time.
*
* @author Christian Marchiori
* @version 1.00
* @since 1.00
*
*/
public final class ChartServlet extends AbstractDatabaseServlet {

    /**
     * Searches invoices by their period of time.
    *
    * @param req the HTTP request from the client.
    * @param res the HTTP response from the server.
    *
    * @throws IOException if any error occurs in the client/server communication.
    */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.FILTER_INVOICES_BY_PERIOD);

        final Date BASE_DATE = new Date(70, 01, 01);
        LocalDate currentDate = LocalDate.now();
        final Date CURRENT_DATE = Date.valueOf(currentDate);
        // request parameter
        Date startDate = BASE_DATE;
        Date endDate = BASE_DATE;

        // model
        List<Invoice> el = null;
        Message m = null;

        try {

            // retrieves the request parameter
            try {
                startDate = Date.valueOf(req.getParameter("startDate"));
            } catch (IllegalArgumentException ex) {
                startDate = BASE_DATE;
            }

            try {
                endDate = Date.valueOf(req.getParameter("endDate"));
            } catch(IllegalArgumentException ex){
                endDate = CURRENT_DATE;
            }

            // creates a new object for accessing the database and searching the employees
            el = new FilterInvoiceByDateDAO(getConnection(), startDate, endDate).access().getOutputParam();

            String tmp_string = "Invoices succesfully searched. Start Date: " + startDate.toString() + " End Date: " + endDate.toString() + ".";
            m = new Message(tmp_string);

            LOGGER.info("Invoices successfully searched by startDate %s and endDate %s.", startDate.toString(), endDate.toString());

        } catch (IllegalArgumentException ex) {
            m = new Message("Cannot search for invoices. Invalid input parameters: date must be in the format yyyy-[m]m-[d]d.", "E100",
                    ex.getMessage());

            LOGGER.error("Cannot search for invoices. Invalid input parameters: date must be in the format yyyy-[m]m-[d]d.", ex);
        } catch (SQLException ex) {
            m = new Message("Cannot search for invoices: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot search for invoices: unexpected error while accessing the database.", ex);
        }
    
        //INVOICE BY DATE (DAY)
        HashMap<String,Integer> map_numb_day = new HashMap<>();
        for (Invoice i: el){
            
            map_numb_day.put(new SimpleDateFormat("d/MM/yyyy").format(i.getInvoice_date()),
            1+map_numb_day.getOrDefault(new SimpleDateFormat("d/MM/yyyy").format(i.getInvoice_date()), 0));

        }

        //INVOICE BY DATE (MONTH)
        HashMap<String,Integer> map_numb_month = new HashMap<>();
        for (Invoice i: el){
            
            map_numb_month.put(new SimpleDateFormat("MMMM yyyy").format(i.getInvoice_date()),
            1+map_numb_month.getOrDefault(new SimpleDateFormat("MMMM yyyy").format(i.getInvoice_date()), 0));

        }

        //INVOICE BY DATE (MONTH)
        TreeMap<Date,Integer> tmap_numb_month = new TreeMap<>();
        for (Invoice i: el){
            
            tmap_numb_month.put(new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1),
            1+tmap_numb_month.getOrDefault(new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1), 0));

        }

        //INVOICE BY DATE (YEAR)
        HashMap<String,Integer> map_numb_year = new HashMap<>();
        for (Invoice i: el){
            
            map_numb_year.put(new SimpleDateFormat("yyyy").format(i.getInvoice_date()),
            1+map_numb_year.getOrDefault(new SimpleDateFormat("yyyy").format(i.getInvoice_date()), 0));

        }

        //TOTAL BY DATE (DAY)
        HashMap<String,Double> map_total_day = new HashMap<>();
        for (Invoice i: el){
            
            map_total_day.put(new SimpleDateFormat("d/MM/yyyy").format(i.getInvoice_date()),
            i.getTotal()+map_total_day.getOrDefault(new SimpleDateFormat("d/MM/yyyy").format(i.getInvoice_date()), 0.0));

        }

        //TOTAL BY DATE (MONTH)
        HashMap<String,Double> map_total_month = new HashMap<>();
        for (Invoice i: el){
            
            map_total_month.put(new SimpleDateFormat("MMMM yyyy").format(i.getInvoice_date()),
            i.getTotal()+map_total_month.getOrDefault(new SimpleDateFormat("MMMM yyyy").format(i.getInvoice_date()), 0.0));

        }

        //TOTAL BY DATE (YEAR)
        HashMap<String,Double> map_total_year = new HashMap<>();
        for (Invoice i: el){
            
            map_total_year.put(new SimpleDateFormat("yyyy").format(i.getInvoice_date()),
            i.getTotal()+map_total_year.getOrDefault(new SimpleDateFormat("yyyy").format(i.getInvoice_date()), 0.0));

        }

        //INVOICE BY CUSTOMER
        HashMap<Integer,Integer> map_numb_customer = new HashMap<>();
        for (Invoice i: el){
            
            map_numb_customer.put(i.getCustomer_id(),
            1+map_numb_customer.getOrDefault(i.getCustomer_id(), 0));

        }

        try {
            // set the MIME media type of the response
            res.setContentType("text/html; charset=utf-8");

            // get a stream to write the response
            PrintWriter out = res.getWriter();

            // write the HTML page
            out.printf("<!DOCTYPE html>%n");

            out.printf("<html lang=\"en\">%n");
            out.printf("<head>%n");
            out.printf("<meta charset=\"utf-8\">%n");
            out.printf("<title>Filter invoices by period</title>%n");
            out.printf("</head>%n");

            out.printf("<body>%n");
            out.printf("<h1>Filter invoices by period</h1>%n");
            out.printf("<hr/>%n");

            if (m.isError()) {
                out.printf("<ul>%n");
                out.printf("<li>error code: %s</li>%n", m.getErrorCode());
                out.printf("<li>message: %s</li>%n", m.getMessage());
                out.printf("<li>details: %s</li>%n", m.getErrorDetails());
                out.printf("</ul>%n");
            } else {
                out.printf("<p>%s</p>%n", m.getMessage());

                /* THIS PART UNCOMMENTED SHOWS EVERY SINGLE DETAIL ABOUT AN INVOICE
                out.printf("<table>%n");
                out.printf("<tr>%n");
                out.printf("<td>Invoice id</td><td>Customer id</td><td>Status</td><td>Warning date</td><td>Warning pdf file</td><td>Invoice number</td><td>Invoice date</td><td>Invoice pdf file</td><td>Invoice xml file</td><td>Total</td><td>Discount</td><td>Pension fund refund</td><td>Has stamp</td><%n");
                out.printf("</tr>%n");


                for (Invoice e : el) {
                    out.printf("<tr>%n");
                    out.printf("<td>%d</td><td>%d</td><td>%d</td><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%s</td>%n",
                            e.getInvoice_id(),
                            e.getCustomer_id(),
                            e.getStatus(),
                            e.getWarning_number(),
                            e.getWarning_date().toString(),
                            e.getWarning_pdf_file(),
                            e.getInvoice_number(),
                            e.getInvoice_date().toString(),
                            e.getInvoice_pdf_file(),
                            e.getInvoice_xml_file(),
                            e.getTotal(),
                            e.getDiscount(),
                            e.getPension_fund_refund(),
                            e.hasStamp()
                            );
                    out.printf("</tr>%n");
                }
                */

                out.printf("<table>%n");
                out.printf("<tr>%n");
                out.printf("<td>Invoice id</td><td>Customer id</td><td>Status</td><td>Invoice number</td><td>Invoice date</td><td>Total</td><td>Discount</td><td>Pension fund refund</td>%n");
                out.printf("</tr>%n");


                for (Invoice e : el) {
                    out.printf("<tr>%n");
                    out.printf("<td>%d</td><td>%d</td><td>%d</td><td>%s</td><td>%s</td><td>%.2f</td><td>%.2f</td><td>%.2f</td>",
                            e.getInvoice_id(),
                            e.getCustomer_id(),
                            e.getStatus(),
                            e.getInvoice_number(),
                            e.getInvoice_date(),
                            e.getTotal(),
                            e.getDiscount(),
                            e.getPension_fund_refund()


                    );
                    out.printf("</tr>%n");
                }
                out.printf("</table>%n");
                
                //TABLE 2
                out.printf("<table>%n");
                out.printf("<tr>%n");
                out.printf("<td>Date</td><td>Amount</td>%n");
                out.printf("</tr>%n");
                //map_numb_month
                /*
                for (Map.Entry<AbstractMap.SimpleEntry<String,String>,Integer> e: map_numb_month.entrySet()) {
                    out.printf("<tr>%n");
                    out.printf("<td>%s</td><td>%d</td>%n",
                            e.getKey().getValue()+" "+e.getKey().getKey(),
                            e.getValue()
                    );
                    out.printf("</tr>%n");
                }
                */
                //tmap_numb_month
                int size = tmap_numb_month.size();
                for (int i=0;i<size;i++) {
                    Date d = tmap_numb_month.firstKey();
                    out.printf("<tr>%n");
                    out.printf("<td>%s</td><td>%d</td>%n",
                            new SimpleDateFormat("MMMM yyyy").format(d),
                            tmap_numb_month.get(d)
                    );
                    out.printf("</tr>%n");
                    tmap_numb_month.remove(d);
                }
                //map_numb_year
                /*
                for (Map.Entry<String,Integer> e: map_numb_year.entrySet()) {
                    out.printf("<tr>%n");
                    out.printf("<td>%s</td><td>%d</td>%n",
                            e.getKey(),
                            e.getValue()
                    );
                    out.printf("</tr>%n");
                }
                */
                //map_total_month
                /*
                for (Map.Entry<AbstractMap.SimpleEntry<String,String>,Double> e: map_total_month.entrySet()) {
                    out.printf("<tr>%n");
                    out.printf("<td>%s</td><td>%.2f</td>%n",
                            e.getKey().getValue()+" "+e.getKey().getKey(),
                            e.getValue()
                    );
                    out.printf("</tr>%n");
                }
                */
                
                out.printf("</table>%n");
            }

            out.printf("</body>%n");

            out.printf("</html>%n");

            // flush the output stream buffer
            out.flush();

            // close the output stream
            out.close();
        } catch (IOException ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating invoice. StartDate %s , EndDate %s.", startDate.toString(), endDate.toString()), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}