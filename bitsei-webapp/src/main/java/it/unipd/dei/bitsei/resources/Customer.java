package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the data about a customer.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Customer extends AbstractResource {

    /**
     * The business name of the customer company
     */
    private Integer customerID;

    /**
     * The business name of the customer company
     */
    private final String businessName;

    /**
     * The vat number of the customer company
     */
    private final String vatNumber;

    /**
     * The salary of the customer company
     */
    private final String taxCode;

    /**
     * The address of the customer company
     */
    private final String address;

    /**
     * The city of the customer company
     */
    private final String city;

    /**
     * The province of the customer company
     */
    private final String province;

    /**
     * The province of the customer company
     */
    private final String postalCode;

    /**
     * The full address of the customer company
     */
    private final String fullAddress;

    /**
     * The province of the customer company
     */
    private final String emailAddress;

    /**
     * The province of the customer company
     */
    private final String pec;

    /**
     * The province of the customer company
     */
    private final String uniqueCode;

    /**
     * The province of the customer company
     */
    private final Integer companyID;




    /**
     * Constructor used to create a new customer
     *
     * @param businessName
     *            the legal name of the customer company.
     * @param vatNumber
     *            the VAT number associated to the customer company
     * @param taxCode
     *            the tax code associated to the customer company (can be the same of the vat number if the company is an autonomous juridical person).
     * @param address
     *            the address associated to the juridical person.
     * @param city
     *            the city associated to the juridical person.
     * @param province
     *            the province associated to the juridical person.
     * @param postalCode
     *            the postal code associated to the juridical person.
     * @param emailAddress
     *            the email address of the customer where notifications and documentation will be submitted.
     * @param pec
     *            the juridical contact email.
     * @param uniqueCode
     *            the unique code associated to the Italian Income Agency invoice submitter provider.
     * @param companyID
     *            the companyID that is setting the customer.
     */
    public Customer(final String businessName, final String vatNumber, final String taxCode, final String address, final String city, final String province, final String postalCode, final String emailAddress, final String pec, final String uniqueCode, final int companyID) {
        this.customerID = -1;
        this.businessName = businessName;
        this.vatNumber = vatNumber;
        this.taxCode = taxCode;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.fullAddress = this.address + " " + this.postalCode + " " + this.city + " (" + this.province + ")";
        this.emailAddress = emailAddress;
        this.pec = pec;
        this.uniqueCode = uniqueCode;
        if (companyID != -1) {
            this.companyID = companyID;
        }
        else {
            this.companyID = null;
        }
    }

    /**
     * Constructor used to create a new customer
     *
     * @param businessName
     *            the legal name of the customer company.
     * @param vatNumber
     *            the VAT number associated to the customer company
     * @param taxCode
     *            the tax code associated to the customer company (can be the same of the vat number if the company is an autonomous juridical person).
     * @param address
     *            the address associated to the juridical person.
     * @param city
     *            the city associated to the juridical person.
     * @param province
     *            the province associated to the juridical person.
     * @param postalCode
     *            the postal code associated to the juridical person.
     * @param emailAddress
     *            the email address of the customer where notifications and documentation will be submitted.
     * @param pec
     *            the juridical contact email.
     * @param uniqueCode
     *            the unique code associated to the Italian Income Agency invoice submitter provider.
     * @param companyID
     *            the companyID that is setting the customer.
     * @param customerID
     *            the customerID of the customer.
     */
    public Customer( final int customerID, final String businessName, final String vatNumber, final String taxCode, final String address, final String city, final String province, final String postalCode, final String emailAddress, final String pec, final String uniqueCode, final int companyID) {
        this.customerID = customerID;
        this.businessName = businessName;
        this.vatNumber = vatNumber;
        this.taxCode = taxCode;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.fullAddress = this.address + " " + this.postalCode + " " + this.city + " (" + this.province + ")";
        this.emailAddress = emailAddress;
        this.pec = pec;
        this.uniqueCode = uniqueCode;
        if (companyID != -1) {
            this.companyID = companyID;
        }
        else {
            this.companyID = null;
        }
    }

    /**
     * Constructor used to delete an existing customer
     *
     * @param customerID
     *            the customerID (primary key).
     */
    public Customer(int customerID) {
        this.customerID = customerID;
        this.businessName = null;
        this.vatNumber = null;
        this.taxCode = null;
        this.address = null;
        this.city = null;
        this.province = null;
        this.postalCode = null;
        this.fullAddress = null;
        this.emailAddress = null;
        this.pec = null;
        this.uniqueCode = null;
        this.companyID = -1;
    }

    /**
     * Returns the business name of the customer company.
     *
     * @return the business name of the customer company.
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * Returns the vat number of the customer company.
     *
     * @return the vat number of the customer company.
     */
    public String getVatNumber() {
        return vatNumber;
    }

    /**
     * Returns the tax code of the customer company.
     *
     * @return the tax code of the customer company.
     */
    public String getTaxCode() {
        return taxCode;
    }

    /**
     * Returns the address of the customer company.
     *
     * @return the address of the customer company.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the address of the customer company.
     *
     * @return the address of the customer company.
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the business name of the customer company.
     *
     * @return the business name of the customer company.
     */
    public String getProvince() {
        return province;
    }

    /**
     * Returns the business name of the customer company.
     *
     * @return the business name of the customer company.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns the email address of the customer company.
     *
     * @return the email address of the customer company.
     */
    public String getFullAddress() {
        return fullAddress;
    }

    /**
     * Returns the email address of the customer company.
     *
     * @return the email address of the customer company.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Returns the pec address of the customer company.
     *
     * @return the pec address of the customer company.
     */
    public String getPec() {
        return pec;
    }

    /**
     * Returns the unique code of the customer company.
     *
     * @return the unique code of the customer company.
     */
    public String getUniqueCode() {
        return uniqueCode;
    }

    /**
     * Returns the company id that has registered this customer.
     *
     * @return the company id that has registered this customer.
     */
    public int getCompanyID() {
        return companyID;
    }

    /**
     * Returns the customer id.
     *
     * @return the customer id.
     */
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {this.customerID = customerID;}


    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject(); // {

        jg.writeFieldName("customer"); // 'message':

        jg.writeStartObject(); // {

        jg.writeStringField("customerID", customerID.toString()); //'message':
        jg.writeStringField("businessName", businessName); //'message':
        jg.writeStringField("vatNumber", vatNumber); //'message':
        jg.writeStringField("taxCode", taxCode); //'message':
        jg.writeStringField("address", address);
        jg.writeStringField("city", city);
        jg.writeStringField("province", province);
        jg.writeStringField("postalCode", postalCode);
        jg.writeStringField("emailAddress", emailAddress);
        jg.writeStringField("pec", pec);
        jg.writeStringField("uniqueCode", uniqueCode);
        jg.writeStringField("companyID", companyID.toString());

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    public static Customer fromJSON(final InputStream in) throws IOException {

/*
        jg.writeStringField("businessName", businessName); //'message':
        jg.writeStringField("vatNumber", vatNumber); //'message':
        jg.writeStringField("taxCode", taxCode); //'message':
        jg.writeStringField("address", address); //'message':
        jg.writeStringField("city", city); //'message':
        jg.writeStringField("province", province); //'message':
        jg.writeStringField("postalCode", postalCode); //'message':
        jg.writeStringField("emailAddress", emailAddress); //'message':
        jg.writeStringField("pec", pec); //'message':
        jg.writeStringField("uniqueCode", uniqueCode); //'message':
*/

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
    }
}
