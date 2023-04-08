package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.CloseInvoiceDAO;
import it.unipd.dei.bitsei.dao.CreateCustomerDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CloseInvoiceServlet extends AbstractDatabaseServlet {

    /**
     * Creates a new customer into the database.
     *
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     *
     * @throws ServletException
     *             if any error occurs while executing the servlet.
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        int invoice_id = 0;

        List<Object> out;
        Message m = null;


        try {

            // retrieves the request parameters
            invoice_id = Integer.parseInt(req.getParameter("invoice_id"));

            // creates a new object for accessing the database and stores the customer
            out = new CloseInvoiceDAO(getConnection(), invoice_id).access().getOutputParam();
            m = new Message(String.format("Data for invoice warning fetched"));
            LOGGER.info("Data for invoice warning fetched");


            //generate invoice




        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot perform action. Invalid input parameters: invoice id must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot perform action. Invalid input parameters: invoice id must be integer.",
                    ex);
        } catch (SQLException ex) {

                m = new Message("Cannot perform action: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot perform action: unexpected error while accessing the database.", ex);

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