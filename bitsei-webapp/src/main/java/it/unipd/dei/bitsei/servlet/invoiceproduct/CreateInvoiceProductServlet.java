package it.unipd.dei.bitsei.servlet.invoiceproduct;

import it.unipd.dei.bitsei.dao.invoiceproduct.CreateInvoiceProductDAO;
import it.unipd.dei.bitsei.resources.InvoiceProduct;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DateTimeException;


/**
 * Creates a new invoice product into the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CreateInvoiceProductServlet extends AbstractDatabaseServlet {
    /**
     * Creates a new invoice product into the database.
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     *
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        // model
        InvoiceProduct i;
        Message m;

        int invoice_id;
        int product_id;
        int quantity;
        double unit_price;
        double related_price;
        String related_price_description;
        Date purchase_date;

        try {

            // retrieves the request parameters
            invoice_id = Integer.parseInt(req.getParameter("invoice_id"));
            product_id = Integer.parseInt(req.getParameter("product_id"));
            quantity = Integer.parseInt(req.getParameter("quantity"));
            unit_price = Double.parseDouble(req.getParameter("unit_price"));
            related_price = Double.parseDouble(req.getParameter("related_price"));
            related_price_description = req.getParameter("related_price_description");
            purchase_date = Date.valueOf(req.getParameter("purchase_date"));


            LOGGER.info("DATA: invID: " + invoice_id + " prodID: " + product_id + " quantity: " + quantity + " unPrice: " + unit_price + " relPrice: " + related_price + " relPriceDesc: " + related_price_description + " purchDate: " + purchase_date);


            if(quantity<0) throw  new IllegalArgumentException(("ERROR. Quantity cannot be negative."));
            if(unit_price<0) throw  new IllegalArgumentException(("ERROR. Unit price cannot be negative."));
            if(related_price<0) throw  new IllegalArgumentException(("ERROR. Related price cannot be negative."));
            long millis=System.currentTimeMillis();
            Date curr_date = new java.sql.Date(millis);
            if(purchase_date.compareTo(curr_date) > 0) throw new DateTimeException("ERROR, INVALID DATE. Purchase date after current date.");


            // creates a new foo invoice product
            i = new InvoiceProduct(invoice_id, product_id, quantity, unit_price, related_price, related_price_description, purchase_date);

            // creates a new object for accessing the database and stores the invoice product
            new CreateInvoiceProductDAO(getConnection(), i).access();

            m = new Message(String.format("Invoice product successfully stored in the database."));

            LOGGER.info("Invoice product successfully stored in the database.");

        } catch (NumberFormatException ex) {
            m = new Message(
                    "Cannot create the invoice product. Invalid input parameters: invoice_id, product_id and quantity must be integers; unit_price and related_price must be double; related_price_description must be string; purchase_date must be date.",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the invoice product. Invalid input parameters: invoice_id, product_id and quantity must be integers; unit_price and related_price must be double; related_price_description must be string; purchase_date must be date.",
                    ex);
        } catch (DateTimeException ex) {
            m = new Message(
                    "Cannot create the invoice product. Invalid input parameters: invalid date",
                    "E100", ex.getMessage());

            LOGGER.error(
                    "Cannot create the invoice product. Invalid input parameters: invalid date",
                    ex);
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot create the invoice product: invoice product already exists."), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot create the invoice product: invoice product already exists."),
                        ex);
            } else if("23503".equals(ex.getSQLState())) {
                m = new Message(String.format("Cannot create the invoice product: invalid id for invoice or product."), "E300",
                        ex.getMessage());

                LOGGER.error(
                        new StringFormattedMessage("Cannot create the invoice product: invalid id for invoice or product."),
                        ex);
            } else {
                m = new Message("Cannot create the invoice product: unexpected error while accessing the database.", "E200",
                        ex.getMessage());

                LOGGER.error("Cannot create the invoice product: unexpected error while accessing the database.", ex);
            }
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
