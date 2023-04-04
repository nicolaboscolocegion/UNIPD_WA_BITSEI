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

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a change password.
 */
public class ChangePassword extends AbstractResource {

    /**
     * The new password of the user that wants to update his password
     */
    private final String password;

    /**
     * The reset token of the user that wants to update his password in email
     */
    private final String reset_token;


    /**
     * Creates a new user
     *
     * @param password    the name number of the user
     * @param reset_token the surname of the user.
     */
    public ChangePassword(final String password, final String reset_token) {
        this.password = password;
        this.reset_token = reset_token;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Returns the reset token of the user.
     *
     * @return the reset token of the user.
     */
    public final String getReset_token() {
        return reset_token;
    }


    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

    }

    /**
     * Creates a {@code User} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code User} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static ChangePassword fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jPassword = null;
        String jReset_token = null;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"data".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Data object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no User object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "reset_token" -> {
                            jp.nextToken();
                            jReset_token = jp.getText();
                        }
                        case "password" -> {
                            jp.nextToken();
                            jPassword = jp.getText();
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an Data object from JSON.", e);
            throw e;
        }

        return new ChangePassword(jPassword, jReset_token);
    }
}
