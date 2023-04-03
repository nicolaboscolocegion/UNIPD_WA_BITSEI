package it.unipd.dei.bitsei.resources;

/**
 * Represents the data about a customer.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Customer {

    /**
     * The business name of the customer company
     */
    private final int customerID;

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
     * Creates a new customer
     *
     * @param businessName
     *            the legal name of the customer company.
     * @param vatNumber
     *            the VAT number associated to the customer company
     * @param taxCode
     *            the tax code associated to the customer company (can be the same of the vat number if the company is an autonomous juridical person).
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

    public Customer(int customerID) {
        this.customerID = customerID;
        this.businessName = null;
        this.vatNumber = null;
        this.taxCode = null;
        this.address = null;
        this.city = null;
        this.province = null;
        this.postalCode = null;
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
}
