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

import org.eclipse.tags.shaded.org.apache.regexp.RE;

import it.unipd.dei.bitsei.dao.bankAccount.UpdateBankAccountDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.BankAccount;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.RestURIParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Update the bank account RR
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class UpdateBankAccoutRR extends AbstractRR{
    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public UpdateBankAccoutRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CHANGE_BANK_ACCOUNT, req, res, con);
    }

    /**
     * updates a bank account 
     */
    @Override
    protected void doServe() throws IOException {
        Message m;
        InputStream requestStream = req.getInputStream();
        
        try{

            RestURIParser uri = new RestURIParser(req.getRequestURI());
            

            int oldBankAccountID = uri.getResourceID();
            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

            BankAccount newBankAccount= BankAccount.fromJSON(requestStream);

            //controlls if the operation wa success
            boolean updated = false;

            if(!(newBankAccount.getBankName()==null || newBankAccount.getIban()==null || newBankAccount.getCompanyId() == -1 || newBankAccount.getBankAccountFriendlyName()==null)){
                //try to change the bank accoutn
                updated = new UpdateBankAccountDAO(con, oldBankAccountID, newBankAccount, owner_id).access().getOutputParam();
            }

            if(updated){
                res.setStatus(HttpServletResponse.SC_OK);
                LOGGER.info("changed " + oldBankAccountID +  " to "  + newBankAccount.getIban());
            }else{
                LOGGER.error("Bad request for: " + oldBankAccountID);

                m = new Message("Cannot change the bank account", "E5A1", null);
                res.setContentType("text/plain");
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
            }
        }catch(SQLException e){
            LOGGER.error("Cannot change bank account: unexpected error while accessing the database.", e);

            m = new Message("Cannot change bank account: unexpected error while accessing the database.", "E5A1", null);
            res.setContentType("text/plain");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
    
}
