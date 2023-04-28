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

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.BankAccount;

/**
 * Delete bank account
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class GetBankAccountDAO extends AbstractDAO<BankAccount>{

    private final static String STATEMENT="SELECT * FROM bitsei_schema.\"BankAccount\"  WHERE  bankaccount_id=?";
    private final static String CONTROLL_STATEMANT = "SELECT bitsei_schema.\"Company\".owner_id, bitsei_schema.\"BankAccount\".bankaccount_id  FROM bitsei_schema.\"BankAccount\" INNER JOIN bitsei_schema.\"Company\" ON bitsei_schema.\"BankAccount\".company_id = bitsei_schema.\"Company\".company_id WHERE owner_id=? AND bankaccount_id=?";

    /**
     * bank account to retrieve
     */
    private int bankAccount_id;
    /**
     * owner of the company
     */
    private int owner_id;
    /**
     * Get the bank account
     * 
     * @param con connection to the database
     * @param baID Bank account to get
     * @param owner_id owner of the company
     */

    public GetBankAccountDAO(Connection con, int baID,int owner_id) {
        super(con);
        this.bankAccount_id=baID;
        this.owner_id=owner_id;
    }

    /**
     * get the bank account 
     */
    @Override
    protected void doAccess() throws Exception {
        outputParam = null;
        //controlls if the owner is correct
        PreparedStatement controll_statemant = null;
        ResultSet controll_rs=null;


        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //output
        BankAccount bankAccount;

        //controlls if the owner owns the company
        try{

            controll_statemant = con.prepareStatement(CONTROLL_STATEMANT);
            
            controll_statemant.setInt(1, owner_id);
            controll_statemant.setInt(2, bankAccount_id);

            controll_rs = controll_statemant.executeQuery();

            if (!controll_rs.next()) {
                LOGGER.info("owner dosen't own bank account, companyID: " + bankAccount_id + " ownerID: " +owner_id);
                return;
            }

            

        }finally{
            if (controll_rs != null) {
                controll_rs.close();
            }

            if (controll_statemant != null) {
                controll_statemant.close();
            }
        }

        

        try{
            //execute the query
            pstmt= con.prepareStatement(STATEMENT);
            
            pstmt.setInt(1, bankAccount_id);

            rs = pstmt.executeQuery();

            if(!rs.next())
                return;

            bankAccount = 
                new BankAccount(
                    rs.getInt("bankaccount_id"),
                    rs.getString("IBAN"), 
                    rs.getString("bank_name"), 
                    rs.getString("bankaccount_friendly_name"), 
                    rs.getInt("company_id"))
                    ;

        
        }finally{
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
        //if the bank account id is 0 means that the resurce wasn't find
        if(bankAccount.getBankAccountID()==0){
            return;
        }

        outputParam=bankAccount;
    }

}
