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
     * Creates a new customer
     *
     * @param businessName
     *            the legal name of the customer company.
     * @param vatNumber
     *            the VAT number associated to the customer company
     * @param taxCode
     *            the tax code associated to the customer company (can be the same of the vat number if the company is an autonomous juridical person).
     */
    public Customer(final String businessName, final String vatNumber, final String taxCode) {
        this.businessName = businessName;
        this.vatNumber = vatNumber;
        this.taxCode = taxCode;

    }

    /**
     * Returns the business name of the customer company.
     *
     * @return the business name of the customer company.
     */
    public final String getBusinessName() {
        return businessName;
    }

    /**
     * Returns the vat number of the customer company.
     *
     * @return the vat number of the customer company.
     */
    public final String getVatNumber() {
        return vatNumber;
    }

    /**
     * Returns the tax code of the customer company.
     *
     * @return the tax code of the customer company.
     */
    public final String getTaxCode() {
        return taxCode;
    }

}
