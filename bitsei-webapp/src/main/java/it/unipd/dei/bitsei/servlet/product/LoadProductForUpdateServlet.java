package it.unipd.dei.bitsei.servlet.product;

import it.unipd.dei.bitsei.dao.product.LoadProductForUpdateDAO;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.Product;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Searches product by his id.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class LoadProductForUpdateServlet extends AbstractDatabaseServlet {

    /**
     * Searches product by his id.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // request parameter
        int product_id = -1;

        // model
        Product product = null;
        Message m = null;

        try {

            // retrieves the request parameter
            product_id = Integer.parseInt(req.getParameter("product_id"));

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            // creates a new object for accessing the database and searching the product
            product = new LoadProductForUpdateDAO(getConnection(), product_id, owner_id).access().getOutputParam();

            m = new Message("Product successfully searched.");

            LOGGER.info("Product successfully searched by product_id %d.", product_id);

        } catch (NumberFormatException ex) {
            m = new Message("Cannot search for product. Invalid input parameters: product_id must be integer.", "E100",
                    ex.getMessage());

            LOGGER.error("Cannot search for product. Invalid input parameters: product_id must be integer.", ex);
        } catch (SQLException ex) {
            m = new Message("Cannot search for product: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot search for product: unexpected error while accessing the database.", ex);
        }


        try {
            // stores the product list and the message as a request attribute
            req.setAttribute("product", product);
            req.setAttribute("message", m);

            // forwards the control to the product-data JSP
            req.getRequestDispatcher("/jsp/get-product-by-id.jsp").forward(req, res);

        } catch(Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response after searching for product %d.", product_id), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}