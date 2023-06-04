--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2023-06-02 15:21:37

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
-- TOC entry 6 (class 2615 OID 30847)
-- Name: bitsei_schema; Type: SCHEMA; Schema: -; Owner: bitsei_user
--

CREATE SCHEMA bitsei_schema;


ALTER SCHEMA bitsei_schema OWNER TO bitsei_user;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 30848)
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
-- TOC entry 216 (class 1259 OID 30853)
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
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 216
-- Name: BankAccount_bankaccount_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."BankAccount_bankaccount_id_seq" OWNED BY bitsei_schema."BankAccount".bankaccount_id;


--
-- TOC entry 217 (class 1259 OID 30854)
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
                                         unique_code character(7),
                                         has_telegram_notifications boolean DEFAULT false,
                                         has_mail_notifications boolean DEFAULT false,
                                         fiscal_company_type integer DEFAULT 0,
                                         pec character varying
);


ALTER TABLE bitsei_schema."Company" OWNER TO bitsei_user;

--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN "Company".fiscal_company_type; Type: COMMENT; Schema: bitsei_schema; Owner: bitsei_user
--

COMMENT ON COLUMN bitsei_schema."Company".fiscal_company_type IS '0 - REGIME FORFETTARIO DITTA INDIVIDUALE
1 - REGIME FORFETTARIO DITTA INDIVIDUALE CON RITENUTA
2 - REGIME ORDINARIO DITTA INDIVIDUALE
3 - PARTITA IVA GENERICA';


--
-- TOC entry 218 (class 1259 OID 30862)
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
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 218
-- Name: Company_company_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Company_company_id_seq" OWNED BY bitsei_schema."Company".company_id;


--
-- TOC entry 219 (class 1259 OID 30863)
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
                                          unique_code character(7),
                                          company_id integer NOT NULL
);


ALTER TABLE bitsei_schema."Customer" OWNER TO bitsei_user;

--
-- TOC entry 220 (class 1259 OID 30868)
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
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 220
-- Name: Customer_customer_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Customer_customer_id_seq" OWNED BY bitsei_schema."Customer".customer_id;


--
-- TOC entry 221 (class 1259 OID 30869)
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
-- TOC entry 222 (class 1259 OID 30876)
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
-- TOC entry 223 (class 1259 OID 30881)
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
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 223
-- Name: Invoice_invoice_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Invoice_invoice_id_seq" OWNED BY bitsei_schema."Invoice".invoice_id;


--
-- TOC entry 224 (class 1259 OID 30882)
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
-- TOC entry 225 (class 1259 OID 30888)
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
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 225
-- Name: Log_log_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Log_log_id_seq" OWNED BY bitsei_schema."Log".log_id;


--
-- TOC entry 226 (class 1259 OID 30889)
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
-- TOC entry 227 (class 1259 OID 30895)
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
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 227
-- Name: Owner_owner_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Owner_owner_id_seq" OWNED BY bitsei_schema."Owner".owner_id;


--
-- TOC entry 228 (class 1259 OID 30896)
-- Name: Password_Reset_Token; Type: TABLE; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE TABLE bitsei_schema."Password_Reset_Token" (
                                                      owner_id integer NOT NULL,
                                                      token character varying(128) NOT NULL,
                                                      token_expiry timestamp without time zone NOT NULL
);


ALTER TABLE bitsei_schema."Password_Reset_Token" OWNER TO bitsei_user;

--
-- TOC entry 229 (class 1259 OID 30899)
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
-- TOC entry 230 (class 1259 OID 30904)
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
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 230
-- Name: Product_product_id_seq; Type: SEQUENCE OWNED BY; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER SEQUENCE bitsei_schema."Product_product_id_seq" OWNED BY bitsei_schema."Product".product_id;


--
-- TOC entry 3212 (class 2604 OID 30905)
-- Name: BankAccount bankaccount_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount" ALTER COLUMN bankaccount_id SET DEFAULT nextval('bitsei_schema."BankAccount_bankaccount_id_seq"'::regclass);


--
-- TOC entry 3213 (class 2604 OID 30906)
-- Name: Company company_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company" ALTER COLUMN company_id SET DEFAULT nextval('bitsei_schema."Company_company_id_seq"'::regclass);


--
-- TOC entry 3217 (class 2604 OID 30907)
-- Name: Customer customer_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer" ALTER COLUMN customer_id SET DEFAULT nextval('bitsei_schema."Customer_customer_id_seq"'::regclass);


--
-- TOC entry 3218 (class 2604 OID 30908)
-- Name: Invoice invoice_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice" ALTER COLUMN invoice_id SET DEFAULT nextval('bitsei_schema."Invoice_invoice_id_seq"'::regclass);


--
-- TOC entry 3221 (class 2604 OID 30909)
-- Name: Log log_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log" ALTER COLUMN log_id SET DEFAULT nextval('bitsei_schema."Log_log_id_seq"'::regclass);


--
-- TOC entry 3223 (class 2604 OID 30910)
-- Name: Owner owner_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Owner" ALTER COLUMN owner_id SET DEFAULT nextval('bitsei_schema."Owner_owner_id_seq"'::regclass);


--
-- TOC entry 3225 (class 2604 OID 30911)
-- Name: Product product_id; Type: DEFAULT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product" ALTER COLUMN product_id SET DEFAULT nextval('bitsei_schema."Product_product_id_seq"'::regclass);


--
-- TOC entry 3399 (class 0 OID 30848)
-- Dependencies: 215
-- Data for Name: BankAccount; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--



