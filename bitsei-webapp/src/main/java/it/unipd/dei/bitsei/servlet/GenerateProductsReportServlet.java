package it.unipd.dei.bitsei.servlet;


import it.unipd.dei.bitsei.dao.FetchProductsDAO;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.FileSystems;



import static it.unipd.dei.bitsei.utils.ReportClass.exportReport;

/**
 * Servlet for products reporting
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GenerateProductsReportServlet extends AbstractDatabaseServlet{

    /**
     * Generates products pdf report.
     *
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     *
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());


        // model
        List<Product> lp;
        Message m = null;

        try {
            // creates a new object for accessing the database and fetching the products
            lp = new FetchProductsDAO(getConnection()).access().getOutputParam();
            m = new Message("Products successfully fetched.");

            LOGGER.info("Products successfully fetched.");
            LOGGER.info("fetched " + lp.size() + " prods");


            String absPath = super.getServletContext().getRealPath("/");
            String separator = FileSystems.getDefault().getSeparator();
            Map<String, Object> map = new HashMap<>();
            map.put("proudlyCreatedBy", "Dott. Mirco CAZZARO");
            map.put("company_name", "TEST NAME");
            map.put("company_logo", absPath + "company_logos" + separator + "user_logo_sample.png");
            map.put("box", absPath + "jrxml" + separator + "box.png");
            map.put("bitsei_logo", absPath + "company_logos" + separator + "bitsei_1024_gray_multi.png");

            //exportProductReport(lp, super.getServletContext().getRealPath("/"));
            exportReport(lp, absPath, "/jrxml/ProductList.jrxml", "product_reports.pdf", map);



        } catch (SQLException ex) {
            m = new Message("Cannot search for products: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot search for products: unexpected error while accessing the database.", ex);
        } catch (JRException ex) {
            m = new Message("Cannot create jasper report: unexpected error.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot create jasper report: unexpected error.", ex);
        }



        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeUser();

    }

}