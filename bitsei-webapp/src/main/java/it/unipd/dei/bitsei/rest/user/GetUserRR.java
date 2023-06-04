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
package it.unipd.dei.bitsei.rest.user;

import it.unipd.dei.bitsei.dao.user.GetUserDAO;
import it.unipd.dei.bitsei.dao.user.ListUserDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.User;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.ResourceList;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for getting {@link User}s.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public final class GetUserRR extends AbstractRR {

    /**
     * Creates a new REST resource for listing {@code User}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GetUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.GET_USER, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        User el = null;
        Message m = null;

        try {

            String email = req.getSession().getAttribute("email").toString();

            // creates a new DAO for accessing the database and lists the user(s)
            el = new GetUserDAO(con, email).access().getOutputParam();

            if (el != null) {
                LOGGER.info("User successfully fetch.");

                res.setStatus(HttpServletResponse.SC_OK);
                el.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while fetching user.");

                m = new Message("Cannot fetch user: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot fetch user: unexpected database error.", ex);

            m = new Message("Cannot fetch user: unexpected database error.", "E5A1", "");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}
