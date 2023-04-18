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
    private final int companyId;
    /**
     * Creates a new REST resource for listing {@code Invoice}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param
     */
    public ListInvoiceRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int companyId) {
        super(Actions.LIST_INVOICES_BY_COMPANY_ID, req, res, con);
        this.companyId = companyId;
    }

    @Override
    protected void doServe() throws IOException {

        List<Invoice> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceDAO(con, "listAll", companyId).access().getOutputParam();

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
        }
    }

    /*
    public void listInvoicesByFilters(int companyId, Map<String, Object> filtersMap) throws IOException {
        List<Invoice> el = null;
        Message m = null;

        try {
            String path = req.getRequestURI();
            path = path.substring(path.lastIndexOf("with-filters") + "with-filters".length());
            String[] filtersInPath = path.split("/");
            for(int i=0; i<filtersInPath.length-1; i+=2) {
                filtersMap.put(filtersInPath[i], filtersInPath[i+1]);
            }


            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceDAO(con, "listAll", companyId).getOutputParam(); //TODO

            if (el != null) {
                LOGGER.info("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Invoice(s) successfully listed ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Fatal error while listing invoice(s).");

                m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Cannot list invoice(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (Throwable ex) {
            LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Cannot list invoice(s): unexpected database error. ##", ex);

            m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Cannot list invoice(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
    */

}
