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

            // retrieves the request parameters
            product_id = Integer.parseInt(req.getParameter("product_id"));


            // creates a new product
            p = new Product(product_id);

            // creates a new object for accessing the database and delete the product
            new DeleteProductDAO(getConnection(), p).access();

            m = new Message(String.format("Product successfully deleted."));
            LOGGER.info("Product successfully removed from the database.");

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot delete the product. Invalid input parameters: product_id must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot delete the product. Invalid input parameters: product_id must be integer.",
                    ex);
        } catch (SQLException ex) {

            m = new Message("Cannot delete the product: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot delete the product: unexpected error while accessing the database.", ex);

        }  catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
        }

        // Send message as a request attribute
        req.setAttribute("message", m);

        // forwards the control to the delete-product JSP
        req.getRequestDispatcher("/jsp/delete-product.jsp").forward(req, res);

        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();


    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        //LogContext.setIPAddress(req.getRemoteAddr());

        if(req.getParameter("method").contains("delete")) {
            this.doDelete(req,res);
        }

    }


    }
