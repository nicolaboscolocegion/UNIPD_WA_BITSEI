package it.unipd.dei.bitsei.rest.listing;

import it.unipd.dei.bitsei.dao.listing.ListInvoiceDAO;
import it.unipd.dei.bitsei.dao.listing.ListInvoiceProductDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * List invoice rows of a specific invoice.
 *
 * @author Mirco Cazzaro
 * @version 1.00
 * @since 1.00
 */
public final class ListInvoiceProductRR extends AbstractRR {

    /**
     * Creates a new REST resource for listing {@code Invoice}s.
     *
     * @param req        the HTTP request.
     * @param res        the HTTP response.
     * @param con        the connection to the database.
     */
    public ListInvoiceProductRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_INVOICES_BY_COMPANY_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final int owner_id, company_id, invoice_id;

        try {
            owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            LOGGER.warn(owner_id);
            String[] paths = req.getRequestURI().split("/");
            company_id = Integer.parseInt(paths[4]);
            invoice_id = Integer.parseInt(paths[5]);
        } catch (Exception e) {
            LOGGER.warn("## ListInvoiceByFiltersRR: Illegal value for attribute {owner_id} ##" + e);
            return;
        }

        List<InvoiceProduct> el = null;
        Message m = null;

        try {
            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceProductDAO(con, owner_id, company_id, invoice_id).access().getOutputParam();

            if (el != null) {
                LOGGER.info("## ListInvoiceProductRR: Invoice Row(s) successfully listed. ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## ListInvoiceProductRR: Fatal error while listing invoice row(s). ##");

                m = new Message("## ListInvoiceProductRR: Cannot list invoice row(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## ListInvoiceProductRR: Cannot list invoice row(s): unexpected database error. ##", ex);

            m = new Message("## ListInvoiceProductRR: Cannot list invoice row(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}
