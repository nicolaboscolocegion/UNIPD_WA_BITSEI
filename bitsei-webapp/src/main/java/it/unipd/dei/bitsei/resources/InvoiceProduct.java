package it.unipd.dei.bitsei.resources;

/**
 * Represents the data about an invoice row of an Invoice.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InvoiceProduct {

    /**
     * The unique id of the product.
     */
    private final Integer invoice_id;

    /**
     * The unique id of the product.
     */
    private final Integer product_id;

    /**
     * The external key pointing to the id of the company that sells the product.
     */
    private final Integer quantity;
    /**
     * The name of the product.
     */
    private final Double unit_price;
    /**
     * The default price per unit of the product.
     */
    private final Double related_price;
    /**
     * A path pointing to the logo/image of the product.
     */
    private final String related_price_description;

    private final String purchase_date = "01/01/1971";
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
     */
    public InvoiceProduct(final int invoice_id, final int product_id, final int quantity, final double unit_price, final double related_price, final String related_price_description) {
        this.invoice_id = invoice_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.related_price = related_price;
        this.related_price_description = related_price_description;
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

    public String getPurchase_date() {
        return purchase_date;
    }

    public Double getTotal() {
        return total;
    }
}
