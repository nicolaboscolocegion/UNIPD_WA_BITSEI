package it.unipd.dei.bitsei.rest.documentation;

import java.io.*;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


import it.unipd.dei.bitsei.dao.documentation.GenerateInvoiceDAO;
import it.unipd.dei.bitsei.utils.mail.MailManager;
import it.unipd.dei.bitsei.resources.*;

import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.telegram.BitseiBot;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import static it.unipd.dei.bitsei.utils.ReportClass.exportReport;
import static org.apache.taglibs.standard.functions.Functions.trim;


/**
 * Generates the invoice in pdf and xml formats.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GenerateInvoiceRR extends AbstractRR {


    private final String absPath; //String absPath = super.getServletContext().getRealPath("/");
    private int invoice_id;
    List<Object> out;

    RestURIParser r = null;

    /**
     * Creates a GenerateInvoice entity.
     *
     * @param req     the HTTP request.
     * @param res     the HTTP response.
     * @param con     the connection to the database.
     * @param absPath the absolute path of the project
     * @param r       the RestURIParser
     */
    public GenerateInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, String absPath, RestURIParser r) {
        super(Actions.GENERATE_INVOICE, req, res, con);
        this.absPath = absPath;
        this.r = r;
    }


    /**
     * Generates the invoice in pdf and xml formats.
     */
    @Override
    protected void doServe() throws IOException {

        invoice_id = r.getResourceID();

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());
        // model
        int invoice_id = 0;
        Double iva = 0.0;
        Double ritenuta = 0.0;

        Message m = null;

        try {

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            // creates a new object for accessing the database and stores the customer
            //String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            java.util.Date utilToday = new Date();
            java.sql.Date today = new java.sql.Date(utilToday.getTime());
            String fileName = "invoice_" + UUID.randomUUID() + ".pdf";

            out = new GenerateInvoiceDAO(con, this.invoice_id, today, fileName, owner_id, r.getCompanyID()).access().getOutputParam();


            String separator = FileSystems.getDefault().getSeparator();
            Map<String, Object> map = new HashMap<>();
            map.put("company_logo", absPath + "company_logos" + separator + "user_logo_sample.png");
            map.put("stamp", absPath + "jrxml" + separator + "stamp.png");
            map.put("bitsei_logo", absPath + "company_logos" + separator + "bitsei_1024_gray_multi.png");

            map.put("company_name", out.get(3));
            map.put("company_address", out.get(4));
            map.put("company_city_postalcode_province", out.get(5));
            map.put("company_mail", out.get(6));
            map.put("company_vat", out.get(7));
            map.put("company_tax", out.get(8));
            map.put("company_pec", out.get(9));
            map.put("company_unique_code", out.get(10));

            Invoice i = (Invoice) out.get(2);
            //map.put("invoice_date", i.getWarning_date());
            map.put("invoice_date", "01/01/2023");
            //map.put("invoice_number", i.getWarning_Number());
            map.put("invoice_number", "0110");

            Customer c = (Customer) out.get(1);
            map.put("customer_name", c.getBusinessName());
            map.put("customer_address", c.getAddress());
            map.put("customer_city_postalcode_province", c.getCity() + " " + c.getPostalCode() + " (" + c.getProvince() + ")");
            map.put("customer_mail", c.getEmailAddress());

            //depends on company fiscal type
            String riferimentoNormativo = "Operazione effettuata ai sensi dell'art. 1 commi da 54 a 89 Legge n. 190/2014 - Regime forfettario";
            //depends on company fiscal type

            map.put("footer", riferimentoNormativo);

            List<DetailRow> ldr = (List<DetailRow>) out.get(0);

            double total_related_price = 0;
            for (DetailRow dr : ldr) {
                double rp = dr.getRelated_price_numeric();
                total_related_price += rp;
            }
            if (total_related_price > 0) {
                ldr.add(new DetailRow("Costi accessori", "", 1, "", total_related_price, 0, ""));
            }

            double total = 0;
            for (DetailRow dr : ldr) {
                total += dr.getTotalD();
            }

            int fiscal_company_type = (int) out.get(11);
            if (fiscal_company_type == 0 || fiscal_company_type == 1 || fiscal_company_type == 2) {
                if (total >= 77.47) {
                    ldr.add(new DetailRow("Imposta di bollo", "", 1, "", 2, 0, ""));
                    total += 2;
                }
                if (i.getPension_fund_refund() > 0) {
                    ldr.add(new DetailRow("Rivalsa INPS", "", 1, "", Math.round(total * i.getPension_fund_refund()) / 100, 0, ""));
                    total += Math.round(total * i.getPension_fund_refund() / 100);
                }
            }

            if (fiscal_company_type == 0) {
                ldr.add(new DetailRow("TOTALE", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
            } else {
                iva = (double) (Math.round(total * 22) / 100);

                ldr.add(new DetailRow("Totale imponibile", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
                ldr.add(new DetailRow("Iva (22%)", "", 1, "€", iva, 0, ""));
                ldr.add(new DetailRow("Totale fattura", "", 1, "€", (Math.round(total * 100) / 100) + iva, 0, ""));
                if (fiscal_company_type == 1) {
                    ritenuta = (double) Math.round(total * 22) / 100;
                    ldr.add(new DetailRow("Ritenuta d'acconto (20%)", "", 1, "€", ritenuta * (-1), 0, ""));
                    ldr.add(new DetailRow("Totale da pagare", "", 1, "€", (Math.round(total * 100) / 100) + iva - ritenuta, 0, ""));
                }
            }

            //generate invoice
            exportReport(ldr, absPath, "/jrxml/Invoice.jrxml", fileName, map);


            for (Iterator<DetailRow> it = ldr.iterator(); it.hasNext(); ) {
                DetailRow dr = it.next();
                if (dr.getProduct_description().equals("TOTALE") || dr.getProduct_description().equals("Totale imponibile") || dr.getProduct_description().equals("Iva (22%)") || dr.getProduct_description().equals("Totale") || dr.getProduct_description().equals("Ritenuta d'acconto") || dr.getProduct_description().equals("Totale da pagare")) {
                    it.remove();
                }
            }
            String owner_email = req.getSession().getAttribute("email").toString();

            //XML
            Document el_invoice = DocumentHelper.createDocument();
            Element pFatturaElettronica = el_invoice.addElement("p:FatturaElettronica");
            pFatturaElettronica.addAttribute("versione", "FPR12");
            pFatturaElettronica.addAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
            pFatturaElettronica.addAttribute("xmlns:p", "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2");
            pFatturaElettronica.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            pFatturaElettronica.addAttribute("xsi:schemaLocation", "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2 http://www.fatturapa.gov.it/export/fatturazione/sdi/fatturapa/v1.2/Schema_del_file_xml_FatturaPA_versione_1.2.xsd");

            Element fatturaElettronicaHeader = pFatturaElettronica.addElement("FatturaElettronicaHeader");
            Element datiTrasmissione = fatturaElettronicaHeader.addElement("DatiTrasmissione");
            Element idTrasmittente = datiTrasmissione.addElement("IdTrasmittente");
            idTrasmittente.addElement("IdPaese").addText("IT"); //owner piva
            idTrasmittente.addElement("IdCodice").addText((String) map.get("company_vat")); //owner piva
            datiTrasmissione.addElement("ProgressivoInvio").addText(i.getInvoice_number()); //invoicenumber
            datiTrasmissione.addElement("FormatoTrasmissione").addText("FPR12"); //formatoinvio
            datiTrasmissione.addElement("CodiceDestinatario").addText((String) map.get("company_unique_code")); //owner uniquecode
            Element contattiTrasmittente = datiTrasmissione.addElement("ContattiTrasmittente");
            contattiTrasmittente.addElement("Email").addText(owner_email); //owner email

            Element cedentePrestatore = fatturaElettronicaHeader.addElement("CedentePrestatore");
            Element datiAnagrafici = cedentePrestatore.addElement("DatiAnagrafici");
            Element idFiscaleIVA = datiAnagrafici.addElement("IdFiscaleIVA");
            idFiscaleIVA.addElement("IdPaese").addText("IT"); //owner piva
            idFiscaleIVA.addElement("IdCodice").addText((String) map.get("company_vat")); //owner piva
            datiAnagrafici.addElement("CodiceFiscale").addText((String) map.get("company_tax")); //owner tax code
            Element anagrafica = datiAnagrafici.addElement("Anagrafica");
            anagrafica.addElement("Denominazione").addText((String) map.get("company_name")); //company business name
            if ((Integer) out.get(11) == 0) {
                datiAnagrafici.addElement("RegimeFiscale").addText("RF19"); //company fiscal type (forfettario)
            } else {
                datiAnagrafici.addElement("RegimeFiscale").addText("RF01"); //company fiscal type (ordinario con/senza ritenuta)
            }
            Element sede = cedentePrestatore.addElement("Sede");
            sede.addElement("Indirizzo").addText((String) map.get("company_address")); //company address
            sede.addElement("CAP").addText((String) out.get(12)); //company postal code
            sede.addElement("Comune").addText((String) out.get(13)); //company city
            sede.addElement("Provincia").addText((String) out.get(14)); //company province
            sede.addElement("Nazione").addText("IT");
            Element contatti = cedentePrestatore.addElement("Contatti");
            contatti.addElement("Email").addText(owner_email);//owner email

            Element cessionarioCommittente = fatturaElettronicaHeader.addElement("CessionarioCommittente");
            Element cDatiAnagrafici = cessionarioCommittente.addElement("DatiAnagrafici");
            Element cIdFiscaleIVA = cDatiAnagrafici.addElement("IdFiscaleIVA");
            cIdFiscaleIVA.addElement("IdPaese").addText("IT"); //customer piva
            cIdFiscaleIVA.addElement("IdCodice").addText(c.getVatNumber()); //customer piva
            Element cAnagrafica = cDatiAnagrafici.addElement("Anagrafica");
            cAnagrafica.addElement("Denominazione").addText(c.getBusinessName()); //customer businessname
            Element cSede = cessionarioCommittente.addElement("Sede");
            cSede.addElement("Indirizzo").addText(c.getAddress()); //customer address
            cSede.addElement("CAP").addText(c.getPostalCode()); //customer postal code
            cSede.addElement("Comune").addText(c.getCity());//customer city
            cSede.addElement("Provincia").addText(c.getProvince()); //customer province
            cSede.addElement("Nazione").addText("IT");


            Element fatturaElettronicaBody = pFatturaElettronica.addElement("FatturaElettronicaBody");

            Element datiGenerali = fatturaElettronicaBody.addElement("DatiGenerali");
            Element datiGeneraliDocumento = datiGenerali.addElement("DatiGeneraliDocumento");
            datiGeneraliDocumento.addElement("TipoDocumento").addText("TD01"); //type document: invoice
            datiGeneraliDocumento.addElement("Divisa").addText("EUR");
            datiGeneraliDocumento.addElement("Data").addText(i.getInvoice_date().toString()); //invoice date
            datiGeneraliDocumento.addElement("Numero").addText(i.getInvoice_number()); // invoice number
            if ((Integer) out.get(11) == 0) {
                if (i.getTotal() >= 77.47) {
                    Element datiBollo = datiGeneraliDocumento.addElement("DatiBollo");
                    datiBollo.addElement("BolloVirtuale").addText("SI"); //invoice hasstamp
                    datiBollo.addElement("ImportoBollo").addText("2.00"); //stamp: std default price
                }
            }
            if ((Integer) out.get(11) == 1) {
                Element datiRitenuta = datiGeneraliDocumento.addElement("DatiRitenuta");
                datiRitenuta.addElement("TipoRitenuta").addText("RT01");
                datiRitenuta.addElement("ImportoRitenuta").addText(String.format(Locale.UK, "%.2f", ritenuta));
                datiRitenuta.addElement("AliquotaRitenuta").addText("20.00");
                datiRitenuta.addElement("CausalePagamento").addText("A");
            }

            datiGeneraliDocumento.addElement("ImportoTotaleDocumento").addText(String.format(Locale.UK, "%.2f", total + iva));
            datiGeneraliDocumento.addElement("Causale").addText("N. " + i.getInvoice_number() + " del " + i.getInvoice_date() + " riferita all'avviso n. " + i.getWarning_number() + " del " + i.getWarning_date()); // built from invoice number + invoice date + warning number + warning date
            Element datiOrdineAcquisto = datiGenerali.addElement("DatiOrdineAcquisto");
            datiOrdineAcquisto.addElement("RiferimentoNumeroLinea").addText("1"); //CHECK
            datiOrdineAcquisto.addElement("IdDocumento").addText(i.getInvoice_number());//invoice number
            datiOrdineAcquisto.addElement("NumItem").addText("1"); // check

            Element datiBeniServizi = fatturaElettronicaBody.addElement("DatiBeniServizi");
            //FOREACH INVOICE_PRODUCT ENTRY
            Integer k = 0;

            for (DetailRow dr : ldr) {
                k++;
                Element dettaglioLinee = datiBeniServizi.addElement("DettaglioLinee");
                dettaglioLinee.addElement("NumeroLinea").addText(k.toString()); //row counter
                dettaglioLinee.addElement("Descrizione").addText(dr.getProduct_description()); //product description
                dettaglioLinee.addElement("Quantita").addText(dr.getNumericQuantity()); //invoice_product quantity
                if (dr.getMeasurement_unit() != "") {
                    dettaglioLinee.addElement("UnitaMisura").addText(dr.getMeasurement_unit()); //product measurement unit
                }
                dettaglioLinee.addElement("PrezzoUnitario").addText(String.format(dr.getNumericUnit_price())); //invoice_product unit price
                dettaglioLinee.addElement("PrezzoTotale").addText(String.format(Locale.UK, "%.2f", dr.getTotalD())); //quantity * unit_price
                if ((Integer) out.get(11) == 0) {
                    dettaglioLinee.addElement("AliquotaIVA").addText("0.00"); //depends on company fiscal type
                    dettaglioLinee.addElement("Natura").addText("N2.2"); //to specify ONLY in company fiscal type is FORFETTARIO
                } else {
                    dettaglioLinee.addElement("AliquotaIVA").addText("22.00"); //depends on company fiscal type
                }

            }

            //end foreach
            Element datiRiepilogo = datiBeniServizi.addElement("DatiRiepilogo");
            if ((Integer) out.get(11) == 0) {
                datiRiepilogo.addElement("AliquotaIVA").addText("0.00"); // depends on company fiscal type
                datiRiepilogo.addElement("Natura").addText("N2.2"); //to specify ONLY in company fiscal type is FORFETTARIO
            } else {
                datiRiepilogo.addElement("AliquotaIVA").addText("22.00"); // depends on company fiscal type
            }
            datiRiepilogo.addElement("ImponibileImporto").addText(String.format(Locale.UK, "%.2f", total)); // total of all detail line above
            if ((Integer) out.get(11) == 0) {
                datiRiepilogo.addElement("Imposta").addText("0.00"); // depends on company fiscal type
                datiRiepilogo.addElement("RiferimentoNormativo").addText(riferimentoNormativo); //depends on company fiscal type
            } else {
                datiRiepilogo.addElement("Imposta").addText(String.format(Locale.UK, "%.2f", iva)); // depends on company fiscal type
                datiRiepilogo.addElement("EsigibilitaIVA").addText("I");
            }


            Element datiPagamento = fatturaElettronicaBody.addElement("DatiPagamento");
            datiPagamento.addElement("CondizioniPagamento").addText("TP02"); //standard (immediate payment)
            Element dettaglioPagamento = datiPagamento.addElement("DettaglioPagamento");
            dettaglioPagamento.addElement("ModalitaPagamento").addText("MP05"); //standard: bonifico
            dettaglioPagamento.addElement("ImportoPagamento").addText(String.format(Locale.UK, "%.2f", total + iva - ritenuta)); //total of all rows above + taxes
            dettaglioPagamento.addElement("IBAN").addText((String) out.get(15)); //bankaccount iban selected

            StringWriter sw = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(sw, OutputFormat.createPrettyPrint());
            xmlWriter.write(el_invoice);
            xmlWriter.close();
            FileWriter f = new FileWriter(absPath + "xml" + separator + "IT" + c.getVatNumber() + "_" + trim(i.getInvoice_number().toString()) + ".xml");
            f.write(sw.getBuffer().toString());
            f.close();


            //sending mail with attachment notification to customer
            MailManager.sendAttachmentMail(c.getEmailAddress(), "New invoice from " + map.get("customer_name"), "Dear " + c.getBusinessName() + "\na new invoice has been sent from " + map.get("customer_name") + " to you. Please do not reply to this message.", "text/html;charset=UTF-8", absPath + "/pdf/" + fileName, "application/pdf", fileName);

            //sending mail notification to company owner
            MailManager.sendMail(req.getSession().getAttribute("email").toString(), "New invoice for " + map.get("customer_name"), "Attention: the invoice " + fileName + " has been sent to " + c.getBusinessName() + "(" + c.getEmailAddress() + ").", "text/html;charset=UTF-8");

            //sending telegram notification to company owner
            String telegram_chat_id = (String) out.get(12);
            if (telegram_chat_id != null && !telegram_chat_id.equals("")) {
                BitseiBot bt = new BitseiBot();
                bt.sendMessageWithAttachments((String) out.get(12), "New invoice for " + map.get("customer_name") + "\n\nAttention: the invoice " + fileName + " has been sent to " + c.getBusinessName() + "(" + trim(c.getEmailAddress()) + ").", absPath + "/pdf/" + fileName);
            }


            res.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException ex) {
            LOGGER.error("Cannot generate invoice: unexpected error while accessing the database." + ex.getStackTrace());
            m = new Message("Cannot generate invoice: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("Owner not parsable.", "E5A1", ex.getMessage());
            LOGGER.info("Owner not parsable." + ex.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (JRException e) {
            m = new Message("Error on generating Invoice PDF file.");
            LOGGER.info("Error on generating Invoice PDF file. " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (MessagingException e) {
            m = new Message("Error on sending email.");
            LOGGER.info("Error on sending email. " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}


