package it.unipd.dei.bitsei.servlet;


import it.unipd.dei.bitsei.dao.UpdateInvoiceDAO;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DateTimeException;

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;

/**
 * Updates an invoice into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateInvoiceServlet extends AbstractDatabaseServlet {

    /**
     * Updates the invoice into the database.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     *
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Invoice i = null;
        Message m = null;

        int invoice_id = -1;
        int customer_id = -1;
        int status = 0;
        int warning_number = 1;
        Date warning_date = null;
        String warning_pdf_file = null;
        String invoice_number = null;
        Date invoice_date = null;
        String invoice_pdf_file = null;
        String invoice_xml_file = null;
        double total = 0;
        double discount = 0;
        double pension_fund_refund = 0;
        boolean has_stamp = false;

        try {

            // retrieves the request parameters
            String customer_id_raw = req.getParameter("customer_id");
            try {
                customer_id = Integer.valueOf(customer_id_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.info("No customer id provided, will be set to null.");
            }
            status = Integer.parseInt(req.getParameter("status"));
            warning_number = Integer.parseInt(req.getParameter("warning_number"));
            warning_date = Date.valueOf(req.getParameter("warning_date"));
            warning_pdf_file = req.getParameter("warning_pdf_file");
            invoice_number = req.getParameter("invoice_number");
            invoice_date = Date.valueOf(req.getParameter("invoice_date"));
            invoice_pdf_file = req.getParameter("invoice_pdf_file");
            invoice_xml_file = req.getParameter("invoice_xml_file");
            total = Double.parseDouble(req.getParameter("total"));
            discount = Double.parseDouble(req.getParameter("discount"));
            pension_fund_refund = Double.parseDouble(req.getParameter("pension_fund_refund"));
            has_stamp = Boolean.parseBoolean(req.getParameter("has_stamp"));

            String invoice_id_raw = req.getParameter("invoice_id");
            try {
                invoice_id = Integer.valueOf(invoice_id_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.error("Error while passing invoice");
            }

            LOGGER.info("DATA: custID: " + customer_id + " status: " + status + " warnNum: " + warning_number + " warnDate: " + warning_date + " warnPDF: " + warning_pdf_file + " invoNum: " + invoice_number + " invoDate: " + invoice_date + " invoPDF: " + invoice_pdf_file + " invoXML: " + invoice_xml_file + " total: " + total + " discount: " + discount + " pensionFund: " + pension_fund_refund + " stamp: " + has_stamp);

            fieldRegexValidation("[^\\s]+(\\.(?i)(pdf))$", warning_pdf_file, "WARNING_PDF_FILE");
            fieldRegexValidation("[^\\s]+(\\.(?i)(pdf))$", invoice_pdf_file, "INVOICE_PDF_FILE");
            fieldRegexValidation("[^\\s]+(\\.(?i)(xml))$", invoice_xml_file, "INVOICE_XML_FILE");
            long millis=System.currentTimeMillis();
            Date curr_date = new java.sql.Date(millis);
            if(warning_date.compareTo(curr_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Warning date after current date.");
            if(invoice_date.compareTo(curr_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Invoice date after current date.");
            if(warning_date.compareTo(invoice_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Warning date after invoice date.");

            // creates a new foo invoice
            i = new Invoice(invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp);

            // creates a new object for accessing the database and stores the customer
            new UpdateInvoiceDAO(getConnection(), i).access();

            m = new Message(String.format("Invoice successfully updated."));

            LOGGER.info("Invoice successfully updated.");

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot update the invoice. Invalid input parameters: customer_id, status and warning_number must be integers; warning_pdf_file, invoice_number, invoice_pdf_file and invoice_pdf_file must be string; total, discount and pension_fund_refund must be double; has_stamp must be boolean; warning_date and invoice_date must be date.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot update the invoice. Invalid input parameters: customer_id, status and warning_number must be integers; warning_pdf_file, invoice_number, invoice_pdf_file and invoice_pdf_file must be string; total, discount and pension_fund_refund must be double; has_stamp must be boolean; warning_date and invoice_date must be date.",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot update the invoice: invoice already exists."), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot update the invoice: invoice already exists."),
                        ex);
            } else {
                m = new Message("Cannot update the invoice: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot update the invoice: unexpected error while accessing the database.", ex);
            }
        }  catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
        }


        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();


    }

}