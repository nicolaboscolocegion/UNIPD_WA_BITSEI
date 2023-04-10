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

import it.unipd.dei.bitsei.dao.DeleteCompanyDAO;
import it.unipd.dei.bitsei.resources.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for deleting {@link Company}.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public final class DeleteCompanyRR extends AbstractRR {

    /**
     * Creates a new REST resource for listing {@code Company}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteCompanyRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.GET_COMPANIES, req, res, con);
    }


    @Override
    protected void doServe() throws IOException {

        boolean is_ok = false;
        Message m = null;

        try {
            String uri = req.getRequestURI();
            String id = uri.substring(uri.lastIndexOf('/') + 1);
            if (id.isEmpty() || id.isBlank()) {
                throw new IOException("company id cannot be empty.");
            }

            int company_id = Integer.parseInt(id);
            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            is_ok = new DeleteCompanyDAO(con, company_id, owner_id).access().getOutputParam();

            if (is_ok) {
                LOGGER.info("Company successfully delete.");

                res.setStatus(HttpServletResponse.SC_NO_CONTENT);
                res.getOutputStream().flush();
            } else {
                LOGGER.error("Fatal error while deleting Company.");

                m = new Message("Cannot delete Company: make sure you have a right access to this company.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot delete Company: unexpected database error.", ex);

            m = new Message("Cannot delete Company: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}
