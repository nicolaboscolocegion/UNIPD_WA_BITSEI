package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.sql.Date;

/**
 * Represents the data about amounts of invoices grouped by date.
 */
//public class Invoice extends AbstractResource {
public class InvoiceByDate {
    /**
     * The date in format: Month YYYY
     */
    private final String date;


    /**
     * The amount of invoices by date
     */
    private final int amount;


    /**
     * Creates a new object with date and amount
     *
     * @param date        the date in format: Month YYYY
     * @param amount       amount of invoices by date
     */
    public InvoiceByDate(final String date, final int amount) {
        this.date = date;
        this.amount = amount;
    }

    /**
     * Returns the date in format: Month YYYY.
     *
     * @return the date in format: Month YYYY
     */
    public final String getDate() {
        return date;
    }

    /**
     * Returns the amount of invoices by date.
     *
     * @return the amount of invoices by date
     */
    public final int getAmount() {
        return amount;
    }


    /*
    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("invoice");
        jg.writeStartObject();

        jg.writeNumberField("invoice_id", invoice_id);
        jg.writeNumberField("customer_id", customer_id);
        jg.writeNumberField("status", status);
        jg.writeNumberField("warning_number", warning_number);
        jg.writeStringField("warning_date", warning_date.toString());
        jg.writeStringField("warning_pdf_file", warning_pdf_file);
        jg.writeStringField("invoice_number", invoice_number);
        jg.writeStringField("invoice_date", invoice_date.toString());
        jg.writeStringField("invoice_pdf_file", invoice_pdf_file);
        jg.writeStringField("invoice_xml_file", invoice_xml_file);
        jg.writeNumberField("total", total);
        jg.writeNumberField("discount", discount);
        jg.writeNumberField("pension_fund_refund", pension_fund_refund);
        jg.writeBooleanField("has_stamp", has_stamp);

        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }
    */

    /**
     * Creates a {@code Invoice} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code Invoice} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    /*
    public static Invoice fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jInvoice_id = 0;
        int jCustomer_id = 0;
        int jStatus = 0;
        int jWarning_number = 0;
        Date jWarning_date = null;
        String jWarning_pdf_file = null;
        String jInvoice_number = null;
        Date jInvoice_date = null;
        String jInvoice_pdf_file = null;
        String jInvoice_xml_file = null;
        double jTotal = 0;
        double jDiscount = 0;
        double jPension_fund_refund = 0;
        boolean jHas_stamp = false;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"invoice".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No User object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no User object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "invoice_id":
                            jp.nextToken();
                            jInvoice_id = jp.getIntValue();
                            break;
                        case "customer_id":
                            jp.nextToken();
                            jCustomer_id = jp.getIntValue();
                            break;
                        case "status":
                            jp.nextToken();
                            jStatus = jp.getIntValue();
                            break;
                        case "warning_number":
                            jp.nextToken();
                            jWarning_number = jp.getIntValue();
                            break;
                        case "warning_date":
                            jp.nextToken();
                            String tmpDate = jp.getText();
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(tmpDate);
                            jWarning_date = date;
                            break;
                        case "warning_pdf_file":
                            jp.nextToken();
                            jWarning_pdf_file = jp.getText();
                            break;
                        case "invoice_number":
                            jp.nextToken();
                            jInvoice_number = jp.getText();
                            break;
                        case "invoice_date":
                            jp.nextToken();
                            String tmpDate2 = jp.getText();
                            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(tmpDate2);
                            jInvoice_date = date2;
                            break;
                        case "invoice_pdf_file":
                            jp.nextToken();
                            jInvoice_pdf_file = jp.getText();
                            break;
                        case "invoice_xml_file":
                            jp.nextToken();
                            jInvoice_xml_file = jp.getText();
                            break;
                        case "total":
                            jp.nextToken();
                            jTotal = jp.getDoubleValue();
                            break;
                        case "discount":
                            jp.nextToken();
                            jDiscount = jp.getDoubleValue();
                            break;
                        case "pension_fund_refund":
                            jp.nextToken();
                            jPension_fund_refund = jp.getDoubleValue();
                            break;
                        case "has_stamp":
                            jp.nextToken();
                            jHas_stamp = jp.getBooleanValue();
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new Invoice(jInvoice_id, jCustomer_id, jStatus, jWarning_number, jWarning_date, jWarning_pdf_file, jInvoice_number, jInvoice_date, jInvoice_pdf_file, jInvoice_xml_file, jTotal, jDiscount, jPension_fund_refund, jHas_stamp);
    }
     */
}