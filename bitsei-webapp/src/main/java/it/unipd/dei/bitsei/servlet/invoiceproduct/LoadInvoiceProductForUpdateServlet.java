package it.unipd.dei.bitsei.servlet.invoiceproduct;

import it.unipd.dei.bitsei.dao.invoiceproduct.LoadInvoiceProductForUpdateDAO;
import it.unipd.dei.bitsei.resources.InvoiceProduct;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Searches invoice product by id.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class LoadInvoiceProductForUpdateServlet extends AbstractDatabaseServlet {
    /**
     * Default constructor.
     */
    public LoadInvoiceProductForUpdateServlet(){}

    /**
     * Searches invoice product by ids.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // request parameter
        int invoice_id = -1;
        int product_id = -1;

        // model
        InvoiceProduct invoiceProduct = null;
        Message m = null;

        try {

            // retrieves the request parameter
            invoice_id = Integer.parseInt(req.getParameter("invoice_id"));
            product_id = Integer.parseInt(req.getParameter("product_id"));

            // creates a new object for accessing the database and searching the invoice product
            invoiceProduct = new LoadInvoiceProductForUpdateDAO(getConnection(), invoice_id, product_id).access().getOutputParam();

            m = new Message("Invoice product successfully searched.");

            LOGGER.info("Invoice product successfully searched by invoice_id %d and product_id %d.", invoice_id, product_id);

        } catch (NumberFormatException ex) {
            m = new Message("Cannot search for invoice product. Invalid input parameters: invoice_id and product_id must be integer.", "E100",
                    ex.getMessage());

            LOGGER.error("Cannot search for invoice product. Invalid input parameters: invoice_id and product_id must be integer.", ex);
        } catch (SQLException ex) {
            m = new Message("Cannot search for invoice product: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot search for invoice product: unexpected error while accessing the database.", ex);
        }


        try {
            // stores the invoice list and the message as a request attribute
            req.setAttribute("invoiceProduct", invoiceProduct);
            req.setAttribute("message", m);

            // forwards the control to the invoice-data JSP
            req.getRequestDispatcher("/jsp/invoice-product-data.jsp").forward(req, res);

        } catch(Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response after searching for invoice product with invoice_id %d and product_id %d.", invoice_id, product_id), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}