--
-- TOC entry 3401 (class 0 OID 30854)
-- Dependencies: 217
-- Data for Name: Company; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Company" VALUES (2, 'BAROSCO', 2, '\x89504e470d0a1a0a0000000d4948445200000200000002000806000000f478d4fa000000097048597300000ec300000ec301c76fa8640000001974455874536f667477617265007777772e696e6b73636170652e6f72679bee3c1a0000200049444154789ceddd777814d5fec7f1efcc96745280002115144554541414f00a8a5d445111ec78bda2a2a25e2bb65c0ba0a228f682052b60c502620105012976406969340990deb6ccf9fd01f88310206577cf96f7eb797848363b673ed70b9ccf9cd99931044050534a39b66cd9d2d66eb7a71a86912a22493b7e25eff275a26118714aa95811891311a788248a887dc7efbb8a171147bdd7dc225259efb55211f18a489988b844a4ca308c6aa554d58e9fedfaab44444a95529b3d1e4f719b366d361b86e1f1c5ff7e00fe61e80e0044b28a8a8ab61e8f275d44324cd3cc544aa52ba5d20dc3e8a8944a350ca3ad88b4d59db3998a0dc3d8ac942a564aad330c63bd6118ebbc5e6f81611845369b6d5dab56adb6e80e09442a0a00e0474a295b595959a6881c20229d1bf83d4663bc60502322ab4564cd8edf578bc81aa5d4eaa4a4a442c3302cade980304601007ca4b8b838cde1701ca294ea6618c6cedf8f90ed4bf2683a97526ab56118cb9452cb0dc358e6f57a97a7a4a4fc69188657773820d451008026524a39b66ddbd6c534cd1e22d243447a188671a488c46a8e1629dc4aa95522b25444962aa596d6d6d6fe94969656ad3b18104a2800c03e28a56c2525258798a6d94b448e15919e22d255b67fb80ec1c32322cb456491882cb42cebc7e4e4e4e59c4200f68e0200ec62ebd6adadec767b5fa5541f11394e448e169104cdb1d03ce522b25844161a86f183cbe59ad7b66ddb0adda18060410140442b2e2e4e70381cbd44648052aaaf61183d65cf4be4101ebc4aa9bf0cc39827225f9ba6399bab1010c928008828454545310909097d45e4e41dbfba0b7f0f22952522bf8ac85722f2556262e23cc3306a35670202867ff810f64a4a4aba1b8671aa6c9ff0fb8a48b4e648084e3522325744beb22cebcb949494df750702fc890280b0a3948a2e2b2bebab941a6818c6392292a93b13425281887c6959d667c9c9c95fb13a807043014058282f2f6fedf57a0799a679b6526a8070ed3d7cab4a446629a5a69ba6393d3131719bee40404b510010b2cacbcbdb78bdde3344e4821d4bfc7c780f81e0554a2d1491694ea7736a5c5cdc46dd8180e6a00020a4545656b6f37abd4344648852aab78898ba3321a27945e407c330a69aa639352121a1587720a0b12800087a3b3eb97f9652ea328ef411c4bc4aa98586614c76bbddef72cf01043b0a00829252ca5e5151719a6559978ac840e1a139082dd52232dd308cb75ab56a35936717201851001054cacbcb0ff27abdc30cc3182e7c7a1f614029b55144a629a526a5a4a4fca63b0fb0130500daedb81bdf5011192edb6fbf0b8425a5d40f8661bce672b9a6a4a6a656eace83c846018036e5e5e5075b967585885c2d22c99ae30081542122ef2aa59e4b4e4efe55771844260a00024a29e52c2b2b1b24db27fd01baf3004160a988bc545151f1664646468dee30881c140004447171719ac3e1b84eb64ffc6d75e70182d066a5d44b4ea7f339ee2d8040a000c0af4a4b4b8f129111227299700f7ea0315c22f289699a135ab56ab5407718842f0a007c4e29659695950d564add6c18466fdd7980506518c63ccbb2262425257d6c1886a53b0fc20b05003ea394729696960e350ce32e113958771e208cac554a4d4c4a4a7a918712c157280068b1e2e2e204bbdd7ea56118b7894847dd798030f6b752ea051179323939b954771884360a009a6d63ee846c71448d8d1979d1e92292a83b0f10414aab9e7e7386515b7757dac3b716e80e83d0440140936d786a72a67bfe4f93ea66ce3dc9d1fb4823f9ddf1ba230111a764d8ade29ebb5439fbf7fa31ba7be78bda3f745b9eee4c082d76dd01103a363cf376967beee2572aef9f7892555a41790434b32aab8dda4f671feb9abb644dfe85372d8aedddfdd2d451c357e9ce85d04001c07e6dca7d3eb56ee5ea572a73279e656d2965e207828c555a61d44e99d1cb35eb87bff2cfbb61be23abddb08e4fdc53a43b17821b05007b557cfb2309152bf25e2b7fead573ad927253771e00fb6695941bb51fcceae34a49cc2f187ad38c8423ba5e9c72e78832ddb9109c2800d8835ab2c451f4ea674f97be3be3dfdea28dfc1901428cb5adccac796fc6997573966ccdbbf0a6b7b34fec7a953162845b772e04178eeab09ba25b1fb971edbf1f28ad7eeedd114cfe4068b33615dbeaa6ccb86ccd539f9417ddf0e01dbaf320b8f00f3c444464e3ade3ceac5dfcc7cb55e35feda03b0b00dff22c5f13ed59be66dcea23cfb9d5deed802bb2df1affb9ee4cd08f0210e1f2464f384296fe3ea5fcc9c95d94c7ab3b0e003ff2fcbca28df7f7959fe5f5bbe4175bbbe4f333a73cbd467726e843018850454f4c8df12efce1edbaa7df3c475554f1c97e2042288f57eae62c3ec26815bf2a7fc8a88fdded3a0f3bf0e91beb74e742e0f1198008b4eefaffdd5cf7fc2bdb6aa7ce3c97c91f884caabcd2a89d3af35c99fe5169fe6577dca23b0f028f021041d68d79e1b8b5275e965ff9ecbb4f785615f0685e00e22dd8105d3bf9e3c7d7f6ba60d3fa3bc6f1f4ce08c2298008b021f7c558f7ea5553abc6be74a6aaa8d21d07401072fdf85b3bf7ef2b7f587be6d55fc7a6b43aa7fd9be3f9c722ccb10210e6f286dd3cbceaf5295b6bdefa94c91fc03ea9ea5a717dfedd808a793f6f29baea9e7febce03ff6205204cad1839a6754c61e167b553661e2b96a53b0e8010e2cd5b175d35e9fd57d6f6bff426e99c31a0d32b63fed69d09bec70a40182a1876d3edb60f3edf54fbe96c267f00cda394b8662f3ad4f3c9ecf505436fba5f771cf81e05208ce45fff604efec9c397d7bc3be3116bd316567700b49855bccd56f3de8cdc357d871515df32a68bee3cf01d0a409828b8e89671ae773e5d5dfbd5fcaebab300083fee793fa5974efa6045fe9051afeace02dfa00084b875f73ede7ded71176eac79e7f33bac6d65fcff09c06fbc659566edd499c3d7f418fcf7badb1e3d42771eb40c1346082bbcf6fe076b5e7cff27d7825fdaebce022072b8972e4bad7ef5839fd65d73df78dd59d07c9c270e416b878d6a279bb67d5dfdfc7b87eace022032595b4b8dca17a6fc77ed09979c6b743ba04fce73b99b746742d3b00210620aafba67b8f7875f8a5cb31731f903d0cef5dde24eee8fbf292afcf7ddd7e9ce82a6610520442ceb7681333623f193ead73e3c4dbc3cb50f40f0b0366cb657bff6e1b379275e7659545cf489699fbe54ad3b13f68f1580109077c59dfd9c75a55beb66ce65f207109c2c4beabefdb157d55ff99b8bae7fb09fee38d83f0a4090cb1f7ee778d7875f7deb5d5d18af3b0b00ec8f7765415ccdeb1f7e5b78c55dcfe8ce827da30004a94dcf4f4ecd3ffb9a15b5af7df45f555ec9237b01840cabb2daa87efdc39179fd2e5dbd3af7f954dd79d0300a40102abaf1c1d32a27bc5d503b7df6c1bab3004073d5cd59d459bdf96151dec8ff0dd29d057ba2000499bcc12327d44cfae00bcfcafc68dd5900a0a5bc6b0a9deed73ffaa8e0a25b9ed29d05bbe32a8020b161e0d5b1b5a5950bea3efcfa70dd5900c097acaa1aa3e69dcf6f5cdbffd21363d3db1ddbfecdf13c9b3c08b00210048aaeb8e3b0ea55851b5c739732f903085baed98b0eadfcf1f78df9c3471fa53b0b2800da155e7efbf0dacfbefbd9f3675ea2ee2c00e06f9e95f909ae4fbe5e5c78f91dd7eace12e928001ae50f19f56af53b9fbfeadd5262d39d050002c5da5666d6bcfde973f9675efd81ee2c918c02a0c1d6dc89adf2065cb1a276eacce1e2f6e88e030001a73c5ea9fdfcbbc16b4fb8644dd1cdb929baf344220a4080e58fcc3d6adbb42f37d47dbd804bfc00443cd7778b3bd57db5a460dd2de3baebce126928000154745dee60d7075f2ff22e5b1da73b0b00040bcf1fabe26bde9abe346fc47dc37467892414800029b8f2ae3bab277ff2beb5a998f3fd00508f77f3569bfbade96f175c71e7fdbab3440a0a4000140cbdf9f5dac99f8c5595d5dcd21700f6c2aaaa316ade9c9e9b3f64d4bbbab344026e04e447aa5f3f7b5ecc01736adefba28fee2c001012bc5ea99d3a7368de69ffee9a9d50dec398368d47a0fa092b007e52f4efdc94b5d56df25c33e632f9034013d5cd9cd77d6d91515074f3135c21e02714003fc8bbe2ae83ebe62ec8772ffa3d5d7716000855ee85bf76ac9b31ab20ffba07bbeace128e28003e56f8df71fff27c3bff57cfcafc04dd590020d479fecc8b774fffead7829bc70ed09d25dc50007ca870f413436bdf9a3edb5bb8c9a93b0b00840befbabf1daeb73ef9b2f0a687b84cd08728003eb2ee9631d7d63effee3bd6df5bf96f0a003ee62d2e316b5efde8eda21b1fbc41779670c164e50345ffb9fb7f55cfbff79c5552ce657e00e027aabcd2a87e79dac4c2e177dead3b4b38a000b450e1e5773c57fdfac7f7a99a3add510020eca99a3aa97973fa436b075fffb4ee2ca18e02d002f9436ffeb07af227d72a1ee8030001a33c5e717dfccdf585675f3349779650460168a6fc0b467d5afbde17e78a52baa30040e4b12ca99e3efbcabcd3aee291c2cd440168868221a33eaf9d36f32cdd390020d2d5cd9c3b38efac119febce118a28004d5470e14d5fd54c9d7986ee1c0080edea3e9b7346fee091dfeace116a28004d5030ece66f6aa6cce066140010646a3ffcba7fc1f937ccd59d239450001a29ffdc91736bdefde244dd3900000dab797f56dfbc33fef383ee1ca18202d00879675efd43ed475ff7d59d0300b06f755f7cdf3bff8cff7ca33b4728a000ec47dea06b67d77dfe5d6fdd3900008d53fbc5f727e60dbce60bdd39821d05601ff287ddf251dd27dff6d39d0300d034759fce3e3dffec6ba7eace11cc28007b917fd5ddefd4bef7c539ba7300009aa7f6d3d917149c77fd4bba73042b0a40030a46dc37b1f6b58f8671931f0008614a49cdc7dffea7e0c2518fea8e128c2800f5e45f79d7c3b5af7e708378bdbaa300005acaeb95da0fbfbeade08a3befd71d25d850007651f89fbb6fad7dfbb3d1dcdb1f00c287727ba4e6bd2f720bfe3d7a94ee2cc18402b043e175b957d4befdd9a352e7d21d0500e06bb575523765c684a21b1eba5877946041011091a21b1e38a16eea8c495675ada13b0b00c03facca6aa3e6ede9930b6e7cf824dd598241c41780a29bc71e50fbe99c59de2da511ffdf0200c29db5adcc747f3ceb8ba211f71ca83b8b6e113de96dbbfa8ec4ba59739778f3d73b756701000486a77093b3f6fb9f96165c3b365977169d22b600a80b2eb095feb6f637cfb23589bab3000002cbbb624d8267c992df55bf7e76dd597489d80290b72d66a16be16f99ba730000f4702ffea3639e95b640770e5d22b200ac3dfdaa69ae6f7e3c5a770e00805eaeef971e9d77e6d5efeacea143c41580fcf36fb8c7f5e50fe7ebce0100080e7533e60ecdbfe8967b75e708b4882a00eb6ff8dfc0ba99f31e10cbd21d0500102c2c4b5cd367ffafe8860707eb8e1248115300368e79e190aa8f677fa82aabb9d61f00b01babb2daa8f960d6948df73ed14d779640898802503ce99384ea0fbe5c68156d8cd84f7b0200f6cddab0d95ef5c99c05c58f4c4ad09d2510226242ac9af2f122f7d2e511f17f2800a0f93cbffd9550d9fabb4522d25577167f0bfb15808273474ea99d35ff60dd390000a1a16ef68f07e79f7fc35bba73f85b581780fc0b6fbebe66faec21ba730000424bddc7df5c5c78f17fafd19dc39fc2b600140d1b75b8ebcb794f8ad7ab3b0a0020c4288f576a67cc7ba6e88a3b0ed39dc55fc2b200e45d911b5df7dbeaefacd2729bee2c0080d0646d2bb5b97efc6d6ed10537c7e8cee20f615900645dde7ccfb2d549ba630000429b7bc5da447759d91cdd39fc21ec0a40feb09bc7d77dbdf048dd390000e1c135eb879e8557dcf1b8ee1cbe165605a070d4c3a7d67d3ae716dd390000e1a576da97376fba65cc59ba73f852d81480bcdc67dbd77e3a7b3a77fa0300f89a555563544e9ffdfea6dce7537567f195b02900eadbf9f3adb5454edd390000e1c9b3ba30aa66ce0ff374e7f095b02800f9436e7ac6f5fd921cdd390000e1ad6ecee2030b2fb9f569dd397c21e40bc0faffdcddbbee8befaed39d030010196a3ef97664c135f7f6d19da3a542ba002cbb20d7593bffe72f38ef0f000814555165b8bf5b3a23ef8adc68dd595a22a40b406ce5ba59ee656b1275e700004416cf8a3509aa78c3a7ba73b444c81680a2cb6ebbaa6ed6fc1374e700004426d78cb903f22ebd3d649f17109205a070e8f569b55f2f788efbfc0300b4b12c71cdfae1e95517df9eae3b4a73846401f0e46f9ee7dd50ecd09d030010d9d4df5becb6bc82d9ba733447c81580bca1373fe15af82b97fc010082826bfe2f07e45ffcdf4775e768aa902a00450f3c7d98fbabf9a374e700006057ae2fbeffefa67b9f08a94707875401f07cb560a6b5b534a4320300c29f55526ed6ccfb65a6ee1c4d11329369d1f03b9faa9bbb344d770e00001a5237fbc7b4a2ffdcf3a4ee1c8d151205a0e8c6070fac993efb7add390000d897eaf7bfbc71e31de3bae9ced118215100dc8bff98c5d23f0020d8a99272a36ed1b290381510f4936afe9051f7b816fc92ad3b0700008d513b7b517afef03bc7ebceb13f415d00f2075fddc1fdcdc25cdd390000680ad767736ede70f7f82cdd39f625a80b802a73cdf06e2db5e9ce0100405358c525a6eba7159fe9ceb12f415b00d68dfcdfe575731675d79d030080e6a8fd72fea145373cf81fdd39f626280b809a3831aae6ab05cf73af7f0040c8b22c717dbde029f5da6b41f9d8e0a02c00850bf33ff4aecc8bd19d0300809670af581353f8cdf229ba733424e80ac0ba312f1c57fbd97767e8ce0100802fd47df2cdc0a2bb1eeba53b477d415700dc33beff409557ea8e0100804f581555867bfe6fd374e7a82fa80a40e1f03beea99bbbb483ee1c0000f892ebbb45198557df3b5a778e5d054d01d8f4d8e4b8ba6f17ddab3b070000fe50f7d5fcfb37e4be18ab3bc74e415300ea7efa798ab7608353770e0000fcc19bb7cee95ab5ea2ddd39760a8a025030eea56e7533e7f1c13f004058737d36fb9c0db94f1fac3b87489014006bee92f7ad927243770e0000fcc92aab34ea96fe3155770e912028001b6e7fe41cd7ac1f82a20d0100e06fae19730f2bbce9c17374e7d05e006ae72c7e45b93dba6300001010cae315cfe2652febcea1b500145d7d4fae6bd1efad7566000020d05c3ffcdca6f0aabbb55e16a8ad00a88913a36abf5e78a7aefd0300a0936bcea27bd58b2f3a74ed5f5b01285cb2ee45efdaa2285dfb07004027cfeac2e8750bf39ed1b57f2d0560ebc4b75ad57dfbe3c53af60d0040b0a8f9f2872b8b1f9994a063df5a0a40c5c29fdfb0d66db2ebd8370000c1c2dab0d95eb3ecaf5774ec3be00560ede827dbb9bffae1ec40ef17008060543773eef99b1e9b9c1ae8fd06bc00986b0adef5169768bffc10008060e0ddbccdacfbe997c981de6f4027e2bf1f9cd8d9f5e5dc7e81dc270000c1ae6ee6dc5336e43e9519c87d06b400d42c59fea6555ac12d7f0100d88555526ed4fdf257405701025600d6dd32a68bebab05c7066a7f00008412d7d70bfe5570f3d84e81da5fc00a80674dd16b56752d47ff0000344055561b2a7fdda440ed2f200560c343cf64d5cd5e745c20f6050040a872cdfef184f50f3d9b11887d05a400b87e5ffdba2aafe4e81f00807db04a2b0ccff23501b92f80df0bc0da875f6e57f7f58213fcbd1f0000c2816bd60f03368e99d8d6dffbf17b01b02d5bf18ada5acad13f00008de0dd526abafec87fd6dffbf16b01d8fcecd478d7ec45a7fb731f0000849bba6f179dbbe1eadc587feec3af05a06ec1a2e7bd1b8b6dfedc070000e1c6da546c7795963ce6cf7df8ad00a8175f74d47ebff8427f8d0f004038732ffcf54a25e2b753e87e2b00858b0b1ef6166e72f86b7c0000c299b77063f4ba0b47ddeaaff1fd56003c3f2d1fe1afb101008804ee9505a15500d6dff6e885ee9fff6ce58fb101008814ae9f57a4165e7dcf39fe18db2f05c0fdd3b231a2943f86060020a278d6148df1c7b83e2f0045a31f3bbc76de4f017b98010000e1cc357769d78d778cebe6eb717d5e003c2b0a9e913a97af8705002032b9dc52b766fd445f0febd302b03577622bcfbc257d7c3926000091cef5fd927ec58f4c4af0e5983e2d0095ab8b1ef3169704ec11c3000044026bf336b3ea8f3fc7fa724c9f4ed6ee9f970ff3e5780000603befef2b2ff1e5783e2b0085ff1d738e67d96a9f2e4f000080eddcbffc995838eac1b37d359ecf0a807745de03be1a0b0000ecc95abbee7e5f8de59302b061fc8b6ddcf37f3ed4176301008086b9e6fd7464d113afa4f8622c9f1400cf8a8247acd20abf3db0000000885825e5865abef6215f8ce59302e05abaec7c5f8c030000f6cdfdf39f3ef9c07d8b0b40d18d0f9ee6f9f52feefb0f004000b87f5a9e5470c383035a3a4e8b0b80b5baf001eefb0f0040802825d6daa2167ff0be4505404d9c1855b7e8b71e2d0d0100001acfb3f88f9e2a37d7d992315a54000a576cbbc3da52ca9dff00000820efe6adb6755bcc9b5a32468b266fefcafc2b5bb23d0000681ecfcabcab5bb27db30bc0c689afb6f52cfe3db3253b070000cde35ef06ba78d6326b66deef6cd2e00eedf56ffcf2aafe4da7f000034b02aaa0c77fe96bb9abb7db30b8067f91aaefd07004023eff255439bbb6db30a40f19897bab8972c6bf6b20300006839d7a2df3b14ddf9c401cdd9b65905a0e6cfd50f2a97bb399b0200001f512eb7581b3636eb0141cd2a00aedffe3aad39db010000dff2fcbeeacce66cd7e402b069f463877a7e5fc5ad7f01000802eedf56266fb86bfcc14dddaec905c0bdbef84ef17a9bba190000f007af573c1b8a6f6fea664d2f007fe5b3fc0f004010f1ac2a68f269802615800d4f4dce74fdbca27553770220f8d47db350eabe59a83b06001f702d5d96bafea167339ab24d930a80e78fbfee923a57d35201083aaaa24aca478d91b2eb1f12abb442771c002d55e7124fd1dfb7356593261500efaac2814d4b042018958f7e52bcebfe166b63b154dcf7b4ee38007cc0bb326f5053dedfe802b03577622bf7d265694d8f042098b8e62c929ab73efde7fb9ac99f48dd57f3352602e00bee25cb328a1f9994d0d8f737ba0054afdb74b35551c5bdff8110a62aaaa4ecfa874494daedf5b21bc7702a000871aaa2caa8fe6bf5c8c6bebfd105c05bf437f7fe0742dccea5fffa3815008407b57ef3858d7d6fa30b806bf99a26df640040f0a8bff45f1fa70280d0e7fe6b6db7c6beb75105a0e87f4f9f6e156db4373f12009df6b6f45f1fa70280d0e6cddfe0583f7afcc98d796fa30a805a5d7875cb2201d0696f4bfff5712a00087d9ef5c5ff69ccfb1a55003c6b0afbb42c0e005df6b7f45f1fa70280d0e65d53787c63deb7df02b075e25baddcbfad6cdbf2480002adb14bfff5712a00085d9e5fff6ab7f9d9a9f1fb7bdf7e0b40f55f6b46a8ca6adfa40210508d5dfaaf8f530140e8b22aaa8cba152b86efef7dfb2d009ec28d5cfe0784a0a62efdd7c7a9002074798b36edf772c0fd1600efca82c37c130740a03477e9bf3e4e0500a1c9bd32ff88fdbd679f05a0e0ce099d3cabf2637c170940203477e9bf3e4e0500a1c9bbb2206ec31de332f7f59e7d1600635bc9956259be4d05c0af5abaf45f1fa7028010e4f58aa7acfaf27dbd659f05c0dab4f954df2602e04fbe5afaaf8f530140e8b1366d397d5f3fdf6701f0e6adebeadb3800fca9fcae093e59faafcfda582c15f74ef4f9b800fcc793b7fed07dfd7caf05e0ef275f6ee7f92b3fcef79100f8836bce22a979fb33bf8d5ff3e6744e050021c4b3624dc2c689afeef53e3e7b2d00aebcbf872b97db3fa900f894bf96feebe3540010425c6ef1ae597fc9de7ebcd70260adfffb0cff2402e06bfe5afaaf8f53014068f16cdc32706f3fdb6b01f0e46dd8e7b90300c1c1df4bfff5712a00081ddebcf57bbd974f830560d905b94eef9f6b92fc1709802f046ae9bf3e4e0500a1c1b3624d6b75f5d58e867ed66001886f2b675b5535867f630168a9402dfdd7c7a9002034a8ca6a634354c7d31afa59c3a700ca2bcff26b22002d16e8a5fffa38150084066f79d5a0865e6fb000585b4b7bfa370e8096d0b5f45f1fa70280e0676d29edd5d0eb0d1780a24d59fe8d03a025742dfdd7c7a90020f879d76dca69e8f53d0a40f1a44f12dcab0b63fd1f094073e85efaaf8f53014070f3accc8fdb3c75767cfdd7f7280075bfff7e8ed4d605261580260996a5fffa381500042f555327aec58bf7782ec01e05406d293d253091003455b02cfdd7c7a90020b879fedef3c1407b1400efdf5b8f0a4c1c004d116c4bfff5712a00085e6af396a3ebbfb6470170afe3038040b009d6a5fffa38150004276fd1dfd9f55fdbad00a8d9b3ed56e1469e00080499605dfaaf8f53014070b20a37c6aba9536dbbbe66dff59bf55f2ef997aaaa096c2a204cb8972e93da4fe7f87c5c555513d44bfff5d5bcf5a918d15162c4c5f87cece881fdc4d1a39bcfc705c29d5551656cfe7d7d4f1159b0f3b5dd0a809455f60f7428205cd80fc894ead73e125516e14be04a49f5cbd37c3eac91102771a32ef5f9b840a4a8db5a3e40762900bb9d02f09494eff12101008d63242648dc8821ba6384adb86b878a99dc4a770c2064a992d26376fd7ef70f016e2b3d30a069803013377298188909ba63841d23214e62af1daa3b0610d2ac6de507edfafd6e05c0da58dc3eb07180f0c22a807f70f40fb49c776371daaedfff53009452a6a76023b700065a885500dfe2e81ff00d6ffefa38a594b1f3fb7f0ac0e6fb9eec6595571a0d6f06a0b15805f02d8efe01df50e59546c1ed8f1db9f3fb7f0a40eda6adffd21309083fac02f80647ff806f995b4bfafdf3f5ce2f8cea9aee5ad20061885500dfe0e81ff0b1daba7fe6fa7f0a80b7bcea003d6980f0c42a40cb70f40ff89e5555fdcfd57eff7f154079655a83ef06d02cac02b40c47ff80ef59a515e93bbffea700585b4b53f4c401c217ab00cdc3d13fe027dbcafe99eb4d1111a594e1ddb0395a5f22203cb10ad03c1cfd03fee1d9b4e59fcbfd4d11914d2fbc97656d2be31240c00f5805681a8efe01ffb1b6941a854fbf9b26b2a30078d714f4d41b09085fac02340d47ff801f2925929fd753644701b04a2b0fd79b08086fac02340e47ff80ff1915d5dd45761680f2ca83f6fd76002dc12a40e370f40ff89faaa83a5864e755003535195ad300118055807de3e81f080cabaa2653646701a8ae6dab350d10015805d8378efe81c0b06aea5245769e02a8ac4ed21b07880cac02348ca37f2080aab6cff9db0b4059258f0106028055808671f40f048e2aab8c13d97923a06da54ebd7180c8c12ac0ee38fa0702cbda6dae4dc8000020004944415456ee141131d5b2654eaba4dcdcdf0640b8b04acab5ee9f5580dd05c3d1bfee3f134020595b4b6c6ac91287b9ee836f0f526e8fee3c40c0940efdaf945e315a3cab0bb5656015603bdd47ffdec28d527ed358291976abb60c40a029975bb67cb72cc7b44a2b0fd61d060828cb92da8fbf912d3d2fd456045805d84ed7d1ffce897fcb51e749f5eb1f8be1f5063c03a053cd86f58798664d4d27dd41002d341781485f05d071f45f7fe2571e267e44a86a5727d3aaa9eba03b07a095a62210e9ab00813cfa67e20776675557a799a6c7db5a7710202868280291ba0a10a8a37f267ea06186d76a637addde14dd4180a012c02210a9ab00fe3efa67e207f6c3e34936c5ede1ee1b4043025404226d15c09f47ff4cfc40e3289727d1546e370500d8173f1781485b05f0c7d13f133fd0441e4f2bd370b9e374e70042821f8b40a4ac02f8fae89f891f6826973bc15435b531ba730021c50f45205256017c75f4cfc40fb48c55eb8a36554d6d94ee204048f2711108f755005f1cfd33f103be6155d7469b56752d0f02025ac2474520dc57015a72f4cfc40ff8584dadd394da3a87ee1c4058f0411108d75580e61efd33f1037e5253eb3095c7cb9300015f6a411108d75580a61efd33f103fea5bc96cd14af450100fca1994520dc56019a72f4cfc40f048661790d5379590100fcaa894520dc56011a73f4cfc40f0496727b4dd3f07874e7002243138a40b8ac02ecefe89f891fd0c4eb354ce5b50cdd398088d28822102eab007b3bfa67e207f4521eaf610a7ff1003df65304427d15a0a1a37f267e2048783c622a8f87150040a7bd1481505f05d8f5e89f891f082eac0000c1a4812210aaab003b8ffe99f881e0a4dc1eb1f3171208323b8a40dd677324fac2d325fa94de5233ed4bdda99a24fab4be5271cf53523b6506933e108c3c5eb1ebce00048ab76083544d7843dc3fafd01da55194c72b356f7fa63b46b3845a61712d5d2ea5578c96f8d1578bbd4bb6ee384040d80dbb4d94cbd29d03f09b9d137fcd5b9f72348a86ed5875a99d3e5ba2cfee4f1140f8b3dbc42e769b88cbad3b0ae0734cfc68328a002284e1b08bddb0db9512e14a00840d267eb418450061ceb0dbd4f61500200c30f1c3e728020853ca6e17bb613395b0028010c6c40fbfa30820cc98769bb22b3b1702203431f123e028020817369bb21b369b25223c11102183891fda510410e20c87cdb28bcde41a408404267e041d8a004294326d96ddb0db2800086a4cfc087a14018418c3665a76898e728b48b4ee30407d4cfc08391401848a9868b769c646bb74e7001ae2fa7e89d47db390c91fa1c7b2c4bd7499b87ffc4d7712a06131d12ebb11135da73b07d090984bcf96e8a16748dd07b3a4e29149e2cd5ba73b12b05fb6f4761277fdc51273c539624447e98e0334c88c8daeb51b31d135ba83007b6338ec123df40c893aef148a00821a133f428919edacb52b87bd527710607f280208564cfc08490e7bb9dd703a2b74e7001a8b228060c1c48f90e67054d8c56e2bd39d03682a8a007461e24738309d8e52bbe1304b7407019a8b22804061e24758b1dbb6d90d87638bee1c404b5104e02f4cfc084b36db56bb1113b541770ec0572802f015267e84b5b8d80d765b42dc1add39005fa308a0b998f81109ccb89855762329f14fdd41007fa108a0b198f811499c6d5bfd65b63fe5e85586c3ae3b0be0573b8b409b455324e985fbc59693ae3b1282842dbd9db41a778bb459324d62afb990c91f61cf88724adb630eca338da38f761b29893c1110118122809d98f811a96cad93bc46fffe1e5344c44c6ec5038110512802918b891f91ce4849ac1311b18b8898890955c223811181f88c40e4e01c3fb09dd12aae4a64470130e263cb44a4b5d64480461481f0c5c40fd4131f5726b2a300486ccc6611e9a4330f100c2802e183891f6898191db54964e72980d898221139566b22208850044217133fb06f666c5491c8ff9f02f84b6f1c203851044207133fd0384642dc9f223b0a804a8cff4d6f1c20b851048217133fd0346662c2cf223b0a80bd73f642bd7180d04011081e4cfc40331886b852db2d16113145443a8e1c5664b64e527a5301a183fb08e8c375fc40f3996d9254ceedc33789ec2800222266fb36b5fa2201a1e99f22f0e37be23cbe87ee3861cf797c0f69f3d3074cfc4033d9dab7a9def9f53f05c0d626799b9e3840e8b3b695896bf11fba63843df7a2df456d29d11d030859664ad2d67fbefee7d556f1ebb5a401c240f513af8bd4d6e98e11f6549d4baa9e7a53770c20641989f1ff7c68e99f0260c4c7aed11307086dde4d5ba46af274dd312246f5eb1f8bb561b3ee18404832e2a257effc7ad702f0ab9e384068e3e83fb05805009acf8889f967aeffa70044b76ffdbd9e3840e8e2e85f0f560180e6b15a27cfd9f9f53f0520f5819b7e345bc5732920d0041cfdebc12a00d0746662bcca7af4b69ffff97ee717866158f6ac0ed50d6f06a03e8efef5621500681a33ab63a56118ff1ce89bbbfed068d76653e02301a189a37fbd5805009ac6d6a1cdc65dbfdfad00d8da24af0c6c1c203471f41f1c5805001acf4c49dcedc17fbb1500498c5f1ad0344088e2e83f38b00a00349e9192bc68d7ef772b0066eba46f031b07083d1cfd0717560180c6315312bed9edfb5dbfe978f251738db898c02602420c47ffc185550060ffcc8438d5f1b08e7b5f0130faf7f7d8b23a56063616103a38fa0f4eac0200fb66cb4aab30860cf1eefa9ab9c79bd2530b021709082d1cfd07275601807d33d3dbe5edf15afd176ceddaf04140a0011cfd0737560180bdb3b56bbda4fe6b7b1680d4949981890384168efe831bab00c0de19a9c933eabfb66701e87ed474898e0a4c22204470f41f1a580500f664c44449fabf0edd7f01687fd9a9558e0333b92530b00b8efe4303ab00c09eec07e554190307ee31afef510044446ce9edf7f8b00010a938fa0f2dac0200bb33d3dbad69f0f506dfdd36655183af031188a3ffd0c22a00b03b7bdb94850dbdde6001305b2570b8030847ffa18a5500601709719f34f4b2bda117335c459faf8a8b5156558de1df54407073fff89b449fd657778c4653d5b55237eb07bf8c1d754a1f3162a3fd32b63fb816ff21d1834ed41d03d0ca888f55e929de590dfdacc10260bcf4927b4d8fc125d6d26529fe8d0604b7e8412786d424e22dd820c57e2a00ad1ebb556c59697e191b807fd8bb76de62e4e67a1afa59c39f0110117b76da1ffe8b040000fccd96ddf1b7bdfd6caf05c04ceff0857fe200008040b0776cfbd9de7eb6d702e0484f79cd703afc93080000f897d321929ab4d74b62f65a00dadf76ed66db41393c1910008010643fa47379fae8ebb7eeede77b2d002222b69cf415be8f040000fccd9edd719f9fe5db77016897f2a56fe300008040303ba4ee71ffffdd7ebeaf1f3a3ab69924369b6f13010000ffb2d9c46c97f2fabedeb2cf02d021f7e67c1e0c04004068b11d945d959e7be3ba7dbd679f054044c4d62567afd710020080e0e3e892fdf3fedeb3df0260a6b77fdf377100004020981ddbbdb7dff7ecef0d313d8e78c94888f34d220000e05766429c8a3ea4eb1bfb7ddffeded0f6df832a1c877729f64d2c0000e04ff6ee5dfe4e1d3964bff7f1d96f011011b177ce98d7f2480000c0df6c9d32bf6bccfb1a5900325f6c591c00001008b6f4762f37e67d8d2a0069f7dff0a53dab83bb65910000803fd9723abad3c7fcf79bc6bcb751054044c43ca8d39fcd8f040000fccdde257b9fb7ffdd55a30b803dabc3d4e6c50100008160746cff6e63dfdbe802107d44f7278d56f1aa79910000803f19ade2556c66c7e71bfbfe461780d491432a1d47755ddfbc580000c09f1c47772b4ccd1db9dfcbff766a74011011b11f9433bde991000080bf39ba647fdc94f737a9001819696324cad9b4440000c0bfa2a344ba747eac299b34a90064dc73ed7ae7515db92b20000041c4d9e3904d19b75cdea4d3f44d2a002222f62e39339aba0d0000f01f5be78ccf9aba4dd30b404efa58c36e6bea660000c00f0c875dccccf4479bba5d930b405aee0d7f3a0eeb52dad4ed000080efd90f3f685bc643a3563575bb2617001111fba15d6636673b0000e05bf6433a3779f95fa49905c0e8d2e93e713a9ab3290000f09528a744774efb5f73366d5601c8b8f79a55cea30fdddc9c6d0100806f441d73e88676b937af6dceb6cd2a002222f6433a4d6beeb60000a0e51c87746ef4bdffeb6b7601303ba5dd6ff26c000000b4305bc52bc96e3fb6d9db3777c3f4d1d76f751c73684173b7070000cd673fb6fb9af4d1d76f6deef6cd2e002222b6833b4f6ac9f60000a079ec5db25f6cd1f62dd938e3f0768fac6ed726d7fbf716ee0c0404015b467b49cdffda2f639bade2fc322e80a6b3a5b6f666b4b126b6648c16150063c40877fe99572ff17efe5daf968c03c0474c53cca404dd2900f899e3986e0b8ddc5c574bc668d12900111147d79c7bc4305a3a0c0000680cc310c9c9bcafa5c3b4b800741c7fd7d78e230e2e6be938000060ff9c47772bc97ee6de6f5b3a4e8b0b808888fda86e537d310e0000d837fb915ddff1c5383e2900b623bbdc65b64ee29e000000f8919992a8544edabd3e19cb1783a45f7ff956c7b1dd7ff3c5580000a061cee37bfc9475d77525be18cb2705404424ba6ba7bbf9302000007e6218220766ddefabe17c56003a8cbff373c7a10796fb6a3c0000f0ff1c471c5c9a3dfececf7d359ecf0a808888e3c883dff6e5780000603be7e15d26fb723c9f1680e85ec7dc6eb66b6df9724c0000229dd9aeb5e538f4d0d13e1dd39783a58e1c52e9ec73d45c5f8e090040a48beed7f3dbf6b75d56e5cb317d5a004444a20f3ff07a898ef2f5b00000442423264aec871e78a3afc7f57901689f3bea0fe7f13d56fb7a5c00002251d4093dff4cbb77e40a5f8febf3022022e23ca2eb682e090400a0850c439c471ce4d373ff3bf9a500a43f76fb34c7518794fa636c00002285f39843b7a68dbbed237f8ced97022022e238fad0e7fd3536000091c071d4214ffb6b6cbf15808c0ee67db6acb4163dab1800804865cb49af4b3ff1d087fc35bedf0a80919beb71f63dea5d7f8d0f0040388bee7bd41bc690215e7f8defb7022022e238b0cb75b6b4548f3ff7010040b8b17568eb751c7ed82dfedc875f0b405aee886a47bf9e9ff9731f0000841be780e33ef4f58d7feaf36b011011893ab6dbd5b6d4146e0f0c004023d8da265bceae1923fdbd1fbf17800e375e59ececdf73b6bff703004038883aa5f7ac0ea36f2cf6f77efc5e0044446c5969579889f12a10fb0200205499c9ad94d1f5c0ab02b2af40ec24fdd13bd639faf59c17887d010010aa9cfd7bcdceb8e7daf581d857400a80888899d6ee0a232e86550000001a6024c429674efb2b03b5bf801580ace773d7469ddcfb8740ed0f0080501275f27173d21ebfa72050fb0b5801101189eed1f53233b915ab000000ecc24c4954f6c30fba2ca0fb0ce4cedadf7b435ed4297dbe09e43e0100087651a7f6f9223df7c67581dc67400b808888b37ba78bcc76adb92f0000002262a6a65851477509d8b9ff7ff61be81d76187d6371d449c7fae5d1860000849ae8d3fa4e697fdbb59b03bddf8017001191a8238fb8dc96d9c1ad63df0000040b5b56077774af63aed6b16f2d05a0fd6d975545f5ef3959c7be010008168e138f7b2575e4904a1dfbd652004444327262afb377cea8d5b57f000074b21f985593756cf6285dfbd756008cdc5c97b37fcf7b74ed1f00009decbd8fbad7183142dbe9706d05404424f395318f3b7b1ebe496706000002cd716cf7f5d96f8c7b5c6706ad054044c4d6fda04b0d875d770c000002c36197a8c30fbe54770ced0520ebe587be76f6efb554770e00000221eaa46317a6bff4c06cdd39b4170011116fc70ee79a4909dc1c080010d6cc9444cb9edaea7cdd394482a4001cf0dac345cefec74ed19d0300007f8aea77cce48cc91302f2b8dffd098a02202252656f73853da7638dee1c0000f8832dbb634d71815bcb4d7f1a123405a0dbb45c97f3b823efd69d0300007f70feabc75d472f7d2968ee821b3405404424f39dc727388fef51a43b070000be14f5afa3f3b2263ff694ee1cbb0aaa022022623fe9b8c166ab78a53b070000be6026c429e3a84382e2837fbb0aba0290997bc392a8b3fa7da63b070000be107576ff0fb29fbcfb27dd39ea0bba02202292794eaff31ddd0ea8d29d03008096701c9c53957960c2c5ba733424280b80316488cb71dc11ff119b4d771400009ac73425aacf91d71ab9b92edd511a129405404424f39587df8dea77cccfba730000d01c51fd7b2e4e9f34f64ddd39f626680b80888844196798ad93bcba630000d014669b644f6c6ad240dd39f625a80b40ce17af6f8a3eb9f77dba730000d0148e7e3d1f6af7ee537febceb12f415d00444432df9b30c6d9e7a8b5ba730000d018cebe47adcc797fe2ff74e7d89fa02f00222266d7ce03ccd6893c2c080010d4cc9444cb7150d699ba733446481480ec571eca8b3aef94c775e70000605fa2069d343e63d2b8d5ba7334464814001191ac971ebadd71c23141f104250000ea73f6ef5998f5dad83b74e768ac9029002222b1271d7baaad4d12a702000041c54c49b4628eed7ebaee1c4d115205a0c37dd72f730eecff88ee1c0000ec2aeacc131eed30f6d6e5ba7334454815001191acd7c68d76f63e2224ceaf0000c29fb36f8fbfb2de7cec2edd399a2ae40a808888d1b1f5f1665adba079a632002032991d53dd7107679fa03b4773846401c899f6dca6e8937b8fe0590100005d0cbb4da206f4beaadd2b6382fa863f7b139205404424f38d475f8b3ab5cfb7ba7300002253d4e9c7cfcc7ae391c9ba733457c816001191eab88ea73bba752ed39d03001059ecdd0e28cdeaa0ced69da32542ba00749b96ebb2f5ea7eba111fab74670100440623214e45f7eb7586f1d24b21fd59b4902e002222d9af8e5d107de6091375e700004486e841274d4c7ff6be05ba73b454c817001191ac294fde14d5afe71add390000e12daa7fcfbfb2de7aec26dd397c212c0a808848f429c71c673f28a756770e004078b21f9059eb39f698e375e7f095b029001d46df581c7356ffb38d84383e0f0000f029232e46c59ed267d081636f2cd69dc557c2a6008888747cfc8eafa2ce3b79ac1886ee2800807061181275de29e3d39ecb9da53b8a2f8555011011c97efd91bb9d038e5bac3b0700203c449fda777ef6e4476fd79dc3d7c2ae008888d42465f5b51f7a6089ee1c0080d066efdaa94cb54f3f49770e7f08cb02d06d5aaecb76f461bdcd36c95edd590000a1c9d63ac9ebec7d64df9cd773c3f203e66159004444725e1ffba7e384a3af33ec3c2f0000d03486c32eced3fa5c9b3969cc1fbab3f84bd8160011919c0f9e7929eaacfe6feace0100082dd1e70e989cf5f6132febcee14f615d004444b23f7ef6b2a8937b2fd79d0300101aa24eedf35bd6d4a72ed79dc3dfc2be008888c49d737a2f478f43ca75e7000004377bf783cae38eebd147778e40888802903a724865cc79a7f5b26576f0e8ce0200084e66c7769ed881fd8f4dcd1d59a93b4b204444011011491b3de2cff8c1279fc39d020100f599f1b12afadc932f487be8e615bab3044ac4140011910e4fdefd79f4d9fdef141b5706000076304d719e7de27d99cfdcfbb1ee2881145105404424ebedc71f759edcfb6ddd390000c121ea8ce3dfc97ee7f18774e708b4882b0022229d66be7249d480e37ed49d0300a057d4c9bde7e77cf6d2c5ba73e81091054044243bb9aa8ff3b8230a74e70000e8e1ecdba3303ba9f25fba73e812b105c09836cd9bd035eb30c7615d4a756701000496bdfbc165497d0e3fdc98362d626f191fb1054044a4edab8f56987d7a1c69cbee58a73b0b0020306c9d32eaec27f73d32e5913bcb7467d129a20b808848ce0bb9f9ce41270eb0b54db674670100f897999a6239ceea7762f6f8dbf27467d12de20b808848d653f7cc8b1e7ad665667c2cf70800803065c4c7aad8a1a70fcb9e78cf7cdd5982010560878ca7ef793bf69281d79ab1d19400000837d151123deccc51e913ef9baa3b4ab0a000ec22fd85075e8c1a76e65de274e88e0200f015875d62ce3bf9aeac971f7a5a7794604201a8276bd29847a2079ff2a061e76e810010f26c3689bbe0d447b2de7e7c9cee28c18602d080ecf79eb82f7af080a7c5e43f0f00842cc39098c1035eca78e7893b75470946cc707b913575e28d31e79cf8a6ee1c0080e6891ad86f6ad6b4892374e7085614807dc8faf0d9cba2ce38e153dd3900004d1335b0df2739d35fb850778e604601d88f9c2f5e3adb794a9f59ba7300001a27e6ecfe5fe57cfae239ba73043b0a4023749af5eaa931c3cef856770e00c0be459f3b604ed6f4174ed19d231450001a29ebdd0927459f7fca0cdd3900000d8bb9f0f4b9d91f3ddb5f778e5041016882ecf79f3e2366c8e99febce0100d85dcc05a77e9d35e5c9887db25f7350009a286bea9367459f73e207ba73000044c43024fa82533fca9a36f164dd51420d05a019b23f7efefce8c127bf2686a13b0a00442ec390e84127bd933d6de260dd51421105a099b23f7ce6cae8b34e98c4cd82004003d394a8b34f7c31fbe3672fd61d2554317bb540f6a72f5e157dde298f190ebbee280010391c76891d7afaf89c4f9ebb46779450460168a1ec694fdd1e73c9c05b78943000f89f111b2d31970c1c9df9ce13b7e9ce12ea28003e90f9dab809b1979f33dc486e450900003f3113e355f46583aec97a6ddc58dd59c20105c047d29fbdff8db811175c686bdfd6ab3b0b00841bb36db2157be5e0a1592f3cf0a2ee2ce18202e043e9e36e9fe6bcf8ac7eb6ac3497ee2c00102e6ce9ed5cd1834fea9f3ee1eea9bab384130a808f653d7ee7bc98534e38cc7e504eb9ee2c0010eaec07772a8f1ed0ebd0cc171ffe5e7796704301f083f4977357269f726c86b3cf51f9bab30040a872f43cbcd0766cafac8cd71f5ba53b4b38a200f849eba773cb734e3ea873d439277da73b0b00849aa8137bfdd8294b3ae5bc9e5baa3b4bb8e202763f3272732d11e9977fe1a857eb3efc7ab8727b74470280e066b349ccd9fd26677df4dce5baa3843b560002207bca5357465d3ae87623218ecb0401602fccb8181533e4d4bb98fc0383021020d9af8e792ce6b241e7da3ab465190000ea31535b7be32f3af3bcac77278cd39d255250000228f3d9fb3f891976d6d18ec3ba54e8ce0200c1c2d1fda0f2e8a1671e91f6f2c31fe9ce124928000196fec49dbf9ae70d4e8d1ed87fa1ee2c00a05bf4c9bd57249c7b6ac7cc8977ffa13b4ba4a1006890933bbc36fbd3178e8bbd64e08b12e5d41d070002cfe990986167bc91fdd56b87a4e68eacd41d2712510034ca7c6bfc353143cfb8dc6c93cced8301440c5b6a6b6fecf0c15766bd3be10add5922190540b3ac371e991c75e609dd1d8774e65a570061cfd1ed8012c7a97d0fcb7cf181d77467897414802090f5c623cbcceaea0ecee3ba2fd29d0500fcc57962af9fcdb66dd3b2df7c7485ee2ca000048d9cfc39b59d164ced157be9a0b146ab78ee1700206c98f1b12a6ae819133a7d3bf9a89c39afd7eace83ed28004126f3cd4747c70f1f7caaad4b367f4900843cfbc19d6aa3475e7c7ace7b136ed19d05bba30004a18e4fddfd55cc8d17a5470fecb75c771600682ee7bf8e5e153bf4a48ccc476efd527716ec890210a4d2afbf7c6bf6a72f768bfbf7e047392500209418f1b12a6ef8b9133b7dff7697b4dc5bb7e8ce83865100825cc6a4b177449dd9bf8fed80cc72dd5900607fec07e594470d39ad4fc66be346e9ce827da3008480ec77c72f704525b58d3aedf89962b3e98e03007bb2d9c479faf13fd83ab46f97fdead805bae360ff781c7088e8b66c9a4b96c9e9f917df7a9167ee92573d851ba374670200111133adad3bba7fcf6b32df7ee255dd59d078ac008498ecb7c7bfe31e784ac7a8d3fafeaa3b0b00384f38666dab8b06a533f9871e0a4008eafaece8ad3933271d117bd579f7986d932ddd7900441eb37592153b7cf0439dbe7bab73fbf1b76dd69d074d4701086199af8c79b8d535438e701e77c426dd5900440efb71476c48b8fa82c3325f1b7bafee2c683e0a40886bffe02dbf775a30a543f4e5e74c305312590d00e0376662bc1535e8a4170f5830a56387b1b7729f921047010813d96f3c724becad57758d3ab5cf2add5900841f67ef230b622f19d835e793e7aed19d05bec1550061247df4d52b45a4cbba1b1f7ca8e6fd59777a376ce69a41002d62a6b5753b4f3c6e74f65b8f8d97f9bad3c09758010843e913efbdc77ed395e951834e5cca7d0300348b694ad40947ff9270eaf11db3df7a6cbcee38f03d5600c254ceedc33789c8d17957dc7985f7fbc5cf79d6ae8bd19d094068b077cea8893ea9d7b5e92f3dfc867ca73b0dfc8515803097f3fab8d7a3ae1fd13ae6a2b33e3513e278a60080bd3262a3c579daf15f461d7558ebf4971e7e43771ef8172b001120e39621352272f6bad1138e70fdb0f403d7f74b3a89a20b00d8c130c4d9bf6781fd5fc79c9f997bc312dd711018ac004490f43137ffd2e9bbb73ac7df36fc5a7bb7036a74e701a09ffdc0eceaf81143aeebf4ede46c26ffc842018840e98fdef142e7fb2f4f8a1b71c1db664a224b014004325bc55b51e70e98dab97f5652fa0b0f3caf3b0f028f530011ca1832c42522976cbc63dcd8da9ffe9ae69afd6357e5f1ea8e05c0cf0cbb4d9c7d7b2c55b1cef3723e7ab640771ee8430188701d1eb97399881c5274d3c3a7bb96fcf1927bde4fe9ba3301f00ffb915db7380f3de892cc371ff9527716e84701808888643c79f70c11c928baedd11b5cb3e68d75fffa579cee4c007cc37e48e76a47df1e7765bdf4e044f959771a040b0a007693f1d8ed4fabd9b39f2f9a327b62ede7dfffc72adac89f112044d93ab4f5440f38eed5f481c75c670c19c2393eec867fdcb107a37f7f8f885cb739f7d9dbab7ff9e355d7dc9fceb3b696f28151204498ad93aca853fb7e1e7fec9197b4bef192727953772204230a00f62a357764a5880c597deb63a9511b36bde2faea8733bdc5251401204899c9ad94f3c463e7db32db5d9831e19ef5f28eee4408661400ecd701e36fdb2c2267af1dfd643b677ed1cb75b3e69d656d293574e702b09d9994a09c271d37df7970d6c5690fdfca27fbd1281400345aa73137fd2d2267e7ddf6687b5b5ed1abb55fcd3f4d4428028026667cac72f6eff56354b7ac611dc6dd95af3b0f420bff78a3d936dc312e53e2e3c7c6de70c9692292a23b0f1041b6564f7c73862a2d1bddf1897b8a74874168a200a0c59452d1a5a5a5430cc3182d2207e9ce0384b14d4aa917955213525252ca748724ae66d900000468494441544168a300c0679452664949c9998661dc6918466fdd798030b25a29f54c5252d20b8661d4e90e83f04001805f6cdbb6adaf699aff1591b385674e00cd35dbb2ac09c9c9c99f1986c1733be0531400f85569696927a5d428c330ae129158dd798010e012914f0cc3783c3131f147dd6110be280008888a8a8ab696655dab941a2922a9baf30041e86fa5d40b0e87e3f9f8f8f8bf758741f8a30020a09452ceb2b2b2412272b5889c24fc1904968ac84b1515156f666464d4e80e83c8c13fbed0a6bcbcfc20cbb2868bc85522d25a771e2080ca44648a6559cfa4a4a4fcae3b0c22130500da6ddab4292e2a2aea02d334af544af515fe5c223c2911f95e29f56a4d4dcdfb696969d5ba0321b2f10f2d82cad6ad5b336c36db45223242447274e7017c60bd88bc6518c62b898989ab75870176a200202829a5ccf2f2f2932dcbbad4308c7344244e7726a0092a94521f1b86f1666262e237866158ba0301f5510010f49452d1252525279ba679a9880c1211a7ee4c4003bc22325b29f5a6dbedfe303535b5527720605f28000829e5e5e5ad2dcb3a5f448688c8092262d31c0991cd2322734464aa61181f2426266ed39c0768340a0042565959598a65596789c80586619c22ac0c2030bc4aa9852232cde170bcc735fb08551400848592929224d334072aa50689c8292292a03b13c24ab9887c6918c674afd7fb290fe24138a00020ec28a56c252525c799a679966c7f164157dd991092d68ac8d796657d969c9cfca561182edd81005fa20020ec9597971fecf57a4f330ce364d9feb901ae2840432a9552df89c857369b6d46ab56ad56ea0e04f8130500114529e52c2d2dedbde33303278bc891c20709239557447e1291594aa9af9292921670948f4842014044dbbc7973bcd3e93c5629d5d7308c3e22f22fe1c384e1ca2b22bf28a57e300c639e6118dff0a97d44320a00b08b1d85a0f78e42d04b447a8948a2ee5c68965211f95129b5d0308c79b5b5b50bdab76f5fa53b14102c2800c03e28a5cccacaca833d1e4f2fc3308e95ed85e0101171688e86ddb9456499ec98f0ed76fb8ff1f1f17f1a86a1740703821505006822a59463dbb66d5d4cd3ec21223d44a4876118dd45245e73b4485129227f29a5968bc852a5d4d2aaaaaaa53c4a17681a0a00e023c5c5c5690e87e310a55437c33076fe7eb8704f82e6aa534aad310c6399526ab96118cbbc5eeff294949415dc5b1f68390a00e0474a2963dbb66de9a6691e60184667113960c7afce3b7e457a392897edd7dbafdef16b8d526ab56559ab535252d6b3840ff80f0500d0a8b4b434d9b2ac74bbdd9e6559568688a42ba5320cc34813917622d276c72f536bd0a6b344a478c7afbf95521b0cc328129122d334d7793c9e02c3308a9293934bf5c6042217050008724a295b5555555b97cbd5d634cdb622922222493b7f29a5924424c9308c581149300c235a291523db6f78e494ed5731ec5a20a24524a6de6e6a44a47697ef2d11291311978854198651ad94aa93ed8fb9ad169152c3304a65fb27ed4b76fe6e5956b1d3e92c8e8b8bdbcc323d10dcfe0fbe3c6854f5ec36890000000049454e44ae426082', 'Marco Barosco srl', '05104990287', 'BRSMRC71A11L349X', 'Via sant''apollonia 4', 'Trebaseleghe', 'PD', '35010', 'TQ4KZH ', true, true, 0, 'marco.barosco@pec.it');


