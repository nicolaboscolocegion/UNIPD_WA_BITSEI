package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the data for home page.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class HomeData extends AbstractResource {


    private Double total = 0.0;
    private Integer closed_inv = 0;
    private String most_money_cust = "";
    private Double most_money_cust_val = 0.0;
    private Integer active_cust = 0;


    public HomeData(double total, int closed_inv, String most_money_cust, double most_money_cust_val, int active_cust) {
        this.total = total;
        this.closed_inv = closed_inv;
        this.most_money_cust = most_money_cust;
        this.most_money_cust_val = most_money_cust_val;
        this.active_cust = active_cust;
    }

    public double getTotal() {
        return total;
    }

    public int getClosed_inv() {
        return closed_inv;
    }

    public String getMost_money_cust() {
        return most_money_cust;
    }

    public double getMost_money_cust_val() {
        return most_money_cust_val;
    }

    public int getActive_cust() {
        return active_cust;
    }

   @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject(); // {

        jg.writeFieldName("home-data"); // 'message':

        jg.writeStartObject(); // {

        jg.writeStringField("total", total.toString()); //'message':
        jg.writeStringField("closed_inv", closed_inv.toString()); //'message':
        jg.writeStringField("most_money_cust", most_money_cust); //'message':
        jg.writeStringField("most_money_cust_val", most_money_cust_val.toString()); //'message':
        jg.writeStringField("active_cust", active_cust.toString()); //'message':


        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    /**
     * fetch JSONs with all the data for the customer
     * @param in The input stream
     * @return new Customer entity
     * @throws IOException if an I/O error occurs
     */
    /*public static Customer fromJSON(final InputStream in) throws IOException {


        // the fields read from JSON
        String jBusinessName = null;
        String jVatNumber = null;
        String jTaxCode = null;
        String jAddress = null;
        String jCity = null;
        String jProvince = null;
        String jPostalCode = null;
        String jEmailAddress = null;
        String jPec = null;
        String jUniqueCode = null;
        Integer jCompanyID = null;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"customer".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Customer object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no User object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "businessName":
                            jp.nextToken();
                            jBusinessName = jp.getText();
                            break;
                        case "vatNumber":
                            jp.nextToken();
                            jVatNumber = jp.getText();
                            break;
                        case "taxCode":
                            jp.nextToken();
                            jTaxCode = jp.getText();
                            break;
                        case "address":
                            jp.nextToken();
                            jAddress = jp.getText();
                            break;
                        case "city":
                            jp.nextToken();
                            jCity = jp.getText();
                            break;
                        case "province":
                            jp.nextToken();
                            jProvince = jp.getText();
                            break;
                        case "postalCode":
                            jp.nextToken();
                            jPostalCode = jp.getText();
                            break;
                        case "emailAddress":
                            jp.nextToken();
                            jEmailAddress = jp.getText();
                            break;
                        case "pec":
                            jp.nextToken();
                            jPec = jp.getText();
                            break;
                        case "uniqueCode":
                            jp.nextToken();
                            jUniqueCode = jp.getText();
                            break;
                        case "companyID":
                            jp.nextToken();
                            jCompanyID = Integer.parseInt(jp.getText());
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Customer object from JSON.", e);
            throw e;
        }

        return new Customer(jBusinessName, jVatNumber, jTaxCode, jAddress, jCity, jProvince, jPostalCode, jEmailAddress, jPec, jUniqueCode, jCompanyID);
    }*/

}
