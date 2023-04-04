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


import java.io.*;
import java.sql.Date;

/**
 * Represents a password reset.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class PasswordRest extends AbstractResource {

    /**
     * The token of the user that wants to reset his password
     */
    private final String token;

    /**
     * The expiry date of the token
     */
    private final Date expiry_date;

    /**
     * user id of the user that wants to reset his password
     */
    private final int user_id;


    /**
     * Creates an error message.
     *
     * @param token       the message.
     * @param expiry_date the code of the error.
     * @param user_id     additional details about the error.
     */
    public PasswordRest(final String token, final Date expiry_date, final int user_id) {
        this.token = token;
        this.expiry_date = expiry_date;
        this.user_id = user_id;
    }

    /**
     * Returns the token.
     *
     * @return the message.
     */
    public final String getToken() {
        return token;
    }

    /**
     * Returns the expiry date of the token.
     *
     * @return the expiry date of the token, if any, {@code null} otherwise.
     */
    public final Date getExpiryToken() {
        return expiry_date;
    }

    /**
     * Returns user id, if any.
     *
     * @return user id, if any, {@code null} otherwise.
     */
    public final int getUserID() {
        return user_id;
    }


    @Override
    protected void writeJSON(final OutputStream out) throws IOException {
    }

}
