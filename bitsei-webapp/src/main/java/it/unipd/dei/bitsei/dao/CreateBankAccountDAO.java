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
package it.unipd.dei.bitsei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.dei.bitsei.resources.BankAccount;

/**
 * Create a new bank account
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class CreateBankAccountDAO extends AbstractDAO<Boolean>{

    private static String STATEMENT = "INSERT INTO \"BankAccount\"  (\"IBAN\" , bank_name , bankaccount_friendly_name ,company_id) VALUES ('?', '?', '?', '?');";

    /**
     * new bank account for a company
     */
    private BankAccount newBankAccount;

    public CreateBankAccountDAO(Connection con, BankAccount ba) {
        super(con);
        newBankAccount = ba;
    }

    /**
     * create a new bank account for a company
     */
    @Override
    protected void doAccess() throws Exception {
        outputParam = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            //query
            pstmt= con.prepareStatement(STATEMENT);

            pstmt.setString(1, newBankAccount.getIban());
            pstmt.setString(2, newBankAccount.getBankName());
            pstmt.setString(3, newBankAccount.getBankAccountFriendlyName());
            pstmt.setString(4, String.valueOf(newBankAccount.getCompanyId()));

            rs = pstmt.executeQuery();

            outputParam = true;

        }finally{
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    
}
