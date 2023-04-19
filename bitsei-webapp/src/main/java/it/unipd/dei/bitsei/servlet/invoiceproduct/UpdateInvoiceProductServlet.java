package it.unipd.dei.bitsei.servlet.invoiceproduct;


import it.unipd.dei.bitsei.dao.invoiceproduct.UpdateInvoiceProductDAO;
import it.unipd.dei.bitsei.resources.InvoiceProduct;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DateTimeException;


/**
 * Updates an invoice product into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class UpdateInvoiceProductServlet extends AbstractDatabaseServlet {

    /**
     * Updates the invoice product into the database.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     *
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        InvoiceProduct i = null;
        Message m = null;

        int invoice_id = -1;
        int product_id = -1;
        int quantity = 0;
        double unit_price = 0;
        double related_price = 0;
        String related_price_description = null;
        Date purchase_date = null;

        try {

            // retrieves the request parameters
            String invoice_id_raw = req.getParameter("invoice_id");
            try {
                invoice_id = Integer.valueOf(invoice_id_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.error("Error while passing invoice.");
            }
            String product_id_raw = req.getParameter("product_id");
            try {
                product_id = Integer.valueOf(product_id_raw);
            }
            catch (NumberFormatException ex) {
                LOGGER.error("Error while passing product.");
            }


            quantity = Integer.parseInt(req.getParameter("quantity"));
            unit_price = Double.parseDouble(req.getParameter("unit_price"));
            related_price = Double.parseDouble(req.getParameter("related_price"));
            related_price_description = req.getParameter("related_price_description");
            purchase_date = Date.valueOf(req.getParameter("purchase_date"));



            LOGGER.info("DATA: quant: " + quantity + " unPrice: " + unit_price + " relPrice: " + related_price + " relPriceDesc: " + related_price_description + " purchDate: " + purchase_date);

            if(quantity<0) throw  new IllegalArgumentException(("ERROR. Quantity cannot be negative."));
            if(unit_price<0) throw  new IllegalArgumentException(("ERROR. Unit price cannot be negative."));
            if(related_price<0) throw  new IllegalArgumentException(("ERROR. Related price cannot be negative."));
            long millis=System.currentTimeMillis();
            Date curr_date = new java.sql.Date(millis);
            if(purchase_date.compareTo(curr_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Purchase date after current date.");

            // creates a new foo invoice
            i = new InvoiceProduct(invoice_id, product_id, quantity, unit_price, related_price, related_price_description, purchase_date);

            // creates a new object for accessing the database and stores the customer
            new UpdateInvoiceProductDAO(getConnection(), i).access();

            m = new Message(String.format("Invoice product successfully updated."));

            LOGGER.info("Invoice product successfully updated.");

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot update the invoice product. Invalid input parameters: invoice_id, product_id and quantity must be integers; unit_price and related_price must be double; related_price_description must be string; purchase_date must be date.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot update the invoice product. Invalid input parameters: invoice_id, product_id and quantity must be integers; unit_price and related_price must be double; related_price_description must be string; purchase_date must be date.",
                    ex);
        } catch (SQLException ex) {
            m = new Message("Cannot update the invoice product: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

            LOGGER.error("Cannot update the invoice product: unexpected error while accessing the database.", ex);

        }  catch (IllegalArgumentException ex) {
            m = new Message(
                    "Invalid input parameters. ",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Invalid input parameters. " + ex.getMessage(), ex);
        }


        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeResource();


    }

}