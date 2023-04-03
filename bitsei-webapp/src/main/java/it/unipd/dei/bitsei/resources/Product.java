package it.unipd.dei.bitsei.resources;

/**
 * Represents the data about a product.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Product {
    /**
     * The external key pointing to the id of the company that sells the product.
     */
    private final int company_id;
    /**
     * The name of the product.
     */
    private final String title;
    /**
     * The default price per unit of the product.
     */
    private final int default_price;
    /**
     * A path pointing to the logo/image of the product.
     */
    private final String logo;
    /**
     * The measurement unit of the product.
     */
    private final String measurement_unit;
    /**
     * A textual description of the product.
     */
    private final String description;

    /**
     * Creates a new Product.
     *
     * @param company_id The id of the company that sells the product.
     *
     * @param title The name of the product.
     *
     * @param default_price The default price of the product.
     *
     * @param logo A path pointing to the logo/image of the product.
     *
     * @param measurement_unit The measurement unit of the product.
     *
     * @param description A textual description of the product.
     */
    public Product(final int company_id, final String title, final int default_price, final String logo, final String measurement_unit, final String description) {
        this.company_id = company_id;
        this.title = title;
        this.default_price = default_price;
        this.logo = logo;
        this.measurement_unit = measurement_unit;
        this.description = description;
    }

    /**
     * Returns the id of the company that sells the product.
     *
     * @return the id of the company that sells the product.
     */
    public int getCompany_id() { return company_id; }

    /**
     * Returns the name (title) of the product.
     *
     * @return the name (title) of the product.
     */
    public String getTitle() { return title; }

    /**
     * Returns the default price of the product.
     *
     * @return the default price of the product.
     */
    public int getDefault_price() { return default_price; }

    /**
     * Returns the path to the logo/image of the product.
     *
     * @return the path to the logo/image of the product.
     */
    public String getLogo() { return logo; }

    /**
     * Returns the measurement unit of the product.
     *
     * @return the measurement unit of the product.
     */
    public String getMeasurement_unit() { return measurement_unit; }

    /**
     * Returns the textual description of the product.
     *
     * @return the textual description of the product.
     */
    public String getDescription() { return description; }
}
