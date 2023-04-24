package it.unipd.dei.bitsei.servlet.product;


import it.unipd.dei.bitsei.dao.product.UpdateProductDAO;
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

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;

/**
 * Updates a product into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateProductServlet extends AbstractDatabaseServlet {

    /**
     * Updates the product into the database.
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
        Product p = null;
        Message m = null;

        int product_id = -1;
        int company_id = -1;
        String title = null;
        int default_price = 1;
        String logo = null;
        String measurement_unit = null;
        String description = null;

        try {

            // retrieves the request parameters
            String company_id_raw = req.getParameter("company_id");
            try {
                company_id = Integer.valueOf(company_id_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.info("No company id provided for %s, will be set to null.", title);
            }
            //company_id = Integer.valueOf(req.getParameter("company_id"));
            title = req.getParameter("title");
            default_price = Integer.valueOf(req.getParameter("default_price"));
            logo = req.getParameter("logo");
            measurement_unit = req.getParameter("measurement_unit");
            description = req.getParameter("description");

            String product_id_raw = req.getParameter("product_id");
            try {
                product_id = Integer.valueOf(product_id_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.error("Error while passing product %s", title);
            }

            LOGGER.info("DATA: compID: " + company_id + " title: " + title + " defPrice: " + default_price + " logo: " + logo + " measurUnit: " + measurement_unit + " descr: " + description);

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            fieldRegexValidation("[^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$", logo, "LOGO");

            // creates a new foo product
            p = new Product(product_id, company_id, title, default_price, logo, measurement_unit, description);

            // creates a new object for accessing the database and stores the customer
            new UpdateProductDAO(getConnection(), p, owner_id).access();

            m = new Message(String.format("Product %s successfully updated.", title));

            LOGGER.info("Product %s successfully updated.", title);

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot update the product. Invalid input parameters: company_id and default_price must be integers, and title, logo, measurement_unit and description must be string.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot update the product. Invalid input parameters: company_id and default_price must be integers, and title, logo, measurement_unit and description must be string.",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot update the product: product %s already exists.", title), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot update the product: product %s already exists.", title),
                        ex);
            } else {
                m = new Message("Cannot update the product: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot update the product: unexpected error while accessing the database.", ex);
            }
        }  catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
        }

        // Send message as a request attribute
        req.setAttribute("message", m);

        // forwards the control to the update-product JSP
        req.getRequestDispatcher("/jsp/update-product.jsp").forward(req, res);


        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();


    }

}