package it.unipd.dei.bitsei.rest.customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;


import it.unipd.dei.bitsei.dao.customer.UpdateCustomerDAO;
import it.unipd.dei.bitsei.resources.Actions;

import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;


public class UpdateCustomerRR extends AbstractRR {


    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public UpdateCustomerRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.UPDATE_CUSTOMER, req, res, con);
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

            String uri = req.getRequestURI();
            String id = uri.substring(uri.lastIndexOf('/') + 1);
            if (id.isEmpty() || id.isBlank()) {
                throw new IOException("company id cannot be empty.");
            }

            int customerID = Integer.parseInt(id);


            c = Customer.fromJSON(requestStream);

            c.setCustomerID(customerID);

            //filterCompanyOwner(companyID, ownerID)

            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", c.getEmailAddress(), "EMAIL");
            fieldRegexValidation("^(IT)?[0-9]{11}$", c.getVatNumber(), "VAT NUMBER");
            fieldRegexValidation("^([A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1})$|([0-9]{11})$", c.getTaxCode(), "TAX CODE");
            fieldRegexValidation("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@(?:\\w*.?pec(?:.?\\w+)*)", c.getPec(), "PEC");
            fieldRegexValidation("^[0-9]{5}$", c.getPostalCode(), "POSTAL CODE");
            fieldRegexValidation("[A-Z0-9]{6,7}", c.getUniqueCode(), "UNIQUE CODE");


            // creates a new object for accessing the database and stores the customer
            new UpdateCustomerDAO(con, c).access();

            m = new Message(String.format("Customer %s successfully updated.", c.getBusinessName()));
            LOGGER.info("Customer %s successfully updated in the database.", c.getBusinessName());

            res.setStatus(HttpServletResponse.SC_OK);
            c.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot update customer: unexpected error while accessing the database.", ex);
            m = new Message("Cannot update customer: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided for " +  c.getBusinessName() + ", will be set to null.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided for %s, will be set to null.", c.getBusinessName());
            m.toJSON(res.getOutputStream());
        }
    }
}


