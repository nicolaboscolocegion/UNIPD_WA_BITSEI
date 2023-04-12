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
import java.sql.SQLException;

import it.unipd.dei.bitsei.resources.BankAccount;

/**
 * Updates bank account 
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class UpdateBankAccountDAO extends AbstractDAO<Boolean>{

    private static String STATEMENT ="UPDATE \"BankAccount\" SET \"IBAN\" ='?', bank_name ='?', bankaccount_friendly_name='?' WHERE company_id ='?' and \"IBAN\" ='?';";

    /**
     * old bank account
     */
    private BankAccount oldBankAccount;
    /**
     * new bank account
     */
    private BankAccount newBankAccount;

    /**
     * 
     * @param con the connection
     * @param oldba the bank account to update
     * @param newBA the new bank account
     */
    public UpdateBankAccountDAO(Connection con, BankAccount oldBA, BankAccount newBA) {
        super(con);
        oldBankAccount=oldBA;
        newBankAccount=newBA;
    }


    /**
     * updates the old bank account with a new one, if the process is complete will return true 
     */
    @Override
    public void doAccess() throws SQLException {
        outputParam = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            pstmt= con.prepareStatement(STATEMENT);

            pstmt.setString(1, newBankAccount.getIban());
            pstmt.setString(2, newBankAccount.getBankName());
            pstmt.setString(3, newBankAccount.getBankAccountFriendlyName());
            
            pstmt.setInt(4, oldBankAccount.getCompanyId());
            pstmt.setString(5, oldBankAccount.getIban());

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
