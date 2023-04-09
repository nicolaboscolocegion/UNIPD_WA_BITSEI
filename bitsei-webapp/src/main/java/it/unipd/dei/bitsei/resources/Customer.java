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
    private final Integer customerID;

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

    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("customer");
        jg.writeStartObject();

        try {
            jg.writeNumberField("customer_id", customerID);
            jg.writeStringField("business_name", businessName);
            jg.writeStringField("vat_number", vatNumber);
            jg.writeStringField("tax_code", taxCode);
            jg.writeStringField("address", address);
            jg.writeStringField("city", city);
            jg.writeStringField("province", province);
            jg.writeStringField("postal_code", postalCode);
            jg.writeStringField("full_address", fullAddress);
            jg.writeStringField("email_address", emailAddress);
            jg.writeStringField("pec", pec);
            jg.writeStringField("unique_code", uniqueCode);
            jg.writeNumberField("company_id", companyID);
        } catch (Throwable T) {
            //LOGGER.warn("## CUSTOMER CLASS: Customer #%d has null field(s).", customer_id);
        }

        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Creates a {@link Customer} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code Customer} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static Customer fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jCustomer_id = -1;
        String jBusiness_name = null;
        String jVat_number = null;
        String jTax_code = null;
        String jAddress = null;
        String jCity = null;
        String jProvince = null;
        String jPostal_code = null;
        String jFull_address = null;
        String jEmail_address = null;
        String jPec = null;
        String jUnique_code = null;
        int jCompany_id = -1;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"customer".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Customer object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Customer object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "customer_id":
                            jp.nextToken();
                            jCustomer_id = jp.getIntValue();
                            break;
                        case "business_name":
                            jp.nextToken();
                            jBusiness_name = jp.getText();
                            break;
                        case "vat_number":
                            jp.nextToken();
                            jVat_number = jp.getText();
                            break;
                        case "tax_code":
                            jp.nextToken();
                            jTax_code = jp.getText();
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
                        case "postal_code":
                            jp.nextToken();
                            jPostal_code = jp.getText();
                            break;
                        case "full_address":
                            jp.nextToken();
                            jFull_address = jp.getText();
                            break;
                        case "email_address":
                            jp.nextToken();
                            jEmail_address = jp.getText();
                            break;
                        case "pec":
                            jp.nextToken();
                            jPec = jp.getText();
                            break;
                        case "unique_code":
                            jp.nextToken();
                            jUnique_code = jp.getText();
                            break;
                        case "company_id":
                            jp.nextToken();
                            jCompany_id = jp.getIntValue();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new Customer(jCustomer_id, jBusiness_name, jVat_number, jTax_code, jAddress, jCity, jProvince, jPostal_code, jEmail_address, jPec, jUnique_code, jCompany_id);
    }


}
