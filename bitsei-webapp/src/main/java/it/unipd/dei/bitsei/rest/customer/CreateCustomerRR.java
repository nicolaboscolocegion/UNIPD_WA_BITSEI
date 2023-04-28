package it.unipd.dei.bitsei.rest.customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;


import it.unipd.dei.bitsei.dao.customer.CreateCustomerDAO;
import it.unipd.dei.bitsei.resources.Actions;

import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CreateCustomerRR extends AbstractRR {


    private RestURIParser r = null;

    /**
     * Creates a new customer
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public CreateCustomerRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.CREATE_CUSTOMER, req, res, con);
        this.r = r;
    }


    /**
     * creates a new customer
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Customer c = null;
        Message m = null;


        try {

            c = Customer.fromJSON(requestStream);


            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", c.getEmailAddress(), "EMAIL");
            fieldRegexValidation("^(IT)?[0-9]{11}$", c.getVatNumber(), "VAT NUMBER");
            fieldRegexValidation("^([A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1})$|([0-9]{11})$", c.getTaxCode(), "TAX CODE");
            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@(?:\\w*.?pec(?:.?\\w+)*)", c.getPec(), "PEC");
            fieldRegexValidation("^[0-9]{5}$", c.getPostalCode(), "POSTAL CODE");
            fieldRegexValidation("[A-Z0-9]{6,7}", c.getUniqueCode(), "UNIQUE CODE");


            // creates a new object for accessing the database and stores the customer
            new CreateCustomerDAO(con, c, owner_id, r.getCompanyID()).access();

            m = new Message(String.format("Customer %s successfully inserted.", c.getBusinessName()));
            LOGGER.info("Customer succesfully inserted.");
            res.setStatus(HttpServletResponse.SC_OK);
            c.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot create customer: unexpected error while accessing the database.", ex);
            m = new Message("Cannot create customer: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("Owner not parsable.", "E5A1", ex.getMessage());
            LOGGER.info("Owner not parsable." + ex.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}


