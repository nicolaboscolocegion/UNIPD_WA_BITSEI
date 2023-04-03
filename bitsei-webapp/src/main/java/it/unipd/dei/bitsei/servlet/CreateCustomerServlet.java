package it.unipd.dei.bitsei.servlet;



import java.io.IOException;
import java.sql.SQLException;

import it.unipd.dei.bitsei.dao.CreateCustomerDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CreateCustomerServlet extends AbstractDatabaseServlet {

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
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // TODO: request parameters

        // model
        Customer e = null;
        Message m = null;

        String bname = "Nicola SRL";
        String vatnumber = "123456789";
        String taxcode = "BSCNCL99A10B563P";

        try {

            // creates a new foo customer
            e = new Customer(bname, vatnumber,taxcode);

            // creates a new object for accessing the database and stores the customer
            new CreateCustomerDAO(getConnection(), e).access();

            m = new Message(String.format("Customer %s successfully created.", bname));

            LOGGER.info("Customer %s successfully created in the database.", bname);

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot create the customer. Invalid input parameters: business name, vat number and tax code must be string.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the customer. Invalid input parameters: business name, vat number and tax code must be string",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot create the customer: customer %s already exists.", bname), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot create the customer: customer %s already exists.", bname),
                        ex);
            } else {
                m = new Message("Cannot create the customer: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot create the customer: unexpected error while accessing the database.", ex);
            }
        }


        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();


    }

}