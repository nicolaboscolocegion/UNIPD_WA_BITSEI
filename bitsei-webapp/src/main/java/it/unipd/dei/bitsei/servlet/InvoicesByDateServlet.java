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

 import it.unipd.dei.bitsei.dao.InvoicesByDateDAO;
 import it.unipd.dei.bitsei.resources.Actions;
 import it.unipd.dei.bitsei.resources.InvoiceByDate;
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
 
 /**
  * Show the amounts of invoices grouped by date.
  *
  * @author Christian Marchiori
  * @version 1.00
  * @since 1.00
  */
 public final class InvoicesByDateServlet extends AbstractDatabaseServlet {
 
     /**
      * Show the amounts of invoices grouped by date.
      *
      * @param req the HTTP request from the client.
      * @param res the HTTP response from the server.
      *
      * @throws IOException if any error occurs in the client/server communication.
      */
     public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
 
         LogContext.setIPAddress(req.getRemoteAddr());
         LogContext.setAction(Actions.INVOICES_BY_DATE);
 
 
         // request parameter
         Date startDate = new Date(1900, 01, 01);
         Date endDate = new Date(1900, 01, 01);
 
         // model
         List<InvoiceByDate> el = null;
         Message m = null;
 
         try {
 
             // retrieves the request parameter
             startDate = Date.valueOf(req.getParameter("startDate"));
             endDate = Date.valueOf(req.getParameter("endDate"));
 
             // creates a new object for accessing the database and showing the amount of invoices grouped by date.
             el = new InvoicesByDateDAO(getConnection(), startDate, endDate).access().getOutputParam();
 
             m = new Message("Chart successfully created.");
 
             LOGGER.info("Chart successfully created by startDate  and endDate ."/*, startDate.toString(), endDate.toString()*/);
 
         } catch (IllegalArgumentException ex) {
             m = new Message("Cannot search for invoices. Invalid input parameters: date must be in the format yyyy-[m]m-[d]d.", "E100",
                     ex.getMessage());
 
             LOGGER.error("Cannot search for invoices. Invalid input parameters: date must be in the format yyyy-[m]m-[d]d.", ex);
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
             out.printf("<title>Chart Invoices by Date</title>%n");
             out.printf("</head>%n");
 
             out.printf("<body>%n");
             out.printf("<h1>Chart Invoices by Date</h1>%n");
             out.printf("<hr/>%n");
 
             if (m.isError()) {
                 out.printf("<ul>%n");
                 out.printf("<li>error code: %s</li>%n", m.getErrorCode());
                 out.printf("<li>message: %s</li>%n", m.getMessage());
                 out.printf("<li>details: %s</li>%n", m.getErrorDetails());
                 out.printf("</ul>%n");
             } else {
                 out.printf("<p>%s</p>%n", m.getMessage());
 
                 out.printf("<table>%n");
                 out.printf("<tr>%n");
                 out.printf("<td>Date</td><td>Amount</td>%n");
                 out.printf("</tr>%n");
 
                 for (InvoiceByDate e : el) {
                     out.printf("<tr>%n");
                     out.printf("<td>%s</td><td>%d</td>%n",
                             e.getDate().toString(),
                             e.getAmount()
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
             LOGGER.error(new StringFormattedMessage("Unable to send response when creating invoice. StartDate  , EndDate ."/*, startDate.toString(), endDate.toString()*/), ex);
             throw ex;
         } finally {
             LogContext.removeIPAddress();
             LogContext.removeAction();
             LogContext.removeUser();
         }
     }
 
 }