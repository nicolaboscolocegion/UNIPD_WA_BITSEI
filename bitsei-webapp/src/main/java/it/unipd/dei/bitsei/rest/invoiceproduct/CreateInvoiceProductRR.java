package it.unipd.dei.bitsei.rest.invoiceproduct;

import it.unipd.dei.bitsei.dao.invoiceproduct.CreateInvoiceProductDAO;
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
import java.util.logging.Logger;


/**
 * Creates a new invoice product into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CreateInvoiceProductRR extends AbstractRR {
    int company_id;
    int invoice_id;
    int product_id;
    /**
     * Creates a new invoice product.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param company_id the id of the company of the owner of the session.
     * @param invoice_id the id of the invoice to which the invoice product item is related.
     * @param product_id the id of the product to which the invoice product item is related.
     */
    public CreateInvoiceProductRR(HttpServletRequest req, HttpServletResponse res, Connection con, int company_id, int invoice_id, int product_id) {
        super(Actions.CREATE_INVOICE_PRODUCT, req, res, con);
        this.company_id = company_id;
        this.invoice_id = invoice_id;
        this.product_id = product_id;
    }

    /**
     * creates a new invoice product.
     */
    @Override
    protected void doServe() throws IOException {

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        InvoiceProduct ip = null;
        Message m = null;


        try {

            ip = InvoiceProduct.fromJSON(requestStream);
            LOGGER.info("InvoiceProduct successfully created from JSON.");
            LOGGER.warn(ip.getInvoice_id());

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            if(ip.getQuantity()<0) throw  new IllegalArgumentException(("ERROR. Quantity cannot be negative."));
            if(ip.getUnit_price()<0) throw  new IllegalArgumentException(("ERROR. Unit price cannot be negative."));
            if(ip.getRelated_price()<0) throw  new IllegalArgumentException(("ERROR. Related price cannot be negative."));
            long millis=System.currentTimeMillis();
            Date curr_date = new java.sql.Date(millis);
            if(ip.getPurchase_date().compareTo(curr_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Purchase date after current date.");


            // creates a new object for accessing the database and store the invoice product
            new CreateInvoiceProductDAO(con, ip, owner_id, company_id).access();

            m = new Message(String.format("InvoiceProduct successfully inserted."));
            LOGGER.info("InvoiceProduct successfully inserted.");
            res.setStatus(HttpServletResponse.SC_OK);
            ip.toJSON(res.getOutputStream());


        }catch(SQLException ex){
            LOGGER.error("Cannot create invoice product: unexpected error while accessing the database.", ex.getMessage());
            m = new Message("Cannot create invoice product: unexpected error while accessing the database.", "E5A1", "");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided.", "E5A1", "");
            LOGGER.info("No company id provided.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        }catch (DateTimeException ex) {
            m = new Message(
                    "Cannot create the invoice product. Invalid input parameters: invalid date",
                    "E100", "");

            LOGGER.error(
                    "Cannot create the invoice product. Invalid input parameters: invalid date",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        }catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", "");

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}