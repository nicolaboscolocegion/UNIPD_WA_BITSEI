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


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.jose4j.lang.JoseException;

import it.unipd.dei.bitsei.dao.UserAuthDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.LoginResource;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.Token;
import it.unipd.dei.bitsei.utils.TokenJWT;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Control the authentication of the user, if the user is authenticated the server will send a token to the client
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class LoginUserRR extends AbstractRR {

    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public LoginUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LOGIN, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        //email of the user
        String email = null;
        //password in clear of the username, this will be changed when doing the frontend
        String password = null;

        //Response
        Message m;

        int owner_id; //authentication flag

        try {
            // retrieves the user parameters from the request
            final LoginResource loginResource = LoginResource.fromJSON(req.getInputStream());
            // creates a new object for accessing the database and searching the employees
            owner_id = new UserAuthDAO(con, loginResource).access().getOutputParam();

            // if the user is authenticated
            if (owner_id != -1) {
                // set the MIME media type of the response
                res.setContentType("application/json; charset=utf-8");

                TokenJWT token = new TokenJWT(loginResource.getEmail(), loginResource.getPassword(), owner_id);
                res.setStatus(HttpServletResponse.SC_OK);
                new Token(token.getTokenString()).toJSON(res.getOutputStream());
            } else {
                // set the MIME media type of the response
                res.setContentType("application/json; charset=utf-8");

                m = new Message("Wrong email or password", "E5A1", "Wrong email or password");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                m.toJSON(res.getOutputStream());
            }

        } catch (SQLException ex) {
            LOGGER.error("Cannot search for user: unexpected error while accessing the database.", ex);

            m = new Message("Cannot search for user: unexpected error while accessing the database.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (JoseException e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when Authenticate"), e);

            m = new Message("Unable to send response when Authenticate", "E5A1", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}

