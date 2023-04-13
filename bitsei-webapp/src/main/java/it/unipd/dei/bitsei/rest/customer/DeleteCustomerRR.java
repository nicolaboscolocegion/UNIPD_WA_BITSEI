package it.unipd.dei.bitsei.rest.customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;


import it.unipd.dei.bitsei.dao.customer.DeleteCustomerDAO;
import it.unipd.dei.bitsei.resources.Actions;

import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class DeleteCustomerRR extends AbstractRR {


    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteCustomerRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.DELETE_CUSTOMER, req, res, con);
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


            //filterCompanyOwner(companyID, ownerID)

            // creates a new object for accessing the database and stores the customer
            c = (Customer) new DeleteCustomerDAO<Customer>(con, customerID).access().getOutputParam();

            m = new Message(String.format("Customer %s successfully deleted.", c.getBusinessName()));
            LOGGER.info("Customer %s deleted updated in the database.", c.getBusinessName());

            res.setStatus(HttpServletResponse.SC_OK);
            c.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot delete customer: unexpected error while accessing the database.", ex);
            m = new Message("Cannot delete customer: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            m.toJSON(res.getOutputStream());
        }
    }
}


