package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.FetchCustomersDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static it.unipd.dei.bitsei.utils.ReportClass.exportCustomerReport;


public class GenerateCustomersReportServlet extends AbstractDatabaseServlet{

    /**
     * Searches employees by their salary.
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
        List<Customer> lc;
        Message m = null;

        try {


            // creates a new object for accessing the database and searching the employees
            lc = new FetchCustomersDAO(getConnection()).access().getOutputParam();

            m = new Message("Customers successfully fetched.");

            LOGGER.info("Customers successfully fetched.");

            //LOGGER.info("abs path: " + super.getServletContext().getRealPath("/"));

            exportCustomerReport(lc, super.getServletContext().getRealPath("/"));



        } catch (SQLException ex) {
            m = new Message("Cannot search for customers: unexpected error while accessing the database.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot search for customers: unexpected error while accessing the database.", ex);
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