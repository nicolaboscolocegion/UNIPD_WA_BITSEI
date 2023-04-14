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
package it.unipd.dei.bitsei.rest.bankAccount;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import it.unipd.dei.bitsei.dao.bankAccount.customer.ListBankAccountsDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.BankAccount;
import it.unipd.dei.bitsei.resources.Company;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.ResourceList;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Handiling the listing of all bank accounts givin a a company
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class ListBankAccountsRR extends AbstractRR{

    /**
     * lists all bank accounts
     *
     * @param action the action to log
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListBankAccountsRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_BANK_ACCOUNTS, req, res, con);
    }

    /**
     * lists all bank accounts
     */
    @Override
    protected void doServe() throws IOException{
        List<BankAccount> el = null;
        Message m = null;

        

        try {
            
            String uri = req.getRequestURI();
            String id = uri.substring(uri.lastIndexOf('/') + 1);
            if (id.isEmpty() || id.isBlank()) {
                throw new IOException("company id cannot be empty.");
            }

            int company_id = Integer.parseInt(id);
            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            

            // creates a new DAO for accessing the database and lists the user(s)
            el = new ListBankAccountsDAO(con, company_id, owner_id).access().getOutputParam();

            if (el != null) {
                LOGGER.info("bank account(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());

            } else { // it should not happen
                LOGGER.error("Fatal error while listing bank account(s).");

                m = new Message("Cannot list bank account(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list bank account(s): unexpected database error.", ex);

            m = new Message("Cannot list bank account(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
    
}
