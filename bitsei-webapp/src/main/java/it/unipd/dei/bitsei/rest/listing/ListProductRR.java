package it.unipd.dei.bitsei.rest.listing;

import it.unipd.dei.bitsei.dao.listing.ListProductDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link Product}s.
 */
public final class ListProductRR extends AbstractRR {
    private final int company_id;
    /**
     * Creates a new REST resource for listing {@code Product}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListProductRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int company_id) {
        super(Actions.LIST_PRODUCTS_BY_COMPANY_ID, req, res, con);
        this.company_id = company_id;
    }

    @Override
    protected void doServe() throws IOException {
        final int owner_id;
        try {
            owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
        }
        catch(Exception e) {
            LOGGER.warn("## ListInvoiceByFiltersRR: Illegal value for attribute {owner_id} ##");
            return;
        }

        List<Product> el = null;
        Message m = null;

        try {

            // creates a new DAO for accessing the database and lists the product(s)
            el = new ListProductDAO(con, owner_id, company_id).access().getOutputParam();

            if (el != null) {
                LOGGER.info("## ListProductRR: Product(s) successfully listed. ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## ListProductRR: Fatal error while listing product(s). ##");

                m = new Message("## ListProductRR: Cannot list product(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## ListProductRR: Cannot list product(s): unexpected database error. ##", ex);

            m = new Message("## ListProductRR: Cannot list product(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}
