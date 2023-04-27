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
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import it.unipd.dei.bitsei.dao.bankAccount.DeleteBankAccountDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.BankAccount;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * delete bank account RR
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class DeleteBankAccountRR extends AbstractRR{

    
    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeleteBankAccountRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.DELETE_BANK_ACCOUNT, req, res, con);
    }


    /**
     * creates bank a new bank account
     */
    @Override
    public void doServe() throws IOException {
        Message m;
        
        int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

        try{
            
            RestURIParser uri = new RestURIParser(req.getRequestURI());

            int BankAccountID = uri.getResourceID();

            //try to change the bank accoutn
            boolean delited = new DeleteBankAccountDAO(con, BankAccountID ,owner_id).access().getOutputParam();

            if(delited){
                res.setStatus(HttpServletResponse.SC_OK);
                LOGGER.info("delete bank account with ID: " + BankAccountID);
            }else{
                LOGGER.error("Fatal error while creating.");

                m = new Message("Cannot delete the bank account", "E5A1", null);
                res.setContentType("text/plain");
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
            }
        }catch(SQLException e){
            LOGGER.error("Cannot dlete bank account: unexpected error while accessing the database.", e);

            m = new Message("Cannot delete bank account: unexpected error while accessing the database.", "E5A1", null);
            res.setContentType("text/plain");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
    

