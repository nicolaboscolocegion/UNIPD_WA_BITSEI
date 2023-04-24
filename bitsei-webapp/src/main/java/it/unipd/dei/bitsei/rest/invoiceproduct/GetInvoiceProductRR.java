package it.unipd.dei.bitsei.rest.invoiceproduct;

import it.unipd.dei.bitsei.dao.invoice.GetInvoiceDAO;
import it.unipd.dei.bitsei.dao.invoiceproduct.GetInvoiceProductDAO;
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
 * Searches invoice product by ids.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetInvoiceProductRR extends AbstractRR {
    int company_id;
    int invoice_id;
    int product_id;

    /**
     * Gets an invoice product from the database by ID.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param company_id the id of the company of the owner of the session.
     * @param invoice_id the id of the invoice to which the invoice product item is related.
     * @param product_id the id of the product to which the invoice product item is related.
     */
    public GetInvoiceProductRR(HttpServletRequest req, HttpServletResponse res, Connection con, int company_id, int invoice_id, int product_id) {
        super(Actions.GET_INVOICE_PRODUCT, req, res, con);
        this.company_id = company_id;
        this.invoice_id = invoice_id;
        this.product_id = product_id;
    }


    /**
     * fetches the invoice product.
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        InvoiceProduct ip = null;
        Message m = null;


        try {

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            // creates a new object for accessing the database and store the invoice product
            ip = new GetInvoiceProductDAO(con, invoice_id, product_id, owner_id, company_id).access().getOutputParam();

            m = new Message(String.format("Invoice product successfully fetched."));
            LOGGER.info("Invoice product successfully fetched.");
            res.setStatus(HttpServletResponse.SC_OK);
            ip.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot fetch invoice product: unexpected error while accessing the database.", ex);
            m = new Message("Cannot fetch invoice product: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided.");
            m.toJSON(res.getOutputStream());
        }
    }
}
