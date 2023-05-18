package it.unipd.dei.bitsei.rest.product;

import it.unipd.dei.bitsei.dao.product.GetProductDAO;
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
 * Searches product by id.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetProductRR extends AbstractRR {
    private RestURIParser r = null;

    /**
     * Gets a product from the database by ID.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public GetProductRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.GET_PRODUCT, req, res, con);
        this.r = r;
    }


    /**
     * fetches the product.
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Product p = null;
        Message m = null;


        try {

            int product_id = r.getResourceID();

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());


            // creates a new object for accessing the database and store the product
            p = new GetProductDAO(con, product_id, owner_id, r.getCompanyID()).access().getOutputParam();

            m = new Message(String.format("Product successfully fetched."));
            LOGGER.info("Product successfully fetched.");
            res.setStatus(HttpServletResponse.SC_OK);
            p.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot fetch product: unexpected error while accessing the database.", ex);
            m = new Message("Cannot fetch product: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
