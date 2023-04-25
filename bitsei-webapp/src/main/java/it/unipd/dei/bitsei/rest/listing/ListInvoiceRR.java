package it.unipd.dei.bitsei.rest.listing;

import it.unipd.dei.bitsei.dao.listing.ListInvoiceDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link Invoice}s.
 */
public final class ListInvoiceRR extends AbstractRR {
    private final int company_id;
    /**
     * Creates a new REST resource for listing {@code Invoice}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param
     */
    public ListInvoiceRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int company_id) {
        super(Actions.LIST_INVOICES_BY_COMPANY_ID, req, res, con);
        this.company_id = company_id;
    }

    @Override
    protected void doServe() throws IOException {
        final int owner_id;
        List<Invoice> el = null;
        Message m = null;

        try {
            owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceDAO(con, owner_id, company_id).access().getOutputParam();

            if (el != null) {
                LOGGER.info("## ListInvoiceRR: Invoice(s) successfully listed. ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## ListInvoiceRR: Fatal error while listing invoice(s). ##");

                m = new Message("## ListInvoiceRR: Cannot list invoice(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## ListInvoiceRR: Cannot list invoice(s): unexpected database error. ##", ex);

            m = new Message("## ListInvoiceRR: Cannot list invoice(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("## ListInvoiceRR: Owner not parsable. ##", "E5A1", ex.getMessage());
            LOGGER.info("## ListInvoiceRR: Owner not parsable. " + ex.getStackTrace() + " ##");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("## ListInvoiceRR: Runtime exception: " + e.getStackTrace() + " ##");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }


}
