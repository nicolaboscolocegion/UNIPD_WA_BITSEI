package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a user.
 */
public class User extends AbstractResource {

    /**
     * The name of the user
     */
    private final String name;

    /**
     * The surname of the user
     */
    private final String surname;

    private final String password;
    /**
     * Creates a new user
     *
     * @param name    the name number of the user
     * @param surname the surname of the user.
     */
    public User(final String name, final String surname, final String password){
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name of the user.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the surname of the user.
     *
     * @return the surname of the user.
     */
    public final String getSurname() {
        return surname;
    }

    public final String getPassword() {
        return password;
    }

    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("user");
        jg.writeStartObject();

        jg.writeStringField("name", name);
        jg.writeStringField("surname", surname);

        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }

    /**
     * Creates a {@code User} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     * @return the {@code User} created from the JSON representation.
     * @throws IOException if something goes wrong while parsing.
     */
    public static User fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        String jName = null;
        String jSurname = null;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"user".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No User object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no User object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "name":
                            jp.nextToken();
                            jName = jp.getText();
                            break;
                        case "surname":
                            jp.nextToken();
                            jSurname = jp.getText();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new User(jName, jSurname, null);
    }
}
