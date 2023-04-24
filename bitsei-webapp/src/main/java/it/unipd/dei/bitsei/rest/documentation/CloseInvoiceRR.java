package it.unipd.dei.bitsei.rest.documentation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


import it.unipd.dei.bitsei.dao.documentation.CloseInvoiceDAO;
import it.unipd.dei.bitsei.mail.MailManager;
import it.unipd.dei.bitsei.resources.*;

import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.telegram.BitseiBot;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

import static it.unipd.dei.bitsei.utils.ReportClass.exportReport;
import static org.apache.taglibs.standard.functions.Functions.trim;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CloseInvoiceRR extends AbstractRR {


    private final String absPath; //String absPath = super.getServletContext().getRealPath("/");
    private int invoice_id;
    List<Object> out;
    RestURIParser r;

    /**
     * Creates a new customer
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CloseInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, String absPath, RestURIParser r) {
        super(Actions.CLOSE_INVOICE, req, res, con);
        this.absPath = absPath;
        this.r = r;
    }


    /**
     * creates a new customer
     */
    @Override
    protected void doServe() throws IOException {

        invoice_id = r.getResourceID();

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());
        // model
        int invoice_id = 0;


        Message m = null;



        try {

            // retrieves the request parameters

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            // creates a new object for accessing the database and stores the customer
            //String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            java.util.Date utilToday = new Date();
            java.sql.Date today = new java.sql.Date(utilToday.getTime());
            String fileName = "warning_" + UUID.randomUUID() + ".pdf";

            out = new CloseInvoiceDAO(con, this.invoice_id, today, fileName, owner_id, r.getCompanyID()).access().getOutputParam();
            m = new Message(String.format("Data for invoice warning fetched"));
            LOGGER.info("Data for invoice warning fetched");


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
            exportReport(ldr, absPath, "/jrxml/Invoice.jrxml", fileName, map);

            //sending mail with attachment notification to customer
            MailManager.sendAttachmentMail(c.getEmailAddress(), "New invoice warning from " + map.get("customer_name"), "Dear " + c.getBusinessName() + "\na new invoice warning has been sent from " + map.get("customer_name") + " to you. Please do not reply to this message.", "text/html;charset=UTF-8", absPath + "/pdf/" + fileName, "application/pdf", fileName);

            //sending mail notification to company owner
            MailManager.sendMail(req.getSession().getAttribute("email").toString(), "New invoice warning for " + map.get("customer_name"), "Attention: the invoice warning " + fileName + " has been sent to " + c.getBusinessName() + "(" + c.getEmailAddress() + ").", "text/html;charset=UTF-8");

            //sending telegram notification to company owner
            String telegram_chat_id = (String) out.get(12);
            if (telegram_chat_id != null && !telegram_chat_id.equals("")) {
                BitseiBot bt = new BitseiBot();
                bt.sendMessageWithAttachments((String) out.get(12), "New invoice warning for " + map.get("customer_name") + "\n\nAttention: the invoice warning " + fileName + " has been sent to " + c.getBusinessName() + "(" + trim(c.getEmailAddress()) + ").", absPath + "/pdf/" + fileName);
            }

        }catch(SQLException ex){
            LOGGER.error("Cannot create customer: unexpected error while accessing the database.", ex);
            m = new Message("Cannot create customer: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided for " +  out.get(3) + ", will be set to null.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided for %s, will be set to null.", out.get(3));
            m.toJSON(res.getOutputStream());
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}


