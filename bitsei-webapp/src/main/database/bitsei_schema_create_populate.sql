--
-- PostgreSQL database dump
--

-- Dumped from database version 13.11
-- Dumped by pg_dump version 15.2

-- Started on 2023-05-31 15:22:56

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
-- TOC entry 6 (class 2615 OID 17601)
-- Name: bitsei_schema; Type: SCHEMA; Schema: -; Owner: bitsei_user
--

CREATE SCHEMA bitsei_schema;


ALTER SCHEMA bitsei_schema OWNER TO bitsei_user;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 17714)
-- Name: BankAccount; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."BankAccount" (
                                             bankaccount_id integer NOT NULL,
                                             "IBAN" character(34) NOT NULL,
                                             bank_name character varying,
                                             bankaccount_friendly_name character varying NOT NULL,
                                             company_id integer NOT NULL
);


ALTER TABLE bitsei_schema."BankAccount" OWNER TO bitsei_user;

--
-- TOC entry 214 (class 1259 OID 17712)
-- Name: BankAccount_bankaccount_id_seq; Type: SEQUENCE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE SEQUENCE bitsei_schema."BankAccount_bankaccount_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bitsei_schema."BankAccount_bankaccount_id_seq" OWNER TO bitsei_user;

--
-- TOC entry 3831 (class 0 OID 0)
-- Dependencies: 214
-- Name: BankAccount_bankaccount_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."BankAccount_bankaccount_id_seq" OWNED BY bitsei_schema."BankAccount".bankaccount_id;


--
-- TOC entry 205 (class 1259 OID 17628)
-- Name: Company; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Company" (
                                         company_id integer NOT NULL,
                                         title character varying NOT NULL,
                                         owner_id integer NOT NULL,
                                         logo bytea,
                                         business_name character varying NOT NULL,
                                         vat_number character(11),
                                         tax_code character(16),
                                         address character varying,
                                         city character varying,
                                         province character(2),
                                         postal_code character(5),
                                         unique_code character(6),
                                         has_telegram_notifications boolean DEFAULT false,
                                         has_mail_notifications boolean DEFAULT false,
                                         fiscal_company_type integer DEFAULT 0,
                                         pec character varying
);


ALTER TABLE bitsei_schema."Company" OWNER TO bitsei_user;

--
-- TOC entry 3832 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN "Company".fiscal_company_type; Type: COMMENT; Schema: bitsei_schema; Owner: bitsei_user
--

COMMENT ON COLUMN bitsei_schema."Company".fiscal_company_type IS '0 - REGIME FORFETTARIO DITTA INDIVIDUALE
1 - REGIME FORFETTARIO DITTA INDIVIDUALE CON RITENUTA
2 - REGIME ORDINARIO DITTA INDIVIDUALE
3 - PARTITA IVA GENERICA';


--
-- TOC entry 204 (class 1259 OID 17626)
-- Name: Company_company_id_seq; Type: SEQUENCE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE SEQUENCE bitsei_schema."Company_company_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bitsei_schema."Company_company_id_seq" OWNER TO bitsei_user;

--
-- TOC entry 3833 (class 0 OID 0)
-- Dependencies: 204
-- Name: Company_company_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Company_company_id_seq" OWNED BY bitsei_schema."Company".company_id;


--
-- TOC entry 207 (class 1259 OID 17646)
-- Name: Customer; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Customer" (
                                          customer_id integer NOT NULL,
                                          business_name character varying NOT NULL,
                                          vat_number character(11),
                                          tax_code character(16),
                                          address character varying,
                                          city character varying,
                                          province character(2),
                                          postal_code character(5),
                                          email character varying NOT NULL,
                                          pec character varying,
                                          unique_code character(6),
                                          company_id integer NOT NULL
);


ALTER TABLE bitsei_schema."Customer" OWNER TO bitsei_user;

--
-- TOC entry 206 (class 1259 OID 17644)
-- Name: Customer_customer_id_seq; Type: SEQUENCE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE SEQUENCE bitsei_schema."Customer_customer_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bitsei_schema."Customer_customer_id_seq" OWNER TO bitsei_user;

--
-- TOC entry 3834 (class 0 OID 0)
-- Dependencies: 206
-- Name: Customer_customer_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Customer_customer_id_seq" OWNED BY bitsei_schema."Customer".customer_id;


--
-- TOC entry 209 (class 1259 OID 17663)
-- Name: Invoice; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Invoice" (
                                         invoice_id integer NOT NULL,
                                         customer_id integer NOT NULL,
                                         status smallint DEFAULT 0 NOT NULL,
                                         warning_number character varying,
                                         warning_date date,
                                         warning_pdf_file character varying,
                                         invoice_number character varying,
                                         invoice_date date,
                                         invoice_pdf_file character varying,
                                         invoice_xml_file character varying,
                                         total double precision,
                                         discount double precision,
                                         pension_fund_refund double precision NOT NULL,
                                         has_stamp boolean DEFAULT false
);


ALTER TABLE bitsei_schema."Invoice" OWNER TO bitsei_user;

--
-- TOC entry 216 (class 1259 OID 17729)
-- Name: Invoice_Product; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Invoice_Product" (
                                                 invoice_id integer NOT NULL,
                                                 product_id integer NOT NULL,
                                                 quantity integer NOT NULL,
                                                 unit_price double precision NOT NULL,
                                                 related_price double precision,
                                                 related_price_description character varying,
                                                 purchase_date date
);


ALTER TABLE bitsei_schema."Invoice_Product" OWNER TO bitsei_user;

--
-- TOC entry 208 (class 1259 OID 17661)
-- Name: Invoice_invoice_id_seq; Type: SEQUENCE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE SEQUENCE bitsei_schema."Invoice_invoice_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bitsei_schema."Invoice_invoice_id_seq" OWNER TO bitsei_user;

--
-- TOC entry 3835 (class 0 OID 0)
-- Dependencies: 208
-- Name: Invoice_invoice_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Invoice_invoice_id_seq" OWNED BY bitsei_schema."Invoice".invoice_id;


--
-- TOC entry 211 (class 1259 OID 17681)
-- Name: Log; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Log" (
                                     log_id integer NOT NULL,
                                     is_send boolean DEFAULT false,
                                     log_state character(255),
                                     message character(255),
                                     invoice_id integer
);


ALTER TABLE bitsei_schema."Log" OWNER TO bitsei_user;

--
-- TOC entry 210 (class 1259 OID 17679)
-- Name: Log_log_id_seq; Type: SEQUENCE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE SEQUENCE bitsei_schema."Log_log_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bitsei_schema."Log_log_id_seq" OWNER TO bitsei_user;

--
-- TOC entry 3836 (class 0 OID 0)
-- Dependencies: 210
-- Name: Log_log_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Log_log_id_seq" OWNED BY bitsei_schema."Log".log_id;


--
-- TOC entry 202 (class 1259 OID 17604)
-- Name: Owner; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Owner" (
                                       owner_id integer NOT NULL,
                                       firstname character varying,
                                       lastname character varying,
                                       username character varying NOT NULL,
                                       password character varying NOT NULL,
                                       email character varying NOT NULL,
                                       telegram_chat_id character(32),
                                       number_of_companies smallint DEFAULT 3 NOT NULL
);


ALTER TABLE bitsei_schema."Owner" OWNER TO bitsei_user;

--
-- TOC entry 201 (class 1259 OID 17602)
-- Name: Owner_owner_id_seq; Type: SEQUENCE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE SEQUENCE bitsei_schema."Owner_owner_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bitsei_schema."Owner_owner_id_seq" OWNER TO bitsei_user;

--
-- TOC entry 3837 (class 0 OID 0)
-- Dependencies: 201
-- Name: Owner_owner_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Owner_owner_id_seq" OWNED BY bitsei_schema."Owner".owner_id;


--
-- TOC entry 203 (class 1259 OID 17614)
-- Name: Password_Reset_Token; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Password_Reset_Token" (
                                                      owner_id integer NOT NULL,
                                                      token character varying(128) NOT NULL,
                                                      token_expiry timestamp without time zone NOT NULL
);


ALTER TABLE bitsei_schema."Password_Reset_Token" OWNER TO bitsei_user;

--
-- TOC entry 213 (class 1259 OID 17698)
-- Name: Product; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Product" (
                                         product_id integer NOT NULL,
                                         company_id integer NOT NULL,
                                         title character varying NOT NULL,
                                         default_price double precision NOT NULL,
                                         logo character varying,
                                         measurement_unit character(5) NOT NULL,
                                         description character varying
);


ALTER TABLE bitsei_schema."Product" OWNER TO bitsei_user;

