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
import java.util.LinkedList;
import java.util.List;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.BankAccount;

/**
 * List all bank account of a given company
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class ListBankAccountsDAO extends AbstractDAO<List<BankAccount>>{

    private final static String STATEMENT="select * from \"BankAccount\" where company_id=?";
    private final static String CONTROLL_STATEMANT = "SELECT * FROM \"Company\" WHERE company_id=? AND owner_id=?";


    private final int company_id;
    private final int owner_id;

    /**
     * constructor for: retrives all bank account in a list by givin a certain company
     * @param con connettion of the database
     * @param companyID company id to search
     * @param ownerID ID of the owner of the company
     */
    public ListBankAccountsDAO(Connection con, int companyID, int ownerID) {
        super(con);
        this.company_id = companyID;
        this.owner_id = ownerID;
    }  

    /**
     * retrives all bank account in a list by givin a certain company
     */
    @Override
    public void doAccess() throws SQLException {
        outputParam = null;
        //controlls if the owner is correct
        PreparedStatement controll_statemant = null;
        ResultSet controll_rs=null;
        //fetched ID of the owner if exist
        int fetchedID=0;
        //lists all the bank accounts
        List<BankAccount> bankAccountList =new LinkedList<BankAccount>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        //controlls if the owner owns the company
        try{

            controll_statemant = con.prepareStatement(CONTROLL_STATEMANT);
            
            controll_statemant.setInt(1, company_id);
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
            LOGGER.info("owner dosen't own company, companyID: " + company_id + " ownerID: " +owner_id);
            return;
        }


        try{
            //execute the query
            pstmt= con.prepareStatement(STATEMENT);
            
            pstmt.setInt(1, company_id);
            

            rs = pstmt.executeQuery();

            while (rs.next()) {
                bankAccountList.add(
                    new BankAccount(
                        rs.getInt("bankaccount_id"),
                        rs.getString("IBAN"), 
                        rs.getString("bank_name"), 
                        rs.getString("bankaccount_friendly_name"), 
                        rs.getInt("company_id"))
                    );
            }

            outputParam=bankAccountList;
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
