package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about an user.
 */
public class ChangePassword extends AbstractResource {

    /**
     * The name of the user
     */
    private final String password;

    /**
     * The surname of the user
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
     * Returns the name of the user.
     *
     * @return the name of the user.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Returns the surname of the user.
     *
     * @return the surname of the user.
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
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new ChangePassword(jPassword, jReset_token);
    }
}