--
-- TOC entry 212 (class 1259 OID 17696)
-- Name: Product_product_id_seq; Type: SEQUENCE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE SEQUENCE bitsei_schema."Product_product_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bitsei_schema."Product_product_id_seq" OWNER TO bitsei_user;

--
-- TOC entry 3838 (class 0 OID 0)
-- Dependencies: 212
-- Name: Product_product_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Product_product_id_seq" OWNED BY bitsei_schema."Product".product_id;


--
-- TOC entry 3648 (class 2604 OID 17717)
-- Name: BankAccount bankaccount_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount" ALTER COLUMN bankaccount_id SET DEFAULT nextval('bitsei_schema."BankAccount_bankaccount_id_seq"'::regclass);


--
-- TOC entry 3637 (class 2604 OID 17631)
-- Name: Company company_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company" ALTER COLUMN company_id SET DEFAULT nextval('bitsei_schema."Company_company_id_seq"'::regclass);


--
-- TOC entry 3641 (class 2604 OID 17649)
-- Name: Customer customer_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer" ALTER COLUMN customer_id SET DEFAULT nextval('bitsei_schema."Customer_customer_id_seq"'::regclass);


--
-- TOC entry 3642 (class 2604 OID 17666)
-- Name: Invoice invoice_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice" ALTER COLUMN invoice_id SET DEFAULT nextval('bitsei_schema."Invoice_invoice_id_seq"'::regclass);


--
-- TOC entry 3645 (class 2604 OID 17684)
-- Name: Log log_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log" ALTER COLUMN log_id SET DEFAULT nextval('bitsei_schema."Log_log_id_seq"'::regclass);


--
-- TOC entry 3635 (class 2604 OID 17607)
-- Name: Owner owner_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Owner" ALTER COLUMN owner_id SET DEFAULT nextval('bitsei_schema."Owner_owner_id_seq"'::regclass);


--
-- TOC entry 3647 (class 2604 OID 17701)
-- Name: Product product_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product" ALTER COLUMN product_id SET DEFAULT nextval('bitsei_schema."Product_product_id_seq"'::regclass);


--
-- TOC entry 3824 (class 0 OID 17714)
-- Dependencies: 215
-- Data for Name: BankAccount; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."BankAccount" VALUES (1, 'IT25674893201252722926            ', 'ISP', 'Intesa San Paolo', 1);
INSERT INTO bitsei_schema."BankAccount" VALUES (2, 'RS22589574115252722926            ', NULL, 'topiramate', 2);
INSERT INTO bitsei_schema."BankAccount" VALUES (3, 'NO7354026602729                   ', 'Cementos Pacasmayo S.A.A.', 'SPEEDGEL RX', 3);
INSERT INTO bitsei_schema."BankAccount" VALUES (4, 'LB6194303ZRZFRZCSIUA9VSAA4IL      ', 'K12 Inc', 'acid reducer', 4);
INSERT INTO bitsei_schema."BankAccount" VALUES (5, 'GL1168276555229934                ', '51job, Inc.', 'Thyroplex', 5);
INSERT INTO bitsei_schema."BankAccount" VALUES (6, 'SM50W26657384281RGO7R3KCVRL       ', 'FuelCell Energy, Inc.', 'Hemorrhoidal', 6);
INSERT INTO bitsei_schema."BankAccount" VALUES (7, 'GR988955427V64YLQUNEVQZWVZ8       ', 'Rice Energy Inc.', 'good sense childrens pain and fever', 7);
INSERT INTO bitsei_schema."BankAccount" VALUES (8, 'PK58ELLXSRH7I4Q8ICBUQY3X          ', 'Blackrock New York Municipal Bond Trust', 'Ben E.Keith', 8);
INSERT INTO bitsei_schema."BankAccount" VALUES (9, 'SA0295AQHYWT7UCAK2Y5VCW4          ', 'Pieris Pharmaceuticals, Inc.', 'Mosquito', 9);
INSERT INTO bitsei_schema."BankAccount" VALUES (10, 'CY6310674838NS9SGIRGD4XN5WPL      ', 'Mannatech, Incorporated', 'Sinusalia', 10);
INSERT INTO bitsei_schema."BankAccount" VALUES (11, 'EE829569090940141244              ', 'Renren Inc.', 'Clopidogrel', 11);
INSERT INTO bitsei_schema."BankAccount" VALUES (12, 'IT63M0202447050YGSQ1YNM8W3Z       ', 'Legg Mason Developed EX-US Diversified Core ETF', 'Maybelline Dream Pure BB Beauty Balm', 12);
INSERT INTO bitsei_schema."BankAccount" VALUES (13, 'LV07LVEKJZR9W7TC8RDWN             ', 'Bank of Commerce Holdings (CA)', 'Shopko Antibacterial Foaming Hand Sanitizer', 13);
INSERT INTO bitsei_schema."BankAccount" VALUES (14, 'CZ4926110825948894379977          ', 'Qualstar Corporation', 'Sudan Grass', 14);
INSERT INTO bitsei_schema."BankAccount" VALUES (15, 'SM27X0904118107OXQPGJWYROYF       ', 'Cogent Communications Holdings, Inc.', 'Propranolol Hydrochloride', 15);
INSERT INTO bitsei_schema."BankAccount" VALUES (16, 'BH34WGUGQXLRHZN0ZLJAUG            ', 'Costco Wholesale Corporation', 'FML FORTE', 16);
INSERT INTO bitsei_schema."BankAccount" VALUES (17, 'IT15R4875778062GEWCCUMLTU9R       ', 'Matlin & Partners Acquisition Corporation', 'Ropinirole', 17);
INSERT INTO bitsei_schema."BankAccount" VALUES (18, 'FR824925289037ULORS6N1BCH61       ', 'Immune Design Corp.', 'Warfarin Sodium', 18);
INSERT INTO bitsei_schema."BankAccount" VALUES (19, 'PS79PJNSUOPLQV3ZW7NCWHK10UG9Q     ', 'Peabody Energy Corporation', 'Tussin CF', 19);
INSERT INTO bitsei_schema."BankAccount" VALUES (20, 'FR5308838769397G28UJXS77G59       ', 'PowerShares LadderRite 0-5 Year Corporate Bond Portfolio', 'Vitiligo', 20);
INSERT INTO bitsei_schema."BankAccount" VALUES (21, 'AL7019150716R6NFVYTOZMX8WTHV      ', 'First Trust NASDAQ Global Auto Index Fund', 'allergy relief-d', 21);
INSERT INTO bitsei_schema."BankAccount" VALUES (22, 'BH56VIXXZ0MBE5M/EOXSRO            ', 'Coty Inc.', 'DIGOX', 22);
INSERT INTO bitsei_schema."BankAccount" VALUES (23, 'NO5666557700422                   ', 'Edison International', 'Promethazine Hydrochloride', 23);
INSERT INTO bitsei_schema."BankAccount" VALUES (24, 'LI266874242EYOX4KOI97             ', 'Pacific Continental Corporation (Ore)', 'Sweet Potato', 24);
INSERT INTO bitsei_schema."BankAccount" VALUES (25, 'VG67BYAQ7989027205035433          ', 'Huntington Ingalls Industries, Inc.', 'Non-Drowsy Formula Wal-Tussin', 25);
INSERT INTO bitsei_schema."BankAccount" VALUES (26, 'BE29388134888359                  ', 'EastGroup Properties, Inc.', 'Rouge Dior 740 Rosewood Serum', 26);
INSERT INTO bitsei_schema."BankAccount" VALUES (27, 'LI0678342YX4WDCML2WSR             ', 'Nasdaq, Inc.', 'MILK OF MAGNESIA', 27);
INSERT INTO bitsei_schema."BankAccount" VALUES (28, 'MC8150993340257TCMG25HGLY46       ', 'Nuveen California Municipal Value Fund, Inc.', 'Red Snapper', 28);
INSERT INTO bitsei_schema."BankAccount" VALUES (29, 'MU94HFEU1991914493526973439SHG    ', 'Nuveen California Municipal Value Fund, Inc.', 'Venlafaxine', 29);
INSERT INTO bitsei_schema."BankAccount" VALUES (30, 'KZ10586SSR5JWKVC2GUT              ', 'American Realty Investors, Inc.', 'potassium chloride', 30);


