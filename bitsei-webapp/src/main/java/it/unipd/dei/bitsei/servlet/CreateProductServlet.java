package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.CreateCustomerDAO;
import it.unipd.dei.bitsei.dao.CreateProductDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;

import static it.unipd.dei.bitsei.utils.RegexValidation.fieldRegexValidation;

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
            company_id = Integer.parseInt(req.getParameter("company_id"));
            title = req.getParameter("title");
            default_price = Integer.parseInt(req.getParameter("default_price"));
            logo = req.getParameter("logo");
            measurement_unit = req.getParameter("measurement_unit");
            description = req.getParameter("description");

            LOGGER.info("DATA: compID: " + company_id + " title: " + title + " defPrice: " + default_price + " logo: " + logo + " measurUnit: " + measurement_unit + " descr: " + description);


            //fieldRegexValidation("([0-9a-zA-Z :\\-_!@$%^&*()])+(.jpg|.JPG|.jpeg|.JPEG|.bmp|.BMP|.gif|.GIF|.psd|.PSD)$", logo, "LOGO");



            // creates a new foo customer
            p = new Product(company_id, title, default_price, logo, measurement_unit,  description);

            // creates a new object for accessing the database and stores the customer
            new CreateProductDAO(getConnection(), p).access();

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

        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();
    }
}
