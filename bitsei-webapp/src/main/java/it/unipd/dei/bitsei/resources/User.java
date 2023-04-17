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
 * Represents the data about a user.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class User extends AbstractResource {
    /**
     * The id of the user
     */
    private final int user_id;

    /**
     * The name of the user
     */
    private final String firstname;

    /**
     * The surname of the user
     */
    private final String lastname;

    /**
     * The username of the user
     */
    private final String username;

    /**
     * The email of the user
     */
    private final String email;
    /**
     * The telegram chat id of the user
     */
    private final String telegram_chat_id;
    /**
     * The password of the user
     */
    private final String password;

    /**
     * Creates a new user instance without password
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

    /**
     * Creates a new user instance with just password -> used for reset password
     *
     * @param password the password of the user.
     */
    public User(final String password) {
        this.user_id = -1;
        this.firstname = null;
        this.lastname = null;
        this.username = null;
        this.email = null;
        this.telegram_chat_id = null;
        this.password = password;
    }

    /**
     * Creates a new user instance without user_id and password -> used for registration or login
     *
     * @param firstname        the name number of the user
     * @param lastname         the surname of the user.
     * @param username         the username of the user.
     * @param email            the email of the user.
     * @param telegram_chat_id the telegram chat id of the user.
     */
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

    /**
     * Returns the surname of the user.
     *
     * @return the surname of the user.
     */
    public final String getLastname() {
        return lastname;
    }

    /**
     * Returns the userid of the user.
     *
     * @return the user id of the user.
     */
    public final String getUsername() {
        return username;
    }

    /**
     * Returns the email of the user.
     *
     * @return the email of the user.
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Returns the telegram id  of the user.
     *
     * @return the telegram chat id of the user.
     */
    public final String getTelegram_chat_id() {
        return telegram_chat_id;
    }

    /**
     * Returns the user id of the user.
     *
     * @return the user id of the user.
     */
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
