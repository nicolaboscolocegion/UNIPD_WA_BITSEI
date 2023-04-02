package it.unipd.dei.bitsei.resources;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.*;
import java.sql.Date;

/**
 * Represents a message or an error message.
 */
public class PasswordRest extends AbstractResource {

    /**
     * The message
     */
    private final String token;

    /**
     * The code of the error, if any
     */
    private final Date expiry_date;

    /**
     * Additional details about the error, if any
     */
    private final int user_id;


    /**
     * Creates an error message.
     *
     * @param token         the message.
     * @param expiry_date   the code of the error.
     * @param user_id       additional details about the error.
     */
    public PasswordRest(final String token, final Date expiry_date, final int user_id) {
        this.token = token;
        this.expiry_date = expiry_date;
        this.user_id = user_id;
    }

    /**
     * Returns the message.
     *
     * @return the message.
     */
    public final String getToken() {
        return token;
    }

    /**
     * Returns the code of the error, if any.
     *
     * @return the code of the error, if any, {@code null} otherwise.
     */
    public final Date getExpiryToken() {
        return expiry_date;
    }

    /**
     * Returns additional details about the error, if any.
     *
     * @return additional details about the error, if any, {@code null} otherwise.
     */
    public final int getUserID() {
        return user_id;
    }


    @Override
    protected void writeJSON(final OutputStream out) throws IOException {}



}
