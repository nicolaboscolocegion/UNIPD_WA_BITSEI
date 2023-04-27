package it.unipd.dei.bitsei.rest.documentation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


import it.unipd.dei.bitsei.dao.documentation.FetchProductsDAO;
import it.unipd.dei.bitsei.resources.*;

import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

import static it.unipd.dei.bitsei.utils.ReportClass.exportReport;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GenerateProductsReportRR extends AbstractRR {


    private final String absPath;
    private final RestURIParser r;
    // model


    /**
     * Creates a new customer
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GenerateProductsReportRR(HttpServletRequest req, HttpServletResponse res, Connection con, String absPath, RestURIParser r) {
        super(Actions.CLOSE_INVOICE, req, res, con);
        this.absPath = absPath;
        this.r = r;
    }


    /**
     * creates a new customer
     */
    @Override
    protected void doServe() throws IOException {


        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());
        // model


        Message m = null;
        List<Product> lp;


        try {
            // creates a new object for accessing the database and fetching the products
            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            lp = new FetchProductsDAO(con, owner_id, r.getCompanyID()).access().getOutputParam();
            m = new Message("Products successfully fetched.");

            LOGGER.info("Products successfully fetched.");
            LOGGER.info("fetched " + lp.size() + " prods");

            String separator = FileSystems.getDefault().getSeparator();
            Map<String, Object> map = new HashMap<>();
            map.put("proudlyCreatedBy", "Dott. Mirco CAZZARO");
            map.put("company_name", "TEST NAME");
            map.put("company_logo", absPath + "company_logos" + separator + "user_logo_sample.png");
            map.put("box", absPath + "jrxml" + separator + "box.png");
            map.put("bitsei_logo", absPath + "company_logos" + separator + "bitsei_1024_gray_multi.png");


            exportReport(lp, absPath, "/jrxml/ProductList.jrxml", "product_reports.pdf", map);

            res.setStatus(HttpServletResponse.SC_OK);

        }catch(SQLException ex){
            LOGGER.error("Products report: unexpected error while accessing the database.", ex);
            m = new Message("Products report: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("Owner not parsable.", "E5A1", ex.getMessage());
            LOGGER.info("Owner not parsable." + ex.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (JRException e) {
            m = new Message("Error on generating products report PDF file.");
            LOGGER.info("Error on generating products report PDF file. " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}


