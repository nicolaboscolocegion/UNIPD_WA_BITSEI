-- DROP SCHEMA bitsei_schema;

CREATE SCHEMA IF NOT EXISTS bitsei_schema AUTHORIZATION bitsei_user;



-- bitsei_schema."Owner" definition

-- Drop table

-- DROP TABLE bitsei_schema."Owner";

CREATE TABLE bitsei_schema."Owner" (
                                       owner_id serial4 NOT NULL,
                                       firstname bpchar(50) NULL,
                                       lastname bpchar(50) NULL,
                                       username varchar NOT NULL,
                                       "password" varchar NOT NULL,
                                       email bpchar(255) NOT NULL,
                                       telegram_chat_id bpchar(32) NULL,
                                       number_of_companies int2 NOT NULL DEFAULT 3,
                                       CONSTRAINT "Owner_pkey" PRIMARY KEY (owner_id)
);

-- Permissions

ALTER TABLE bitsei_schema."Owner" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Owner" TO bitsei_user;


-- bitsei_schema."Password_Reset_Token" definition

-- Drop table

-- DROP TABLE bitsei_schema."Password_Reset_Token";

CREATE TABLE bitsei_schema."Password_Reset_Token" (
                                                      owner_id int4 NOT NULL,
                                                      "token" varchar(128) NOT NULL,
                                                      token_expiry timestamp NOT NULL,
                                                      CONSTRAINT "Password_Reset_Token_pkey" PRIMARY KEY (owner_id, token),
                                                      CONSTRAINT "Password_Reset_Token_token_key" UNIQUE (token),
                                                      CONSTRAINT "Owner" FOREIGN KEY (owner_id) REFERENCES bitsei_schema."Owner"(owner_id)
);

-- Permissions

ALTER TABLE bitsei_schema."Password_Reset_Token" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Password_Reset_Token" TO bitsei_user;


-- bitsei_schema."Company" definition

-- Drop table

-- DROP TABLE bitsei_schema."Company";

CREATE TABLE bitsei_schema."Company" (
                                         company_id serial4 NOT NULL,
                                         title bpchar(255) NOT NULL,
                                         owner_id int4 NOT NULL,
                                         logo bytea NULL,
                                         business_name bpchar(255) NOT NULL,
                                         vat_number bpchar(11) NULL,
                                         tax_code bpchar(16) NULL,
                                         address bpchar(255) NULL,
                                         city bpchar(255) NULL,
                                         province bpchar(2) NULL,
                                         postal_code bpchar(5) NULL,
                                         unique_code bpchar(6) NULL,
                                         has_telegram_notifications bool NULL DEFAULT false,
                                         has_mail_notifications bool NULL DEFAULT false,
                                         CONSTRAINT "Company_pkey" PRIMARY KEY (company_id),
                                         CONSTRAINT "Owner" FOREIGN KEY (owner_id) REFERENCES bitsei_schema."Owner"(owner_id)
);

-- Permissions

ALTER TABLE bitsei_schema."Company" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Company" TO bitsei_user;


-- bitsei_schema."Customer" definition

-- Drop table

-- DROP TABLE bitsei_schema."Customer";

CREATE TABLE bitsei_schema."Customer" (
                                          customer_id serial4 NOT NULL,
                                          business_name bpchar(255) NOT NULL,
                                          vat_number bpchar(11) NULL,
                                          tax_code bpchar(16) NULL,
                                          address bpchar(255) NULL,
                                          city bpchar(255) NULL,
                                          province bpchar(2) NULL,
                                          postal_code bpchar(5) NULL,
                                          email bpchar(255) NOT NULL,
                                          pec bpchar(255) NULL,
                                          unique_code bpchar(6) NULL,
                                          company_id int4 NOT NULL,
                                          CONSTRAINT "Customer_pkey" PRIMARY KEY (customer_id),
                                          CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id)
);
CREATE INDEX "fki_Company" ON bitsei_schema."Customer" USING btree (company_id);

-- Permissions

ALTER TABLE bitsei_schema."Customer" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Customer" TO bitsei_user;


-- bitsei_schema."Invoice" definition

-- Drop table

-- DROP TABLE bitsei_schema."Invoice";

