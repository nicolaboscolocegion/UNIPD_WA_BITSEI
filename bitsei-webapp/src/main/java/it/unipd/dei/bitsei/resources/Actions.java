/*
 * Copyright 2022-2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.bitsei.resources;


/**
 * Contains constants for the actions performed by the application.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public final class Actions {

    public static final String LIST_USER = "LIST_USER";

    /**
     * login of the user
     */
    public static final String LOGIN = "LOGIN";


    /**
     * The rest password
     */
    public static final String REST_PASSWORD = "REST_PASSWORD";

    /**
     * The change password
     */
    public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";


    public static final String CREATE_COMPANY = "CREATE_COMPANY";
    public static final String UPDATE_COMPANY = "UPDATE_COMPANY";

    public static final String LIST_COMPANIES = "LIST_COMPANIES";
    public static final String GET_COMPANY = "GET_COMPANY";
    public static final String DELETE_COMPANY = "DELETE_COMPANY";

    public static final String GET_COMPANY_IMAGE = "GET_COMPANY_IMAGE";

    /**
     * The list of the invoices filtered by Company ID
     */
    public static final String LIST_INVOICES_BY_COMPANY_ID = "LIST_INVOICES_BY_COMPANY_ID";

    /**
     * The list of the products filtered by Company ID
     */
    public static final String LIST_PRODUCTS_BY_COMPANY_ID = "LIST_PRODUCTS_BY_COMPANY_ID";

    /**
     * The list of the customers filtered by Company ID
     */
    public static final String LIST_CUSTOMERS_BY_COMPANY_ID = "LIST_CUSTOMERS_BY_COMPANY_ID";

    /**
     * The filter of invoices by their period of time
     */
    public static final String FILTER_INVOICES_BY_PERIOD = "FILTER_INVOICES_BY_PERIOD";

    /**
     * The filter of invoices by their total
     */
    public static final String FILTER_INVOICES_BY_TOTAL = "FILTER_INVOICES_BY_TOTAL";

    /**
     * The chart of amount of invoices ordered by date
     */
    public static final String INVOICES_BY_DATE = "INVOICES_BY_DATE";


    /**
     * The invoices get by filters
     */
    public static final String GET_INVOICES_BY_FILTERS = "GET_INVOICES_BY_FILTERS";
    public static final String LIST_INVOICES_BY_FILTERS = "LIST_INVOICES_BY_FILTERS";
    public static final String CREATE_CUSTOMER = "CREATE_CUSTOMER";
    public static final String UPDATE_CUSTOMER = "UPDATE_CUSTOMER";
    public static final String GET_CUSTOMER = "GET_CUSTOMER";
    public static final String DELETE_CUSTOMER = "DELETE_CUSTOMER";
    public static final String CLOSE_INVOICE = "CLOSE_INVOICE";
    public static final String SEND_MAIL = "SEND_MAIL";

    /**
     * Creation of new invoice.
     */
    public static final String CREATE_INVOICE = "CREATE_INVOICE";
    /**
     * Update of invoice.
     */
    public static final String UPDATE_INVOICE = "UPDATE_INVOICE";
    /**
     * Get of invoice.
     */
    public static final String GET_INVOICE = "GET_INVOICE";
    /**
     * Deletion of invoice.
     */
    public static final String DELETE_INVOICE = "DELETE_INVOICE";

    /**
     * Creation of new invoice.
     */
    public static final String CREATE_INVOICE_PRODUCT = "CREATE_INVOICE_PRODUCT";
    /**
     * Update of invoice.
     */
    public static final String UPDATE_INVOICE_PRODUCT = "UPDATE_INVOICE_PRODUCT";
    /**
     * Get of invoice.
     */
    public static final String GET_INVOICE_PRODUCT = "GET_INVOICE_PRODUCT";
    /**
     * Deletion of invoice.
     */
    public static final String DELETE_INVOICE_PRODUCT = "DELETE_INVOICE_PRODUCT";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}