--
-- TOC entry 3403 (class 0 OID 30863)
-- Dependencies: 219
-- Data for Name: Customer; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Customer" VALUES (1, 'CONTARINA S.P.A', '02196020263', 'CZZMRC99L23B563P', 'Via Vittorio Veneto 6', 'Lovadina di spresiano', 'TV', '31036', 'mirco.cazzaro@studenti.unipd.it', 'mirco.cazzaro@pec.it', 'X2PH38J', 2);
INSERT INTO bitsei_schema."Customer" VALUES (2, 'ECIPA SOCETA'' CONSORTILE S.r.l', '02289210276', 'ZNNFBA00L11L781Z', 'Via della Pila 3/b', 'Marghera', 'VE', '30030', 'fabio.zanini@studenti.unipd.it', 'fabio.zanini@pec.it', 'M5UXCR1', 2);
INSERT INTO bitsei_schema."Customer" VALUES (3, 'ELEVEN S.r.l', '04845150269', 'MRCCRS00L11D325V', 'Via Del Credito 27', 'Castelfranco Veneto', 'TV', '35010', 'chrisitan.marchiori@studenti.unipd.it', 'christian.marchiori@pec.it', 'SUBM70N', 2);
INSERT INTO bitsei_schema."Customer" VALUES (4, 'FVG SERVIZI cooperativa sociale onlus', '01766260937', 'SHMFZD99L11Z224O', 'Via Zardini 3', 'Fiume Veneto', 'PN', '32156', 'farzad.shami@studenti.unipd.it', 'farzad.shami@pec.it', 'KRRH6B9', 2);
INSERT INTO bitsei_schema."Customer" VALUES (5, 'SAFE ENGINEERING SRL', '04331980278', 'MRTMRC00L10H620Z', 'Corte Marin Sanudo 5', 'Venezia', 'VE', '30001', 'marco.martinelli.4@studenti.unipd.it', 'marco.martinelli@pec.it', '2LCMINU', 2);


