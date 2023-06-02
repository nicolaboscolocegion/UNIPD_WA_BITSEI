package it.unipd.dei.bitsei.rest.listing;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;


import it.unipd.dei.bitsei.dao.customer.GetCustomerDAO;
import it.unipd.dei.bitsei.dao.listing.GetHomeDataDAO;
import it.unipd.dei.bitsei.resources.*;

import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Search customer by his id.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetHomeDataRR extends AbstractRR {

    private RestURIParser r = null;

    /**
     * Gets a customer from the database by ID
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param r   the URI parser.
     */
    public GetHomeDataRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.GET_HOME_DATA, req, res, con);
        this.r = r;

        LOGGER.info("Costruttore");
    }


    /**
     * fetches the customer
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());
        Message m = null;
        HomeData hd = null;

        // model


        try {
            LOGGER.info("Try");

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            LOGGER.info("Owner preso");
            hd = new GetHomeDataDAO(con, owner_id, r.getCompanyID()).access().getOutputParam();
            //hd = new HomeData(1,1, "aaaa", 1,1);
            LOGGER.info("Dao eseguito");
            res.setStatus(HttpServletResponse.SC_OK);
            hd.toJSON(res.getOutputStream());


        } catch (NumberFormatException ex) {
            m = new Message("Owner not parsable.", "E5A1", ex.getMessage());
            LOGGER.info("Owner not parsable." + ex.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {

            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


