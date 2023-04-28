/*
 * Copyright 2022-2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;

/**
 * Represents a JSON string value for one field.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class StringValue extends AbstractResource {

    /**
     * The string value of the given field.
     */
    private final String strVal;


    /**
     * string value of the given field.
     *
     * @param strVal the value.
     */
    public StringValue(final String strVal) {
        this.strVal = strVal;
    }


    /**
     * Returns the value.
     *
     * @return the value.
     */
    public final String getValue() {
        return strVal;
    }

    /**
     * Fetch JSONs StringValue object
     * @param in the InputStream
     * @param field_name the field name
     * @return new StringValue objects
     * @throws IOException if an I/O error occurs
     */
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
                    LOGGER.error("No {} object found in the stream.", field_name);
                    throw new EOFException("Unable to parse JSON: no specific object found.");
                }
            }

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                if (jp.getCurrentName().equals(field_name)) {
                    jp.nextToken();
                    jValue = jp.getText();
                }
            }

        } catch (IOException e) {
            LOGGER.error("Unable to parse a object value from JSON.", e);
            throw e;
        }

        return new StringValue(jValue);
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

    }
}