--
-- TOC entry 3814 (class 0 OID 17628)
-- Dependencies: 205
-- Data for Name: Company; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Company" VALUES (2, 'Wikivu', 2, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313133783137342e706e672f6464646464642f303030303030', 'Rohan-Dare', '68828-067  ', '49789-020       ', '889 Harper Pass', 'Markópoulo Oropoú', 'LI', NULL, '2     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (4, 'Yakidoo', 4, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313039783135332e706e672f6464646464642f303030303030', 'Emmerich-Jerde', '61787-063  ', '63824-551       ', '0 Forest Junction', 'Sumberrejo', 'TR', NULL, '4     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (5, 'Skalith', 5, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313532783233342e706e672f6363303030302f666666666666', 'Gerhold LLC', '36987-2084 ', '55154-5581      ', '6 Briar Crest Place', 'Umeå', 'TO', '90181', '5     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (8, 'Thoughtsphere', 8, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313836783137352e706e672f6666343434342f666666666666', 'DuBuque, Cronin and Beier', '63545-639  ', '35356-169       ', '16 Cherokee Park', 'Kentville', 'RO', '64649', '8     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (10, 'Voomm', 10, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f323430783231302e706e672f6464646464642f303030303030', 'Leffler-Walter', '11673-390  ', '10893-656       ', '543 Summer Ridge Crossing', 'Heshui', 'VE', NULL, '10    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (11, 'Twimbo', 11, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313039783233362e706e672f6464646464642f303030303030', 'Lesch-Smith', '36987-3408 ', '59262-273       ', '7 Beilfuss Junction', 'Sijihong', 'TN', NULL, '11    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (14, 'Twitterbeat', 14, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313037783230372e706e672f3566613264642f666666666666', 'Yundt-Tremblay', '49288-0797 ', '36987-1838      ', '33635 Drewry Point', 'Cimalati', 'CA', NULL, '14    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (16, 'Realmix', 16, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313135783231332e706e672f6464646464642f303030303030', 'Lemke and Sons', '39822-0409 ', '45802-896       ', '9927 East Avenue', 'Los Lotes', 'FI', NULL, '16    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (17, 'Blogtag', 17, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313833783131382e706e672f6363303030302f666666666666', 'Howe, Rodriguez and Braun', '67015-1100 ', '16714-561       ', '933 Corry Junction', 'Svalöv', 'RO', '26890', '17    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (20, 'Ooba', 20, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313532783234382e706e672f6464646464642f303030303030', 'Sipes, Waters and Bahringer', '0573-0213  ', '37808-417       ', '65 Cambridge Center', 'Lalupon', 'MI', NULL, '20    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (22, 'Kamba', 22, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f323233783231342e706e672f3566613264642f666666666666', 'Cronin and Sons', '54868-5575 ', '51191-2046      ', '12 Mcguire Plaza', 'Duqiong', 'TO', NULL, '22    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (23, 'Ntag', 23, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313831783136382e706e672f6666343434342f666666666666', 'Renner and Sons', '69154-060  ', '33992-0268      ', '82939 Caliangt Way', 'Sabana Grande de Boyá', 'RO', '10405', '23    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (26, 'Devpoint', 26, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f323530783134372e706e672f6464646464642f303030303030', 'Gleichner Group', '0536-1011  ', '76282-257       ', '684 Hoffman Trail', 'Kasangulu', 'VA', NULL, '26    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (28, 'Brightbean', 28, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313234783138332e706e672f3566613264642f666666666666', 'Bogisich-Sipes', '65923-002  ', '60760-292       ', '62350 Trailsway Court', 'Anzhero-Sudzhensk', 'TO', '65247', '28    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (29, 'Ozu', 29, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313630783134362e706e672f3566613264642f666666666666', 'Heidenreich-Koss', '64117-163  ', '55154-5423      ', '240 Mifflin Circle', 'Tarsouat', 'RO', NULL, '29    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (1, 'Jakarta', 1, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313133783137342e706e672f6464646464642f303030303030', 'Jakarta inc.', '68856-067  ', '43289-020       ', 'Via Roma 45', 'Milano', 'MI', NULL, '1     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (3, 'Youfeed', 3, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313132783137372e706e672f6464646464642f303030303030', 'Leannon and Sons', '43419-009  ', '59767-008       ', '88 Mandrake Road', 'Helong', 'VR', NULL, '3     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (6, 'Realbridge', 6, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313630783136332e706e672f3566613264642f666666666666', 'McCullough Inc', '42254-158  ', '0409-4350       ', '5 Lindbergh Crossing', 'Mundri', 'MI', NULL, '6     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (7, 'Dablist', 7, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f323136783133392e706e672f3566613264642f666666666666', 'Zemlak and Sons', '47335-573  ', '0338-1135       ', '6 Bay Court', 'Krajan Jabungcandi', 'NA', NULL, '7     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (9, 'Dablist', 9, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313739783136302e706e672f6363303030302f666666666666', 'Hand-Nitzsche', '60505-2638 ', '48951-3032      ', '7785 Southridge Pass', 'Sîngerei', 'MI', '26227', '9     ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (12, 'Jetpulse', 12, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f323436783136312e706e672f6363303030302f666666666666', 'Gulgowski-Dibbert', '68462-141  ', '67253-231       ', '2 Transport Circle', 'Aston', 'PD', '78900', '12    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (13, 'Shuffletag', 13, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313833783137392e706e672f3566613264642f666666666666', 'Littel, Olson and Bradtke', '49349-653  ', '65841-770       ', '54155 Utah Center', 'Aparecida de Goiânia', 'VI', '74900', '13    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (15, 'Fliptune', 15, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313637783134372e706e672f6666343434342f666666666666', 'Farrell-Lynch', '11527-051  ', '65601-755       ', '708 Westridge Lane', 'Wuxue Shi', 'BO', NULL, '15    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (18, 'Avavee', 18, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313837783133322e706e672f6666343434342f666666666666', 'Dicki, Smitham and Cassin', '50975-001  ', '52125-480       ', '77444 Raven Point', 'Isoka', 'TO', NULL, '18    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (19, 'Demivee', 19, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f323037783230302e706e672f6363303030302f666666666666', 'Schneider Inc', '66116-388  ', '59762-2001      ', '90307 Stoughton Park', 'Songying', 'MI', NULL, '19    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (21, 'Feednation', 21, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313939783231322e706e672f3566613264642f666666666666', 'Schulist-Hand', '11410-301  ', '50436-7602      ', '587 Anniversary Road', 'Lembung Tengah', 'PD', NULL, '21    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (24, 'Yoveo', 24, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313031783234332e706e672f3566613264642f666666666666', 'Hoeger LLC', '36987-2557 ', '64239-101       ', '0 Bunting Trail', 'Glad', 'FI', '38140', '24    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (25, 'Wikizz', 25, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313436783232362e706e672f6363303030302f666666666666', 'Rau LLC', '0179-0048  ', '60371-344       ', '092 8th Hill', 'Kuasha', 'NA', NULL, '25    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (27, 'Meevee', 27, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313032783232352e706e672f6464646464642f303030303030', 'Bruen-Satterfield', '54868-4531 ', '63304-346       ', '462 Eagle Crest Way', 'San Agustín Acasaguastlán', 'VE', '02003', '27    ', false, false, 0, NULL);
INSERT INTO bitsei_schema."Company" VALUES (30, 'Wordtune', 30, '\x687474703a2f2f64756d6d79696d6167652e636f6d2f313137783139302e706e672f6666343434342f666666666666', 'Mohr LLC', '45865-411  ', '58929-100       ', '52285 Crest Line Street', 'Sila', 'RO', NULL, '30    ', false, false, 0, NULL);


