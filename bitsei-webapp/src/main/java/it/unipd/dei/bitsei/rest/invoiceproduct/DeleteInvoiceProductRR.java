package it.unipd.dei.bitsei.rest.invoiceproduct;

import it.unipd.dei.bitsei.dao.invoice.DeleteInvoiceDAO;
import it.unipd.dei.bitsei.dao.invoiceproduct.DeleteInvoiceProductDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Deletes an invoice product from the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class DeleteInvoiceProductRR extends AbstractRR {
    private RestURIParser r = null;

    /**
     * Deletes the invoice product.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteInvoiceProductRR(HttpServletRequest req, HttpServletResponse res, Connection con, RestURIParser r) {
        super(Actions.DELETE_INVOICE_PRODUCT, req, res, con);
        this.r = r;
    }


    /**
     * Deletes an invoice product.
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        InvoiceProduct ip = null;
        Message m = null;


        try {

            int invoice_id = r.getResourceID(); //ATTENZIONE
            int product_id = r.getResourceID(); //ATTENZIONE


            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            // creates a new object for accessing the database and delete the invoice
            ip = (InvoiceProduct) new DeleteInvoiceProductDAO<InvoiceProduct>(con, invoice_id, product_id, owner_id, r.getCompanyID()).access().getOutputParam();

            m = new Message(String.format("Invoice product successfully deleted."));
            LOGGER.info("Invoice product deleted in the database.");

            res.setStatus(HttpServletResponse.SC_OK);
            ip.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot delete invoice product: unexpected error while accessing the database.", ex);
            m = new Message("Cannot delete invoice product: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            m.toJSON(res.getOutputStream());
        }catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
        }
    }
}
