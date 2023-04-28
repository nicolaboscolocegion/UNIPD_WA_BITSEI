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
     * The default price per unit of the product.
     */
    private final Double related_price_numeric;
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
     * @param product_description       The description of the product sold.
     * @param purchase_date             The date of the purchase.
     * @param measurement_unit          The measurement unit of the product sold.
     * @param quantity                  The quantity the product sold.
     * @param unit_price                The actual unitary price of the product.
     * @param related_price             The (eventual) related_price of the product.
     * @param related_price_description A textual description of the related_price.
     */
    public DetailRow(final String product_description, final String purchase_date, final int quantity, final String measurement_unit, final double unit_price, final double related_price, final String related_price_description) {

        this.product_description = product_description;
        if (quantity > 0) {
            this.quantity = Integer.toString(quantity);
            this.measurement_unit = measurement_unit;
        } else {
            this.quantity = "";
            this.measurement_unit = "";
        }
        if (unit_price > 0) {
            this.unit_price = unit_price;
        } else {
            this.unit_price = 0.00;
        }

        this.purchase_date = purchase_date;
        if (related_price > 0) {
            this.related_price = Double.toString(related_price);
            this.related_price_numeric = related_price;
            this.related_price_description = related_price_description;
        } else {
            this.related_price = "";
            this.related_price_numeric = 0.0;
            this.related_price_description = "";
        }

        this.totalS = Double.toString(quantity * unit_price);
        this.totalD = quantity * unit_price;
    }


    /**
     * Get product description
     * @return product description
     */
    public String getProduct_description() {
        return product_description;
    }

    /**
     * Get product quantity as string
     * @return product quantity as string
     */
    public String getQuantity() {
        return quantity + " " + this.measurement_unit + ".";
    }

    /**
     * Get product quantity as string with decimals
     * @return product quantity as string with decimals
     */
    public String getNumericQuantity() {
        return quantity + ".00";
    }

    /**
     * Get row measurement unit
     * @return row measurement unit
     */
    public String getMeasurement_unit() {
        return measurement_unit;
    }

    /**
     * Get unit price with currency
     * @return unit price with currency
     */
    public String getUnit_price() {
        return unit_price + " €";
    }

    /**
     * Get unit price with currency
     * @return unit price with currency
     */
    public String getNumericUnit_price() {
        return String.format(Locale.UK, "%.2f", unit_price);
    }

    /**
     * Get related price as string with currency
     * @return related price as string with currency
     */
    public String getRelated_price() {
        return related_price + " €";
    }

    /**
     * Get related price as double
     * @return related price as double
     */
    public Double getRelated_price_numeric() {
        return related_price_numeric;
    }

    /**
     * Get related price description
     * @return related price description
     */
    public String getRelated_price_description() {
        return related_price_description;
    }

    /**
     * Get purchase date
     * @return purchase date
     */
    public String getPurchase_date() {
        return purchase_date;
    }

    /**
     * Get total as string with currency
     * @return total as string with currency
     */
    public String getTotalS() {
        return totalS + " €";
    }

    /**
     * Get total as double
     * @return total as double
     */
    public Double getTotalD() {
        return totalD;
    }
}
