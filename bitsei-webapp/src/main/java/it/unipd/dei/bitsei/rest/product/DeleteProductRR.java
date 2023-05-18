package it.unipd.dei.bitsei.rest.product;

import it.unipd.dei.bitsei.dao.product.DeleteProductDAO;
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
 * Deletes a product from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class DeleteProductRR extends AbstractRR {


    private RestURIParser r = null;

    /**
     * Deletes the product.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public DeleteProductRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.DELETE_PRODUCT, req, res, con);
        this.r = r;
    }


    /**
     * Deletes an invoice.
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

            // creates a new object for accessing the database and delete the product

            p = (Product) new DeleteProductDAO<Product>(con, product_id, owner_id, r.getCompanyID()).access().getOutputParam();


            m = new Message(String.format("Product successfully deleted."));
            LOGGER.info("Product deleted in the database.");

            res.setStatus(HttpServletResponse.SC_OK);
            p.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot delete product: unexpected error while accessing the database.", ex);
            m = new Message("Cannot delete product: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}