package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.sql.Date;

/**
 * Represents the data about an invoice.
 */
//public class Invoice extends AbstractResource {
public class Invoice {
    /**
     * The id of the invoice
     */
    private final int invoice_id;

    /**
     * The id of the customer
     */
    private final int customer_id;

    /**
     * The status of the invoice
     */
    private final int status;

    /**
     * The warning number of the invoice
     */
    private final int warning_number;

    /**
     * The warning date of the invoice
     */
    private final Date warning_date;

    /**
     * The warning pdf file of the invoice
     */
    private final String warning_pdf_file;

    /**
     * The invoice number of the invoice
     */
    private final String invoice_number;

    /**
     * The date of the invoice
     */
    private final Date invoice_date;

    /**
     * The invoice pdf file of the invoice
     */
    private final String invoice_pdf_file;

    /**
     * The invoice xml file of the invoice
     */
    private final String invoice_xml_file;

    /**
     * The total of the invoice
     */
    private final double total;

    /**
     * The discount of the invoice
     */
    private final double discount;

    /**
     * The pension fund refund of the invoice
     */
    private final double pension_fund_refund;

    /**
     * The stamp of the invoice
     */
    private final boolean has_stamp;

    /**
     * Creates a new invoice
     *
     * @param invoice_id        the id of the invoice
     * @param customer_id       the id of the customer
     * @param status            the status of the invoice
     * @param warning_number    the warning number of the invoice
     * @param warning_date      the warning date of the invoice
     * @param warning_pdf_file  the warning pdf file of the invoice
     * @param invoice_number    the invoice number of the invoice
     * @param invoice_date      the invoice date of the invoice
     * @param invoice_pdf_file  the invoice pdf file of the invoice
     * @param invoice_xml_file  the invoice xml file of the invoice
     * @param total             the total of the invoice
     * @param discount          the discount of the invoice
     * @param pension_fund_refund   the pension fund refund of the invoice
     * @param has_stamp         the stamp of the invoice
     */
    public Invoice(final int invoice_id, final int customer_id, final int status, final int warning_number, final Date warning_date, final String warning_pdf_file, final String invoice_number, final Date invoice_date, final String invoice_pdf_file, final String invoice_xml_file, final double total, final double discount, final double pension_fund_refund, final boolean has_stamp) {
        this.invoice_id = invoice_id;
        this.customer_id = customer_id;
        this.status = status;
        this.warning_number = warning_number;
        this.warning_date = warning_date;
        this.warning_pdf_file = warning_pdf_file;
        this.invoice_number = invoice_number;
        this.invoice_date = invoice_date;
        this.invoice_pdf_file = invoice_pdf_file;
        this.invoice_xml_file = invoice_xml_file;
        this.total = total;
        this.discount = discount;
        this.pension_fund_refund = pension_fund_refund;
        this.has_stamp = has_stamp;
    }

    /**
     * Returns the id of the invoice.
     *
     * @return the id of the invoice
     */
    public final int getInvoice_id() {
        return invoice_id;
    }

    /**
     * Returns the id of the customer.
     *
     * @return the id of the customer
     */
    public final int getCustomer_id() {
        return customer_id;
    }

    /**
     * Returns the status of the invoice.
     *
     * @return the status of the invoice
     */
    public final int getStatus() {
        return status;
    }

    /**
     * Returns the warning date of the invoice.
     *
     * @return the warning date of the invoice
     */
    public final Date getWarning_date() {
        return warning_date;
    }

    /**
     * Returns the warning pdf file of the invoice.
     *
     * @return the warning pdf file of the invoice
     */
    public final String getWarning_pdf_file() {
        return warning_pdf_file;
    }

    /**
     * Returns the invoice number of the invoice.
     *
     * @return the invoice number of the invoice
     */
    public final String getInvoice_number() {
        return invoice_number;
    }

    /**
     * Returns the date of the invoice.
     *
     * @return the date of the invoice
     */
    public final Date getInvoice_date() {
        return invoice_date;
    }

    /**
     * Returns the invoice pdf file of the invoice.
     *
     * @return the invoice pdf file of the invoice
     */
    public final String getInvoice_pdf_file() {
        return invoice_pdf_file;
    }

    /**
     * Returns the invoice xml file of the invoice.
     *
     * @return the invoice xml file of the invoice
     */
    public final String getInvoice_xml_file() {
        return invoice_xml_file;
    }

    /**
     * Returns the total of the invoice.
     *
     * @return the total of the invoice
     */
    public final double getTotal() {
        return total;
    }

    /**
     * Returns the discount of the invoice.
     *
     * @return the discount of the invoice
     */
    public final double getDiscount() {
        return discount;
    }

    /**
     * Returns the pension fund refund of the invoice.
     *
     * @return the pension fund refund of the invoice
     */
    public final double getPension_fund_refund() {
        return pension_fund_refund;
    }

    /**
     * Returns true if the invoice has stamp.
     *
     * @return true if the invoice has stamp
     */
    public final boolean hasStamp() {
        return has_stamp;
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
