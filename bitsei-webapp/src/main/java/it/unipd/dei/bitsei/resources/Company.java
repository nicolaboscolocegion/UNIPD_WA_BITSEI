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

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a company.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class Company extends AbstractResource {
    /**
     * The id of the user
     */
    private int company_id;

    private final String title;
    private final String logo;
    private final String business_name;

    private final String vat_number;
    private final String tax_code;
    private final String address;
    private final String province;
    private final String city;
    private final String postal_code;

    private final String unique_code;

    private final boolean has_mail_notifications;
    private final boolean has_telegram_notifications;


    /**
     * Creates a new user instance without password
     *
     * @param title                      the title of the company
     * @param logo                       the logo of the company
     * @param business_name              the business name of the company
     * @param vat_number                 the vat number of the company
     * @param tax_code                   the tax code of the company
     * @param address                    the address of the company
     * @param province                   the province of the company
     * @param city                       the city of the company
     * @param postal_code                the postal code of the company
     * @param unique_code                the unique code of the company
     * @param has_mail_notifications     the mail notifications of the company
     * @param has_telegram_notifications the telegram notifications of the company
     */
    public Company(final int company_id, final String title, final String logo, final String business_name, final String vat_number, final String tax_code, final String address, final String province, final String city, final String postal_code, final String unique_code, final boolean has_mail_notifications, final boolean has_telegram_notifications) {
        this.company_id = company_id;
        this.title = title;
        this.logo = logo;
        this.business_name = business_name;
        this.vat_number = vat_number;
        this.tax_code = tax_code;
        this.address = address;
        this.province = province;
        this.city = city;
        this.postal_code = postal_code;
        this.unique_code = unique_code;
        this.has_mail_notifications = has_mail_notifications;
        this.has_telegram_notifications = has_telegram_notifications;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public String getTitle() {
        return title;
    }

    public String getLogo() {
        return logo;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public String getVat_number() {
        return vat_number;
    }

    public String getTax_code() {
        return tax_code;
    }

    public String getAddress() {
        return address;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getUnique_code() {
        return unique_code;
    }

    public boolean isHas_mail_notifications() {
        return has_mail_notifications;
    }

    public boolean isHas_telegram_notifications() {
        return has_telegram_notifications;
    }


    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeNumberField("company_id", company_id);
        jg.writeStringField("title", title);
        jg.writeStringField("logo", logo);
        jg.writeStringField("business_name", business_name);
        jg.writeStringField("vat_number", vat_number);
        jg.writeStringField("tax_code", tax_code);
        jg.writeStringField("address", address);
        jg.writeStringField("province", province);
        jg.writeStringField("city", city);
        jg.writeStringField("postal_code", postal_code);
        jg.writeStringField("unique_code", unique_code);
        jg.writeBooleanField("has_mail_notifications", has_mail_notifications);
        jg.writeBooleanField("has_telegram_notifications", has_telegram_notifications);

        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Creates a {@code User} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code User} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static Company fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jCompany_id = -1;
        String jTitle = null;
        String jLogo = null;
        String jBusiness_name = null;
        String jVat_number = null;
        String jTax_code = null;
        String jAddress = null;
        String jProvince = null;
        String jCity = null;
        String jPostal_code = null;
        String jUnique_code = null;
        boolean jHas_mail_notifications = false;
        boolean jHas_telegram_notifications = false;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"company".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Company object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Company object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "company_id" -> {
                            jp.nextToken();
                            jCompany_id = jp.getIntValue();
                        }
                        case "title" -> {
                            jp.nextToken();
                            jTitle = jp.getText();
                        }
                        case "logo" -> {
                            jp.nextToken();
                            jLogo = jp.getText();
                        }
                        case "business_name" -> {
                            jp.nextToken();
                            jBusiness_name = jp.getText();
                        }
                        case "vat_number" -> {
                            jp.nextToken();
                            jVat_number = jp.getText();
                        }
                        case "tax_code" -> {
                            jp.nextToken();
                            jTax_code = jp.getText();
                        }
                        case "address" -> {
                            jp.nextToken();
                            jAddress = jp.getText();
                        }
                        case "province" -> {
                            jp.nextToken();
                            jProvince = jp.getText();
                        }
                        case "city" -> {
                            jp.nextToken();
                            jCity = jp.getText();
                        }
                        case "postal_code" -> {
                            jp.nextToken();
                            jPostal_code = jp.getText();
                        }
                        case "unique_code" -> {
                            jp.nextToken();
                            jUnique_code = jp.getText();
                        }
                        case "has_mail_notifications" -> {
                            jp.nextToken();
                            jHas_mail_notifications = jp.getBooleanValue();
                        }
                        case "has_telegram_notifications" -> {
                            jp.nextToken();
                            jHas_telegram_notifications = jp.getBooleanValue();
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new Company(jCompany_id, jTitle, jLogo, jBusiness_name, jVat_number, jTax_code, jAddress, jProvince, jCity, jPostal_code, jUnique_code, jHas_mail_notifications, jHas_telegram_notifications);
    }

}