--
-- TOC entry 3816 (class 0 OID 17646)
-- Dependencies: 207
-- Data for Name: Customer; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Customer" VALUES (1, 'AppleLike Inc.', '56479-182  ', '22342-305       ', 'Via Venezia 79', 'Padova', 'PD', NULL, 'applelike@google.com', 'applelike@pec-mac.com', '1     ', 1);
INSERT INTO bitsei_schema."Customer" VALUES (2, 'Reinger Group', '49631-182  ', '14783-305       ', '4 Mockingbird Junction', 'Hengjie', 'RM', NULL, 'rfrankum1@google.nl', 'plyster1@pec-mac.com', '2     ', 2);
INSERT INTO bitsei_schema."Customer" VALUES (3, 'Harvey-Turner', '49281-650  ', '16714-357       ', '9 Arizona Road', 'Thành Phố Lạng Sơn', 'RO', NULL, 'cmccathie2@imgur.com', 'tbette2@pec-usa.gov', '3     ', 3);
INSERT INTO bitsei_schema."Customer" VALUES (4, 'Cronin-Braun', '68428-118  ', '59779-616       ', '32 Everett Court', 'Manuel Antonio Mesones Muro', 'MI', NULL, 'kpringle3@bluehost.com', 'rbetterton3@pec-deliciousdays.com', '4     ', 4);
INSERT INTO bitsei_schema."Customer" VALUES (5, 'Schaden LLC', '43492-0001 ', '15370-032       ', '779 Judy Parkway', 'Daliuhao', 'TO', NULL, 'tfitzalan4@aboutads.info', 'rsheekey4@pec-domainmarket.com', '5     ', 5);
INSERT INTO bitsei_schema."Customer" VALUES (6, 'Mills-Jerde', '59535-1001 ', '49230-200       ', '32650 Del Sol Lane', 'Rennes', 'MI', '35009', 'csowerby5@freewebs.com', 'ffutter5@pec-furl.net', '6     ', 6);
INSERT INTO bitsei_schema."Customer" VALUES (7, 'Lowe, Graham and Klocko', '22431-527  ', '52125-178       ', '9925 Melrose Parkway', 'Sacramento', 'VE', '95833', 'hmerner6@creativecommons.org', 'elorman6@pec-free.fr', '7     ', 7);
INSERT INTO bitsei_schema."Customer" VALUES (8, 'Braun Inc', '43353-798  ', '54868-6220      ', '1 Tony Court', 'Adelaide', 'VI', '5839 ', 'kgallant7@clickbank.net', 'atofano7@pec-infoseek.co.jp', '8     ', 8);
INSERT INTO bitsei_schema."Customer" VALUES (9, 'Marks, Green and Oberbrunner', '49643-388  ', '68387-600       ', '04 Elgar Road', 'Lampa', 'TO', NULL, 'lpeyto8@unc.edu', 'cspoure8@pec-addthis.com', '9     ', 9);
INSERT INTO bitsei_schema."Customer" VALUES (10, 'Lowe Inc', '50742-101  ', '76237-138       ', '0 Loftsgordon Pass', 'Jintian', 'TN', NULL, 'cspencelayh9@mit.edu', 'lbrownsett9@pec-live.com', '10    ', 10);
INSERT INTO bitsei_schema."Customer" VALUES (11, 'Pagac-Dicki', '21695-811  ', '11523-7334      ', '919 Hooker Park', 'Shangbahe', 'PD', NULL, 'ecostara@va.gov', 'ptranfielda@pec-harvard.edu', '11    ', 11);
INSERT INTO bitsei_schema."Customer" VALUES (12, 'Schinner, Morar and Morar', '48951-3091 ', '43857-0100      ', '0817 Clove Crossing', 'Xinbu', 'PD', NULL, 'hgeraudyb@thetimes.co.uk', 'tarnob@pec-tmall.com', '12    ', 12);
INSERT INTO bitsei_schema."Customer" VALUES (13, 'Wehner-Funk', '63354-010  ', '55138-006       ', '2 Little Fleur Park', 'Lianyi', 'MI', NULL, 'cbouldonc@zdnet.com', 'lphilimorec@pec-furl.net', '13    ', 13);
INSERT INTO bitsei_schema."Customer" VALUES (14, 'Bins Inc', NULL, NULL, NULL, NULL, NULL, NULL, 'tvernid@mediafire.com', NULL, NULL, 14);
INSERT INTO bitsei_schema."Customer" VALUES (15, 'Quitzon and Sons', '37205-519  ', '49288-0167      ', '92 Northport Hill', 'Borovoy', 'NA', '30913', 'crunhame@ucsd.edu', 'ehullinse@pec-blinklist.com', '15    ', 15);
INSERT INTO bitsei_schema."Customer" VALUES (16, 'Champlin Inc', '0904-5792  ', '10812-511       ', '6 Cottonwood Center', 'Āshkhāneh', 'NA', NULL, 'hbrammerf@t.co', 'dlougheidf@pec-cnet.com', '16    ', 16);
INSERT INTO bitsei_schema."Customer" VALUES (17, 'Langosh-Spinka', '98132-720  ', '40104-811       ', '40 Merrick Circle', 'Wangqingtuo', 'CA', NULL, 'gdamerellg@usda.gov', 'sclarycottg@pec-sourceforge.net', '17    ', 17);
INSERT INTO bitsei_schema."Customer" VALUES (18, 'Torphy-Skiles', '55154-7801 ', '55504-0230      ', '3 Bowman Circle', 'Sagua de Tánamo', 'VR', NULL, 'bbakeyh@newyorker.com', 'mlinzeyh@pec-harvard.edu', '18    ', 18);
INSERT INTO bitsei_schema."Customer" VALUES (19, 'Hoppe-Trantow', '49035-150  ', '50436-3933      ', '0 Cottonwood Trail', 'Güiria', 'MI', NULL, 'dflorezi@psu.edu', 'csandomi@pec-ebay.co.uk', '19    ', 19);
INSERT INTO bitsei_schema."Customer" VALUES (20, 'Larson-Robel', NULL, NULL, NULL, NULL, NULL, NULL, 'ccrispej@newsvine.com', NULL, NULL, 20);
INSERT INTO bitsei_schema."Customer" VALUES (21, 'Schultz Inc', '60631-425  ', '49999-631       ', '1 Larry Trail', 'Hatsukaichi', 'TO', '73013', 'jrowetk@thetimes.co.uk', 'mjaquemek@pec-t.co', '21    ', 21);
INSERT INTO bitsei_schema."Customer" VALUES (22, 'Frami-Hane', '36987-2837 ', '37000-253       ', '04 Sutteridge Junction', 'Maniwaki', 'TO', '67894', 'crennelsl@google.es', 'cfairburnel@pec-google.es', '22    ', 22);
INSERT INTO bitsei_schema."Customer" VALUES (23, 'Johnston-Cummerata', '65517-0009 ', '55910-756       ', '9371 Melody Alley', 'Leuweheq', 'PO', NULL, 'hchastenetm@canalblog.com', 'lluckcuckm@pec-businessinsider.com', '23    ', 23);
INSERT INTO bitsei_schema."Customer" VALUES (24, 'Ortiz-Kulas', '23155-194  ', '0075-2915       ', '736 Sunnyside Drive', 'Dunleer', 'VI', '34268', 'hkildalen@stumbleupon.com', 'kshilstonen@pec-boston.com', '24    ', 24);
INSERT INTO bitsei_schema."Customer" VALUES (25, 'Witting-Koch', NULL, NULL, NULL, NULL, NULL, NULL, 'ibugbyo@virginia.edu', NULL, NULL, 25);
INSERT INTO bitsei_schema."Customer" VALUES (26, 'Stracke-Collins', '55154-1926 ', '59535-7501      ', '2429 Comanche Avenue', 'São Mamede de Infesta', 'VR', '44655', 'awaslinp@sourceforge.net', 'nlegonidecp@pec-woothemes.com', '26    ', 26);
INSERT INTO bitsei_schema."Customer" VALUES (27, 'Denesik-O''Conner', '0363-1466  ', '63629-1286      ', '71943 Sunnyside Drive', 'Donglu', 'TN', NULL, 'ofrentzq@wikipedia.org', 'khectorq@pec-seesaa.net', '27    ', 27);
INSERT INTO bitsei_schema."Customer" VALUES (28, 'Haley, Schaden and Herzog', '0009-3701  ', '58232-0329      ', '26 Del Sol Pass', 'Međa', 'NA', NULL, 'bpittwoodr@topsy.com', 'jduddenr@pec-simplemachines.org', '28    ', 28);
INSERT INTO bitsei_schema."Customer" VALUES (29, 'Kutch-Hilll', '0187-1613  ', '55714-2262      ', '85 Hanover Lane', 'Gaurnadi', 'MI', '8242 ', 'psickamores@joomla.org', 'ljakolevitchs@pec-dailymail.co.uk', '29    ', 29);
INSERT INTO bitsei_schema."Customer" VALUES (30, 'Krajcik LLC', '53645-1001 ', '55714-2258      ', '8 Gulseth Place', 'Pilawa', 'RO', '08440', 'hgrummittt@bigcartel.com', 'aferriet@pec-odnoklassniki.ru', '30    ', 30);


