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

package it.unipd.dei.bitsei.servlet.listing;

import it.unipd.dei.bitsei.dao.listing.FilterInvoiceByTotalDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * Searches invoices by their total.
 *
 * @author Marco Martinelli
 * @version 1.00
 * @since 1.00
 *
 */
public final class FilterInvoiceByTotalServlet extends AbstractDatabaseServlet {

	/**
	 * Searches invoices by their total.
	 *
	 * @param req the HTTP request from the client.
	 * @param res the HTTP response from the server.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

		LogContext.setIPAddress(req.getRemoteAddr());
		LogContext.setAction(Actions.FILTER_INVOICES_BY_TOTAL);

		final double BASE_TOTAL = 0.00;
		final double MAX_TOTAL = 9E20;
		// request parameter
		double startTotal = BASE_TOTAL;
		double endTotal = MAX_TOTAL;

		// model
		List<Invoice> el = null;
		Message m = null;

		try {

			try {
				startTotal = Double.parseDouble(req.getParameter("startTotal"));
				if (startTotal <= 0)
					startTotal = BASE_TOTAL;
			} catch (NumberFormatException ex) {
				startTotal = BASE_TOTAL;
			}

			try {
				endTotal = Double.parseDouble(req.getParameter("endTotal"));
			} catch (NumberFormatException ex) {
				endTotal = MAX_TOTAL;
			}

			// creates a new object for accessing the database and searching the employees
			el = new FilterInvoiceByTotalDAO(getConnection(), startTotal, endTotal).access().getOutputParam();

			String tmp_string = "Invoices succesfully searched. Start Total: " + startTotal + " End Total: " + endTotal + ".";
			m = new Message(tmp_string);

			LOGGER.info("Invoices successfully searched from startTotal %.2f to endTotal %.2f.", startTotal, endTotal);

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
			// set the MIME media type of the response
			res.setContentType("text/html; charset=utf-8");

			// get a stream to write the response
			PrintWriter out = res.getWriter();

			// write the HTML page
			out.printf("<!DOCTYPE html>%n");

			out.printf("<html lang=\"en\">%n");
			out.printf("<head>%n");
			out.printf("<meta charset=\"utf-8\">%n");
			out.printf("<title>Filter invoices by total</title>%n");
			out.printf("</head>%n");

			out.printf("<body>%n");
			out.printf("<h1>Filter invoices by total</h1>%n");
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
			}

			out.printf("</body>%n");

			out.printf("</html>%n");

			// flush the output stream buffer
			out.flush();

			// close the output stream
			out.close();
		} catch (IOException ex) {
			LOGGER.error(new StringFormattedMessage("Unable to send response when creating invoice. StartTotal %.2f , EndTotal %.2f", startTotal, endTotal), ex);
			throw ex;
		} finally {
			LogContext.removeIPAddress();
			LogContext.removeAction();
			LogContext.removeUser();
		}
	}

}
