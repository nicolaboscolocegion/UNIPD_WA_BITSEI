package it.unipd.dei.bitsei.rest.documentation;

import java.io.*;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


import it.unipd.dei.bitsei.dao.documentation.CloseInvoiceDAO;
import it.unipd.dei.bitsei.dao.documentation.GenerateInvoiceDAO;
import it.unipd.dei.bitsei.resources.*;

import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import static it.unipd.dei.bitsei.utils.ReportClass.exportReport;

/**
 * Creates a new customer into the database.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GenerateInvoiceRR extends AbstractRR {


    private final String absPath; //String absPath = super.getServletContext().getRealPath("/");
    private int invoice_id;
    List<Object> out;
    /**
     * Creates a new customer
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GenerateInvoiceRR(HttpServletRequest req, HttpServletResponse res, Connection con, String absPath) {
        super(Actions.CLOSE_INVOICE, req, res, con);
        this.absPath = absPath;
    }


    /**
     * creates a new customer
     */
    @Override
    protected void doServe() throws IOException {

        String uri = req.getRequestURI();
        String id = uri.substring(uri.lastIndexOf('/') + 1);
        if (id.isEmpty() || id.isBlank()) {
            throw new IOException("invoice id cannot be empty.");
        }

        invoice_id = Integer.parseInt(id);

        InputStream requestStream = req.getInputStream();

        LogContext.setIPAddress(req.getRemoteAddr());
        // model
        int invoice_id = 0;


        Message m = null;



        try {

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            // creates a new object for accessing the database and stores the customer
            //String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            java.util.Date utilToday = new Date();
            java.sql.Date today = new java.sql.Date(utilToday.getTime());
            String fileName = "invoice_" + UUID.randomUUID() + ".pdf";

            out = new GenerateInvoiceDAO(con, this.invoice_id, today, fileName, owner_id).access().getOutputParam();


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
                String riferimentoNormativo = "Operazione senza applicazione dell'IVA ai sensi delle Legge 190 del 23 Dicembre 2014 art. 1 commi da 54 a 89. Operazione effettuata ai sensi dell’art. 1, commi da 54 a 89 della Legge n. 190/2014 – Regime forfettario. Il compenso non è soggetto a ritenute d’acconto ai sensi della legge 190 del 23 Dicembre 2014 art. 1 comma 67.";
            //depends on company fiscal type

            map.put("footer", riferimentoNormativo);

            List<DetailRow> ldr = (List<DetailRow>) out.get(0);

            double total = 0;
            for (DetailRow dr : ldr) {
                total += dr.getTotalD();
            }

            int fiscal_company_type = (int) out.get(11);
            if (fiscal_company_type == 0 || fiscal_company_type == 1 || fiscal_company_type == 2) {
                if (total >= 77.47) {
                    ldr.add(new DetailRow("Imposta di bollo", "", 1, " €", 2, 0, ""));
                    total += 2;
                }
                if (i.getPension_fund_refund() > 0) {
                    ldr.add(new DetailRow("Rivalsa INPS", "", 1,  "€", Math.round(total*i.getPension_fund_refund())/100, 0, ""));
                    total += Math.round(total*i.getPension_fund_refund()/100);
                }
            }

            if (fiscal_company_type == 0) {
                ldr.add(new DetailRow("TOTALE", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
            }
            else {
                ldr.add(new DetailRow("Totale imponibile", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
                ldr.add(new DetailRow("Iva (22%)", "", 1, "€", Math.round(total * 22) / 100, 0, ""));
                total += Math.round(total * 22) / 100;
                ldr.add(new DetailRow("Totale", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
                if (fiscal_company_type == 1) {
                    ldr.add(new DetailRow("Ritenuta d'acconto", "", 1, "€", Math.round(total * 22) / -100, 0, ""));
                    total += Math.round(total * 22) / -100;
                    ldr.add(new DetailRow("Totale da pagare", "", 1, "€", Math.round(total * 100) / 100, 0, ""));
                }
            }


            //generate invoice
            exportReport(ldr, absPath, "/jrxml/Invoice.jrxml", fileName, map);


            //XML
            Document el_invoice = DocumentHelper.createDocument();
            Element pFatturaElettronica = el_invoice.addElement("p:FatturaElettronica");

            Element fatturaElettronicaHeader = pFatturaElettronica.addElement("FatturaElettronicaHeader");

            Element datiTrasmissione = fatturaElettronicaHeader.addElement("DatiTrasmissione");
            Element idTrasmittente = datiTrasmissione.addElement("IdTrasmittente");
            idTrasmittente.addElement("IdPaese").addText("IT"); //owner piva
            idTrasmittente.addElement("IdCodice").addText("05104990287"); //owner piva
            datiTrasmissione.addElement("ProgressivoInvio").addText("12"); //invoicenumber
            datiTrasmissione.addElement("FormatoTrasmissione").addText("FPR12"); //CHECK
            datiTrasmissione.addElement("CodiceDestinatario").addText("SUBM70N"); //owner uniquecode
            Element contattiTrasmissione = datiTrasmissione.addElement("ContattiTrasmissione");
            contattiTrasmissione.addElement("Email").addText("formazionesicurezza@marcobarosco.it"); //owner email

            Element cedentePrestatore = fatturaElettronicaHeader.addElement("CedentePrestatore");
            Element datiAnagrafici = cedentePrestatore.addElement("DatiAnagrafici");
            Element idFiscaleIVA = datiAnagrafici.addElement("IdFiscaleIVA");
            idFiscaleIVA.addElement("IdPaese").addText("IT"); //owner piva
            idFiscaleIVA.addElement("IdCodice").addText("05104990287"); //owner piva
            datiAnagrafici.addElement("CodiceFiscale").addText("BRSMRC71E11L736V"); //owner tax code
            Element anagrafica = datiAnagrafici.addElement("Anagrafica");
            anagrafica.addElement("Denominazione").addText("BAROSCO MARCO"); //company business name
            datiAnagrafici.addElement("RegimeFiscale").addText("RF19"); //company fiscal type
            Element sede = cedentePrestatore.addElement("Sede");
            sede.addElement("Indirizzo").addText("VIA SANT'APOLLONIA N 4"); //company address
            sede.addElement("CAP").addText("35010"); //company postal code
            sede.addElement("Comune").addText("Trebaseleghe"); //company city
            sede.addElement("Provincia").addText("PD"); //company province
            sede.addElement("IT");
            Element contatti = cedentePrestatore.addElement("Contatti");
            contatti.addElement("Email").addText("formazionesicuezza@marcobarosco.it");//owner email

            Element cessionarioCommittente = fatturaElettronicaHeader.addElement("CessionarioCommittente");
            Element cDatiAnagrafici = cessionarioCommittente.addElement("DatiAnagrafici");
            Element cIdFiscaleIVA = cDatiAnagrafici.addElement("IdFiscaleIVA");
            cIdFiscaleIVA.addElement("IdPaese").addText("IT"); //customer piva
            cIdFiscaleIVA.addElement("IdCodice").addText("02315870283"); //customer piva
            Element cAnagrafica = cDatiAnagrafici.addElement("Anagrafica");
            cAnagrafica.addElement("Denominazione").addText("ROTALCAR"); //customer businessname
            Element cSede = cessionarioCommittente.addElement("Sede");
            cSede.addElement("Indirizzo").addText("Antoniana 328"); //customer address
            cSede.addElement("CAP").addText("35011"); //customer postal code
            cSede.addElement("Comune").addText("Campodarsego");//customer city
            cSede.addElement("Provincia").addText("PD"); //customer province
            cSede.addElement("Nazione").addText("IT");




            Element fatturaElettronicaBody = pFatturaElettronica.addElement("FatturaElettronicaBody");

            Element datiGenerali = fatturaElettronicaBody.addElement("DatiGenerali");
            Element datiGeneraliDocumento = datiGenerali.addElement("DatiGeneraliDocumento");
            datiGeneraliDocumento.addElement("TipoDocumento").addText("TD01"); //type document: invoice
            datiGeneraliDocumento.addElement("Divisa").addText("EUR");
            datiGeneraliDocumento.addElement("Data").addText("2023-03-24"); //invoice date
            datiGeneraliDocumento.addElement("Numero").addText("12"); // invoice number
            Element datiBollo = datiGeneraliDocumento.addElement("DatiBollo");
            datiBollo.addElement("BolloVirtuale").addText("SI"); //invoice hasstamp
            datiBollo.addElement("ImportoBollo").addText("2.00"); //stamp: std default price
            datiGeneraliDocumento.addElement("Causale").addText("N. 12 del 24 marzo 2023 riferita all'avviso n. 193 del 19 marzo 2023"); // built from invoice number + invoice date + warning number + warning date
            Element datiOrdineAcquisto = datiGenerali.addElement("DatiOrdineAcquisto");
            datiOrdineAcquisto.addElement("RiferimentoNumeroLinee").addText("1"); //CHECK
            datiOrdineAcquisto.addElement("IdDocumento").addText("12");//invoice number
            datiOrdineAcquisto.addElement("NumItem").addText("1"); // check

            Element datiBeniServizi = fatturaElettronicaBody.addElement("DatiBeniServizi");
            //FOREACH INVOICE_PRODUCT ENTRY
                datiBeniServizi.addElement("NumeroLinea").addText("1"); //row counter
                datiBeniServizi.addElement("Descrizione").addText("Aggiornamento Carrelli Elevatori"); //product description
                datiBeniServizi.addElement("Quantita").addText("4.00"); //invoice_product quantity
                datiBeniServizi.addElement("UnitaMisura").addText("ore"); //product measurement unit
                datiBeniServizi.addElement("PrezzoUnitario").addText("60.00"); //invoice_product unit price4
                datiBeniServizi.addElement("PrezzoTotale").addText("240.00"); //quantity * unit_price
                datiBeniServizi.addElement("AliquotaIVA").addText("0.00"); //depends on company fiscal type
                datiBeniServizi.addElement("Natura").addText("N2.2"); //to specify ONLY in company fiscal type is FORFETTARIO
            //end foreach
            Element datiRiepilogo = datiBeniServizi.addElement("DatiRiepilogo");
            datiRiepilogo.addElement("AliquotaIVA").addText("0.00"); // depends on company fiscal type
            datiRiepilogo.addElement("Natura").addText("N2.2"); //to specify ONLY in company fiscal type is FORFETTARIO
            datiRiepilogo.addElement("ImponibileImporto").addText("1083.68"); // total of all detail line above
            datiRiepilogo.addElement("Imposta").addText("0.00"); // depends on company fiscal type
            datiRiepilogo.addElement("RiferimentoNormativo").addText(riferimentoNormativo); //depends on company fiscal type

            Element datiPagamento = fatturaElettronicaBody.addElement("DatiPagamento");
            datiPagamento.addElement("CondizioniPagamento").addText("TP02"); //standard (immediate payment)
            Element dettaglioPagamento = datiPagamento.addElement("DettaglioPagamento");
            dettaglioPagamento.addElement("ModalitaPagamento").addText("MP05"); //standard: bonifico
            dettaglioPagamento.addElement("ImportoPagamento").addText("1083.68"); //total of all rows above + taxes
            dettaglioPagamento.addElement("IBAN").addText("IT51A0306962732100000004512"); //bankaccount iban selected

            StringWriter sw = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(sw, OutputFormat.createPrettyPrint());
            xmlWriter.write(el_invoice);
            xmlWriter.close();
            FileWriter f = new FileWriter("xml/IT123456789_1.xml");
            f.write(sw.getBuffer().toString());
            f.close();




        }catch(SQLException ex){
            LOGGER.error("Cannot create customer: unexpected error while accessing the database.", ex);
            m = new Message("Cannot create customer: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }catch (NumberFormatException ex) {
            m = new Message("No company id provided for " +  out.get(3) + ", will be set to null.", "E5A1", ex.getMessage());
            LOGGER.info("No company id provided for %s, will be set to null.", out.get(3));
            m.toJSON(res.getOutputStream());
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}


