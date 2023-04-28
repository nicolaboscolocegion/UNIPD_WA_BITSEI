package it.unipd.dei.bitsei.servlet.product;

import it.unipd.dei.bitsei.dao.product.DeleteProductDAO;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.Product;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Deletes a product from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteProductServlet extends AbstractDatabaseServlet {
    /**
     * Default constructor.
     */
    public DeleteProductServlet(){super();}

    /**
     * Deletes a product from the database.
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
        Product p = null;
        Message m = null;

        int product_id = -1;

        try {

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            // retrieves the request parameters
            product_id = Integer.parseInt(req.getParameter("product_id"));
            int company_id = Integer.parseInt(req.getParameter("company_id"));

            // creates a new product
            p = new Product(product_id);

            // creates a new object for accessing the database and delete the product
            new DeleteProductDAO(getConnection(), p,owner_id, company_id).access();

            m = new Message(String.format("Product successfully deleted."));
            LOGGER.info("Product successfully removed from the database.");

        } catch(SQLException ex){
            LOGGER.error("Cannot delete product: unexpected error while accessing the database.", ex);
            m = new Message("Cannot delete product: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            req.setAttribute("message", m);
        } catch (NumberFormatException ex) {
            m = new Message("Owner not parsable.", "E5A1", ex.getMessage());
            LOGGER.info("Owner not parsable." + ex.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("message", m);
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        // Send message as a request attribute
        req.setAttribute("message", m);

        // forwards the control to the delete-product JSP
        req.getRequestDispatcher("/jsp/product-delete.jsp").forward(req, res);

        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();


    }



}
