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
 * Delete bank account
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class DeleteBankAccountDAO extends AbstractDAO<Boolean>{

    private final static String STATEMENT="DELETE FROM \"BankAccount\"  WHERE \"IBAN\"=? AND company_id=?;";
    /**
     * bank account to delete
     */
    private BankAccount bankAccount;

    public DeleteBankAccountDAO(Connection con, BankAccount b) {
        super(con);
        this.bankAccount=b;
    }

    /**
     * delete the bank account 
     */
    @Override
    protected void doAccess() throws Exception {
        outputParam = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            //execute the query
            pstmt= con.prepareStatement(STATEMENT);
            
            pstmt.setString(1, bankAccount.getIban());
            pstmt.setInt(2, bankAccount.getCompanyId());

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
