package it.unipd.dei.bitsei.rest.documentation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


import it.unipd.dei.bitsei.dao.documentation.FetchCustomersDAO;
import it.unipd.dei.bitsei.resources.*;

import it.unipd.dei.bitsei.rest.AbstractRR;
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
public class GenerateCustomersReportRR extends AbstractRR {


    private final String absPath;
    // model


    /**
     * Creates a new customer
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GenerateCustomersReportRR(HttpServletRequest req, HttpServletResponse res, Connection con, String absPath) {
        super(Actions.CLOSE_INVOICE, req, res, con);
        this.absPath = absPath;
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
        List<Customer> lc;


        try {
            // creates a new object for accessing the database and searching the employees
            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            lc = new FetchCustomersDAO(con, owner_id).access().getOutputParam();
            m = new Message("Customers successfully fetched.");
            LOGGER.info("Customers successfully fetched.");

            String separator = FileSystems.getDefault().getSeparator();
            Map<String, Object> map = new HashMap<>();
            map.put("proudlyCreatedBy", "Dott. Mirco CAZZARO");
            map.put("banner", absPath + "jrxml" + separator + "customer_report_header.png");
            map.put("bitsei_logo", absPath + "company_logos" + separator + "bitsei_1024_gray_multi.png");

            //exportCustomerReport(lc, super.getServletContext().getRealPath("/")); //old style
            exportReport(lc, absPath, "/jrxml/CustomerList.jrxml", "customer_reports.pdf", map);



        }catch(SQLException ex){
            LOGGER.error("Customers report: unexpected error while accessing the database.", ex);
            m = new Message("Customers report: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            m.toJSON(res.getOutputStream());
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}


