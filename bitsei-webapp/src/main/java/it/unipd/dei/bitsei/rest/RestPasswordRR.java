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
package it.unipd.dei.bitsei.rest;

import it.unipd.dei.bitsei.dao.PasswordRestDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.utils.HashGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handling the reset password request, generate a token and send it to the user via email.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class RestPasswordRR extends AbstractRR {

    /**
     * reset password request.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public RestPasswordRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.REST_PASSWORD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Message m;
        PasswordRest password_rest;

        try {
            final StringValue email = StringValue.fromJSON(req.getInputStream(), "email");

            // generate a random token
            String hashed_string = HashGenerator.generateHash();

            password_rest = new PasswordRestDAO(con, email.getValue(), hashed_string).access().getOutputParam();

            if (password_rest != null) {
                LOGGER.info("TOKEN." + password_rest.getToken());

                // TODO: Send email to user with the link to reset password.
                // SendEmail(user.email, password_rest.getReset_token());

                m = new Message("Reset password token send to your email, check your inbox", null, null);
                res.setStatus(HttpServletResponse.SC_OK);
                m.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while creating the password reset token.");

                m = new Message("Cannot create the password reset token: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot create the reset token: unexpected database error.", ex);

            m = new Message("Cannot create the reset token: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}

