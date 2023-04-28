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

import it.unipd.dei.bitsei.dao.listing.GetInvoicesByFiltersJspDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Plots charts about invoices.
* 
* @author Christian Marchiori
* @version 1.00
* @since 1.00
*/
public final class ChartJspServlet extends AbstractDatabaseServlet {
    /**
     * Default constructor.
     */
    public ChartJspServlet(){}

    /**
     * Plots charts about invoices.
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
        boolean filterByTotal = false;
        double startTotal = START_TOTAL;
		double endTotal = END_TOTAL;

		boolean filterByDiscount = false;
        double startDiscount = START_TOTAL;
		double endDiscount = END_TOTAL;
		
		boolean filterByPfr = false;
        double startPfr = START_TOTAL;
		double endPfr = END_TOTAL;

		boolean filterByInvoiceDate = false;
        Date startInvoiceDate = START_DATE;
		Date endInvoiceDate = END_DATE;
		
		boolean filterByWarningDate = false;
        Date startWarningDate = START_DATE;
		Date endWarningDate = END_DATE;

        int chart_type = Integer.parseInt(req.getParameter("chartType"));
        int chart_period;
        if(req.getParameter("chartPeriod")!=null)
            chart_period = Integer.parseInt(req.getParameter("chartPeriod"));
        else
            chart_period = 1;
                
        // model
        List<Invoice> el = null;
        Message m = null;

        try {
            
            if(req.getParameter("filterByTotal") != null)
				filterByTotal = true;

			if(req.getParameter("filterByDiscount") != null)
				filterByDiscount = true;

			if(req.getParameter("filterByPfr") != null)
				filterByPfr = true;

			if(req.getParameter("filterByInvoiceDate") != null)
				filterByInvoiceDate = true;

			if(req.getParameter("filterByWarningDate") != null)
				filterByWarningDate = true;
            
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
            el = new GetInvoicesByFiltersJspDAO(getConnection(),
                    filterByTotal, startTotal, endTotal,
                    filterByDiscount, startDiscount, endDiscount,
                    filterByPfr, startPfr, endPfr,
                    filterByInvoiceDate, startInvoiceDate, endInvoiceDate,
                    filterByWarningDate, startWarningDate, endWarningDate).access().getOutputParam();

            m = new Message("Invoices succesfully searched for charts");

			LOGGER.info("## SERVLET: Invoices successfully searched for charts ##");

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
            switch(chart_period){
                case 1:
                //INVOICE BY DATE (MONTH)
                TreeMap<Date,Integer> tmap_numb_month = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1);
                        tmap_numb_month.put(d, 1+tmap_numb_month.getOrDefault(d, 0));
                    }
                }
                for (Map.Entry<Date,Integer> entry: tmap_numb_month.entrySet()) {
                    String month = new SimpleDateFormat("MMMM yyyy").format(entry.getKey());
                    tmap_labels.add(month.substring(0,1).toUpperCase()+month.substring(1));
                    tmap_data.add(entry.getValue().toString());
                }
                break;
                case 2:
                //INVOICE BY DATE (YEAR)
                TreeMap<Date,Integer> tmap_numb_year = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),0,1);
                        tmap_numb_year.put(d, 1+tmap_numb_year.getOrDefault(d, 0));
                    }
                }
                for (Map.Entry<Date,Integer> entry: tmap_numb_year.entrySet()) {
                    String year = new SimpleDateFormat("yyyy").format(entry.getKey());
                    tmap_labels.add(year);
                    tmap_data.add(entry.getValue().toString());
                }
                break;
                case 3:
                //INVOICE BY DATE (DAY)
                TreeMap<Date,Integer> tmap_numb_day = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),i.getInvoice_date().getDay());
                        tmap_numb_day.put(d, 1+tmap_numb_day.getOrDefault(d, 0));
                    }
                }
                for (Map.Entry<Date,Integer> entry: tmap_numb_day.entrySet()) {
                    String day = new SimpleDateFormat("d/MM/yyyy").format(entry.getKey());
                    tmap_labels.add(day);
                    tmap_data.add(entry.getValue().toString());
                }
                break;
            }
            break;
            case 2:
            switch(chart_period){
                case 1:
                //TOTAL BY DATE (MONTH)
                TreeMap<Date,Double> tmap_total_month = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1);
                        tmap_total_month.put(d, i.getTotal()+tmap_total_month.getOrDefault(d, 0.0));
                    }
                }
                for (Map.Entry<Date,Double> entry: tmap_total_month.entrySet()) {
                    String month = new SimpleDateFormat("MMMM yyyy").format(entry.getKey());
                    tmap_labels.add(month.substring(0,1).toUpperCase()+month.substring(1));
                    tmap_data.add(entry.getValue().toString());
                }
                break;
                case 2:
                //TOTAL BY DATE (YEAR)
                TreeMap<Date,Double> tmap_total_year = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),0,1);
                        tmap_total_year.put(d, i.getTotal()+tmap_total_year.getOrDefault(d, 0.0));
                    }
                }
                for (Map.Entry<Date,Double> entry: tmap_total_year.entrySet()) {
                    String year = new SimpleDateFormat("yyyy").format(entry.getKey());
                    tmap_labels.add(year);
                    tmap_data.add(entry.getValue().toString());
                }
                break;
                case 3:
                //TOTAL BY DATE (DAY)
                TreeMap<Date,Double> tmap_total_day = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),i.getInvoice_date().getDay());
                        tmap_total_day.put(d, i.getTotal()+tmap_total_day.getOrDefault(d, 0.0));
                    }
                }
                for (Map.Entry<Date,Double> entry: tmap_total_day.entrySet()) {
                    String day = new SimpleDateFormat("d/MM/yyyy").format(entry.getKey());
                    tmap_labels.add(day);
                    tmap_data.add(entry.getValue().toString());
                }
                break;
            }
            break;
            case 3:
            //INVOICE BY CUSTOMER
            TreeMap<Integer,Integer> tmap_numb_customer = new TreeMap<>();
            for (Invoice i: el){
                
                tmap_numb_customer.put(i.getCustomer_id(),
                1+tmap_numb_customer.getOrDefault(i.getCustomer_id(),0));

            }
            for (Map.Entry<Integer,Integer> entry: tmap_numb_customer.entrySet()) {
                tmap_labels.add(entry.getKey()+"");
                tmap_data.add(entry.getValue().toString());
            }
            break;
            case 4:
            //TOTAL BY CUSTOMER
            TreeMap<Integer,Double> tmap_total_customer = new TreeMap<>();
            for (Invoice i: el){
                
                tmap_total_customer.put(i.getCustomer_id(),
                i.getTotal()+tmap_total_customer.getOrDefault(i.getCustomer_id(),0.0));

            }
            for (Map.Entry<Integer,Double> entry: tmap_total_customer.entrySet()) {
                
                tmap_labels.add(entry.getKey()+"");
                tmap_data.add(entry.getValue().toString());
            }
            break;
            case 5:
            switch(chart_period){
                case 1:
                //DISCOUNT BY DATE (MONTH)
                TreeMap<Date,Double> tmap_discount_month_total = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1);
                        tmap_discount_month_total.put(d, i.getTotal()+tmap_discount_month_total.getOrDefault(d, 0.0));
                    }
                }
                TreeMap<Date,Integer> tmap_discount_month_numb = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),1);
                        tmap_discount_month_numb.put(d, 1+tmap_discount_month_numb.getOrDefault(d, 0));
                    }
                }
                for (Map.Entry<Date,Double> entry: tmap_discount_month_total.entrySet()) {
                    String month = new SimpleDateFormat("MMMM yyyy").format(entry.getKey());
                    tmap_labels.add(month.substring(0,1).toUpperCase()+month.substring(1));
                    tmap_data.add(String.valueOf(entry.getValue()/tmap_discount_month_numb.get(entry.getKey())));
                }
                break;
                case 2:
                //DISCOUNT BY DATE (YEAR)
                TreeMap<Date,Double> tmap_discount_year_total = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),0,1);
                        tmap_discount_year_total.put(d, i.getTotal()+tmap_discount_year_total.getOrDefault(d, 0.0));
                    }
                }
                TreeMap<Date,Integer> tmap_discount_year_numb = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),0,1);
                        tmap_discount_year_numb.put(d, 1+tmap_discount_year_numb.getOrDefault(d, 0));
                    }
                }
                for (Map.Entry<Date,Double> entry: tmap_discount_year_total.entrySet()) {
                    String year = new SimpleDateFormat("yyyy").format(entry.getKey());
                    tmap_labels.add(year);
                    tmap_data.add(String.valueOf(entry.getValue()/tmap_discount_year_numb.get(entry.getKey())));
                }
                break;
                case 3:
                //DISCOUNT BY DATE (DAY)
                TreeMap<Date,Double> tmap_discount_day_total = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),i.getInvoice_date().getDay());
                        tmap_discount_day_total.put(d, i.getTotal()+tmap_discount_day_total.getOrDefault(d, 0.0));
                    }
                }
                TreeMap<Date,Integer> tmap_discount_day_numb = new TreeMap<>();
                for (Invoice i: el){
                    if(i.getInvoice_date()!=null){
                        Date d = new Date(i.getInvoice_date().getYear(),i.getInvoice_date().getMonth(),i.getInvoice_date().getDay());
                        tmap_discount_day_numb.put(d, 1+tmap_discount_day_numb.getOrDefault(d, 0));
                    }
                }
                for (Map.Entry<Date,Double> entry: tmap_discount_day_total.entrySet()) {
                    String day = new SimpleDateFormat("d/MM/yyyy").format(entry.getKey());
                    tmap_labels.add(day);
                    tmap_data.add(String.valueOf(entry.getValue()/tmap_discount_day_numb.get(entry.getKey())));
                }
                break;
            }
            break;
        }

        try {
            // stores the labels and the data for charts, stores also the message as a request attribute
            //req.setAttribute("invoiceList", el);
            req.setAttribute("tmap_labels", tmap_labels);
            req.setAttribute("tmap_data", tmap_data);
            req.setAttribute("chart_type", chart_type);
            req.setAttribute("chart_period", chart_period);
            req.setAttribute("message", m);

            // forwards the control to the chart-results JSP
            req.getRequestDispatcher("/jsp/chart-results.jsp").forward(req, res);

        } catch(Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating charts."), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}