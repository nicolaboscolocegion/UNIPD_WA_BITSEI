--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2023-03-27 01:02:43

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 16718)
-- Name: bitsei_schema; Type: SCHEMA; Schema: -; Owner: bitsei_user
--

CREATE SCHEMA IF NOT EXISTS bitsei_schema;


ALTER SCHEMA bitsei_schema OWNER TO bitsei_user;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16757)
-- Name: BankAccount; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."BankAccount" (
                                             bankaccount_id integer NOT NULL,
                                             "IBAN" character(34) NOT NULL,
                                             bank_name character(255),
                                             bankaccount_friendly_name character(255) NOT NULL,
                                             company_id integer NOT NULL
);


ALTER TABLE bitsei_schema."BankAccount" OWNER TO bitsei_user;

--
-- TOC entry 216 (class 1259 OID 16760)
-- Name: Company; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Company" (
                                         company_id integer NOT NULL,
                                         title character(255) NOT NULL,
                                         owner_id integer NOT NULL,
                                         logo character(255),
                                         business_name character(255) NOT NULL,
                                         vat_number character(11),
                                         tax_code character(16),
                                         address character(255),
                                         city character(255),
                                         province character(2),
                                         postal_code character(5),
                                         unique_code character(6),
                                         has_telegram_notifications boolean DEFAULT false,
                                         has_mail_notifications boolean DEFAULT false
);


ALTER TABLE bitsei_schema."Company" OWNER TO bitsei_user;

--
-- TOC entry 217 (class 1259 OID 16765)
-- Name: Customer; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Customer" (
                                          customer_id integer NOT NULL,
                                          business_name character(255) NOT NULL,
                                          vat_number character(11),
                                          tax_code character(16),
                                          address character(255),
                                          city character(255),
                                          province character(2),
                                          postal_code character(5),
                                          email character(255) NOT NULL,
                                          pec character(255),
                                          unique_code character(6),
                                          company_id integer NOT NULL
);


ALTER TABLE bitsei_schema."Customer" OWNER TO bitsei_user;

--
-- TOC entry 218 (class 1259 OID 16770)
-- Name: Invoice; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Invoice" (
                                         invoice_id integer NOT NULL,
                                         customer_id integer NOT NULL,
                                         status smallint DEFAULT 0 NOT NULL,
                                         warning_number character(255),
                                         warning_date date,
                                         warning_pdf_file character(255),
                                         invoice_number character(255),
                                         invoice_date date,
                                         invoice_pdf_file character(255),
                                         invoice_xml_file character(255),  -- relative file path, extension must be xml
                                         total double precision,   -- must be positive
                                         discount double precision,  -- must be positive
                                         pension_fund_refund double precision NOT NULL, -- must be within 0 and 4 (%)
                                         has_stamp boolean DEFAULT false
);


ALTER TABLE bitsei_schema."Invoice" OWNER TO bitsei_user;

--
-- TOC entry 219 (class 1259 OID 16774)
-- Name: Invoice_Product; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Invoice_Product" (
                                                 invoice_id integer NOT NULL,
                                                 product_id integer NOT NULL,
                                                 quantity integer NOT NULL,
                                                 unit_price double precision NOT NULL,
                                                 related_price double precision,
                                                 related_price_description character(1024)
);


ALTER TABLE bitsei_schema."Invoice_Product" OWNER TO bitsei_user;

--
-- TOC entry 220 (class 1259 OID 16779)
-- Name: Log; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Log" (
                                     is_send boolean DEFAULT false,
                                     log_id integer NOT NULL,
                                     log_state character(255),
                                     message character(255),
                                     invoice_id integer
);


ALTER TABLE bitsei_schema."Log" OWNER TO bitsei_user;

--
-- TOC entry 221 (class 1259 OID 16785)
-- Name: Owner; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Owner" (
                                       owner_id integer NOT NULL,
                                       firstname character(50),
                                       lastname character(50),
                                       username character varying NOT NULL,
                                       password character varying NOT NULL,
                                       email character(255) NOT NULL,
                                       telegram_chat_id character(32)
);


ALTER TABLE bitsei_schema."Owner" OWNER TO bitsei_user;

