package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.util.ArrayList;

/**
 * Represents the data about a chart.
 */
public class Chart extends AbstractResource {
    /**
     * The array of labels
     */
    private final ArrayList<String> labels;

    /**
     * The array of data
     */
    private final ArrayList<String> data;
    
    /**
     * The chart type
     */
    private final int type;

    /**
     * The chart period
     */
    private final int period;


    /**
     * Creates a new chart
     *
     * @param labels  the array of labels
     * @param data    the array of data
     * @param type    the chart type
     * @param period  the chart period
     */
    public Chart(final ArrayList<String> labels, final ArrayList<String> data, final int type, final int period) {
        this.labels = labels;
        this.data = data;
        this.type = type;
        this.period = period;
    }

    /**
     * Returns the array of labels.
     *
     * @return the array of labels
     */
    public final ArrayList<String> getLabels() {
        return labels;
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
    public final int getType() {
        return type;
    }

    /**
     * Returns the chart period.
     *
     * @return the chart period
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
            jg.writeArrayFieldStart("labels");
            for(String a : labels){
                jg.writeString(a);
            }
            jg.writeEndArray();

            jg.writeArrayFieldStart("data");
            for(String a : data){
                jg.writeString(a);
            }
            jg.writeEndArray();

            jg.writeNumberField("type", type);
            jg.writeNumberField("period", period);
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
     * @return the {@code Chart} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static Chart fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        ArrayList<String> jLabels = new ArrayList<String>();
        ArrayList<String> jData = new ArrayList<String>();
        int jType = -1;
        int jPeriod = -1;

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
                        case "labels":
                            jp.nextToken();
                            if (jp.currentToken() == JsonToken.START_ARRAY) {
                                jp.nextToken();
                                while (jp.currentToken() != JsonToken.END_ARRAY) {
                                    jLabels.add(jp.getText());
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
                        case "type":
                            jp.nextToken();
                            jType = jp.getIntValue();
                            break;
                        case "period":
                            jp.nextToken();
                            jPeriod = jp.getIntValue();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Chart object from JSON.", e);
            throw e;
        }

        return new Chart(jLabels, jData, jType, jPeriod);
    }

}
