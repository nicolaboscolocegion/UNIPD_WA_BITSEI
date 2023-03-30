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


--INSERTION OF TOY DATA
-- Insert data into Owner table
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (1, 'Burr', 'Mycah', 'mmcclosh0', 'UoHpf9KZDD', 'mchaudret0@dailymail.co.uk', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (2, 'Amble', 'Antone', 'alabitt1', 'jEzGv6', 'acarah1@google.es', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (3, 'Markus', 'Emmalynn', 'ebickle2', 'm8ICx8aiUw', 'eberrey2@businesswire.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (4, 'Welby', 'Noach', 'nmcdool3', 'Qf2nKFG', 'npurrington3@tmall.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (5, 'Melly', 'Farica', 'fcona4', 'n6ewlSzU', 'fbinley4@princeton.edu', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (6, 'Adorne', 'Robin', 'rgrewcock5', '40NvnrGBw', 'rputt5@wp.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (7, 'Betty', 'Alanah', 'amoles6', 'wpwgKBkdtY8T', 'abale6@reuters.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (8, 'Florinda', 'Dusty', 'dduckerin7', 'LXUL5Ev', 'dwalden7@yahoo.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (9, 'Chrissie', 'Floyd', 'fcoule8', 'u4OVeahj', 'ftwist8@yale.edu', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (10, 'Gris', 'Ferrel', 'fdellcasa9', '3plXczQ8iI', 'fhaskew9@t-online.de', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (11, 'Anatol', 'Waverley', 'wderoosa', 'sriQUU', 'wvossinga@pcworld.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (12, 'Quinta', 'Margaret', 'mcristofolinib', 'ddtvtf', 'mmaberb@shutterfly.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (13, 'Loy', 'Berkly', 'bchirmc', 'zdewu01Zzn', 'bcockrenc@com.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (14, 'Britt', 'Cynthie', 'ctothacotd', 'M6TmXoMh3', 'cdenged@bizjournals.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (15, 'Roman', 'Harp', 'hgouldthorpee', 'qIua8IV', 'hfullagare@quantcast.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (16, 'Patrica', 'Ardyce', 'aliveseyf', 'TErn0v2MA', 'ajanakf@huffingtonpost.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (17, 'John', 'Devina', 'deynaudg', 'reNdGa9', 'dmatyushonokg@huffingtonpost.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (18, 'Sutherland', 'Alic', 'alintillh', '0FrzJ3tmcSD', 'adachh@wikia.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (19, 'Joete', 'Sloan', 'scheinei', '9TBsFDqs5', 'sheijnei@sina.com.cn', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (20, 'Shandra', 'Petey', 'pudellj', 'BlBOKNNk1E2', 'pfadellj@youku.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (21, 'Eddy', 'Lauryn', 'lmawdittk', 'wRH6f4Wr', 'lludyek@wikia.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (22, 'Tandie', 'Anson', 'askillicornl', 'SG9Z7P2XjF', 'acanadal@netlog.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (23, 'Jacynth', 'Cacilie', 'cgreyesm', 'eRtN0XGB', 'codohertym@sphinn.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (24, 'Avigdor', 'Onfroi', 'obissattn', 'mPHKXtcgpd', 'obensenn@wsj.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (25, 'Anatole', 'Vikki', 'vtallowino', '6l9Rpw', 'vwieldo@tinypic.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (26, 'Tyler', 'Britt', 'bmighelep', 'YkhQBfZeHL92', 'bmcveightyp@bloomberg.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (27, 'Annaliese', 'Livia', 'lgetcliffeq', 'OpEezeplx9L', 'ldikq@ted.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (28, 'Emlynne', 'Tandy', 'temminesr', 'fAp2NZ', 'tgibbr@bizjournals.com', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (29, 'Sophia', 'Trace', 'thegleys', 'snNtqyeM', 'tordemanns@dot.gov', null);
insert into bitsei_schema."Owner" (owner_id, firstname, lastname, username, password, email, telegram_chat_id) values (30, 'Daffy', 'Lisette', 'ldinningtont', 'jAVC0XdmaeK', 'lgianettinit@wp.com', null);

-- Insert data into Company table
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (1, 'Jakarta', 1, 'http://dummyimage.com/113x174.png/dddddd/000000', 'Jakarta inc.', '68856-067', '43289-020', 'Via Roma 45', 'Milano', 'MI', null, 1, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (2, 'Wikivu', 2, 'http://dummyimage.com/113x174.png/dddddd/000000', 'Rohan-Dare', '68828-067', '49789-020', '889 Harper Pass', 'Markópoulo Oropoú', 'LI', null, 2, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (3, 'Youfeed', 3, 'http://dummyimage.com/112x177.png/dddddd/000000', 'Leannon and Sons', '43419-009', '59767-008', '88 Mandrake Road', 'Helong', 'VR', null, 3, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (4, 'Yakidoo', 4, 'http://dummyimage.com/109x153.png/dddddd/000000', 'Emmerich-Jerde', '61787-063', '63824-551', '0 Forest Junction', 'Sumberrejo', 'TR', null, 4, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (5, 'Skalith', 5, 'http://dummyimage.com/152x234.png/cc0000/ffffff', 'Gerhold LLC', '36987-2084', '55154-5581', '6 Briar Crest Place', 'Umeå', 'TO', '90181', 5, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (6, 'Realbridge', 6, 'http://dummyimage.com/160x163.png/5fa2dd/ffffff', 'McCullough Inc', '42254-158', '0409-4350', '5 Lindbergh Crossing', 'Mundri', 'MI', null, 6, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (7, 'Dablist', 7, 'http://dummyimage.com/216x139.png/5fa2dd/ffffff', 'Zemlak and Sons', '47335-573', '0338-1135', '6 Bay Court', 'Krajan Jabungcandi', 'NA', null, 7, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (8, 'Thoughtsphere', 8, 'http://dummyimage.com/186x175.png/ff4444/ffffff', 'DuBuque, Cronin and Beier', '63545-639', '35356-169', '16 Cherokee Park', 'Kentville', 'RO', '64649', 8, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (9, 'Dablist', 9, 'http://dummyimage.com/179x160.png/cc0000/ffffff', 'Hand-Nitzsche', '60505-2638', '48951-3032', '7785 Southridge Pass', 'Sîngerei', 'MI', '26227', 9, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (10, 'Voomm', 10, 'http://dummyimage.com/240x210.png/dddddd/000000', 'Leffler-Walter', '11673-390', '10893-656', '543 Summer Ridge Crossing', 'Heshui', 'VE', null, 10, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (11, 'Twimbo', 11, 'http://dummyimage.com/109x236.png/dddddd/000000', 'Lesch-Smith', '36987-3408', '59262-273', '7 Beilfuss Junction', 'Sijihong', 'TN', null, 11, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (12, 'Jetpulse', 12, 'http://dummyimage.com/246x161.png/cc0000/ffffff', 'Gulgowski-Dibbert', '68462-141', '67253-231', '2 Transport Circle', 'Aston', 'PD', '78900', 12, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (13, 'Shuffletag', 13, 'http://dummyimage.com/183x179.png/5fa2dd/ffffff', 'Littel, Olson and Bradtke', '49349-653', '65841-770', '54155 Utah Center', 'Aparecida de Goiânia', 'VI', '74900', 13, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (14, 'Twitterbeat', 14, 'http://dummyimage.com/107x207.png/5fa2dd/ffffff', 'Yundt-Tremblay', '49288-0797', '36987-1838', '33635 Drewry Point', 'Cimalati', 'CA', null, 14, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (15, 'Fliptune', 15, 'http://dummyimage.com/167x147.png/ff4444/ffffff', 'Farrell-Lynch', '11527-051', '65601-755', '708 Westridge Lane', 'Wuxue Shi', 'BO', null, 15, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (16, 'Realmix', 16, 'http://dummyimage.com/115x213.png/dddddd/000000', 'Lemke and Sons', '39822-0409', '45802-896', '9927 East Avenue', 'Los Lotes', 'FI', null, 16, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (17, 'Blogtag', 17, 'http://dummyimage.com/183x118.png/cc0000/ffffff', 'Howe, Rodriguez and Braun', '67015-1100', '16714-561', '933 Corry Junction', 'Svalöv', 'RO', '26890', 17, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (18, 'Avavee', 18, 'http://dummyimage.com/187x132.png/ff4444/ffffff', 'Dicki, Smitham and Cassin', '50975-001', '52125-480', '77444 Raven Point', 'Isoka', 'TO', null, 18, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (19, 'Demivee', 19, 'http://dummyimage.com/207x200.png/cc0000/ffffff', 'Schneider Inc', '66116-388', '59762-2001', '90307 Stoughton Park', 'Songying', 'MI', null, 19, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (20, 'Ooba', 20, 'http://dummyimage.com/152x248.png/dddddd/000000', 'Sipes, Waters and Bahringer', '0573-0213', '37808-417', '65 Cambridge Center', 'Lalupon', 'MI', null, 20, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (21, 'Feednation', 21, 'http://dummyimage.com/199x212.png/5fa2dd/ffffff', 'Schulist-Hand', '11410-301', '50436-7602', '587 Anniversary Road', 'Lembung Tengah', 'PD', null, 21, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (22, 'Kamba', 22, 'http://dummyimage.com/223x214.png/5fa2dd/ffffff', 'Cronin and Sons', '54868-5575', '51191-2046', '12 Mcguire Plaza', 'Duqiong', 'TO', null, 22, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (23, 'Ntag', 23, 'http://dummyimage.com/181x168.png/ff4444/ffffff', 'Renner and Sons', '69154-060', '33992-0268', '82939 Caliangt Way', 'Sabana Grande de Boyá', 'RO', '10405', 23, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (24, 'Yoveo', 24, 'http://dummyimage.com/101x243.png/5fa2dd/ffffff', 'Hoeger LLC', '36987-2557', '64239-101', '0 Bunting Trail', 'Glad', 'FI', '38140', 24, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (25, 'Wikizz', 25, 'http://dummyimage.com/146x226.png/cc0000/ffffff', 'Rau LLC', '0179-0048', '60371-344', '092 8th Hill', 'Kuasha', 'NA', null, 25, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (26, 'Devpoint', 26, 'http://dummyimage.com/250x147.png/dddddd/000000', 'Gleichner Group', '0536-1011', '76282-257', '684 Hoffman Trail', 'Kasangulu', 'VA', null, 26, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (27, 'Meevee', 27, 'http://dummyimage.com/102x225.png/dddddd/000000', 'Bruen-Satterfield', '54868-4531', '63304-346', '462 Eagle Crest Way', 'San Agustín Acasaguastlán', 'VE', '02003', 27, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (28, 'Brightbean', 28, 'http://dummyimage.com/124x183.png/5fa2dd/ffffff', 'Bogisich-Sipes', '65923-002', '60760-292', '62350 Trailsway Court', 'Anzhero-Sudzhensk', 'TO', '65247', 28, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (29, 'Ozu', 29, 'http://dummyimage.com/160x146.png/5fa2dd/ffffff', 'Heidenreich-Koss', '64117-163', '55154-5423', '240 Mifflin Circle', 'Tarsouat', 'RO', null, 29, false, false);
insert into bitsei_schema."Company" (company_id, title, owner_id, logo, business_name, vat_number, tax_code, address, city, province, postal_code, unique_code, has_telegram_notifications, has_mail_notifications) values (30, 'Wordtune', 30, 'http://dummyimage.com/117x190.png/ff4444/ffffff', 'Mohr LLC', '45865-411', '58929-100', '52285 Crest Line Street', 'Sila', 'RO', null, 30, false, false);

-- Insert data into BankAccount table
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (1, 'IT25674893201252722926', 'ISP', 'Intesa San Paolo', 1);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (2, 'RS22589574115252722926', null, 'topiramate', 2);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (3, 'NO7354026602729', 'Cementos Pacasmayo S.A.A.', 'SPEEDGEL RX', 3);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (4, 'LB6194303ZRZFRZCSIUA9VSAA4IL', 'K12 Inc', 'acid reducer', 4);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (5, 'GL1168276555229934', '51job, Inc.', 'Thyroplex', 5);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (6, 'SM50W26657384281RGO7R3KCVRL', 'FuelCell Energy, Inc.', 'Hemorrhoidal', 6);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (7, 'GR988955427V64YLQUNEVQZWVZ8', 'Rice Energy Inc.', 'good sense childrens pain and fever', 7);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (8, 'PK58ELLXSRH7I4Q8ICBUQY3X', 'Blackrock New York Municipal Bond Trust', 'Ben E.Keith', 8);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (9, 'SA0295AQHYWT7UCAK2Y5VCW4', 'Pieris Pharmaceuticals, Inc.', 'Mosquito', 9);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (10, 'CY6310674838NS9SGIRGD4XN5WPL', 'Mannatech, Incorporated', 'Sinusalia', 10);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (11, 'EE829569090940141244', 'Renren Inc.', 'Clopidogrel', 11);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (12, 'IT63M0202447050YGSQ1YNM8W3Z', 'Legg Mason Developed EX-US Diversified Core ETF', 'Maybelline Dream Pure BB Beauty Balm', 12);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (13, 'LV07LVEKJZR9W7TC8RDWN', 'Bank of Commerce Holdings (CA)', 'Shopko Antibacterial Foaming Hand Sanitizer', 13);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (14, 'CZ4926110825948894379977', 'Qualstar Corporation', 'Sudan Grass', 14);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (15, 'SM27X0904118107OXQPGJWYROYF', 'Cogent Communications Holdings, Inc.', 'Propranolol Hydrochloride', 15);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (16, 'BH34WGUGQXLRHZN0ZLJAUG', 'Costco Wholesale Corporation', 'FML FORTE', 16);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (17, 'IT15R4875778062GEWCCUMLTU9R', 'Matlin & Partners Acquisition Corporation', 'Ropinirole', 17);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (18, 'FR824925289037ULORS6N1BCH61', 'Immune Design Corp.', 'Warfarin Sodium', 18);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (19, 'PS79PJNSUOPLQV3ZW7NCWHK10UG9Q', 'Peabody Energy Corporation', 'Tussin CF', 19);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (20, 'FR5308838769397G28UJXS77G59', 'PowerShares LadderRite 0-5 Year Corporate Bond Portfolio', 'Vitiligo', 20);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (21, 'AL7019150716R6NFVYTOZMX8WTHV', 'First Trust NASDAQ Global Auto Index Fund', 'allergy relief-d', 21);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (22, 'BH56VIXXZ0MBE5M/EOXSRO', 'Coty Inc.', 'DIGOX', 22);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (23, 'NO5666557700422', 'Edison International', 'Promethazine Hydrochloride', 23);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (24, 'LI266874242EYOX4KOI97', 'Pacific Continental Corporation (Ore)', 'Sweet Potato', 24);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (25, 'VG67BYAQ7989027205035433', 'Huntington Ingalls Industries, Inc.', 'Non-Drowsy Formula Wal-Tussin', 25);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (26, 'BE29388134888359', 'EastGroup Properties, Inc.', 'Rouge Dior 740 Rosewood Serum', 26);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (27, 'LI0678342YX4WDCML2WSR', 'Nasdaq, Inc.', 'MILK OF MAGNESIA', 27);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (28, 'MC8150993340257TCMG25HGLY46', 'Nuveen California Municipal Value Fund, Inc.', 'Red Snapper', 28);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (29, 'MU94HFEU1991914493526973439SHG', 'Nuveen California Municipal Value Fund, Inc.', 'Venlafaxine', 29);
insert into bitsei_schema."BankAccount" (bankaccount_id, "IBAN", bank_name, bankaccount_friendly_name, company_id) values (30, 'KZ10586SSR5JWKVC2GUT', 'American Realty Investors, Inc.', 'potassium chloride', 30);

-- Insert data into Customer table
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (1, 'AppleLike Inc.', '56479-182', '22342-305', 'Via Venezia 79', 'Padova', 'PD', null, 'applelike@google.com', 'applelike@pec-mac.com', 1, 1);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (2, 'Reinger Group', '49631-182', '14783-305', '4 Mockingbird Junction', 'Hengjie', 'RM', null, 'rfrankum1@google.nl', 'plyster1@pec-mac.com', 2, 2);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (3, 'Harvey-Turner', '49281-650', '16714-357', '9 Arizona Road', 'Thành Phố Lạng Sơn', 'RO', null, 'cmccathie2@imgur.com', 'tbette2@pec-usa.gov', 3, 3);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (4, 'Cronin-Braun', '68428-118', '59779-616', '32 Everett Court', 'Manuel Antonio Mesones Muro', 'MI', null, 'kpringle3@bluehost.com', 'rbetterton3@pec-deliciousdays.com', 4, 4);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (5, 'Schaden LLC', '43492-0001', '15370-032', '779 Judy Parkway', 'Daliuhao', 'TO', null, 'tfitzalan4@aboutads.info', 'rsheekey4@pec-domainmarket.com', 5, 5);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (6, 'Mills-Jerde', '59535-1001', '49230-200', '32650 Del Sol Lane', 'Rennes', 'MI', '35009', 'csowerby5@freewebs.com', 'ffutter5@pec-furl.net', 6, 6);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (7, 'Lowe, Graham and Klocko', '22431-527', '52125-178', '9925 Melrose Parkway', 'Sacramento', 'VE', '95833', 'hmerner6@creativecommons.org', 'elorman6@pec-free.fr', 7, 7);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (8, 'Braun Inc', '43353-798', '54868-6220', '1 Tony Court', 'Adelaide', 'VI', '5839', 'kgallant7@clickbank.net', 'atofano7@pec-infoseek.co.jp', 8, 8);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (9, 'Marks, Green and Oberbrunner', '49643-388', '68387-600', '04 Elgar Road', 'Lampa', 'TO', null, 'lpeyto8@unc.edu', 'cspoure8@pec-addthis.com', 9, 9);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (10, 'Lowe Inc', '50742-101', '76237-138', '0 Loftsgordon Pass', 'Jintian', 'TN', null, 'cspencelayh9@mit.edu', 'lbrownsett9@pec-live.com', 10, 10);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (11, 'Pagac-Dicki', '21695-811', '11523-7334', '919 Hooker Park', 'Shangbahe', 'PD', null, 'ecostara@va.gov', 'ptranfielda@pec-harvard.edu', 11, 11);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (12, 'Schinner, Morar and Morar', '48951-3091', '43857-0100', '0817 Clove Crossing', 'Xinbu', 'PD', null, 'hgeraudyb@thetimes.co.uk', 'tarnob@pec-tmall.com', 12, 12);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (13, 'Wehner-Funk', '63354-010', '55138-006', '2 Little Fleur Park', 'Lianyi', 'MI', null, 'cbouldonc@zdnet.com', 'lphilimorec@pec-furl.net', 13, 13);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (14, 'Bins Inc', null, null, null, null, null, null, 'tvernid@mediafire.com', null, null, 14);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (15, 'Quitzon and Sons', '37205-519', '49288-0167', '92 Northport Hill', 'Borovoy', 'NA', '30913', 'crunhame@ucsd.edu', 'ehullinse@pec-blinklist.com', 15, 15);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (16, 'Champlin Inc', '0904-5792', '10812-511', '6 Cottonwood Center', 'Āshkhāneh', 'NA', null, 'hbrammerf@t.co', 'dlougheidf@pec-cnet.com', 16, 16);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (17, 'Langosh-Spinka', '98132-720', '40104-811', '40 Merrick Circle', 'Wangqingtuo', 'CA', null, 'gdamerellg@usda.gov', 'sclarycottg@pec-sourceforge.net', 17, 17);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (18, 'Torphy-Skiles', '55154-7801', '55504-0230', '3 Bowman Circle', 'Sagua de Tánamo', 'VR', null, 'bbakeyh@newyorker.com', 'mlinzeyh@pec-harvard.edu', 18, 18);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (19, 'Hoppe-Trantow', '49035-150', '50436-3933', '0 Cottonwood Trail', 'Güiria', 'MI', null, 'dflorezi@psu.edu', 'csandomi@pec-ebay.co.uk', 19, 19);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (20, 'Larson-Robel', null, null, null, null, null, null, 'ccrispej@newsvine.com', null, null, 20);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (21, 'Schultz Inc', '60631-425', '49999-631', '1 Larry Trail', 'Hatsukaichi', 'TO', '73013', 'jrowetk@thetimes.co.uk', 'mjaquemek@pec-t.co', 21, 21);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (22, 'Frami-Hane', '36987-2837', '37000-253', '04 Sutteridge Junction', 'Maniwaki', 'TO', '67894', 'crennelsl@google.es', 'cfairburnel@pec-google.es', 22, 22);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (23, 'Johnston-Cummerata', '65517-0009', '55910-756', '9371 Melody Alley', 'Leuweheq', 'PO', null, 'hchastenetm@canalblog.com', 'lluckcuckm@pec-businessinsider.com', 23, 23);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (24, 'Ortiz-Kulas', '23155-194', '0075-2915', '736 Sunnyside Drive', 'Dunleer', 'VI', '34268', 'hkildalen@stumbleupon.com', 'kshilstonen@pec-boston.com', 24, 24);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (25, 'Witting-Koch', null, null, null, null, null, null, 'ibugbyo@virginia.edu', null, null, 25);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (26, 'Stracke-Collins', '55154-1926', '59535-7501', '2429 Comanche Avenue', 'São Mamede de Infesta', 'VR', '44655', 'awaslinp@sourceforge.net', 'nlegonidecp@pec-woothemes.com', 26, 26);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (27, 'Denesik-O''Conner', '0363-1466', '63629-1286', '71943 Sunnyside Drive', 'Donglu', 'TN', null, 'ofrentzq@wikipedia.org', 'khectorq@pec-seesaa.net', 27, 27);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (28, 'Haley, Schaden and Herzog', '0009-3701', '58232-0329', '26 Del Sol Pass', 'Međa', 'NA', null, 'bpittwoodr@topsy.com', 'jduddenr@pec-simplemachines.org', 28, 28);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (29, 'Kutch-Hilll', '0187-1613', '55714-2262', '85 Hanover Lane', 'Gaurnadi', 'MI', '8242', 'psickamores@joomla.org', 'ljakolevitchs@pec-dailymail.co.uk', 29, 29);
insert into bitsei_schema."Customer" (customer_id, business_name, vat_number, tax_code, address, city, province, postal_code, email, pec, unique_code, company_id) values (30, 'Krajcik LLC', '53645-1001', '55714-2258', '8 Gulseth Place', 'Pilawa', 'RO', '08440', 'hgrummittt@bigcartel.com', 'aferriet@pec-odnoklassniki.ru', 30, 30);

-- Insert data into Invoice table
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (1, 1, 0, 1, '5/1/2022', 'DignissimVestibulum.mp3', 1, '1/14/2023', 'Nullam.xls', 'AtLoremInteger.avi', 168.3, 15.0, 4.1, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (2, 2, 0, 2, '8/17/2022', 'NuncNisl.gif', 2, '5/6/2022', 'VestibulumSed.tiff', 'UltricesVelAugue.jpeg', 929.7, 49.8, 3.5, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (3, 3, 0, 3, '9/15/2022', 'Ultrices.xls', 3, '12/3/2022', 'EgetCongueEget.pdf', 'CubiliaCurae.xls', 851.6, 41.5, 6.4, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (4, 4, 0, 4, '1/31/2023', 'SagittisDui.mp3', 4, '6/2/2022', 'IaculisJustoIn.avi', 'InHacHabitasse.tiff', 392.0, 51.1, 4.1, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (5, 5, 0, 5, '8/27/2022', 'NullamSitAmet.mpeg', 5, '9/14/2022', 'FuscePosuereFelis.xls', 'AeneanAuctor.avi', 909.5, 64.9, 6.3, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (6, 6, 0, 6, '6/1/2022', 'LiberoUtMassa.avi', 6, '1/17/2023', 'Primis.gif', 'HabitassePlateaDictumst.pdf', 271.2, 29.4, 1.1, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (7, 7, 0, 7, '6/9/2022', 'InterdumVenenatisTurpis.mp3', 7, '11/8/2022', 'Vel.avi', 'ElementumPellentesque.mp3', 180.7, 7.9, 8.5, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (8, 8, 0, 8, '7/3/2022', 'PedePosuere.png', 8, '5/25/2022', 'SedVel.avi', 'VelNullaEget.tiff', 598.1, 52.6, 4.2, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (9, 9, 0, null, null, null, null, null, null, null, null, null, 5.5, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (10, 10, 0, 10, '8/18/2022', 'MaurisUllamcorperPurus.mov', 10, '4/15/2022', 'CubiliaCurae.mp3', 'Potenti.jpeg', 875.3, 58.1, 4.6, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (11, 11, 0, null, null, null, 11, '6/11/2022', 'QuamPedeLobortis.mov', 'NuncViverraDapibus.xls', 689.2, 23.8, 7.5, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (12, 12, 0, 12, '1/12/2023', 'TurpisNecEuismod.mp3', 12, '5/4/2022', 'Tellus.ppt', 'DictumstMaecenas.jpeg', 78.2, 46.0, 7.8, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (13, 13, 0, 13, '9/4/2022', 'Nam.doc', 13, '2/7/2023', 'Cubilia.mp3', 'DiamVitae.doc', 476.3, 62.8, 1.9, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (14, 14, 0, 14, '4/8/2022', 'Tincidunt.mp3', 14, '2/9/2023', 'NonPretium.ppt', 'NecEuismodScelerisque.tiff', 222.6, 73.3, 9.0, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (15, 15, 0, 15, '10/1/2022', 'Felis.avi', 15, '2/4/2023', 'AeneanFermentum.jpeg', 'Lectus.xls', 592.1, 14.5, 4.9, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (16, 16, 0, null, null, null, 16, '10/6/2022', 'EgetMassa.ppt', 'Leo.avi', 690.3, 35.8, 4.4, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (17, 17, 0, null, null, null, 17, '6/19/2022', 'Mi.mp3', 'MaurisNonLigula.mp3', 565.7, 3.5, 1.3, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (18, 18, 0, 18, '3/11/2023', 'MagnaVestibulumAliquet.ppt', 18, '9/30/2022', 'Eros.avi', 'PellentesqueViverra.gif', 243.5, 57.6, 5.1, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (19, 19, 0, 19, '3/31/2022', 'Fringilla.pdf', null, null, null, null, null, null, 7.8, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (20, 20, 0, 20, '9/17/2022', 'NequeDuis.ppt', 20, '4/20/2022', 'Pede.ppt', 'SollicitudinMiSit.ppt', 71.2, 46.8, 3.2, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (21, 21, 0, 21, '7/8/2022', 'TristiqueEstEt.mp3', 21, '6/9/2022', 'At.tiff', 'EstDonec.gif', 888.6, 71.6, 2.1, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (22, 22, 0, null, null, null, 22, '7/18/2022', 'DuisAliquamConvallis.ppt', 'EgetEros.ppt', 266.4, 20.9, 1.2, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (23, 23, 0, 23, '2/3/2023', 'FaucibusOrciLuctus.mp3', 23, '7/11/2022', 'A.xls', 'NonMauris.mp3', 591.9, 54.7, 7.8, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (24, 24, 0, 24, '11/5/2022', 'CongueEtiam.ppt', 24, '8/31/2022', 'TinciduntNullaMollis.xls', 'Dis.mp3', 847.3, 79.8, 5.9, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (25, 25, 0, 25, '3/13/2023', 'InSagittis.ppt', 25, '12/18/2022', 'OdioOdio.ppt', 'PulvinarNulla.xls', 907.2, 68.8, 6.6, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (26, 26, 0, 26, '2/12/2023', 'OdioElementumEu.tiff', 26, '11/23/2022', 'Pulvinar.mp3', 'DonecOdioJusto.mp3', 296.5, 2.5, 2.8, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (27, 27, 0, null, null, null, 27, '6/13/2022', 'SedVestibulum.mp3', 'AliquamQuis.doc', 643.0, 65.2, 2.8, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (28, 28, 0, 28, '11/30/2022', 'Nunc.xls', 28, '9/30/2022', 'Orci.ppt', 'TellusNulla.avi', 123.5, 98.9, 2.1, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (29, 29, 0, 29, '8/29/2022', 'ArcuLibero.tiff', 29, '1/30/2023', 'CrasPellentesque.avi', 'DuisAcNibh.avi', 846.8, 75.4, 1.2, false);
insert into bitsei_schema."Invoice" (invoice_id, customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) values (30, 30, 0, null, null, null, 30, '6/23/2022', 'LuctusUltriciesEu.png', 'PretiumQuis.avi', 553.3, 11.9, 2.1, false);

-- Insert data into Product table
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (1, 1, 'ETHANOL', 29, 'http://dummyimage.com/222x224.png/5fa2dd/ffffff', 'Kg', 'Pre-emptive upward-trending analyzer');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (2, 2, 'Antimoium crud, Benzoic ac, Ledum, Nux vom, Quercus, Rhododendron, Silicea', 24, 'http://dummyimage.com/225x208.png/5fa2dd/ffffff', 'Kg', 'Ameliorated mission-critical adapter');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (3, 3, 'azathioprine', 96, null, 'Kg', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (4, 4, 'California Valley White Oak', 92, 'http://dummyimage.com/172x173.png/cc0000/ffffff', 'm', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (5, 5, 'GLYCERIN', 7, 'http://dummyimage.com/113x109.png/dddddd/000000', 'Kg', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (6, 6, 'OXYGEN', 78, 'http://dummyimage.com/226x208.png/5fa2dd/ffffff', 'L', 'Cross-group uniform application');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (7, 7, 'formic acid', 34, 'http://dummyimage.com/227x115.png/ff4444/ffffff', 'L', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (8, 8, 'Alumina', 64, 'http://dummyimage.com/204x129.png/5fa2dd/ffffff', 'm', 'Digitized multi-tasking throughput');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (9, 9, 'Hydrocodone Bitartrate And Acetaminophen', 25, null, 'Kg', 'Focused eco-centric benchmark');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (10, 10, 'Hydrocortisone', 31, 'http://dummyimage.com/141x199.png/cc0000/ffffff', 'L', 'Customer-focused cohesive infrastructure');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (11, 11, 'AZITHROMYCIN', 53, 'http://dummyimage.com/124x233.png/dddddd/000000', 'Kg', 'Open-architected zero tolerance architecture');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (12, 12, 'Doxycycline hyclate', 22, 'http://dummyimage.com/164x213.png/dddddd/000000', 'Kg', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (13, 13, 'lansoprazole', 3, null, 'Kg', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (14, 14, 'Oxygen', 37, 'http://dummyimage.com/143x221.png/dddddd/000000', 'L', 'Seamless homogeneous groupware');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (15, 15, 'isopropyl alcohol', 7, null, 'L', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (16, 16, 'Buprenorphine Hydrochloride', 32, 'http://dummyimage.com/140x130.png/5fa2dd/ffffff', 'L', 'Innovative 5th generation internet solution');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (17, 17, 'American Cockroach', 79, 'http://dummyimage.com/178x118.png/cc0000/ffffff', 'm', 'Synergized zero tolerance array');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (18, 18, 'CHLOROXYLENOL', 65, 'http://dummyimage.com/124x239.png/5fa2dd/ffffff', 'L', 'Function-based human-resource project');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (19, 19, 'Avobenzone', 1, null, 'Kg', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (20, 20, 'Diltiazem HCl', 22, 'http://dummyimage.com/219x149.png/5fa2dd/ffffff', 'Kg', 'Mandatory incremental data-warehouse');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (21, 21, 'Escitalopram Oxalate', 87, null, 'Kg', 'Robust composite pricing structure');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (22, 22, 'Miconazole Nitrate', 61, 'http://dummyimage.com/226x240.png/cc0000/ffffff', 'L', 'Centralized even-keeled artificial intelligence');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (23, 23, 'Levocarnitine', 10, 'http://dummyimage.com/158x118.png/5fa2dd/ffffff', 'L', 'Synergistic zero tolerance firmware');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (24, 24, 'lidocaine and prilocaine', 58, 'http://dummyimage.com/230x143.png/dddddd/000000', 'Kg', 'Exclusive contextually-based local area network');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (25, 25, 'ALCOHOL', 16, 'http://dummyimage.com/223x150.png/dddddd/000000', 'L', 'Team-oriented analyzing productivity');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (26, 26, 'olmesartan medoxomil-hydrochlorothiazide', 90, 'http://dummyimage.com/107x196.png/cc0000/ffffff', 'L', 'Object-based secondary contingency');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (27, 27, 'Avobenzone, Octinoxate, Octisalate, Oxybenzone', 61, 'http://dummyimage.com/163x246.png/cc0000/ffffff', 'L', null);
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (28, 28, 'Titanium Dioxide', 44, null, 'L', 'Innovative national structure');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (29, 29, 'zolpidem tartrate', 10, 'http://dummyimage.com/143x138.png/cc0000/ffffff', 'Kg', 'Upgradable demand-driven concept');
insert into bitsei_schema."Product" (product_id, company_id, title, default_price, logo, measurement_unit, description) values (30, 30, 'Furosemide', 53, 'http://dummyimage.com/230x177.png/cc0000/ffffff', 'Kg', 'Proactive dynamic implementation');

-- Insert data into Invoice_Product table
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (1, 1, 50, 26.9, null, null);
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (2, 2, 25, 86.3, null, null);
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (3, 3, 44, 12.1, 20.3, 'Enhanced interactive solution');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (4, 4, 21, 59.8, 89.0, 'Fundamental maximized installation');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (5, 5, 35, 57.3, 75.9, 'User-centric client-driven functionalities');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (6, 6, 41, 2.3, 65.5, 'User-friendly motivating application');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (7, 7, 34, 6.3, 79.8, 'Fundamental bandwidth-monitored architecture');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (8, 8, 22, 77.0, 97.8, 'Total client-server infrastructure');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (9, 9, 21, 73.1, null, null);
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (10, 10, 25, 39.3, 97.3, 'Cross-group maximized conglomeration');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (11, 11, 17, 2.0, 38.6, 'Centralized 6th generation internet solution');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (12, 12, 6, 57.5, 83.8, 'Digitized zero defect hub');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (13, 13, 18, 15.5, 24.3, 'Managed dedicated time-frame');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (14, 14, 35, 95.5, 51.9, 'Reactive system-worthy structure');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (15, 15, 17, 83.6, 11.8, 'Decentralized attitude-oriented instruction set');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (16, 16, 41, 98.7, 45.8, 'Self-enabling neutral hardware');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (17, 17, 11, 85.2, 3.7, 'Horizontal high-level database');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (18, 18, 33, 66.2, 76.6, 'User-friendly next generation ability');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (19, 19, 40, 91.1, 33.0, 'Intuitive client-server support');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (20, 20, 50, 23.6, 7.2, 'Front-line needs-based info-mediaries');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (21, 21, 4, 89.6, 24.9, 'Horizontal secondary algorithm');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (22, 22, 49, 18.7, 65.2, 'Innovative leading edge application');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (23, 23, 17, 77.8, 76.2, 'Switchable homogeneous focus group');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (24, 24, 24, 96.3, null, null);
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (25, 25, 26, 28.0, 1.8, 'Self-enabling bi-directional functionalities');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (26, 26, 36, 32.6, null, null);
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (27, 27, 28, 49.0, 96.5, 'Secured explicit pricing structure');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (28, 28, 12, 8.1, 30.2, 'Monitored solution-oriented moderator');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (29, 29, 48, 16.8, 48.6, 'Polarised maximized protocol');
insert into bitsei_schema."Invoice_Product" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description) values (30, 30, 2, 8.7, 65.6, 'Cross-platform neutral internet solution');

-- Insert data into Log table
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 1, 'Progressive cohesive projection', 'Devolved actuating moratorium', 1);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 2, 'Reverse-engineered holistic attitude', 'Synergized incremental interface', 2);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 3, 'Persistent grid-enabled moderator', 'Robust stable success', 3);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 4, 'Stand-alone foreground time-frame', 'Cloned content-based migration', 4);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 5, 'Persevering 24/7 function', 'Mandatory attitude-oriented help-desk', 5);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 6, 'Profit-focused value-added matrix', 'Optional well-modulated application', 6);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 7, 'Face to face motivating algorithm', 'Public-key motivating methodology', 7);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 8, 'Networked grid-enabled encryption', 'Reverse-engineered tangible solution', 8);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 9, 'Re-contextualized encompassing analyzer', 'Extended zero defect hub', 9);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 10, 'Horizontal fault-tolerant methodology', 'Organic even-keeled open system', 10);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 11, 'Progressive composite installation', 'Robust systematic array', 11);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 12, 'Proactive 6th generation contingency', 'Compatible logistical policy', 12);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 13, null, null, null);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 14, 'Team-oriented content-based instruction set', 'Synchronised maximized service-desk', 14);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 15, 'Monitored non-volatile utilisation', 'User-centric heuristic time-frame', 15);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 16, 'Organic client-driven intranet', 'Public-key dedicated definition', 16);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 17, 'Advanced static flexibility', 'Virtual homogeneous alliance', 17);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 18, 'Business-focused 5th generation policy', 'Profound exuding definition', 18);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 19, 'Configurable directional parallelism', 'Cross-platform intermediate installation', 19);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 20, null, null, null);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 21, 'Open-architected client-server hardware', 'Down-sized demand-driven methodology', 21);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 22, 'Persevering 3rd generation leverage', 'Multi-channelled upward-trending utilisation', 22);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 23, 'Configurable user-facing frame', 'Mandatory cohesive complexity', 23);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 24, 'Universal clear-thinking installation', 'Customer-focused systematic application', 24);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 25, 'Inverse incremental implementation', 'Streamlined clear-thinking secured line', 25);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 26, 'Cross-group fresh-thinking attitude', 'Networked holistic internet solution', 26);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 27, 'Pre-emptive didactic infrastructure', 'Secured maximized projection', 27);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 28, 'Triple-buffered client-driven framework', 'Object-based multimedia knowledge user', 28);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 29, 'Self-enabling intangible encoding', 'Face to face reciprocal standardization', 29);
insert into bitsei_schema."Log" (is_send, log_id, log_state, message, invoice_id) values (false, 30, 'Optional fresh-thinking standardization', 'Diverse bi-directional budgetary management', 30);