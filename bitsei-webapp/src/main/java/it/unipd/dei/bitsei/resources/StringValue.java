package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;

/**
 * Represents a message or an error message.
 */
public class StringValue extends AbstractResource {

    /**
     * The email
     */
    private final String strVal;


    /**
     * Creates a email.
     *
     * @param strVal the value.
     */
    public StringValue(final String strVal) {
        this.strVal = strVal;
    }


    /**
     * Returns the email.
     *
     * @return the email.
     */
    public final String getValue() {
        return strVal;
    }

    public static StringValue fromJSON(final InputStream in, String field_name) throws IOException {

        // the fields read from JSON
        String jValue = null;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !field_name.equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Email object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Email object found.");
                }
            }

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                if (jp.getCurrentName().equals(field_name)) {
                    jp.nextToken();
                    jValue = jp.getText();
                }
            }

        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new StringValue(jValue);
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

    }
}
