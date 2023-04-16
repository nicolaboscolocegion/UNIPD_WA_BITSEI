package it.unipd.dei.bitsei.resources;

import java.sql.Date;

/**
 * Represents the data about an invoice row of an Invoice.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InvoiceProduct {

    /**
     * The id of the invoice.
     */
    private final Integer invoice_id;

    /**
     * The id of the product.
     */
    private final Integer product_id;

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
        this.invoice_id = invoice_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.related_price = related_price;
        this.related_price_description = related_price_description;
        this.purchase_date = purchase_date;
        this.total = this.quantity * this.unit_price;
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
}
