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
package it.unipd.dei.bitsei.dao.bankAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.BankAccount;

/**
 * Updates bank account 
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class UpdateBankAccountDAO extends AbstractDAO<Boolean>{

    private static String STATEMENT ="UPDATE bitsei_schema.\"BankAccount\" SET \"IBAN\" ='?', bank_name ='?', bankaccount_friendly_name='?' WHERE bankaccount_id=?";
    private final static String CONTROLL_STATEMANT = "SELECT * FROM bitsei_schema.\"Company\" WHERE bankaccount_id=?";


    /**
     * old bank account
     */
    private int oldBankAccountID;
    /**
     * new bank account
     */
    private BankAccount newBankAccount;
    /**
     * owner id of the company
     */
    private int owner_id;
    /**
     * 
     * @param con the connection
     * @param oldba the bank account to update
     * @param newBA the new bank account
     */
    public UpdateBankAccountDAO(Connection con, int oldBA, BankAccount newBA, int ownerID) {
        super(con);
        this.oldBankAccountID=oldBA;
        this.newBankAccount=newBA;
        this.owner_id=ownerID;
    }


    /**
     * updates the old bank account with a new one, if the process is complete will return true 
     */
    @Override
    public void doAccess() throws SQLException {
        outputParam = false;
        //controlls if the owner is correct
        PreparedStatement controll_statemant = null;
        ResultSet controll_rs=null;
        //fetched ID of the owner if exist
        int fetchedID=0;
        // update statemant
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        //controlls if the owner owns the company
        try{

            controll_statemant = con.prepareStatement(CONTROLL_STATEMANT);
            
            controll_statemant.setInt(1, oldBankAccountID);
            controll_statemant.setInt(2, owner_id);

            controll_rs = controll_statemant.executeQuery();

            controll_rs.getInt("company_id");

        }finally{
            if (controll_rs != null) {
                controll_rs.close();
            }

            if (controll_statemant != null) {
                controll_statemant.close();
            }
            
        }
        if(fetchedID==0){
            LOGGER.info("owner dosen't own company, companyID: " + newBankAccount.getCompanyId() + " ownerID: " +owner_id);
            return;
        }

        try{

            pstmt= con.prepareStatement(STATEMENT);

            pstmt.setString(1, newBankAccount.getIban());
            pstmt.setString(2, newBankAccount.getBankName());
            pstmt.setString(3, newBankAccount.getBankAccountFriendlyName());
            
            pstmt.setInt(4, oldBankAccountID);

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
