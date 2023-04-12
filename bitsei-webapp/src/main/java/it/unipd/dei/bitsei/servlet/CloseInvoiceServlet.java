package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.CloseInvoiceDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.DetailRow;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.unipd.dei.bitsei.utils.ReportClass.exportReport;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CloseInvoiceServlet extends AbstractDatabaseServlet {

    /**
     * Creates a new customer into the database.
     *
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     *
     * @throws ServletException
     *             if any error occurs while executing the servlet.
     * @throws IOException
     *             if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        int invoice_id = 0;

        List<Object> out;
        Message m = null;


        try {

            // retrieves the request parameters
            invoice_id = Integer.parseInt(req.getParameter("invoice_id"));

            // creates a new object for accessing the database and stores the customer
            out = new CloseInvoiceDAO(getConnection(), invoice_id).access().getOutputParam();
            m = new Message(String.format("Data for invoice warning fetched"));
            LOGGER.info("Data for invoice warning fetched");

            String absPath = super.getServletContext().getRealPath("/");
            String separator = FileSystems.getDefault().getSeparator();
            Map<String, Object> map = new HashMap<>();
            map.put("company_logo", absPath + "company_logos" + separator + "user_logo_sample.png");
            map.put("stamp", absPath + "jrxml" + separator + "stamp.png");
            map.put("bitsei_logo", absPath + "company_logos" + separator + "bitsei_1024_gray_multi.png");

            map.put("company_name", out.get(3));
            map.put("company_address", out.get(4));
            map.put("company_city_postalcode_province", out.get(5));
            map.put("company_mail", out.get(6));
            map.put("company_vat", out.get(7));
            map.put("company_tax", out.get(8));
            map.put("company_pec", out.get(9));
            map.put("company_unique_code", out.get(10));

            Invoice i = (Invoice) out.get(2);
            //map.put("invoice_date", i.getWarning_date());
            map.put("invoice_date", "01/01/2023");
            //map.put("invoice_number", i.getWarning_Number());
            map.put("invoice_number", "0110");

            Customer c = (Customer) out.get(1);
            map.put("customer_name", c.getBusinessName());
            map.put("customer_address", c.getAddress());
            map.put("customer_city_postalcode_province", c.getCity() + " " + c.getPostalCode() + " (" + c.getProvince() + ")");
            map.put("customer_mail", c.getEmailAddress());

            map.put("footer", "La presente non costituisce fattura; la stessa sarà emessa contestualmente al pagamento indicato, ai sensi dell’art. 6 c. 3 del DPR n. 633 del 26/10/1972.");

            List<DetailRow> ldr = (List<DetailRow>) out.get(0);

            double total = 0;
            for (DetailRow dr : ldr) {
                total += dr.getTotalD();
            }

            int fiscal_company_type = (int) out.get(11);
            if (fiscal_company_type == 0 || fiscal_company_type == 1 || fiscal_company_type == 2) {
                if (total >= 77.47) {
                    ldr.add(new DetailRow("Imposta di bollo", "", 1, " €", 2, 0, ""));
                    total += 2;
                }
                if (i.getPension_fund_refund() > 0) {
                    ldr.add(new DetailRow("Rivalsa INPS", "", 1,  "€", Math.round(total*i.getPension_fund_refund())/100, 0, ""));
                    total += Math.round(total*i.getPension_fund_refund()/100);
                }
            }

            if (fiscal_company_type == 0) {
                ldr.add(new DetailRow("TOTALE", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
            }
            else {
                ldr.add(new DetailRow("Totale imponibile", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
                ldr.add(new DetailRow("Iva (22%)", "", 1, "€", Math.round(total * 22) / 100, 0, ""));
                total += Math.round(total * 22) / 100;
                ldr.add(new DetailRow("Totale", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
                if (fiscal_company_type == 1) {
                    ldr.add(new DetailRow("Ritenuta d'acconto", "", 1, "€", Math.round(total * 22) / -100, 0, ""));
                    total += Math.round(total * 22) / -100;
                    ldr.add(new DetailRow("Totale da pagare", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
                }
            }





            //generate invoice
            exportReport(ldr, absPath, "/jrxml/Invoice.jrxml", "invoice.pdf", map);



        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot perform action. Invalid input parameters: invoice id must be integer.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot perform action. Invalid input parameters: invoice id must be integer.",
                    ex);
        } catch (SQLException ex) {

                m = new Message("Cannot perform action: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot perform action: unexpected error while accessing the database.", ex);

        }  catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
        } catch (JRException ex) {
            m = new Message("Cannot create jasper report: unexpected error.", "E200",
                    ex.getMessage());

            LOGGER.error("Cannot create jasper report: unexpected error.", ex);
        }


        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();


    }

}