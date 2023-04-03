package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.DeleteCustomerDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class DeleteCustomerServlet extends AbstractDatabaseServlet {

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
    public void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Customer c = null;
        Message m = null;

        int customerID = -1;

        try {

            // retrieves the request parameters
            customerID = Integer.parseInt(req.getParameter("customerID"));


            //filterCompanyOwner(companyID, ownerID)


            // creates a new customer
            c = new Customer(customerID);

            // creates a new object for accessing the database and stores the customer
            new DeleteCustomerDAO(getConnection(), c).access();

            m = new Message(String.format("Customer successfully deleted."));
            LOGGER.info("Customer successfully removed from the database.");

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot delete the customer. Invalid input parameters: customerID must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the customer. Invalid input parameters: customerID must be integer.",
                    ex);
        } catch (SQLException ex) {

            m = new Message("Cannot delete the customer: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot delete the customer: unexpected error while accessing the database.", ex);

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