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

import it.unipd.dei.bitsei.dao.user.UpdateUserPasswordDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handling the change password request.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class ChangePasswordRR extends AbstractRR {

    /**
     * change password request.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ChangePasswordRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CHANGE_PASSWORD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Message m;
        boolean is_done;

        try {
            final ChangePassword data = ChangePassword.fromJSON(req.getInputStream());
            LOGGER.info(data.getReset_token());
            is_done = new UpdateUserPasswordDAO(con, data).access().getOutputParam();

            if (is_done) {
                LOGGER.info("User successfully found.");
                m = new Message("Successfully done, now you can login with your new password", null, null);
                res.setStatus(HttpServletResponse.SC_OK);
                m.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while getting user.");

                m = new Message("Cannot change password: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.error("Cannot change password: unexpected database error.", ex);

            m = new Message("Cannot change password: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}

