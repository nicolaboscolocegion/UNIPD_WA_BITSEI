package it.unipd.dei.bitsei.servlet;


import it.unipd.dei.bitsei.dao.UpdateCustomerDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;

import static it.unipd.dei.bitsei.utils.RegexValidation.fieldRegexValidation;

/**
 * Updates a customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateCustomerServlet extends AbstractDatabaseServlet {

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
    public void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Customer c = null;
        Message m = null;

        String businessName = null;
        String vatNumber = null;
        String taxCode = null;
        String address = null;
        String city = null;
        String province = null;
        String postalCode = null;
        String emailAddress = null;
        String pec = null;
        String uniqueCode = null;
        int companyID = -1;
        int customerID = -1;

        try {

            // retrieves the request parameters
            businessName = req.getParameter("businessName");
            vatNumber = req.getParameter("vatNumber");
            taxCode = req.getParameter("taxCode");
            address = req.getParameter("address");
            city = req.getParameter("city");
            province = req.getParameter("province");
            postalCode = req.getParameter("postalCode");
            emailAddress = req.getParameter("emailAddress");
            pec = req.getParameter("pec");
            uniqueCode = req.getParameter("uniqueCode");
            String companyID_raw = req.getParameter("companyID");
            try {
                companyID = Integer.parseInt(companyID_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.info("No company id provided for %s, will be set to null.", businessName);
            }

            String customerID_raw = req.getParameter("customerID");
            try {
                customerID = Integer.parseInt(customerID_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.error("Error while parsing business %s", businessName);
            }

            //filterCompanyOwner(companyID, ownerID)
            LOGGER.info("data parsed: " + emailAddress);

            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", emailAddress, "EMAIL");
            fieldRegexValidation("^(IT)?[0-9]{11}$", vatNumber, "VAT NUMBER");
            fieldRegexValidation("^([A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1})$|([0-9]{11})$", taxCode, "TAX CODE");
            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@(?:\\w*.?pec(?:.?\\w+)*)", pec, "PEC");
            fieldRegexValidation("^[0-9]{5}$", postalCode, "POSTAL CODE");
            fieldRegexValidation("[A-Z0-9]{6,7}", uniqueCode, "UNIQUE CODE");



            // creates a new foo customer
            c = new Customer(customerID, businessName, vatNumber, taxCode, address, city, province, postalCode, emailAddress, pec, uniqueCode, companyID);

            // creates a new object for accessing the database and stores the customer
            new UpdateCustomerDAO(getConnection(), c).access();

            m = new Message(String.format("Customer %s successfully updated.", businessName));

            LOGGER.info("Customer %s successfully updated in the database.", businessName);

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot update the customer. Invalid input parameters: business name, vat number and tax code must be string.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot update the customer. Invalid input parameters: business name, vat number and tax code must be string",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot update the customer: customer %s already exists.", businessName), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot update the customer: customer %s already exists.", businessName),
                        ex);
            } else {
                m = new Message("Cannot update the customer: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot update the customer: unexpected error while accessing the database.", ex);
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