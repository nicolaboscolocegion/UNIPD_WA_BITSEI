package it.unipd.dei.bitsei.resources;

import java.util.Locale;

/**
 * Represents the data about an invoice row of an Invoice.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class DetailRow {

    /**
     * A path pointing to the logo/image of the product.
     */
    private final String product_description;
    /**
     * The external key pointing to the id of the company that sells the product.
     */
    private final String quantity;
    /**
     * The name of the product.
     */

    private final String measurement_unit;
    /**
     * The name of the product.
     */
    private final Double unit_price;
    /**
     * The default price per unit of the product.
     */
    private final String related_price;
    /**
     * A path pointing to the logo/image of the product.
     */
    private final String related_price_description;

    private final String purchase_date;
    private final String totalS;
    private final Double totalD;


    /**
     * Creates a new Product.
     *
     * @param product_description The description of the product sold.
     *
     * @param quantity The quantity the product sold.
     *
     * @param unit_price The actual unitary price of the product.
     *
     * @param related_price The (eventual) related_price of the product.
     *
     * @param related_price_description A textual description of the related_price.
     */
    public DetailRow(final String product_description, final String purchase_date, final int quantity, final String measurement_unit, final double unit_price, final double related_price, final String related_price_description) {

        this.product_description = product_description;
        if (quantity > 0) {
            this.quantity = Integer.toString(quantity);
            this.measurement_unit = measurement_unit;
        }
        else {
            this.quantity = "";
            this.measurement_unit = "";
        }
        if (unit_price > 0) {
            this.unit_price = unit_price;
        }
        else {
            this.unit_price = 0.00;
        }

        this.purchase_date = purchase_date;
        if (related_price > 0) {
            this.related_price = Double.toString(related_price);
            this.related_price_description = related_price_description;
        }
        else {
            this.related_price = "";
            this.related_price_description = "";
        }

        this.totalS = Double.toString(quantity * unit_price);
        this.totalD = quantity * unit_price;
    }


    public String getProduct_description() {
        return product_description;
    }

    public String getQuantity() {
        return quantity + " " + this.measurement_unit + ".";
    }

    public String getNumericQuantity() {
        return quantity + ".00";
    }

    public String getMeasurement_unit() {
        return measurement_unit;
    }

    public String getUnit_price() {
        return unit_price + " €";
    }

    public String getNumericUnit_price() {return String.format(Locale.UK,"%.2f",unit_price);}

    public String getRelated_price() {
        return related_price + " €";
    }

    public String getRelated_price_description() {
        return related_price_description;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public String getTotalS() {
        return totalS + " €";
    }

    public Double getTotalD() {
        return totalD;
    }
}
