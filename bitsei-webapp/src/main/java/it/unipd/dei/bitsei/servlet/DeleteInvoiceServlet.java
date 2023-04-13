package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.DeleteInvoiceDAO;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Deletes an invoice from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteInvoiceServlet extends AbstractDatabaseServlet {

    /**
     * Deletes an invoice from the database.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     *
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Invoice i = null;
        Message m = null;

        int invoice_id = -1;

        try {

            // retrieves the request parameters
            invoice_id = Integer.parseInt(req.getParameter("invoice_id"));


            // creates a new invoice
            i = new Invoice(invoice_id);

            // creates a new object for accessing the database and delete the invoice
            new DeleteInvoiceDAO(getConnection(), i).access();

            m = new Message(String.format("Invoice successfully deleted."));
            LOGGER.info("Invoice successfully removed from the database.");

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot delete the invoice. Invalid input parameters: invoice_id must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot delete the invoice. Invalid input parameters: invoice_id must be integer.",
                    ex);
        } catch (SQLException ex) {

            m = new Message("Cannot delete the invoice: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot delete the invoice: unexpected error while accessing the database.", ex);

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
