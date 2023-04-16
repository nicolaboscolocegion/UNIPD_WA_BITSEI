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
 * Delete bank account
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class DeleteBankAccountDAO extends AbstractDAO<Boolean>{

    private final static String STATEMENT="DELETE FROM bitsei_schema.\"BankAccount\"  WHERE bankaccount_id=?";
    private final static String CONTROLL_STATEMANT = "SELECT * FROM bitsei_schema.\"Company\" WHERE company_id=? AND owner_id=?";

    /**
     * bank account to delete
     */
    private int bankAccountID;
    /**
     * owner of the company
     */
    private int owner_id;

    /**
     * Deletes the bank account
     * 
     * @param con
     * @param baID Bank account ID to delete
     * @param owner_id owner of the company
     */

    public DeleteBankAccountDAO(Connection con, int baID,int owner_id) {
        super(con);
        this.bankAccountID=baID;
        this.owner_id=owner_id;
    }

    /**
     * delete the bank account 
     */
    @Override
    protected void doAccess() throws Exception {
        outputParam = false;
        //controlls if the owner is correct
        PreparedStatement controll_statemant = null;
        ResultSet controll_rs=null;
        //fetched ID of the owner if exist
        int fetchedID=0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //controlls if the owner owns the company
        try{

            controll_statemant = con.prepareStatement(CONTROLL_STATEMANT);
            
            controll_statemant.setInt(1, bankAccountID);
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
            LOGGER.info("owner dosen't own company, companyID: " + bankAccountID + " ownerID: " +owner_id);
            return;
        }

        try{
            //execute the query
            pstmt= con.prepareStatement(STATEMENT);
            
            pstmt.setInt(1, bankAccountID);

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
