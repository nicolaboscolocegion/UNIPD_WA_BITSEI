package it.unipd.dei.bitsei.rest;

import it.unipd.dei.bitsei.dao.ListInvoiceDAO;
import it.unipd.dei.bitsei.dao.ListUserDAO;
import it.unipd.dei.bitsei.resources.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link User}s.
 */
public final class ListInvoicesRR extends AbstractRR {

    private final int currentUser_companyId;
    /**
     * Creates a new REST resource for listing {@code User}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListInvoicesRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int currentUser_companyId) {
        super(Actions.LIST_INVOICES_BY_COMPANY_ID, req, res, con);
        this.currentUser_companyId = currentUser_companyId;
    }


    @Override
    protected void doServe() throws IOException {

        List<Invoice> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the user(s)
            el = new ListInvoiceDAO(con, currentUser_companyId).access().getOutputParam();

            if (el != null) {
                LOGGER.info("Invoice(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing invoice(s).");

                m = new Message("Cannot list invoice(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list invoice(s): unexpected database error.", ex);

            m = new Message("Cannot list invoice(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}
