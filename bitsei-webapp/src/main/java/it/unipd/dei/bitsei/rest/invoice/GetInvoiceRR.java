package it.unipd.dei.bitsei.rest.invoice;

import it.unipd.dei.bitsei.dao.invoice.GetInvoiceDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Searches invoice by id.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetInvoiceRR extends AbstractRR {
    private RestURIParser r = null;

    /**
     * Gets an invoice from the database by ID.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GetInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.GET_INVOICE, req, res, con);
        this.r = r;
    }


    /**
     * fetches the invoice.
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Invoice i = null;
        Message m = null;


        try {

            int invoice_id = r.getResourceID();

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());


            // creates a new object for accessing the database and store the invoice
            i = new GetInvoiceDAO(con, invoice_id, owner_id, r.getCompanyID()).access().getOutputParam();

            m = new Message(String.format("Invoice successfully fetched."));
            LOGGER.info("Invoice successfully fetched.");
            res.setStatus(HttpServletResponse.SC_OK);
            i.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot fetch invoice: unexpected error while accessing the database.", ex);
            m = new Message("Cannot fetch invoice: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            m.toJSON(res.getOutputStream());
        }
    }
}
