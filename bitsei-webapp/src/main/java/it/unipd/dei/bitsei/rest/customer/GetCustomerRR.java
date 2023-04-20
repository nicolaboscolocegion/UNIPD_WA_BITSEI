package it.unipd.dei.bitsei.rest.customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;


import it.unipd.dei.bitsei.dao.customer.GetCustomerDAO;
import it.unipd.dei.bitsei.resources.Actions;

import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Search customer by his id.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetCustomerRR extends AbstractRR {

    private RestURIParser r = null;

    /**
     * Gets a customer from the database by ID
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GetCustomerRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.GET_CUSTOMER, req, res, con);
        this.r = r;
    }


    /**
     * fetches the customer
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Customer c = null;
        Message m = null;


        try {

            int customerID = r.getResourceID();

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());


            // creates a new object for accessing the database and stores the customer
            c = new GetCustomerDAO(con, customerID, owner_id, r.getCompanyID()).access().getOutputParam();

            m = new Message(String.format("Customer %s successfully fetched.", c.getBusinessName()));
            LOGGER.info("Customer succesfully fetched.");
            res.setStatus(HttpServletResponse.SC_OK);
            c.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot fetch customer: unexpected error while accessing the database.", ex);
            m = new Message("Cannot fetch customer: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            m.toJSON(res.getOutputStream());
        }
    }
}


