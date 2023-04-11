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
package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.Company;

import java.sql.*;

/**
 * Creates a new company in the database.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class CreateCompanyDAO extends AbstractDAO<Company> {

    // number of companies that the owner can create
    private final String CAN_CREATE_COMPANIES = "can_create_more_companies";

    /**
     * The SQL statements to be executed
     * count the number of companies of the owner and check if the owner can create more companies
     * insert the new company in the database
     */
    private static final String CHECK_STATEMENT = "SELECT Count(*) < (SELECT \"Owner\".number_of_companies FROM bitsei_schema.\"Owner\" WHERE \"Owner\".owner_id = ?) As can_create_more_companies FROM bitsei_schema.\"Company\" WHERE \"Company\".owner_id = ?";
    private static final String CREATE_STATEMENT = "INSERT INTO bitsei_schema.\"Company\" (title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_mail_notifications, has_telegram_notifications) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)  RETURNING *";


    // the owner of the company
    private final int owner_id;

    // the title of the company to create
    private final String title;

    // the logo of the company to create
    private final byte[] logo;

    // the business name of the company to create
    private final String business_name;

    // the vat number of the company to create
    private final String vat_number;

    // the tax code of the company to create
    private final String tax_code;

    // the address of the company to create
    private final String address;

    // the province of the company to create
    private final String province;

    // the city of the company to create
    private final String city;

    // the postal code of the company to create
    private final String postal_code;

    // the unique code of the company to create
    private final String unique_code;

    // the mail notifications of the company to create
    private final boolean has_mail_notifications;

    // the telegram notifications of the company to create
    private final boolean has_telegram_notifications;

    /**
     * Creates a new object for getting one user.
     *
     * @param con      the connection to the database.
     * @param owner_id the owner of the company
     * @param company  the company of the user
     */
    public CreateCompanyDAO(final Connection con, final int owner_id, final Company company) {
        super(con);
        this.owner_id = owner_id;
        this.title = company.getTitle();
        this.logo = company.getLogo();
        this.business_name = company.getBusiness_name();
        this.vat_number = company.getVat_number();
        this.tax_code = company.getTax_code();
        this.address = company.getAddress();
        this.province = company.getProvince();
        this.city = company.getCity();
        this.postal_code = company.getPostal_code();
        this.unique_code = company.getUnique_code();
        this.has_mail_notifications = company.isHas_mail_notifications();
        this.has_telegram_notifications = company.isHas_telegram_notifications();

    }


    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs;

        Company company = null;
        try {
            pstmt = con.prepareStatement(CHECK_STATEMENT);
            pstmt.setInt(1, owner_id);
            pstmt.setInt(2, owner_id);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                return;
            }

            if (rs.getBoolean(CAN_CREATE_COMPANIES)) {
                pstmt = con.prepareStatement(CREATE_STATEMENT);
                pstmt.setString(1, title);
                pstmt.setInt(2, owner_id);
                pstmt.setBytes(3, logo);
                pstmt.setString(4, business_name);
                pstmt.setString(5, vat_number);
                pstmt.setString(6, tax_code);
                pstmt.setString(7, address);
                pstmt.setString(8, city);
                pstmt.setString(9, province);
                pstmt.setString(10, postal_code);
                pstmt.setString(11, unique_code);
                pstmt.setBoolean(12, has_mail_notifications);
                pstmt.setBoolean(13, has_telegram_notifications);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    company = new Company(
                            rs.getInt("company_id"),
                            rs.getString("title"),
                            rs.getString("business_name"),
                            rs.getString("vat_number"),
                            rs.getString("tax_code"),
                            rs.getString("address"),
                            rs.getString("province"),
                            rs.getString("city"),
                            rs.getString("postal_code"),
                            rs.getString("unique_code"),
                            rs.getBoolean("has_mail_notifications"),
                            rs.getBoolean("has_telegram_notifications")
                    );

                    LOGGER.info("Company successfully stored in the database.");
                }
            }


        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        outputParam = company;

    }
}
