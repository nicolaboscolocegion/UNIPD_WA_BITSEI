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

import it.unipd.dei.bitsei.dao.GetInvoicesByFiltersDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.util.TreeMap;
import java.text.SimpleDateFormat;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import javax.sql.DataSource;

/**
 * Searches employees by their salary.
* 
* @author Nicola Ferro (ferro@dei.unipd.it)
* @version 1.00
* @since 1.00
*/
public final class ChartJspServlet extends AbstractDatabaseServlet {

    /**
     * Searches employees by their salary.
    * 
    * @param req
    *            the HTTP request from the client.
    * @param res
    *            the HTTP response from the server.
    * 
    * @throws ServletException
    *             if any error occurs while executing the servlet.
    * @throws IOException
    *             if any error occurs in the client/server communication.
    */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.INVOICES_BY_DATE);

        // Default value for startTotal and startDiscount
		final double START_TOTAL = 0.00;
		// Default value for endTotal and endDiscount
		final double END_TOTAL = 9E20;
		// Default value for startInvoiceDate
		final LocalDate baseDate = LocalDate.of(1970, 01, 01);
		final Date START_DATE = Date.valueOf(baseDate);
		// Default value for endInvoiceDate
		final LocalDate currentDate = LocalDate.now();
		final Date END_DATE = Date.valueOf(currentDate);

		// request parameters
		double startTotal = START_TOTAL;
		double endTotal = END_TOTAL;

		double startDiscount = START_TOTAL;
		double endDiscount = END_TOTAL;
		
		double startPfr = START_TOTAL;
		double endPfr = END_TOTAL;

		Date startInvoiceDate = START_DATE;
		Date endInvoiceDate = END_DATE;
		
		Date startWarningDate = START_DATE;
		Date endWarningDate = END_DATE;
        int chart_type = Integer.parseInt(req.getParameter("chartType"));

        // model
        List<Invoice> el = null;
        Message m = null;

        try {

            try {
				startTotal = Double.parseDouble(req.getParameter("startTotal"));
				if (startTotal <= 0)
					startTotal = START_TOTAL;
			} catch (NumberFormatException ex) {
				startTotal = START_TOTAL;
			}

			try {
				endTotal = Double.parseDouble(req.getParameter("endTotal"));
			} catch (NumberFormatException ex) {
				endTotal = END_TOTAL;
			}

			try {
				startDiscount = Double.parseDouble(req.getParameter("startDiscount"));
				if (startDiscount <= 0)
					startDiscount = START_TOTAL;
			} catch (NumberFormatException ex) {
				startDiscount = START_TOTAL;
			}

			try {
				endDiscount = Double.parseDouble(req.getParameter("endDiscount"));
			} catch (NumberFormatException ex) {
				endDiscount = END_TOTAL;
			}

			try {
				startPfr = Double.parseDouble(req.getParameter("startPfr"));
				if (startPfr <= 0)
					startPfr = START_TOTAL;
			} catch (NumberFormatException ex) {
				startPfr = START_TOTAL;
			}

			try {
				endPfr = Double.parseDouble(req.getParameter("endPfr"));
			} catch (NumberFormatException ex) {
				endPfr = END_TOTAL;
			}
			
			try {
				startInvoiceDate = Date.valueOf(req.getParameter("startInvoiceDate"));
			} catch (IllegalArgumentException ex) {
				startInvoiceDate = START_DATE;
			}

			try {
				endInvoiceDate = Date.valueOf(req.getParameter("endInvoiceDate"));
			} catch(IllegalArgumentException ex){
				endInvoiceDate = END_DATE;
			}

			try {
				startWarningDate = Date.valueOf(req.getParameter("startWarningDate"));
			} catch (IllegalArgumentException ex) {
				startWarningDate = START_DATE;
			}

			try {
				endWarningDate = Date.valueOf(req.getParameter("endWarningDate"));
			} catch(IllegalArgumentException ex){
				endWarningDate = END_DATE;
			}

            // creates a new object for accessing the database and searching the employees
            el = new GetInvoicesByFiltersDAO(getConnection(), startTotal, endTotal, startDiscount, endDiscount, startPfr, endPfr, startInvoiceDate, endInvoiceDate, startWarningDate, endWarningDate).access().getOutputParam();

            m = new Message("Invoices succesfully searched");

			LOGGER.info("## SERVLET: Invoices successfully searched ##");

        } catch (NumberFormatException ex) {
            m = new Message("Cannot search for invoices. Invalid input parameters: totals must be double.", "E100",
                    ex.getMessage());

            LOGGER.error("Cannot search for invoices. Invalid input parameters: totals must be double.", ex);
        } catch (SQLException ex) {
            m = new Message("Cannot search for invoices: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot search for invoices: unexpected error while accessing the database.", ex);
        }
        
        ArrayList<String> tmap_labels = new ArrayList<>();
        ArrayList<String> tmap_data = new ArrayList<>();

        switch(chart_type){
            case 1:
            //INVOICE BY DATE (MONTH)
            TreeMap<Date,Integer> tmap_numb_month = new TreeMap<>();
            for (Invoice i: el){
                if(i.getInvoice_date()!=null){
                    tmap_numb_month.put(new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1),
                    1+tmap_numb_month.getOrDefault(new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1), 0));
                }
            }
            
            int size = tmap_numb_month.size();
            for (int i=0;i<size;i++) {
                Date d = tmap_numb_month.firstKey();
                
                tmap_labels.add(new SimpleDateFormat("MMMM yyyy").format(d));
                tmap_data.add(tmap_numb_month.get(d).toString());
                
                tmap_numb_month.remove(d);
            }
            break;
            case 2:
            //TOTAL BY DATE (MONTH)
            TreeMap<Date,Double> tmap_total_month = new TreeMap<>();
            for (Invoice i: el){
                if(i.getInvoice_date()!=null){
                    tmap_total_month.put(new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1),
                    i.getTotal()+tmap_total_month.getOrDefault(new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1), 0.0));
                }
            }
            int size2 = tmap_total_month.size();
            for (int i=0;i<size2;i++) {
                Date d = tmap_total_month.firstKey();
                
                tmap_labels.add(new SimpleDateFormat("MMMM yyyy").format(d));
                tmap_data.add(tmap_total_month.get(d).toString());
                
                tmap_total_month.remove(d);
            }
            break;
            case 3:
            //INVOICE BY CUSTOMER
            TreeMap<Integer,Integer> tmap_numb_customer = new TreeMap<>();
            for (Invoice i: el){
                
                tmap_numb_customer.put(i.getCustomer_id(),
                1+tmap_numb_customer.getOrDefault(i.getCustomer_id(),0));

            }
            int size3 = tmap_numb_customer.size();
            for (int i=0;i<size3;i++) {
                int d = tmap_numb_customer.firstKey();
                
                tmap_labels.add(d+"");
                tmap_data.add(tmap_numb_customer.get(d).toString());
                
                tmap_numb_customer.remove(d);
            }
            break;
        }

        try {
            // stores the employee list and the message as a request attribute
            req.setAttribute("invoiceList", el);
            req.setAttribute("tmap_labels", tmap_labels);
            req.setAttribute("tmap_data", tmap_data);
            req.setAttribute("chart_type", chart_type);
            req.setAttribute("message", m);

            // forwards the control to the search-employee-result JSP
            req.getRequestDispatcher("/jsp/chart-results.jsp").forward(req, res);

        } catch(Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating invoices."), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}