CREATE TABLE bitsei_schema."Invoice" (
                                         invoice_id serial4 NOT NULL,
                                         customer_id int4 NOT NULL,
                                         status int2 NOT NULL DEFAULT 0,
                                         warning_number bpchar(255) NULL,
                                         warning_date date NULL,
                                         warning_pdf_file bpchar(255) NULL,
                                         invoice_number bpchar(255) NULL,
                                         invoice_date date NULL,
                                         invoice_pdf_file bpchar(255) NULL,
                                         invoice_xml_file bpchar(255) NULL,
                                         total float8 NULL,
                                         discount float8 NULL,
                                         pension_fund_refund float8 NOT NULL,
                                         has_stamp bool NULL DEFAULT false,
                                         CONSTRAINT "Invoice_pkey" PRIMARY KEY (invoice_id),
                                         CONSTRAINT "Customer" FOREIGN KEY (customer_id) REFERENCES bitsei_schema."Customer"(customer_id)
);

-- Permissions

ALTER TABLE bitsei_schema."Invoice" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Invoice" TO bitsei_user;


-- bitsei_schema."Log" definition

-- Drop table

-- DROP TABLE bitsei_schema."Log";

CREATE TABLE bitsei_schema."Log" (
                                     log_id serial4 NOT NULL,
                                     is_send bool NULL DEFAULT false,
                                     log_state bpchar(255) NULL,
                                     message bpchar(255) NULL,
                                     invoice_id int4 NULL,
                                     CONSTRAINT "Log_pkey" PRIMARY KEY (log_id),
                                     CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id)
);

-- Permissions

ALTER TABLE bitsei_schema."Log" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Log" TO bitsei_user;


-- bitsei_schema."Product" definition

-- Drop table

-- DROP TABLE bitsei_schema."Product";

CREATE TABLE bitsei_schema."Product" (
                                         product_id serial4 NOT NULL,
                                         company_id int4 NOT NULL,
                                         title bpchar(255) NOT NULL,
                                         default_price float8 NOT NULL,
                                         logo bpchar(255) NULL,
                                         measurement_unit bpchar(5) NOT NULL,
                                         description bpchar(1024) NULL,
                                         CONSTRAINT "Product_pkey" PRIMARY KEY (product_id),
                                         CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id)
);

-- Permissions

ALTER TABLE bitsei_schema."Product" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Product" TO bitsei_user;


-- bitsei_schema."BankAccount" definition

-- Drop table

-- DROP TABLE bitsei_schema."BankAccount";

CREATE TABLE bitsei_schema."BankAccount" (
                                             bankaccount_id serial4 NOT NULL,
                                             "IBAN" bpchar(34) NOT NULL,
                                             bank_name bpchar(255) NULL,
                                             bankaccount_friendly_name bpchar(255) NOT NULL,
                                             company_id int4 NOT NULL,
                                             CONSTRAINT "Account_pkey" PRIMARY KEY (bankaccount_id),
                                             CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id)
);
CREATE INDEX fki_c ON bitsei_schema."BankAccount" USING btree (company_id);

-- Permissions

ALTER TABLE bitsei_schema."BankAccount" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."BankAccount" TO bitsei_user;


-- bitsei_schema."Invoice_Product" definition

-- Drop table

-- DROP TABLE bitsei_schema."Invoice_Product";

CREATE TABLE bitsei_schema."Invoice_Product" (
                                                 invoice_id int4 NOT NULL,
                                                 product_id int4 NOT NULL,
                                                 quantity int4 NOT NULL,
                                                 unit_price float8 NOT NULL,
                                                 related_price float8 NULL,
                                                 related_price_description bpchar(1024) NULL,
                                                 purchase_date date NULL,
                                                 CONSTRAINT "Invoice_Product_pkey" PRIMARY KEY (invoice_id, product_id),
                                                 CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id) ON UPDATE CASCADE,
                                                 CONSTRAINT "Product" FOREIGN KEY (product_id) REFERENCES bitsei_schema."Product"(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Permissions

ALTER TABLE bitsei_schema."Invoice_Product" OWNER TO bitsei_user;
GRANT ALL ON TABLE bitsei_schema."Invoice_Product" TO bitsei_user;




-- Permissions

GRANT ALL ON SCHEMA bitsei_schema TO bitsei_user;