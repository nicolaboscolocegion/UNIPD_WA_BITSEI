package it.unipd.dei.bitsei.rest.listing;

import it.unipd.dei.bitsei.dao.listing.ListCustomerDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link Customer}s.
 */
public final class ListCustomerRR extends AbstractRR {
    private final int companyId;
    /**
     * Creates a new REST resource for listing {@code Customer}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListCustomerRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int companyId) {
        super(Actions.LIST_CUSTOMERS_BY_COMPANY_ID, req, res, con);
        this.companyId = companyId;
    }

    @Override
    protected void doServe() throws IOException {

        List<Customer> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the customer(s)
            el = new ListCustomerDAO(con, "listAll", companyId).access().getOutputParam();

            if (el != null) {
                LOGGER.info("## ListCustomerRR: Customer(s) successfully listed. ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## ListCustomerRR: Fatal error while listing customer(s). ##");

                m = new Message("## ListCustomerRR: Cannot list customer(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## ListCustomerRR: Cannot list customer(s): unexpected database error. ##", ex);

            m = new Message("## ListCustomerRR: Cannot list customer(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}
