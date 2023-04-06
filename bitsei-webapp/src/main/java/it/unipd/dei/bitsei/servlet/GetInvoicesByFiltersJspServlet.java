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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Searches invoices by their total.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 *
 */
public final class GetInvoicesByFiltersJspServlet extends AbstractDatabaseServlet {

	/**
	 * Searches invoices by their total.
	 *
	 * @param req the HTTP request from the client.
	 * @param res the HTTP response from the server.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		LogContext.setIPAddress(req.getRemoteAddr());
		LogContext.setAction(Actions.GET_INVOICES_BY_FILTERS);

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

			//String tmp_string = "Invoices succesfully searched. Start Total: " + startTotal + " End Total: " + endTotal + " startDiscount: " + startDiscount + " endDiscount: " + endDiscount + " startPfr: " + startPfr + " endPfr: " + endPfr + " startInvoiceDate: " + startInvoiceDate.toString() + " endInvoiceDate: " + endInvoiceDate.toString() + " startWarningDate: " + startWarningDate + " endWarningDate: " + endWarningDate + ".";
			m = new Message("Invoices succesfully searched");

			LOGGER.info("## SERVLET: Invoices successfully searched ##");

		} catch (NumberFormatException ex) {
			m = new Message("Cannot search for invoices. Invalid input parameters: totals must be double", "E100",
					ex.getMessage());

			LOGGER.error("Cannot search for invoices. Invalid input parameters: totals must be double.", ex);
		} catch (SQLException ex) {
			m = new Message("Cannot search for invoices: unexpected error while accessing the database.", "E200",
					ex.getMessage());

			LOGGER.error("Cannot search for invoices: unexpected error while accessing the database.", ex);
		}

		try {
			// stores the invoice list and the message as a request attribute
			req.setAttribute("invoiceList", el);
			req.setAttribute("message", m);

			// forwards the control to the search-employee-result JSP
			req.getRequestDispatcher("/jsp/get-invoices-by-filters-result.jsp").forward(req, res);

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
