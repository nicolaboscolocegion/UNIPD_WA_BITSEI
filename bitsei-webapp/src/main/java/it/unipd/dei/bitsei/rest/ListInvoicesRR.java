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
import java.util.Map;

/**
 * A REST resource for listing {@link User}s.
 */
public final class ListInvoicesRR extends AbstractRR {
    /**
     * Creates a new REST resource for listing {@code User}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListInvoicesRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LIST_INVOICES_BY_COMPANY_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        List<Invoice> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceDAO(con).access().getOutputParam();

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

    public void listInvoicesByCompanyId(int companyId) throws IOException {
        List<Invoice> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceDAO(con).listInvoicesByCompanyId(companyId);

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
        } catch (SQLException ex) {
            LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Cannot list invoice(s): unexpected database error. ##", ex);

            m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Cannot list invoice(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

    public void listCustomersByCompanyId(int companyId) throws IOException {
        List<Customer> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceDAO(con).listCustomersByCompanyId(companyId);

            if (el != null) {
                LOGGER.info("## CLASS -> ListInvoicesRR ; FUNC -> listCustomersByCompanyId ; Customer(s) successfully listed ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listCustomersByCompanyId ; Fatal error while listing customer(s).");

                m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listCustomersByCompanyId ; Cannot list customer(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listCustomersByCompanyId ; Cannot list customer(s): unexpected database error. ##", ex);

            m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listCustomersByCompanyId ; Cannot list customer(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

    public void listProductsByCompanyId(int companyId) throws IOException {
        List<Product> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceDAO(con).listProductsByCompanyId(companyId);

            if (el != null) {
                LOGGER.info("## CLASS -> ListInvoicesRR ; FUNC -> listProductsByCompanyId ; Product(s) successfully listed ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listProductsByCompanyId ; Fatal error while listing product(s).");

                m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listProductsByCompanyId ; Cannot list product(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listProductsByCompanyId ; Cannot list product(s): unexpected database error. ##", ex);

            m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listProductsByCompanyId ; Cannot list product(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

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
            el = new ListInvoiceDAO(con).listInvoicesByCompanyId(companyId); //TODO

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
        } catch (SQLException ex) {
            LOGGER.error("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Cannot list invoice(s): unexpected database error. ##", ex);

            m = new Message("## CLASS -> ListInvoicesRR ; FUNC -> listInvoicesByCompanyId ; Cannot list invoice(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}
