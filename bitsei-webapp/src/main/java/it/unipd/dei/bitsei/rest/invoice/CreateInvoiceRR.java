package it.unipd.dei.bitsei.rest.invoice;

import it.unipd.dei.bitsei.dao.invoice.CreateInvoiceDAO;
import it.unipd.dei.bitsei.resources.*;
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
 * Creates a new invoice into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CreateInvoiceRR extends AbstractRR {
    private RestURIParser r = null;

    /**
     * Creates a new invoice.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public CreateInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.CREATE_INVOICE, req, res, con);
        this.r = r;
    }

    /**
     * creates a new invoice.
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Invoice i = null;
        Message m = null;


        try {

            i = Invoice.fromJSON(requestStream);
            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            if (i.getPension_fund_refund() < 0 || i.getPension_fund_refund() > 4) {
                LOGGER.error("Pension fund refund can be only beetwen 0 and 4.");
                throw new IllegalArgumentException();
            }

            // creates a new object for accessing the database and store the invoice
            new CreateInvoiceDAO(con, i, owner_id, r.getCompanyID()).access();

            m = new Message(String.format("Invoice successfully inserted."));
            LOGGER.info("Invoice successfully inserted.");
            res.setStatus(HttpServletResponse.SC_OK);
            i.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot create invoice: unexpected error while accessing the database.", ex.getMessage());
            m = new Message("Cannot create invoice: unexpected error while accessing the database.", "E5A1", "");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", "");
            LOGGER.info("No company id provided.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (DateTimeException ex) {
            m = new Message(
                    "Cannot create the invoice. Invalid input parameters: invalid date",
                    "E100", "");

            LOGGER.error(
                    "Cannot create the invoice. Invalid input parameters: invalid date",
                    ex.getMessage());
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
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