--
-- TOC entry 3818 (class 0 OID 17663)
-- Dependencies: 209
-- Data for Name: Invoice; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Invoice" VALUES (1, 1, 0, '1', '2022-05-01', 'DignissimVestibulum.mp3', '1', '2023-01-12', 'Nullam.xls', 'AtLoremInteger.avi', 168.3, 15, 4.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (2, 2, 0, '2', '2022-08-12', 'NuncNisl.gif', '2', '2022-05-06', 'VestibulumSed.tiff', 'UltricesVelAugue.jpeg', 929.7, 49.8, 3.5, false);
INSERT INTO bitsei_schema."Invoice" VALUES (3, 3, 0, '3', '2022-09-12', 'Ultrices.xls', '3', '2022-12-03', 'EgetCongueEget.pdf', 'CubiliaCurae.xls', 851.6, 41.5, 6.4, false);
INSERT INTO bitsei_schema."Invoice" VALUES (4, 4, 0, '4', '2023-12-03', 'SagittisDui.mp3', '4', '2022-06-02', 'IaculisJustoIn.avi', 'InHacHabitasse.tiff', 392, 51.1, 4.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (5, 5, 0, '5', '2022-08-05', 'NullamSitAmet.mpeg', '5', '2022-09-01', 'FuscePosuereFelis.xls', 'AeneanAuctor.avi', 909.5, 64.9, 6.3, false);
INSERT INTO bitsei_schema."Invoice" VALUES (6, 6, 0, '6', '2022-06-01', 'LiberoUtMassa.avi', '6', '2023-01-04', 'Primis.gif', 'HabitassePlateaDictumst.pdf', 271.2, 29.4, 1.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (7, 7, 0, '7', '2022-06-09', 'InterdumVenenatisTurpis.mp3', '7', '2022-11-08', 'Vel.avi', 'ElementumPellentesque.mp3', 180.7, 7.9, 8.5, false);
INSERT INTO bitsei_schema."Invoice" VALUES (8, 8, 0, '8', '2022-07-03', 'PedePosuere.png', '8', '2022-05-05', 'SedVel.avi', 'VelNullaEget.tiff', 598.1, 52.6, 4.2, false);
INSERT INTO bitsei_schema."Invoice" VALUES (9, 9, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5.5, false);
INSERT INTO bitsei_schema."Invoice" VALUES (10, 10, 0, '10', '2022-08-10', 'MaurisUllamcorperPurus.mov', '10', '2022-04-06', 'CubiliaCurae.mp3', 'Potenti.jpeg', 875.3, 58.1, 4.6, false);
INSERT INTO bitsei_schema."Invoice" VALUES (11, 11, 0, NULL, NULL, NULL, '11', '2022-06-11', 'QuamPedeLobortis.mov', 'NuncViverraDapibus.xls', 689.2, 23.8, 7.5, false);
INSERT INTO bitsei_schema."Invoice" VALUES (12, 12, 0, '12', '2023-01-12', 'TurpisNecEuismod.mp3', '12', '2022-05-04', 'Tellus.ppt', 'DictumstMaecenas.jpeg', 78.2, 46, 7.8, false);
INSERT INTO bitsei_schema."Invoice" VALUES (13, 13, 0, '13', '2022-09-04', 'Nam.doc', '13', '2023-02-07', 'Cubilia.mp3', 'DiamVitae.doc', 476.3, 62.8, 1.9, false);
INSERT INTO bitsei_schema."Invoice" VALUES (14, 14, 0, '14', '2022-04-08', 'Tincidunt.mp3', '14', '2023-02-09', 'NonPretium.ppt', 'NecEuismodScelerisque.tiff', 222.6, 73.3, 9, false);
INSERT INTO bitsei_schema."Invoice" VALUES (15, 15, 0, '15', '2022-10-01', 'Felis.avi', '15', '2023-02-04', 'AeneanFermentum.jpeg', 'Lectus.xls', 592.1, 14.5, 4.9, false);
INSERT INTO bitsei_schema."Invoice" VALUES (16, 16, 0, NULL, NULL, NULL, '16', '2022-10-06', 'EgetMassa.ppt', 'Leo.avi', 690.3, 35.8, 4.4, false);
INSERT INTO bitsei_schema."Invoice" VALUES (17, 17, 0, NULL, NULL, NULL, '17', '2022-06-05', 'Mi.mp3', 'MaurisNonLigula.mp3', 565.7, 3.5, 1.3, false);
INSERT INTO bitsei_schema."Invoice" VALUES (18, 18, 0, '18', '2023-03-11', 'MagnaVestibulumAliquet.ppt', '18', '2022-09-04', 'Eros.avi', 'PellentesqueViverra.gif', 243.5, 57.6, 5.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (19, 19, 0, '19', '2022-03-08', 'Fringilla.pdf', NULL, NULL, NULL, NULL, NULL, NULL, 7.8, false);
INSERT INTO bitsei_schema."Invoice" VALUES (20, 20, 0, '20', '2022-09-06', 'NequeDuis.ppt', '20', '2022-04-04', 'Pede.ppt', 'SollicitudinMiSit.ppt', 71.2, 46.8, 3.2, false);
INSERT INTO bitsei_schema."Invoice" VALUES (21, 21, 0, '21', '2022-07-08', 'TristiqueEstEt.mp3', '21', '2022-06-09', 'At.tiff', 'EstDonec.gif', 888.6, 71.6, 2.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (22, 22, 0, NULL, NULL, NULL, '22', '2022-07-08', 'DuisAliquamConvallis.ppt', 'EgetEros.ppt', 266.4, 20.9, 1.2, false);
INSERT INTO bitsei_schema."Invoice" VALUES (23, 23, 0, '23', '2023-02-03', 'FaucibusOrciLuctus.mp3', '23', '2022-07-01', 'A.xls', 'NonMauris.mp3', 591.9, 54.7, 7.8, false);
INSERT INTO bitsei_schema."Invoice" VALUES (24, 24, 0, '24', '2022-11-05', 'CongueEtiam.ppt', '24', '2022-08-02', 'TinciduntNullaMollis.xls', 'Dis.mp3', 847.3, 79.8, 5.9, false);
INSERT INTO bitsei_schema."Invoice" VALUES (25, 25, 0, '25', '2023-03-11', 'InSagittis.ppt', '25', '2022-12-06', 'OdioOdio.ppt', 'PulvinarNulla.xls', 907.2, 68.8, 6.6, false);
INSERT INTO bitsei_schema."Invoice" VALUES (26, 26, 0, '26', '2023-02-12', 'OdioElementumEu.tiff', '26', '2022-11-08', 'Pulvinar.mp3', 'DonecOdioJusto.mp3', 296.5, 2.5, 2.8, false);
INSERT INTO bitsei_schema."Invoice" VALUES (27, 27, 0, NULL, NULL, NULL, '27', '2022-06-07', 'SedVestibulum.mp3', 'AliquamQuis.doc', 643, 65.2, 2.8, false);
INSERT INTO bitsei_schema."Invoice" VALUES (28, 28, 0, '28', '2022-11-03', 'Nunc.xls', '28', '2022-09-01', 'Orci.ppt', 'TellusNulla.avi', 123.5, 98.9, 2.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (29, 29, 0, '29', '2022-08-07', 'ArcuLibero.tiff', '29', '2023-01-08', 'CrasPellentesque.avi', 'DuisAcNibh.avi', 846.8, 75.4, 1.2, false);
INSERT INTO bitsei_schema."Invoice" VALUES (30, 30, 0, NULL, NULL, NULL, '30', '2022-06-07', 'LuctusUltriciesEu.png', 'PretiumQuis.avi', 553.3, 11.9, 2.1, false);


--
-- TOC entry 3825 (class 0 OID 17729)
-- Dependencies: 216
-- Data for Name: Invoice_Product; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Invoice_Product" VALUES (1, 1, 50, 26.9, NULL, NULL, '2021-05-01');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (2, 2, 25, 86.3, NULL, NULL, '2022-06-02');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (5, 5, 35, 57.3, 75.9, 'User-centric client-driven functionalities', '2021-01-01');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (6, 6, 41, 2.3, 65.5, 'User-friendly motivating application', '2022-10-06');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (8, 8, 22, 77, 97.8, 'Total client-server infrastructure', '2021-01-12');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (9, 9, 21, 73.1, NULL, NULL, '2021-02-02');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (10, 10, 25, 39.3, 97.3, 'Cross-group maximized conglomeration', '2022-03-08');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (11, 11, 17, 2, 38.6, 'Centralized 6th generation internet solution', '2021-05-05');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (12, 12, 6, 57.5, 83.8, 'Digitized zero defect hub', '2021-06-12');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (13, 13, 18, 15.5, 24.3, 'Managed dedicated time-frame', '2022-05-09');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (14, 14, 35, 95.5, 51.9, 'Reactive system-worthy structure', '2022-02-03');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (15, 15, 17, 83.6, 11.8, 'Decentralized attitude-oriented instruction set', '2023-02-01');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (16, 16, 41, 98.7, 45.8, 'Self-enabling neutral hardware', '2023-01-02');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (17, 17, 11, 85.2, 3.7, 'Horizontal high-level database', '2021-06-10');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (19, 19, 40, 91.1, 33, 'Intuitive client-server support', '2021-12-11');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (20, 20, 50, 23.6, 7.2, 'Front-line needs-based info-mediaries', '2023-11-03');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (21, 21, 4, 89.6, 24.9, 'Horizontal secondary algorithm', '2021-12-04');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (24, 24, 24, 96.3, NULL, NULL, '2021-12-04');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (25, 25, 26, 28, 1.8, 'Self-enabling bi-directional functionalities', '2022-01-05');
INSERT INTO bitsei_schema."Invoice_Product" VALUES (26, 26, 36, 32.6, NULL, NULL, '2022-02-09');


--
-- TOC entry 3820 (class 0 OID 17681)
-- Dependencies: 211
-- Data for Name: Log; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Log" VALUES (1, false, 'Progressive cohesive projection                                                                                                                                                                                                                                ', 'Devolved actuating moratorium                                                                                                                                                                                                                                  ', 1);
INSERT INTO bitsei_schema."Log" VALUES (2, false, 'Reverse-engineered holistic attitude                                                                                                                                                                                                                           ', 'Synergized incremental interface                                                                                                                                                                                                                               ', 2);
INSERT INTO bitsei_schema."Log" VALUES (3, false, 'Persistent grid-enabled moderator                                                                                                                                                                                                                              ', 'Robust stable success                                                                                                                                                                                                                                          ', 3);
INSERT INTO bitsei_schema."Log" VALUES (4, false, 'Stand-alone foreground time-frame                                                                                                                                                                                                                              ', 'Cloned content-based migration                                                                                                                                                                                                                                 ', 4);
INSERT INTO bitsei_schema."Log" VALUES (5, false, 'Persevering 24/7 function                                                                                                                                                                                                                                      ', 'Mandatory attitude-oriented help-desk                                                                                                                                                                                                                          ', 5);
INSERT INTO bitsei_schema."Log" VALUES (6, false, 'Profit-focused value-added matrix                                                                                                                                                                                                                              ', 'Optional well-modulated application                                                                                                                                                                                                                            ', 6);
INSERT INTO bitsei_schema."Log" VALUES (7, false, 'Face to face motivating algorithm                                                                                                                                                                                                                              ', 'Public-key motivating methodology                                                                                                                                                                                                                              ', 7);
INSERT INTO bitsei_schema."Log" VALUES (8, false, 'Networked grid-enabled encryption                                                                                                                                                                                                                              ', 'Reverse-engineered tangible solution                                                                                                                                                                                                                           ', 8);
INSERT INTO bitsei_schema."Log" VALUES (9, false, 'Re-contextualized encompassing analyzer                                                                                                                                                                                                                        ', 'Extended zero defect hub                                                                                                                                                                                                                                       ', 9);
INSERT INTO bitsei_schema."Log" VALUES (10, false, 'Horizontal fault-tolerant methodology                                                                                                                                                                                                                          ', 'Organic even-keeled open system                                                                                                                                                                                                                                ', 10);
INSERT INTO bitsei_schema."Log" VALUES (11, false, 'Progressive composite installation                                                                                                                                                                                                                             ', 'Robust systematic array                                                                                                                                                                                                                                        ', 11);
INSERT INTO bitsei_schema."Log" VALUES (12, false, 'Proactive 6th generation contingency                                                                                                                                                                                                                           ', 'Compatible logistical policy                                                                                                                                                                                                                                   ', 12);
INSERT INTO bitsei_schema."Log" VALUES (13, false, NULL, NULL, NULL);
INSERT INTO bitsei_schema."Log" VALUES (14, false, 'Team-oriented content-based instruction set                                                                                                                                                                                                                    ', 'Synchronised maximized service-desk                                                                                                                                                                                                                            ', 14);
INSERT INTO bitsei_schema."Log" VALUES (15, false, 'Monitored non-volatile utilisation                                                                                                                                                                                                                             ', 'User-centric heuristic time-frame                                                                                                                                                                                                                              ', 15);
INSERT INTO bitsei_schema."Log" VALUES (16, false, 'Organic client-driven intranet                                                                                                                                                                                                                                 ', 'Public-key dedicated definition                                                                                                                                                                                                                                ', 16);
INSERT INTO bitsei_schema."Log" VALUES (17, false, 'Advanced static flexibility                                                                                                                                                                                                                                    ', 'Virtual homogeneous alliance                                                                                                                                                                                                                                   ', 17);
INSERT INTO bitsei_schema."Log" VALUES (18, false, 'Business-focused 5th generation policy                                                                                                                                                                                                                         ', 'Profound exuding definition                                                                                                                                                                                                                                    ', 18);
INSERT INTO bitsei_schema."Log" VALUES (19, false, 'Configurable directional parallelism                                                                                                                                                                                                                           ', 'Cross-platform intermediate installation                                                                                                                                                                                                                       ', 19);
INSERT INTO bitsei_schema."Log" VALUES (20, false, NULL, NULL, NULL);
INSERT INTO bitsei_schema."Log" VALUES (21, false, 'Open-architected client-server hardware                                                                                                                                                                                                                        ', 'Down-sized demand-driven methodology                                                                                                                                                                                                                           ', 21);
INSERT INTO bitsei_schema."Log" VALUES (22, false, 'Persevering 3rd generation leverage                                                                                                                                                                                                                            ', 'Multi-channelled upward-trending utilisation                                                                                                                                                                                                                   ', 22);
INSERT INTO bitsei_schema."Log" VALUES (23, false, 'Configurable user-facing frame                                                                                                                                                                                                                                 ', 'Mandatory cohesive complexity                                                                                                                                                                                                                                  ', 23);
INSERT INTO bitsei_schema."Log" VALUES (24, false, 'Universal clear-thinking installation                                                                                                                                                                                                                          ', 'Customer-focused systematic application                                                                                                                                                                                                                        ', 24);
INSERT INTO bitsei_schema."Log" VALUES (25, false, 'Inverse incremental implementation                                                                                                                                                                                                                             ', 'Streamlined clear-thinking secured line                                                                                                                                                                                                                        ', 25);
INSERT INTO bitsei_schema."Log" VALUES (26, false, 'Cross-group fresh-thinking attitude                                                                                                                                                                                                                            ', 'Networked holistic internet solution                                                                                                                                                                                                                           ', 26);
INSERT INTO bitsei_schema."Log" VALUES (27, false, 'Pre-emptive didactic infrastructure                                                                                                                                                                                                                            ', 'Secured maximized projection                                                                                                                                                                                                                                   ', 27);
INSERT INTO bitsei_schema."Log" VALUES (28, false, 'Triple-buffered client-driven framework                                                                                                                                                                                                                        ', 'Object-based multimedia knowledge user                                                                                                                                                                                                                         ', 28);
INSERT INTO bitsei_schema."Log" VALUES (29, false, 'Self-enabling intangible encoding                                                                                                                                                                                                                              ', 'Face to face reciprocal standardization                                                                                                                                                                                                                        ', 29);
INSERT INTO bitsei_schema."Log" VALUES (30, false, 'Optional fresh-thinking standardization                                                                                                                                                                                                                        ', 'Diverse bi-directional budgetary management                                                                                                                                                                                                                    ', 30);


--
-- TOC entry 3811 (class 0 OID 17604)
-- Dependencies: 202
-- Data for Name: Owner; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Owner" VALUES (1, 'Burr', 'Mycah', 'mmcclosh0', '$2a$08$ptK.dB3HfvQCT1ni9AUnp.StrBXTugEcU8XbimKHYM50J0rDrC7Ne', 'mchaudret0@dailymail.co.uk', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (2, 'Amble', 'Antone', 'alabitt1', '$2a$08$0BsE6DKMFiKlItjzFXF7puKztkyg0ROlmyIN4q37h94zEO7GJ.ezK', 'acarah1@google.es', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (3, 'Markus', 'Emmalynn', 'ebickle2', '$2a$08$3XyxioBHRGtAtFzKvBg5LuSYe4O/NyGkOn14IoMvIK5PNRHoizeji', 'eberrey2@businesswire.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (4, 'Welby', 'Noach', 'nmcdool3', '$2a$08$B5zuSp/hPhGkK8TKr84Equ4RAwyDVkjnPWhYOL6JOtQmOpzYt8Ab2', 'npurrington3@tmall.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (5, 'Melly', 'Farica', 'fcona4', '$2a$08$oV5QaqVq5yv6TPx7yAfAQu/BBwLVDv8Lnm4x9XselJvY0r0KS9HcG', 'fbinley4@princeton.edu', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (6, 'Adorne', 'Robin', 'rgrewcock5', '$2a$08$JyHlXb4XmEIYyfKqYYwM4OG/9foZYSoJffJrQQ.hCcoE6Y7QIyt2.', 'rputt5@wp.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (7, 'Betty', 'Alanah', 'amoles6', '$2a$08$WPCCT58eZoMdMshB2TNeweHGq51G/9MAcqzeVSOvE91e0YeJk6qPa', 'abale6@reuters.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (8, 'Florinda', 'Dusty', 'dduckerin7', '$2a$08$eXBtIr5qkUI/jUDZttUK9e.ht.FrrxRnt3QujQD2r1chI9KRpPklO', 'dwalden7@yahoo.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (9, 'Chrissie', 'Floyd', 'fcoule8', '$2a$08$kvz25vCeIWQpEtfae6hru.KVCx0dKeihJheWcGl/1BAX9GLOfqApS', 'ftwist8@yale.edu', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (10, 'Gris', 'Ferrel', 'fdellcasa9', '$2a$08$N8LeqDRXJg46NyIxyOVKf.psJKkiSZmxgqOEWiKSx9gi6jAvgUgMi', 'fhaskew9@t-online.de', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (11, 'Anatol', 'Waverley', 'wderoosa', '$2a$08$tbLc7KikFtANvpnCkhOaO.eGQYcq9m.wdkb7i0Z0KDNi8KKGqT3hu', 'wvossinga@pcworld.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (12, 'Quinta', 'Margaret', 'mcristofolinib', '$2a$08$6nLVeqo7Xaq1zrpvyOdJteoRv8fOluuc2wuiBtHh8Jt2gAACWj9pG', 'mmaberb@shutterfly.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (13, 'Loy', 'Berkly', 'bchirmc', '$2a$08$ql9xDfaswxRqdzcag8AIeOdgmPSyrtjkYPxfawsgkizafainmh4OK', 'bcockrenc@com.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (14, 'Britt', 'Cynthie', 'ctothacotd', '$2a$08$TCOVPLfEO3ekzZuyvPk/4eYic.T./PM6iFC3e.eRl/DAS9qhb7MIm', 'cdenged@bizjournals.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (15, 'Roman', 'Harp', 'hgouldthorpee', '$2a$08$4mumD2C9zhXBFuJS5xRY1egwGOYzZK91s2ZFWbMBtvAEriuq79ONu', 'hfullagare@quantcast.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (16, 'Patrica', 'Ardyce', 'aliveseyf', '$2a$08$wOpPD6NFENNEiB7VdtIW9enrdB6P7.41enskS3zYgVXrPmfYhGpLK', 'ajanakf@huffingtonpost.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (17, 'John', 'Devina', 'deynaudg', '$2a$08$sugct8EFm3s1GgRSVh/3AO9pIiEKR.ywXX2/vQaKNs8e.tu6b4cPm', 'dmatyushonokg@huffingtonpost.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (18, 'Sutherland', 'Alic', 'alintillh', '$2a$08$quczDl36dK4dGeUrXdNOBOZJbp.72sZ32f.hAFgEIdddnuWmyjm0C', 'adachh@wikia.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (19, 'Joete', 'Sloan', 'scheinei', '$2a$08$f4Gw2A5RKQWQpzfO3oGg0OoufVLEgoBQwbW6gEpa2Q3c/d07fd.tW', 'sheijnei@sina.com.cn', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (20, 'Shandra', 'Petey', 'pudellj', '$2a$08$mgXrapF63xTo6EUd3N5V9uBY9bCrEQdKF.dOp4aPCnn1iOnLjm4fm', 'pfadellj@youku.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (21, 'Eddy', 'Lauryn', 'lmawdittk', '$2a$08$CvSPmBz5wDjJZjbfe2S7m.TUI1hC.sAfZz/xcZ6.myyk2lvkkxTK2', 'lludyek@wikia.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (22, 'Tandie', 'Anson', 'askillicornl', '$2a$08$8j9Wy07EpHCEKdMo2T13ReSev2RcULyUqDWlvHYxFOoA6Vzsc6Fli', 'acanadal@netlog.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (23, 'Jacynth', 'Cacilie', 'cgreyesm', '$2a$08$s0Gbl1mJ8fXgAg0.2OzYkeA7YATVbOKJo9ncVPlB01XDXRoHbxjpO', 'codohertym@sphinn.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (24, 'Avigdor', 'Onfroi', 'obissattn', '$2a$08$e8efttqQJOrpjt1l00l8deTy8ST6JWPukZC58t6pDTWf2GiNtuFqK', 'obensenn@wsj.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (25, 'Anatole', 'Vikki', 'vtallowino', '$2a$08$RsuV9IN0h83GDu74zW2CmO8tgojWc5Mv8uMS/K5PSms2sTIL8.ph.', 'vwieldo@tinypic.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (26, 'Tyler', 'Britt', 'bmighelep', '$2a$08$P2x6yAC0et4tbPc5JzC/9.g7.Inp4w8Bsp8h8n9UCVQG40Z2zjJgG', 'bmcveightyp@bloomberg.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (27, 'Annaliese', 'Livia', 'lgetcliffeq', '$2a$08$OmO/uYE3t22zZjOmUk7FeOgqmEAQsrvckgRIn2FXET5Xi4.7rJA0m', 'ldikq@ted.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (28, 'Emlynne', 'Tandy', 'temminesr', '$2a$08$eoWHB7P6485nxtG/yUj29e1VgvUnbb3zZACj3hkTv2/GOtvvcafkq', 'tgibbr@bizjournals.com', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (29, 'Sophia', 'Trace', 'thegleys', '$2a$08$Kh.0zJlw34Y41Kn8SD3HRujQW/ejj/1CpA679dMCOk9cd8mqdy3Fi', 'tordemanns@dot.gov', NULL, 3);
INSERT INTO bitsei_schema."Owner" VALUES (30, 'Daffy', 'Lisette', 'ldinningtont', '$2a$08$11EaSXE0bfZxtIR0ZVOdlO4MWYOzVZo5VPqHgWemWn0aHMfBOvKMe', 'lgianettinit@wp.com', NULL, 3);


--
-- TOC entry 3812 (class 0 OID 17614)
-- Dependencies: 203
-- Data for Name: Password_Reset_Token; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--



--
-- TOC entry 3822 (class 0 OID 17698)
-- Dependencies: 213
-- Data for Name: Product; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Product" VALUES (1, 1, 'ETHANOL', 29, 'http://dummyimage.com/222x224.png/5fa2dd/ffffff', 'Kg   ', 'Pre-emptive upward-trending analyzer');
INSERT INTO bitsei_schema."Product" VALUES (2, 2, 'Antimoium crud, Benzoic ac, Ledum, Nux vom, Quercus, Rhododendron, Silicea', 24, 'http://dummyimage.com/225x208.png/5fa2dd/ffffff', 'Kg   ', 'Ameliorated mission-critical adapter');
INSERT INTO bitsei_schema."Product" VALUES (3, 3, 'azathioprine', 96, NULL, 'Kg   ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (4, 4, 'California Valley White Oak', 92, 'http://dummyimage.com/172x173.png/cc0000/ffffff', 'm    ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (5, 5, 'GLYCERIN', 7, 'http://dummyimage.com/113x109.png/dddddd/000000', 'Kg   ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (6, 6, 'OXYGEN', 78, 'http://dummyimage.com/226x208.png/5fa2dd/ffffff', 'L    ', 'Cross-group uniform application');
INSERT INTO bitsei_schema."Product" VALUES (7, 7, 'formic acid', 34, 'http://dummyimage.com/227x115.png/ff4444/ffffff', 'L    ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (8, 8, 'Alumina', 64, 'http://dummyimage.com/204x129.png/5fa2dd/ffffff', 'm    ', 'Digitized multi-tasking throughput');
INSERT INTO bitsei_schema."Product" VALUES (9, 9, 'Hydrocodone Bitartrate And Acetaminophen', 25, NULL, 'Kg   ', 'Focused eco-centric benchmark');
INSERT INTO bitsei_schema."Product" VALUES (10, 10, 'Hydrocortisone', 31, 'http://dummyimage.com/141x199.png/cc0000/ffffff', 'L    ', 'Customer-focused cohesive infrastructure');
INSERT INTO bitsei_schema."Product" VALUES (11, 11, 'AZITHROMYCIN', 53, 'http://dummyimage.com/124x233.png/dddddd/000000', 'Kg   ', 'Open-architected zero tolerance architecture');
INSERT INTO bitsei_schema."Product" VALUES (12, 12, 'Doxycycline hyclate', 22, 'http://dummyimage.com/164x213.png/dddddd/000000', 'Kg   ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (13, 13, 'lansoprazole', 3, NULL, 'Kg   ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (14, 14, 'Oxygen', 37, 'http://dummyimage.com/143x221.png/dddddd/000000', 'L    ', 'Seamless homogeneous groupware');
INSERT INTO bitsei_schema."Product" VALUES (15, 15, 'isopropyl alcohol', 7, NULL, 'L    ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (16, 16, 'Buprenorphine Hydrochloride', 32, 'http://dummyimage.com/140x130.png/5fa2dd/ffffff', 'L    ', 'Innovative 5th generation internet solution');
INSERT INTO bitsei_schema."Product" VALUES (17, 17, 'American Cockroach', 79, 'http://dummyimage.com/178x118.png/cc0000/ffffff', 'm    ', 'Synergized zero tolerance array');
INSERT INTO bitsei_schema."Product" VALUES (18, 18, 'CHLOROXYLENOL', 65, 'http://dummyimage.com/124x239.png/5fa2dd/ffffff', 'L    ', 'Function-based human-resource project');
INSERT INTO bitsei_schema."Product" VALUES (19, 19, 'Avobenzone', 1, NULL, 'Kg   ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (20, 20, 'Diltiazem HCl', 22, 'http://dummyimage.com/219x149.png/5fa2dd/ffffff', 'Kg   ', 'Mandatory incremental data-warehouse');
INSERT INTO bitsei_schema."Product" VALUES (21, 21, 'Escitalopram Oxalate', 87, NULL, 'Kg   ', 'Robust composite pricing structure');
INSERT INTO bitsei_schema."Product" VALUES (22, 22, 'Miconazole Nitrate', 61, 'http://dummyimage.com/226x240.png/cc0000/ffffff', 'L    ', 'Centralized even-keeled artificial intelligence');
INSERT INTO bitsei_schema."Product" VALUES (23, 23, 'Levocarnitine', 10, 'http://dummyimage.com/158x118.png/5fa2dd/ffffff', 'L    ', 'Synergistic zero tolerance firmware');
INSERT INTO bitsei_schema."Product" VALUES (24, 24, 'lidocaine and prilocaine', 58, 'http://dummyimage.com/230x143.png/dddddd/000000', 'Kg   ', 'Exclusive contextually-based local area network');
INSERT INTO bitsei_schema."Product" VALUES (25, 25, 'ALCOHOL', 16, 'http://dummyimage.com/223x150.png/dddddd/000000', 'L    ', 'Team-oriented analyzing productivity');
INSERT INTO bitsei_schema."Product" VALUES (26, 26, 'olmesartan medoxomil-hydrochlorothiazide', 90, 'http://dummyimage.com/107x196.png/cc0000/ffffff', 'L    ', 'Object-based secondary contingency');
INSERT INTO bitsei_schema."Product" VALUES (27, 27, 'Avobenzone, Octinoxate, Octisalate, Oxybenzone', 61, 'http://dummyimage.com/163x246.png/cc0000/ffffff', 'L    ', NULL);
INSERT INTO bitsei_schema."Product" VALUES (28, 28, 'Titanium Dioxide', 44, NULL, 'L    ', 'Innovative national structure');
INSERT INTO bitsei_schema."Product" VALUES (29, 29, 'zolpidem tartrate', 10, 'http://dummyimage.com/143x138.png/cc0000/ffffff', 'Kg   ', 'Upgradable demand-driven concept');
INSERT INTO bitsei_schema."Product" VALUES (30, 30, 'Furosemide', 53, 'http://dummyimage.com/230x177.png/cc0000/ffffff', 'Kg   ', 'Proactive dynamic implementation');


--
-- TOC entry 3839 (class 0 OID 0)
-- Dependencies: 214
-- Name: BankAccount_bankaccount_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."BankAccount_bankaccount_id_seq"', 30, true);


--
-- TOC entry 3840 (class 0 OID 0)
-- Dependencies: 204
-- Name: Company_company_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Company_company_id_seq"', 30, true);


--
-- TOC entry 3841 (class 0 OID 0)
-- Dependencies: 206
-- Name: Customer_customer_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Customer_customer_id_seq"', 30, true);


--
-- TOC entry 3842 (class 0 OID 0)
-- Dependencies: 208
-- Name: Invoice_invoice_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Invoice_invoice_id_seq"', 30, true);


--
-- TOC entry 3843 (class 0 OID 0)
-- Dependencies: 210
-- Name: Log_log_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Log_log_id_seq"', 30, true);


--
-- TOC entry 3844 (class 0 OID 0)
-- Dependencies: 201
-- Name: Owner_owner_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Owner_owner_id_seq"', 30, true);


--
-- TOC entry 3845 (class 0 OID 0)
-- Dependencies: 212
-- Name: Product_product_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Product_product_id_seq"', 30, true);


--
-- TOC entry 3667 (class 2606 OID 17722)
-- Name: BankAccount Account_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount"
    ADD CONSTRAINT "Account_pkey" PRIMARY KEY (bankaccount_id);


--
-- TOC entry 3656 (class 2606 OID 17638)
-- Name: Company Company_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company"
    ADD CONSTRAINT "Company_pkey" PRIMARY KEY (company_id);


--
-- TOC entry 3658 (class 2606 OID 17654)
-- Name: Customer Customer_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer"
    ADD CONSTRAINT "Customer_pkey" PRIMARY KEY (customer_id);


--
-- TOC entry 3670 (class 2606 OID 17736)
-- Name: Invoice_Product Invoice_Product_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Invoice_Product_pkey" PRIMARY KEY (invoice_id, product_id);


--
-- TOC entry 3661 (class 2606 OID 17673)
-- Name: Invoice Invoice_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice"
    ADD CONSTRAINT "Invoice_pkey" PRIMARY KEY (invoice_id);


--
-- TOC entry 3663 (class 2606 OID 17690)
-- Name: Log Log_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log"
    ADD CONSTRAINT "Log_pkey" PRIMARY KEY (log_id);


--
-- TOC entry 3650 (class 2606 OID 17613)
-- Name: Owner Owner_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Owner"
    ADD CONSTRAINT "Owner_pkey" PRIMARY KEY (owner_id);


--
-- TOC entry 3652 (class 2606 OID 17618)
-- Name: Password_Reset_Token Password_Reset_Token_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Password_Reset_Token"
    ADD CONSTRAINT "Password_Reset_Token_pkey" PRIMARY KEY (owner_id, token);


--
-- TOC entry 3654 (class 2606 OID 17620)
-- Name: Password_Reset_Token Password_Reset_Token_token_key; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Password_Reset_Token"
    ADD CONSTRAINT "Password_Reset_Token_token_key" UNIQUE (token);


--
-- TOC entry 3665 (class 2606 OID 17706)
-- Name: Product Product_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product"
    ADD CONSTRAINT "Product_pkey" PRIMARY KEY (product_id);


--
-- TOC entry 3659 (class 1259 OID 17660)
-- Name: fki_Company; Type: INDEX; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE INDEX "fki_Company" ON bitsei_schema."Customer" USING btree (company_id);


--
-- TOC entry 3668 (class 1259 OID 17728)
-- Name: fki_c; Type: INDEX; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE INDEX fki_c ON bitsei_schema."BankAccount" USING btree (company_id);


--
-- TOC entry 3673 (class 2606 OID 17655)
-- Name: Customer Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3676 (class 2606 OID 17707)
-- Name: Product Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3677 (class 2606 OID 17723)
-- Name: BankAccount Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3674 (class 2606 OID 17674)
-- Name: Invoice Customer; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice"
    ADD CONSTRAINT "Customer" FOREIGN KEY (customer_id) REFERENCES bitsei_schema."Customer"(customer_id);


--
-- TOC entry 3675 (class 2606 OID 17691)
-- Name: Log Invoice; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log"
    ADD CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id) ON DELETE CASCADE;


--
-- TOC entry 3678 (class 2606 OID 17737)
-- Name: Invoice_Product Invoice; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id) ON UPDATE CASCADE ON DELETE CASCADE;;


--
-- TOC entry 3671 (class 2606 OID 17621)
-- Name: Password_Reset_Token Owner; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Password_Reset_Token"
    ADD CONSTRAINT "Owner" FOREIGN KEY (owner_id) REFERENCES bitsei_schema."Owner"(owner_id);


--
-- TOC entry 3672 (class 2606 OID 17639)
-- Name: Company Owner; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company"
    ADD CONSTRAINT "Owner" FOREIGN KEY (owner_id) REFERENCES bitsei_schema."Owner"(owner_id);


--
-- TOC entry 3679 (class 2606 OID 17742)
-- Name: Invoice_Product Product; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Product" FOREIGN KEY (product_id) REFERENCES bitsei_schema."Product"(product_id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2023-05-31 15:22:58

--
-- PostgreSQL database dump complete
--

