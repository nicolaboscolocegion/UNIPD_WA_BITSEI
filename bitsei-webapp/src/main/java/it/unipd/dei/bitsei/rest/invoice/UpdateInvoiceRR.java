package it.unipd.dei.bitsei.rest.invoice;

import it.unipd.dei.bitsei.dao.invoice.UpdateInvoiceDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DateTimeException;

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;

/**
 * Updates an invoice into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UpdateInvoiceRR extends AbstractRR {
    private RestURIParser r = null;

    /**
     * Updates the invoice from the ID.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public UpdateInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.UPDATE_INVOICE, req, res, con);
        this.r = r;
    }


    /**
     * Updates an invoice.
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


            i = Invoice.fromJSON(requestStream);

            i.setInvoice_id(invoice_id);

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            if (i.getPension_fund_refund() < 0 || i.getPension_fund_refund() > 4) {
                LOGGER.error("Pension fund refund can be only beetwen 0 and 4.");
                throw new IllegalArgumentException();
            }

            // creates a new object for accessing the database and update the invoice
            new UpdateInvoiceDAO(con, i, owner_id, r.getCompanyID()).access();

            m = new Message(String.format("Invoice successfully updated."));
            LOGGER.info("Invoice successfully updated in the database.");

            res.setStatus(HttpServletResponse.SC_OK);
            i.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot update invoice: unexpected error while accessing the database.", ex.getMessage());
            m = new Message("Cannot update invoice: unexpected error while accessing the database.", "E5A1", "");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("No company id provided, will be set to null.", "E5A1", "");
            LOGGER.info("No company id provided, will be set to null.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (DateTimeException ex) {
            m = new Message(
                    "Cannot create the invoice. Invalid input parameters: invalid date",
                    "E100", "");

            LOGGER.error(
                    "Cannot create the invoice. Invalid input parameters: invalid date",
                    ex);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", "");

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (IllegalCallerException e) {
            m = new Message(
                    "Error: unexpected call to UpdateInvoice after invoice has been closed or emitted.");

            LOGGER.error(
                    "Error: unexpected call to UpdateInvoice after invoice has been closed or emitted.");
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
