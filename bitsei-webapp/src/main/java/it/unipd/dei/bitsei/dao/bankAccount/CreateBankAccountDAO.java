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
 * Create a new bank account
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class CreateBankAccountDAO extends AbstractDAO<Boolean> {

    private final static String STATEMENT = "INSERT INTO bitsei_schema.\"BankAccount\"  (\"IBAN\", bank_name, bankaccount_friendly_name, company_id) VALUES (?, ?, ?, ?)";
    private final static String CONTROLL_STATEMANT = "SELECT * FROM bitsei_schema.\"Company\" WHERE company_id=? AND owner_id=?";

    /**
     * new bank account for a company
     */
    private BankAccount newBankAccount;
    /**
     * owner ID
     */
    private int owner_id;

    /**
     * Constructor that creates an entity of CreateBankAccountDAO.
     *
     * @param con      connection to the database
     * @param ba       new bank account
     * @param owner_id owner ID
     */

    public CreateBankAccountDAO(Connection con, BankAccount ba, int owner_id) {
        super(con);
        newBankAccount = ba;
        this.owner_id = owner_id;
    }

    /**
     * create a new bank account for a company
     */
    @Override
    protected void doAccess() throws SQLException {
        outputParam = false;
        //controlls if the owner is correct
        PreparedStatement controll_statemant = null;
        ResultSet controll_rs = null;
        //statemant for the insert
        PreparedStatement pstmt = null;
        int execution;

        //controlls if the owner owns the company
        try {

            controll_statemant = con.prepareStatement(CONTROLL_STATEMANT);

            controll_statemant.setInt(1, newBankAccount.getCompanyId());
            controll_statemant.setInt(2, owner_id);

            controll_rs = controll_statemant.executeQuery();

            if (!controll_rs.next()) {
                LOGGER.info("owner dosen't own company, companyID: " + newBankAccount.getCompanyId() + " ownerID: " + owner_id);
                return;
            }


        } finally {
            if (controll_rs != null) {
                controll_rs.close();
            }

            if (controll_statemant != null) {
                controll_statemant.close();
            }

        }


        try {
            //query
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, newBankAccount.getIban());
            pstmt.setString(2, newBankAccount.getBankName());
            pstmt.setString(3, newBankAccount.getBankAccountFriendlyName());
            pstmt.setInt(4, newBankAccount.getCompanyId());

            execution = pstmt.executeUpdate();

            if (execution == 1)
                outputParam = true;

        } finally {

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

}