--
-- TOC entry 3405 (class 0 OID 30869)
-- Dependencies: 221
-- Data for Name: Invoice; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Invoice" VALUES (1, 1, 0, '1', '7/12/2021', 'Try.mp3', 1, '9/12/2023', 'Nullam.xls', 'AtLoremInteger.avi', 340, 100, 4, false);
INSERT INTO bitsei_schema."Invoice" VALUES (2, 2, 0, '2', '5/1/2022', 'DignissimVestibulum.mp3', 2, '1/12/2023', 'Nullam.xls', 'AtLoremInteger.avi', 168.3, 15.0, 4.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (3, 3, 0, '3', '8/12/2022', 'NuncNisl.gif', 3, '5/6/2022', 'VestibulumSed.tiff', 'UltricesVelAugue.jpeg', 929.7, 49.8, 3.5, false);
INSERT INTO bitsei_schema."Invoice" VALUES (4, 4, 0, '4', '9/12/2022', 'Ultrices.xls', 4, '12/3/2022', 'EgetCongueEget.pdf', 'CubiliaCurae.xls', 851.6, 41.5, 6.4, false);
INSERT INTO bitsei_schema."Invoice" VALUES (5, 5, 0, '5', '12/3/2023', 'SagittisDui.mp3', 5, '6/2/2022', 'IaculisJustoIn.avi', 'InHacHabitasse.tiff', 392.0, 51.1, 4.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (6, 1, 0, '6', '8/5/2022', 'NullamSitAmet.mpeg', 6, '9/1/2022', 'FuscePosuereFelis.xls', 'AeneanAuctor.avi', 909.5, 64.9, 6.3, false);
INSERT INTO bitsei_schema."Invoice" VALUES (7, 2, 0, '7', '6/1/2022', 'LiberoUtMassa.avi', 7, '1/4/2023', 'Primis.gif', 'HabitassePlateaDictumst.pdf', 271.2, 29.4, 1.1, false);
INSERT INTO bitsei_schema."Invoice" VALUES (8, 3, 0, '8', '6/9/2022', 'InterdumVenenatisTurpis.mp3', 8, '11/8/2022', 'Vel.avi', 'ElementumPellentesque.mp3', 180.7, 7.9, 8.5, false);
INSERT INTO bitsei_schema."Invoice" VALUES (9, 4, 0, '9', '7/3/2022', 'PedePosuere.png', 9, '5/5/2022', 'SedVel.avi', 'VelNullaEget.tiff', 598.1, 52.6, 4.2, false);


--
-- TOC entry 3406 (class 0 OID 30876)
-- Dependencies: 222
-- Data for Name: Invoice_Product; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Invoice_Product" VALUES (1, 4, 4, 80, 20, 'aaaaa', '2023-05-29');


--
-- TOC entry 3408 (class 0 OID 30882)
-- Dependencies: 224
-- Data for Name: Log; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--



--
-- TOC entry 3410 (class 0 OID 30889)
-- Dependencies: 226
-- Data for Name: Owner; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Owner" VALUES (2, 'Marco', 'Barosco', 'mbarosco711', '$2a$12$k35bfJfhaDtvx7atNqqhNeStM14VVNMyGBJZgYYMwqNTCFKOfG0Gi', 'mirco.cazzaro.1999@gmail.com', NULL, 3);


--
-- TOC entry 3412 (class 0 OID 30896)
-- Dependencies: 228
-- Data for Name: Password_Reset_Token; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--



--
-- TOC entry 3413 (class 0 OID 30899)
-- Dependencies: 229
-- Data for Name: Product; Type: TABLE DATA; Schema: bitsei_schema; Owner: bitsei_user
--

INSERT INTO bitsei_schema."Product" VALUES (1, 2, ' Aggiornamento Antincendio Alto Rischio', 50, 'aaa', 'hr   ', 'Svolto ai sensi dell’ art. 71 - 73  del D.Lgs n.81/2008 e s.m.i. e Allegato VI e Allegato A punto 6 dell'' Accordo 53/CSR del 22 febbraio 2012');
INSERT INTO bitsei_schema."Product" VALUES (2, 2, ' Aggiornamento Piattaforme Elevatrici con Stabilizzatori', 40, 'aaa', 'hr   ', 'Svolto ai sensi dell’ art. 36 – 37 – 43 comma 1 lettera b - 46 del D.Lgs n.81/2008 e s.m.i. D.M 10 Marzo 98');
INSERT INTO bitsei_schema."Product" VALUES (3, 2, ' Corso Carroponte', 80, 'aaa', 'hr   ', 'Svolto ai sensi dell’ art. 36 – 37 – 43 comma 1 lettera b - 46 del D.Lgs n.81/2008 e s.m.i. D.M 10 Marzo 98');
INSERT INTO bitsei_schema."Product" VALUES (4, 2, ' Corso Piattaforme Elevatrici senza Stabilizzatori', 20, 'aaa', 'hr   ', 'Svolto ai sensi dell’ art. 71 - 73  del D.Lgs n.81/2008 e s.m.i. e Allegato VI e Allegato A punto 6 dell'' Accordo 53/CSR del 22 febbraio 2012');


INSERT INTO bitsei_schema."BankAccount" VALUES (1, 'IT25674893201252722926', 'Intesa San Paolo', 'ISP', 2);
INSERT INTO bitsei_schema."BankAccount" VALUES (2, 'RS22589574115252722926', 'Antonveneta', 'MPS', 2);
INSERT INTO bitsei_schema."BankAccount" VALUES (3, 'NO7354026602729', 'Unicredit', 'Uni', 2);
INSERT INTO bitsei_schema."BankAccount" VALUES (4, 'LB6194303ZRZFRZCSIUA9VSAA4IL', 'Banca Popolare Emilia-Romagna', 'BPER', 2);


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 216
-- Name: BankAccount_bankaccount_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."BankAccount_bankaccount_id_seq"', 30, true);


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 218
-- Name: Company_company_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Company_company_id_seq"', 30, true);


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 220
-- Name: Customer_customer_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Customer_customer_id_seq"', 30, true);


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 223
-- Name: Invoice_invoice_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Invoice_invoice_id_seq"', 30, true);


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 225
-- Name: Log_log_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Log_log_id_seq"', 30, true);


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 227
-- Name: Owner_owner_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Owner_owner_id_seq"', 30, true);


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 230
-- Name: Product_product_id_seq; Type: SEQUENCE SET; Schema: bitsei_schema; Owner: bitsei_user
--

SELECT pg_catalog.setval('bitsei_schema."Product_product_id_seq"', 30, true);


--
-- TOC entry 3227 (class 2606 OID 30913)
-- Name: BankAccount Account_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount"
    ADD CONSTRAINT "Account_pkey" PRIMARY KEY (bankaccount_id);


--
-- TOC entry 3230 (class 2606 OID 30915)
-- Name: Company Company_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company"
    ADD CONSTRAINT "Company_pkey" PRIMARY KEY (company_id);


--
-- TOC entry 3232 (class 2606 OID 30917)
-- Name: Customer Customer_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer"
    ADD CONSTRAINT "Customer_pkey" PRIMARY KEY (customer_id);


--
-- TOC entry 3237 (class 2606 OID 30919)
-- Name: Invoice_Product Invoice_Product_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Invoice_Product_pkey" PRIMARY KEY (invoice_id, product_id);


--
-- TOC entry 3235 (class 2606 OID 30921)
-- Name: Invoice Invoice_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice"
    ADD CONSTRAINT "Invoice_pkey" PRIMARY KEY (invoice_id);


--
-- TOC entry 3239 (class 2606 OID 30923)
-- Name: Log Log_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log"
    ADD CONSTRAINT "Log_pkey" PRIMARY KEY (log_id);


--
-- TOC entry 3241 (class 2606 OID 30925)
-- Name: Owner Owner_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Owner"
    ADD CONSTRAINT "Owner_pkey" PRIMARY KEY (owner_id);


--
-- TOC entry 3243 (class 2606 OID 30927)
-- Name: Password_Reset_Token Password_Reset_Token_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Password_Reset_Token"
    ADD CONSTRAINT "Password_Reset_Token_pkey" PRIMARY KEY (owner_id, token);


--
-- TOC entry 3245 (class 2606 OID 30929)
-- Name: Password_Reset_Token Password_Reset_Token_token_key; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Password_Reset_Token"
    ADD CONSTRAINT "Password_Reset_Token_token_key" UNIQUE (token);


--
-- TOC entry 3247 (class 2606 OID 30931)
-- Name: Product Product_pkey; Type: CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product"
    ADD CONSTRAINT "Product_pkey" PRIMARY KEY (product_id);


--
-- TOC entry 3233 (class 1259 OID 30932)
-- Name: fki_Company; Type: INDEX; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE INDEX "fki_Company" ON bitsei_schema."Customer" USING btree (company_id);


--
-- TOC entry 3228 (class 1259 OID 30933)
-- Name: fki_c; Type: INDEX; Schema: bitsei_schema; Owner: bitsei_user
--

CREATE INDEX fki_c ON bitsei_schema."BankAccount" USING btree (company_id);


--
-- TOC entry 3250 (class 2606 OID 30934)
-- Name: Customer Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Customer"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3256 (class 2606 OID 30939)
-- Name: Product Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Product"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3248 (class 2606 OID 30944)
-- Name: BankAccount Company; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."BankAccount"
    ADD CONSTRAINT "Company" FOREIGN KEY (company_id) REFERENCES bitsei_schema."Company"(company_id);


--
-- TOC entry 3251 (class 2606 OID 30949)
-- Name: Invoice Customer; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice"
    ADD CONSTRAINT "Customer" FOREIGN KEY (customer_id) REFERENCES bitsei_schema."Customer"(customer_id);


--
-- TOC entry 3254 (class 2606 OID 30954)
-- Name: Log Invoice; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Log"
    ADD CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id);


--
-- TOC entry 3252 (class 2606 OID 30959)
-- Name: Invoice_Product Invoice; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Invoice" FOREIGN KEY (invoice_id) REFERENCES bitsei_schema."Invoice"(invoice_id) ON UPDATE CASCADE;


--
-- TOC entry 3255 (class 2606 OID 30964)
-- Name: Password_Reset_Token Owner; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Password_Reset_Token"
    ADD CONSTRAINT "Owner" FOREIGN KEY (owner_id) REFERENCES bitsei_schema."Owner"(owner_id);


--
-- TOC entry 3249 (class 2606 OID 30969)
-- Name: Company Owner; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Company"
    ADD CONSTRAINT "Owner" FOREIGN KEY (owner_id) REFERENCES bitsei_schema."Owner"(owner_id);


--
-- TOC entry 3253 (class 2606 OID 30974)
-- Name: Invoice_Product Product; Type: FK CONSTRAINT; Schema: bitsei_schema; Owner: bitsei_user
--

ALTER TABLE ONLY bitsei_schema."Invoice_Product"
    ADD CONSTRAINT "Product" FOREIGN KEY (product_id) REFERENCES bitsei_schema."Product"(product_id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2023-06-02 15:21:37

--
-- PostgreSQL database dump complete
--

