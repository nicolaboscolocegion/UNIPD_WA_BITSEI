package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;


/**
 * Represents the data about an invoice row of an Invoice.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InvoiceProduct extends AbstractResource {

    /**
     * The id of the invoice.
     */
    private Integer invoice_id;

    /**
     * The id of the product.
     */
    private Integer product_id;

    /**
     * The quantity of product sold.
     */
    private final Integer quantity;
    /**
     * The price per unit of the product sold.
     */
    private final Double unit_price;
    /**
     * The related price of the product sold.
     */
    private final Double related_price;
    /**
     * A description for the related price of the product sold.
     */
    private final String related_price_description;

    /**
     * Teh date of the purchase of the product.
     */
    private final Date purchase_date;

    /**
     * The total amount = quantity * unit_price.
     */
    private final Double total;


    /**
     * Creates a new Product.
     *
     * @param invoice_id The id of the invoice referred.
     *
     * @param product_id The id of the product referred.
     *
     * @param quantity The quantity the product sold.
     *
     * @param unit_price The actual unitary price of the product.
     *
     * @param related_price The (eventual) related_price of the product.
     *
     * @param related_price_description A textual description of the related_price.
     *
     * @param purchase_date The date of the purchase of the product.
     */
    public InvoiceProduct(final int invoice_id, final int product_id, final int quantity, final double unit_price, final double related_price, final String related_price_description, final Date purchase_date) {
        if (invoice_id != -1) {
            this.invoice_id = invoice_id;
        }
        else {
            this.invoice_id = 0;
        }
        if (product_id != -1) {
            this.product_id = product_id;
        }
        else {
            this.product_id = 0;
        }
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.related_price = related_price;
        this.related_price_description = related_price_description;
        this.purchase_date = purchase_date;
        this.total = this.quantity * this.unit_price;
    }

    /**
     * Constructor used to delete an existing invoice product.
     *
     * @param invoice_id The id of the invoice referred.
     *
     * @param product_id The id of the product referred.
     */
    public InvoiceProduct(int invoice_id, int product_id) {
        this.invoice_id = invoice_id;
        this.product_id = product_id;
        this.quantity = -1;
        this.unit_price = null;
        this.related_price = null;
        this.related_price_description = null;
        this.purchase_date = null;
        this.total = null;
    }

    public Integer getInvoice_id() {
        return invoice_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public Double getRelated_price() {
        return related_price;
    }

    public String getRelated_price_description() {
        return related_price_description;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public Double getTotal() {
        return total;
    }

    /**
     * Sets the invoice ID of this object to the ID passed as parameter.
     * @param invoice_id The value for the invoice ID we want to set.
     */
    public void setInvoice_id(int invoice_id) {this.invoice_id = invoice_id;}

    /**
     * Sets the product ID of this object to the ID passed as parameter.
     * @param product_id The value for the product ID we want to set.
     */
    public void setProduct_id(int product_id) {this.product_id = product_id;}


    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("invoiceproduct");
        jg.writeStartObject();

        try {
            jg.writeNumberField("invoice_id", invoice_id);
            jg.writeNumberField("product_id", product_id);
            jg.writeNumberField("quantity", quantity);
            jg.writeNumberField("unit_price", unit_price);
            jg.writeNumberField("related_price", related_price);
            jg.writeStringField("related_price_description", related_price_description);
            jg.writeStringField("purchase_date", purchase_date.toString());

        } catch (Throwable T) {
            //LOGGER.warn("## INVOICE PRODUCT CLASS: Invoice Product with invoice_id #%d and product_id #%d has null field(s).", invoice_id, product_id);
        }

        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Creates a {@code InvoiceProduct} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code Invoice} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static InvoiceProduct fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jInvoice_id = -1;
        int jProduct_id = -1;
        int jQuantity = -1;
        double jUnit_price = -1;
        double jRelated_price = -1;
        String jRelated_price_description = null;
        Date jPurchase_date = null;
        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"invoiceproduct".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No InvoiceProduct object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no InvoiceProduct object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "invoice_id":
                            jp.nextToken();
                            jInvoice_id = jp.getIntValue();
                            break;
                        case "product_id":
                            jp.nextToken();
                            jProduct_id = jp.getIntValue();
                            break;
                        case "quantity":
                            jp.nextToken();
                            jQuantity = jp.getIntValue();
                            break;
                        case "unit_price":
                            jp.nextToken();
                            jUnit_price = jp.getDoubleValue();
                            break;
                        case "related_price":
                            jp.nextToken();
                            jRelated_price = jp.getDoubleValue();
                            break;
                        case "related_price_description":
                            jp.nextToken();
                            jRelated_price_description = jp.getText();
                            break;
                        case "purchase_date":
                            jp.nextToken();
                            String tmpDate = jp.getText();
                            jPurchase_date = Date.valueOf(tmpDate);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an InvoiceProduct object from JSON.", e);
            throw e;
        }

        return new InvoiceProduct(jInvoice_id, jProduct_id, jQuantity, jUnit_price, jRelated_price, jRelated_price_description, jPurchase_date);
    }
}
