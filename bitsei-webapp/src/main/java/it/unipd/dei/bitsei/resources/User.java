package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.*;

import java.io.*;

/**
 * Represents the data about a user.
 */
public class User extends AbstractResource {
    private final int user_id;
    /**
     * The name of the user
     */
    private final String firstname;

    /**
     * The surname of the user
     */
    private final String lastname;

    private final String username;

    private final String email;
    private final String telegram_chat_id;
    private final String password;

    /**
     * Creates a new user
     *
     * @param user_id          the id of the user
     * @param firstname        the name number of the user
     * @param lastname         the surname of the user.
     * @param username         the username of the user.
     * @param email            the email of the user.
     * @param telegram_chat_id the telegram chat id of the user.
     */
    public User(final int user_id, final String firstname, final String lastname, final String username, final String email, final String telegram_chat_id) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.telegram_chat_id = telegram_chat_id;
        this.password = null;
    }

    public User(final String password) {
        this.user_id = -1;
        this.firstname = null;
        this.lastname = null;
        this.username = null;
        this.email = null;
        this.telegram_chat_id = null;
        this.password = password;
    }

    public User(final String firstname, final String lastname, final String username, final String email, final String telegram_chat_id) {
        this.password = null;
        this.user_id = -1;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.telegram_chat_id = telegram_chat_id;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name of the user.
     */
    public final String getFirstname() {
        return firstname;
    }

    public final String getLastname() {
        return lastname;
    }

    public final String getUsername() {
        return username;
    }

    public final String getEmail() {
        return email;
    }

    public final String getTelegram_chat_id() {
        return telegram_chat_id;
    }

    public final int getUser_id() {
        return user_id;
    }

    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("user");
        jg.writeStartObject();

        jg.writeStringField("firstname", firstname);
        jg.writeStringField("lastname", lastname);
        jg.writeStringField("username", username);
        jg.writeStringField("email", email);
        jg.writeStringField("telegram_chat_id", telegram_chat_id);

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
        String jUsername = null;
        String jEmail = null;
        String jTelegram_chat_id = null;

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
                        case "firstname" -> {
                            jp.nextToken();
                            jName = jp.getText();
                        }
                        case "lastname" -> {
                            jp.nextToken();
                            jSurname = jp.getText();
                        }
                        case "username" -> {
                            jp.nextToken();
                            jUsername = jp.getText();
                        }
                        case "email" -> {
                            jp.nextToken();
                            jEmail = jp.getText();
                        }
                        case "telegram_chat_id" -> {
                            jp.nextToken();
                            jTelegram_chat_id = jp.getText();
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new User(jName, jSurname, jUsername, jEmail, jTelegram_chat_id);
    }
}
