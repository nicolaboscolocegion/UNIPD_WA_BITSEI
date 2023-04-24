package it.unipd.dei.bitsei.servlet.product;

import it.unipd.dei.bitsei.dao.product.CreateProductDAO;
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
 * Creates a new product into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CreateProductServlet extends AbstractDatabaseServlet {
    /**
     * Creates a new product into the database.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     *
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Product p = null;
        Message m = null;

        int company_id = 1;
        String title = null;
        int default_price = 1;
        String logo = null;
        String measurement_unit = null;
        String description = null;

        try {

            // retrieves the request parameters
            company_id = Integer.valueOf(req.getParameter("company_id"));
            title = req.getParameter("title");
            default_price = Integer.valueOf(req.getParameter("default_price"));
            logo = req.getParameter("logo");
            measurement_unit = req.getParameter("measurement_unit");
            description = req.getParameter("description");

            LOGGER.info("DATA: compID: " + company_id + " title: " + title + " defPrice: " + default_price + " logo: " + logo + " measurUnit: " + measurement_unit + " descr: " + description);



            fieldRegexValidation("[^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$", logo, "LOGO");


            // creates a new foo product
            p = new Product(company_id, title, default_price, logo, measurement_unit,  description);

            // creates a new object for accessing the database and stores the product
            new CreateProductDAO(getConnection(), p,1,1).access();

            m = new Message(String.format("Product %s successfully created.", title));

            LOGGER.info("Product %s successfully created in the database.", title);

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot create the product. Invalid input parameters: company_id and default_price must be integers, and title, logo, measurement_unit and description must be string.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the product. Invalid input parameters: company_id and default_price must be integers, and title, logo, measurement_unit and description must be string.",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot create the product: product %s already exists.", title), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot create the product: product %s already exists.", title),
                        ex);
            } else {
                m = new Message("Cannot create the product: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot create the product: unexpected error while accessing the database.", ex);
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

        // forwards the control to the create-product JSP
        req.getRequestDispatcher("/jsp/create-product.jsp").forward(req, res);

        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();
    }
}
