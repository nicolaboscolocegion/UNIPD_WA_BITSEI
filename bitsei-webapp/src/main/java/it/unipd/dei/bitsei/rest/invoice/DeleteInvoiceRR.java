package it.unipd.dei.bitsei.rest.invoice;

import it.unipd.dei.bitsei.dao.invoice.DeleteInvoiceDAO;
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
 * Deletes an invoice from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class DeleteInvoiceRR extends AbstractRR {


    private RestURIParser r = null;

    /**
     * Deletes the invoice.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public DeleteInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.DELETE_INVOICE, req, res, con);
        this.r = r;
    }


    /**
     * Deletes an invoice.
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

            // creates a new object for accessing the database and delete the invoice

            i = (Invoice) new DeleteInvoiceDAO<Invoice>(con, invoice_id, owner_id, r.getCompanyID()).access().getOutputParam();


            m = new Message(String.format("Invoice successfully deleted."));
            LOGGER.info("Invoice deleted in the database.");

            res.setStatus(HttpServletResponse.SC_OK);
            i.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot delete invoice: unexpected error while accessing the database.", ex.getMessage());
            m = new Message("Cannot delete invoice: unexpected error while accessing the database.", "E5A1", "");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", "");
            LOGGER.info("No company id provided.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", "");

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalCallerException e) {
            m = new Message(
                    "Error: unexpected call to DeleteInvoice after invoice has been closed or emitted.");

            LOGGER.error(
                    "Error: unexpected call to DeleteInvoice after invoice has been closed or emitted.");
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}