--
-- TOC entry 222 (class 1259 OID 16790)
-- Name: Product; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Product" (
                                         product_id integer NOT NULL,
                                         company_id integer NOT NULL,
                                         title character(255) NOT NULL,
                                         default_price integer NOT NULL,
                                         logo character(255),
                                         measurement_unit character(5) NOT NULL,
                                         description character(1024)
);


ALTER TABLE bitsei_schema."Product" OWNER TO bitsei_user;

--
-- TOC entry 223 (class 1259 OID 16785)
-- Name: Password_Reset_Token; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Password_Reset_Token" (
                                       owner_id integer NOT NULL,
                                       token VARCHAR(128) NOT NULL UNIQUE,
                                       token_expiry TIMESTAMP NOT NULL,
                                       PRIMARY KEY (owner_id, token)
);

ALTER TABLE bitsei_schema."Password_Reset_Token" OWNER TO bitsei_user;


--
-- TOC entry 3936 (class 2606 OID 16796)
-- Name: BankAccount Account_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount"
    ADD CONSTRAINT "Account_pkey" PRIMARY KEY (bankaccount_id);


--
-- TOC entry 3939 (class 2606 OID 16798)
-- Name: Company Company_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company"
    ADD CONSTRAINT "Company_pkey" PRIMARY KEY (company_id);


--
-- TOC entry 3941 (class 2606 OID 16800)
-- Name: Customer Customer_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer"
    ADD CONSTRAINT "Customer_pkey" PRIMARY KEY (customer_id);


--
-- TOC entry 3946 (class 2606 OID 16802)
-- Name: Invoice_Product Invoice_Product_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Invoice_Product_pkey" PRIMARY KEY (invoice_id, product_id);


--
-- TOC entry 3944 (class 2606 OID 16804)
-- Name: Invoice Invoice_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice"
    ADD CONSTRAINT "Invoice_pkey" PRIMARY KEY (invoice_id);


--
-- TOC entry 3948 (class 2606 OID 16806)
-- Name: Log Log_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log"
    ADD CONSTRAINT "Log_pkey" PRIMARY KEY (log_id);


--
-- TOC entry 3950 (class 2606 OID 16808)
-- Name: Owner Owner_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Owner"
    ADD CONSTRAINT "Owner_pkey" PRIMARY KEY (owner_id);


--
-- TOC entry 3952 (class 2606 OID 16810)
-- Name: Product Product_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product"
    ADD CONSTRAINT "Product_pkey" PRIMARY KEY (product_id);


--
-- TOC entry 3942 (class 1259 OID 16909)
-- Name: fki_Company; Type: INDEX; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE INDEX "fki_Company" ON bitsei_schema."Customer" USING btree (company_id);


--
-- TOC entry 3937 (class 1259 OID 16903)
-- Name: fki_c; Type: INDEX; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE INDEX fki_c ON bitsei_schema."BankAccount" USING btree (company_id);


--
-- TOC entry 3960 (class 2606 OID 16816)
-- Name: Product Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3953 (class 2606 OID 16898)
-- Name: BankAccount Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3955 (class 2606 OID 16904)
-- Name: Customer Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3956 (class 2606 OID 16821)
-- Name: Invoice Customer; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice"
    ADD CONSTRAINT "Customer" FOREIGN KEY (customer_id) REFERENCES bitsei_schema."Customer"(customer_id);


--
-- TOC entry 3957 (class 2606 OID 16826)
-- Name: Invoice_Product Invoice; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id) ON UPDATE CASCADE;


--
-- TOC entry 3959 (class 2606 OID 16831)
-- Name: Log Invoice; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log"
    ADD CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id);


--
-- TOC entry 3954 (class 2606 OID 16836)
-- Name: Company Owner; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company"
    ADD CONSTRAINT "Owner" FOREIGN KEY (owner_id) REFERENCES bitsei_schema."Owner"(owner_id);


--
-- TOC entry 3958 (class 2606 OID 16841)
-- Name: Invoice_Product Product; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Product" FOREIGN KEY (product_id) REFERENCES bitsei_schema."Product"(product_id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2023-03-27 01:02:45

--
-- PostgreSQL database dump complete
--