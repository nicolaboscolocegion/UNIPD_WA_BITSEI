package it.unipd.dei.bitsei.rest.product;

import it.unipd.dei.bitsei.dao.product.CreateProductDAO;
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
 * Creates a new product into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CreateProductRR extends AbstractRR {
    private RestURIParser r = null;

    /**
     * Creates a new product.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public CreateProductRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.CREATE_PRODUCT, req, res, con);
        this.r = r;
    }

    /**
     * creates a new invoice.
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        Product p = null;
        Message m = null;


        try {

            p = Product.fromJSON(requestStream);


            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            fieldRegexValidation("[^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$", p.getLogo(), "LOGO");


            // creates a new object for accessing the database and store the product
            new CreateProductDAO(con, p, owner_id, r.getCompanyID()).access();

            m = new Message(String.format("Product successfully inserted."));
            LOGGER.info("Product successfully inserted.");
            res.setStatus(HttpServletResponse.SC_OK);
            p.toJSON(res.getOutputStream());


        } catch (SQLException ex) {
            LOGGER.error("Cannot create product: unexpected error while accessing the database.", ex);
            m = new Message("Cannot create product: unexpected error while accessing the database.", "E5A1", ex.getMessage());
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
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
