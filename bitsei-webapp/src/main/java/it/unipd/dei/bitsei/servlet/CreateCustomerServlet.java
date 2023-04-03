package it.unipd.dei.bitsei.servlet;



import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

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

        String businessName = "Nicola SRL";
        String vatNumber = "IT05104990287";
        String taxCode = "BSCNCL99A10B563P";
        String address = "Via Roma 1";
        String city = "Milano";
        String province = "MI";
        String postalCode = "00100";
        String emailAddress = "mirco.itis1999@gmail.com";
        String pec = "mirco.cazzaro@pec.it";
        String uniqueCode = "MXU5CS";

        try {
            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", emailAddress, "EMAIL");
            fieldRegexValidation("^(IT)?[0-9]{11}$", vatNumber, "VAT NUMBER");
            fieldRegexValidation("^([A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1})$|([0-9]{11})$", taxCode, "TAX CODE");
            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@(?:\\w*.?pec(?:.?\\w+)*)", pec, "PEC");
            fieldRegexValidation("^[0-9]{5}$", postalCode, "POSTAL CODE");
            fieldRegexValidation("[A-Z0-9]{6,7}", uniqueCode, "UNIQUE CODE");



                    // creates a new foo customer
            e = new Customer(businessName, vatNumber, taxCode, address, city, province, postalCode, emailAddress, pec, uniqueCode);

            // creates a new object for accessing the database and stores the customer
            new CreateCustomerDAO(getConnection(), e).access();

            m = new Message(String.format("Customer %s successfully created.", businessName));

            LOGGER.info("Customer %s successfully created in the database.", businessName);

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot create the customer. Invalid input parameters: business name, vat number and tax code must be string.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the customer. Invalid input parameters: business name, vat number and tax code must be string",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot create the customer: customer %s already exists.", businessName), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot create the customer: customer %s already exists.", businessName),
                        ex);
            } else {
                m = new Message("Cannot create the customer: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot create the customer: unexpected error while accessing the database.", ex);
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

    public void fieldRegexValidation(String regexPattern, String emailAddress, String type) {
        if(!Pattern.compile(regexPattern).matcher(emailAddress).matches()) {
            throw new IllegalArgumentException(String.valueOf(new StringFormattedMessage("%s : format not valid", type)));
        }
    }

}