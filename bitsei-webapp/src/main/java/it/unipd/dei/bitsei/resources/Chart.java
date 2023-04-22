package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.sql.Date;

/**
 * Represents the data about an invoice.
 */
public class Chart extends AbstractResource {
    /**
     * The id of the invoice
     */
    private final String label;

    /**
     * The id of the customer
     */
    private final String data;
    
    /**
     * The id of the invoice
     */
    private final int chart_type;

    /**
     * The id of the customer
     */
    private final int period;


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
    public Chart(final String label, final String data, final int chart_type, final int period) {
        this.label = label;
        this.data = data;
        this.chart_type = chart_type;
        this.period = period;
    }

    /**
     * Returns the id of the invoice.
     *
     * @return the id of the invoice
     */
    public final String getLabel() {
        return label;
    }

    /**
     * Returns the id of the customer.
     *
     * @return the id of the customer
     */
    public final String getData() {
        return data;
    }

    /**
     * Returns the id of the customer.
     *
     * @return the id of the customer
     */
    public final int getChartType() {
        return chart_type;
    }

    /**
     * Returns the id of the customer.
     *
     * @return the id of the customer
     */
    public final int getPeriod() {
        return period;
    }


    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("chart");
        jg.writeStartObject();

        try {
            jg.writeStringField("label", label);
            jg.writeStringField("data", data);
            jg.writeNumberField("chart_type", chart_type);
            jg.writeNumberField("period", period);
        } catch (Throwable T) {
            //LOGGER.warn("## INVOICE CLASS: Invoice #%d has null field(s).", invoice_id);
        }

        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Creates a {@code Invoice} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code Invoice} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static Chart fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jLabel = null;
        String jData = null;
        int jChartType = -1;
        int jPeriod = -1;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"chart-element".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Invoice object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Invoice object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "label":
                            jp.nextToken();
                            jLabel = jp.getText();
                            break;
                        case "data":
                            jp.nextToken();
                            jData = jp.getText();
                            break;
                        case "chart_type":
                            jp.nextToken();
                            jChartType = jp.getIntValue();
                            break;
                        case "period":
                            jp.nextToken();
                            jPeriod = jp.getIntValue();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new Chart(jLabel, jData, jChartType, jPeriod);
    }

}
