package it.unipd.dei.bitsei.rest.product;

import it.unipd.dei.bitsei.dao.product.UpdateProductDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;

/**
 * Updates a product into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UpdateProductRR extends AbstractRR {
    private RestURIParser r = null;

    /**
     * Updates the product from the ID.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public UpdateProductRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.UPDATE_PRODUCT, req, res, con);
        this.r = r;
    }


    /**
     * Updates a product.
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


            p = Product.fromJSON(requestStream);

            p.setProduct_id(product_id);

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            fieldRegexValidation("[^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$", p.getLogo(), "LOGO");


            // creates a new object for accessing the database and update the product
            new UpdateProductDAO(con, p, owner_id, r.getCompanyID()).access();

            m = new Message(String.format("Product successfully updated."));
            LOGGER.info("Product successfully updated in the database.");

            res.setStatus(HttpServletResponse.SC_OK);
            p.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot update product: unexpected error while accessing the database.", ex);
            m = new Message("Cannot update product: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("No company id provided, will be set to null.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided, will be set to null.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

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
