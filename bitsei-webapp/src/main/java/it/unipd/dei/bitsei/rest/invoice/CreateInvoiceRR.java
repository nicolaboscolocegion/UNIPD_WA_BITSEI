package it.unipd.dei.bitsei.rest.invoice;

import it.unipd.dei.bitsei.dao.invoice.CreateInvoiceDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DateTimeException;

import static it.unipd.dei.bitsei.utils.RegexValidationClass.fieldRegexValidation;

/**
 * Creates a new invoice into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CreateInvoiceRR extends AbstractRR {
    private RestURIParser r = null;
    /**
     * Creates a new invoice.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreateInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.CREATE_INVOICE, req, res, con);
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
        Invoice i = null;
        Message m = null;


        try {

            i = Invoice.fromJSON(requestStream);


            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            fieldRegexValidation("[^\\s]+(\\.(?i)(pdf))$", i.getWarning_pdf_file(), "WARNING_PDF_FILE");
            fieldRegexValidation("[^\\s]+(\\.(?i)(pdf))$", i.getInvoice_pdf_file(), "INVOICE_PDF_FILE");
            fieldRegexValidation("[^\\s]+(\\.(?i)(xml))$", i.getInvoice_xml_file(), "INVOICE_XML_FILE");
            long millis=System.currentTimeMillis();
            Date curr_date = new java.sql.Date(millis);
            if(i.getWarning_date().compareTo(curr_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Warning date after current date.");
            if(i.getInvoice_date().compareTo(curr_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Invoice date after current date.");
            if(i.getWarning_date().compareTo(i.getInvoice_date()) > 0) throw new DateTimeException("ERROR, INVALID DATE. Warning date after invoice date.");


            // creates a new object for accessing the database and store the invoice
            new CreateInvoiceDAO(con, i, owner_id, r.getCompanyID()).access();

            m = new Message(String.format("Invoice successfully inserted."));
            LOGGER.info("Invoice successfully inserted.");
            res.setStatus(HttpServletResponse.SC_OK);
            i.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot create invoice: unexpected error while accessing the database.", ex);
            m = new Message("Cannot create invoice: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            m.toJSON(res.getOutputStream());
        }catch (DateTimeException ex) {
            m = new Message(
                    "Cannot create the invoice. Invalid input parameters: invalid date",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the invoice. Invalid input parameters: invalid date",
                    ex);
            m.toJSON(res.getOutputStream());
        }catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
            m.toJSON(res.getOutputStream());
        }
    }
}
