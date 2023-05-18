package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the data about a product.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Product extends AbstractResource{
    /**
     * The unique id of the product.
     */
    private Integer product_id;

    /**
     * The external key pointing to the id of the company that sells the product.
     */
    private final Integer company_id;
    /**
     * The name of the product.
     */
    private final String title;
    /**
     * The default price per unit of the product.
     */
    private final Integer default_price;
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
        this.product_id = -1;
        if (company_id != -1) {
            this.company_id = company_id;
        }
        else {
            this.company_id = null;
        }
        this.title = title;
        this.default_price = default_price;
        this.logo = logo;
        this.measurement_unit = measurement_unit;
        this.description = description;
    }

    /**
     * Creates a new Product.
     *
     * @param product_id The unique id of the product.
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
    public Product(final int product_id, final int company_id, final String title, final int default_price, final String logo, final String measurement_unit, final String description) {
        this.product_id = product_id;
        if (company_id != -1) {
            this.company_id = company_id;
        }
        else {
            this.company_id = null;
        }
        this.title = title;
        this.default_price = default_price;
        this.logo = logo;
        this.measurement_unit = measurement_unit;
        this.description = description;
    }

    /**
     * Constructor used to delete an existing product.
     *
     * @param product_id the product_id (primary key).
     */
    public Product(int product_id) {
        this.product_id = product_id;
        this.company_id = -1;
        this.title = null;
        this.default_price = -1;
        this.logo = null;
        this.measurement_unit = null;
        this.description = null;
    }

    /**
     * Returns the product id (primary key).
     *
     * @return the product id (primary key).
     */
    public int getProduct_id() {
        return product_id;
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

    /**
     * Sets the product ID of this object to the ID passed as parameter.
     * @param product_id The value for the product ID we want to set.
     */
    public void setProduct_id(int product_id) {this.product_id = product_id;}

    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("product");
        jg.writeStartObject();

        try {
            jg.writeNumberField("product_id", product_id);
            jg.writeNumberField("company_id", company_id);
            jg.writeStringField("title", title);
            jg.writeNumberField("default_price", default_price);
            jg.writeStringField("logo", logo);
            jg.writeStringField("measurement_unit", measurement_unit);
            jg.writeStringField("description", description);

        } catch (Throwable T) {
            //LOGGER.warn("## PRODUCT CLASS: Product #%d has null field(s).", product_id);
        }

        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Creates a {@code Product} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code Product} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static Product fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jProduct_id = -1;
        int jCompany_id = -1;
        String jTitle = null;
        int jDefault_price = -1;
        String jLogo = null;
        String jMeasurement_unit = null;
        String jDescription = null;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"product".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Product object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Product object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "product_id":
                            jp.nextToken();
                            jProduct_id = jp.getIntValue();
                            break;
                        case "company_id":
                            jp.nextToken();
                            jCompany_id = jp.getIntValue();
                            break;
                        case "title":
                            jp.nextToken();
                            jTitle = jp.getText();
                            break;
                        case "default_price":
                            jp.nextToken();
                            jDefault_price = jp.getIntValue();
                            break;
                        case "logo":
                            jp.nextToken();
                            jLogo = jp.getText();
                        case "measurement_unit":
                            jp.nextToken();
                            jMeasurement_unit = jp.getText();
                            break;
                        case "description":
                            jp.nextToken();
                            jDescription = jp.getText();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new Product(jProduct_id, jCompany_id, jTitle, jDefault_price, jLogo, jMeasurement_unit, jDescription);
    }

}
