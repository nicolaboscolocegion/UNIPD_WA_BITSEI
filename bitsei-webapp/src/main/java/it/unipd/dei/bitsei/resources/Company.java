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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

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
    private final byte[] logo;

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
    public Company(final String title, final byte[] logo, final String business_name, final String vat_number, final String tax_code, final String address, final String province, final String city, final String postal_code, final String unique_code, final boolean has_mail_notifications, final boolean has_telegram_notifications) {
        this.company_id = -1;
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

    public Company(final int company_id, final String title, final String business_name, final String vat_number, final String tax_code, final String address, final String province, final String city, final String postal_code, final String unique_code, final boolean has_mail_notifications, final boolean has_telegram_notifications) {
        this.logo = null;
        this.company_id = company_id;
        this.title = title;
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

    public byte[] getLogo() {
        return logo;
    }

    public String getLogo_file_name() {
        return "http://localhost:8080/bitsei-1.0/rest/company/image/" + company_id;
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
        jg.writeStringField("logo", getLogo_file_name());
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


    public static Company fromMultiPart(final HttpServletRequest req) throws IOException, ServletException {
        String jTitle = null;
        byte[] jLogo = null;
        String photoMediaType = null;
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
            Collection<Part> parts = req.getParts();

            for (Part part : parts) {
                switch (part.getName()) {
                    case "title" -> {
                        jTitle = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "logo" -> {
                        photoMediaType = part.getContentType();
                        switch (photoMediaType.toLowerCase().trim()) {
                            case "image/png":
                            case "image/jpeg":
                            case "image/jpg":
                                break;
                            default:
                                throw new ServletException("Invalid image type");
                        }
                        jLogo = part.getInputStream().readAllBytes();
                    }
                    case "business_name" -> {
                        jBusiness_name = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "vat_number" -> {
                        jVat_number = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "tax_code" -> {
                        jTax_code = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "address" -> {
                        jAddress = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "province" -> {
                        jProvince = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "city" -> {
                        jCity = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "postal_code" -> {
                        jPostal_code = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "unique_code" -> {
                        jUnique_code = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    }
                    case "has_mail_notifications" -> {
                        jHas_mail_notifications = Boolean.parseBoolean(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim());
                    }
                    case "has_telegram_notifications" -> {
                        jHas_telegram_notifications = Boolean.parseBoolean(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim());
                    }

                }
            }
        } catch (ServletException e) {
            LOGGER.error("Unable to parse an User object from Multipart request.", e);
            throw e;
        }

        return new Company(jTitle, jLogo, jBusiness_name, jVat_number, jTax_code, jAddress, jProvince, jCity, jPostal_code, jUnique_code, jHas_mail_notifications, jHas_telegram_notifications);
    }
}
