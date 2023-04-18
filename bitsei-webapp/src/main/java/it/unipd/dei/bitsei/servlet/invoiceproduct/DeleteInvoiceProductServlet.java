package it.unipd.dei.bitsei.servlet.invoiceproduct;

import it.unipd.dei.bitsei.dao.invoiceproduct.DeleteInvoiceProductDAO;
import it.unipd.dei.bitsei.resources.InvoiceProduct;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Deletes an invoice product from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteInvoiceProductServlet extends AbstractDatabaseServlet {

    /**
     * Deletes an invoice product from the database.
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
        InvoiceProduct i = null;
        Message m = null;

        int invoice_id = -1;
        int product_id = -1;

        try {

            // retrieves the request parameters
            invoice_id = Integer.parseInt(req.getParameter("invoice_id"));
            product_id = Integer.parseInt(req.getParameter("product_id"));


            // creates a new invoice product
            i = new InvoiceProduct(invoice_id, product_id);

            // creates a new object for accessing the database and delete the invoice product
            new DeleteInvoiceProductDAO(getConnection(), i).access();

            m = new Message(String.format("Invoice product successfully deleted."));
            LOGGER.info("Invoice product successfully removed from the database.");

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot delete the invoice product. Invalid input parameters: invoice_id and product_id must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot delete the invoice product. Invalid input parameters: invoice_id and product_id must be integer.",
                    ex);
        } catch (SQLException ex) {

            m = new Message("Cannot delete the invoice product: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot delete the invoice product: unexpected error while accessing the database.", ex);

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
