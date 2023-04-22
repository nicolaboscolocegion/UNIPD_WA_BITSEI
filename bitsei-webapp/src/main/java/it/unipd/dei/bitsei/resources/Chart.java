package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Represents the data about a chart.
 */
public class Chart extends AbstractResource {
    /**
     * The array of labels
     */
    private final ArrayList<String> label;

    /**
     * The array of data
     */
    private final ArrayList<String> data;
    
    /**
     * The chart type
     */
    private final int chart_type;

    /**
     * The chart period
     */
    private final int chart_period;


    /**
     * Creates a new chart
     *
     * @param label         the array of labels
     * @param data          the array of data
     * @param chart_type    the chart type
     * @param chart_period  the chart period
     */
    public Chart(final ArrayList<String> label, final ArrayList<String> data, final int chart_type, final int chart_period) {
        this.label = label;
        this.data = data;
        this.chart_type = chart_type;
        this.chart_period = chart_period;
    }

    /**
     * Returns the array of labels.
     *
     * @return the array of labels
     */
    public final ArrayList<String> getLabel() {
        return label;
    }

    /**
     * Returns the array of data.
     *
     * @return the array of data
     */
    public final ArrayList<String> getData() {
        return data;
    }

    /**
     * Returns the chart type.
     *
     * @return the chart type
     */
    public final int getChartType() {
        return chart_type;
    }

    /**
     * Returns the chart period.
     *
     * @return the chart period
     */
    public final int getChartPeriod() {
        return chart_period;
    }


    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("chart");
        jg.writeStartObject();

        try {
            jg.writeArrayFieldStart("label");
            for(String a : label){
                jg.writeString(a);
            }
            jg.writeEndArray();

            jg.writeArrayFieldStart("data");
            for(String a : data){
                jg.writeString(a);
            }
            jg.writeEndArray();

            jg.writeNumberField("chart_type", chart_type);
            jg.writeNumberField("chart_period", chart_period);
        } catch (Throwable T) {
            //LOGGER.warn("## INVOICE CLASS: Invoice #%d has null field(s).", invoice_id);
        }

        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Creates a {@code Chart} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code Invoice} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static Chart fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        ArrayList<String> jLabel = new ArrayList<String>();
        ArrayList<String> jData = new ArrayList<String>();
        int jChartType = -1;
        int jChartPeriod = -1;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"chart".equals(jp.getCurrentName())) {

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
                            if (jp.currentToken() == JsonToken.START_ARRAY) {
                                jp.nextToken();
                                while (jp.currentToken() != JsonToken.END_ARRAY) {
                                    jLabel.add(jp.getText());
                                    jp.nextToken();
                                }
                            }
                            break;
                        case "data":
                            jp.nextToken();
                            if (jp.currentToken() == JsonToken.START_ARRAY) {
                                jp.nextToken();
                                while (jp.currentToken() != JsonToken.END_ARRAY) {
                                    jData.add(jp.getText());
                                    jp.nextToken();
                                }
                            }
                            break;
                        case "chart_type":
                            jp.nextToken();
                            jChartType = jp.getIntValue();
                            break;
                        case "chart_period":
                            jp.nextToken();
                            jChartPeriod = jp.getIntValue();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Chart object from JSON.", e);
            throw e;
        }

        return new Chart(jLabel, jData, jChartType, jChartPeriod);
    }

}
