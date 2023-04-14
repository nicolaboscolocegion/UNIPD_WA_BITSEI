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

import it.unipd.dei.bitsei.dao.bankAccount.CreateBankAccountDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.BankAccount;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import it.unipd.dei.bitsei.utils.TokenJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateBankAccountRR extends AbstractRR{

    
    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CreateBankAccountRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.CREATE_BANK_ACCOUNT, req, res, con);
    }


    /**
     * creates bank a new bank account
     */
    @Override
    protected void doServe() throws IOException {
        Message m;
        InputStream requestStream = req.getInputStream();
        
        int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

        try{
            //find if there is a new bank accoount in the response
            BankAccount newBankAccount= BankAccount.fromJSON(requestStream);
            
            //try to change the bank accoutn
            boolean created = new CreateBankAccountDAO(con, newBankAccount, owner_id).access().getOutputParam();

            if(created){
                res.setStatus(HttpServletResponse.SC_CREATED);
                LOGGER.info("created new bank account with iban: " + newBankAccount.getIban());
            }else{
                LOGGER.error("Fatal error while creating.");

                m = new Message("Cannot create the bank account", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
            }
        }catch(SQLException e){
            LOGGER.error("Cannot crate bank account: unexpected error while accessing the database.", e);

            m = new Message("Cannot create bank account: unexpected error while accessing the database.", "E5A1", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
    

