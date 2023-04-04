package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.LoadCustomerForUpdateDAO;
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

/**
 * Searches customer by his ID.
 *
 * @author Nicola Ferro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class LoadCustomerForUpdateServlet extends AbstractDatabaseServlet {

    /**
     * Searches employees by their salary.
     *
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     *
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // request parameter
        int customerID = -1;

        // model
        Customer customer = null;
        Message m = null;

        try {

            // retrieves the request parameter
            customerID = Integer.parseInt(req.getParameter("customerID"));

            // creates a new object for accessing the database and searching the employees
            customer = new LoadCustomerForUpdateDAO(getConnection(), customerID).access().getOutputParam();

            m = new Message("Customer successfully searched.");

            LOGGER.info("Customer successfully searched by customerID %d.", customerID);

        } catch (NumberFormatException ex) {
            m = new Message("Cannot search for customer. Invalid input parameters: customerID must be integer.", "E100",
                    ex.getMessage());

            LOGGER.error("Cannot search for customer. Invalid input parameters: customerID must be integer.", ex);
        } catch (SQLException ex) {
            m = new Message("Cannot search for customer: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot search for customer: unexpected error while accessing the database.", ex);
        }


        try {
            // stores the employee list and the message as a request attribute
            req.setAttribute("customer", customer);
            req.setAttribute("message", m);

            // forwards the control to the search-employee-result JSP
            req.getRequestDispatcher("/jsp/customer-data.jsp").forward(req, res);

        } catch(Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response after searching for customer %d.", customerID), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}