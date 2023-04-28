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
public class UpdateBankAccountDAO extends AbstractDAO<Boolean> {

    private static String STATEMENT = "UPDATE bitsei_schema.\"BankAccount\" SET \"IBAN\"=?, bank_name=?, bankaccount_friendly_name=? WHERE bankaccount_id=?";
    private final static String CONTROLL_STATEMANT = "SELECT bitsei_schema.\"Company\".owner_id, bitsei_schema.\"BankAccount\".bankaccount_id  FROM bitsei_schema.\"BankAccount\" INNER JOIN bitsei_schema.\"Company\" ON bitsei_schema.\"BankAccount\".company_id = bitsei_schema.\"Company\".company_id WHERE owner_id=? AND bankaccount_id=?";


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
     * Constructor that allocates an UpdateBankAccountDAO object.
     *
     * @param con     the connection to the database
     * @param oldBA   the bank account to update
     * @param newBA   the new bank account
     * @param ownerID the owner id of the company
     */
    public UpdateBankAccountDAO(Connection con, int oldBA, BankAccount newBA, int ownerID) {
        super(con);
        this.oldBankAccountID = oldBA;
        this.newBankAccount = newBA;
        this.owner_id = ownerID;
    }


    /**
     * updates the old bank account with a new one, if the process is complete will return true
     */
    @Override
    public void doAccess() throws SQLException {
        outputParam = false;
        //controls if the owner is correct
        PreparedStatement controll_statemant = null;
        ResultSet controll_rs = null;
        // update statement
        PreparedStatement pstmt = null;
        //execution control
        int execute;

        //controls if the owner owns the company
        try {

            controll_statemant = con.prepareStatement(CONTROLL_STATEMANT);

            controll_statemant.setInt(1, owner_id);
            controll_statemant.setInt(2, oldBankAccountID);

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

            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, newBankAccount.getIban());
            pstmt.setString(2, newBankAccount.getBankName());
            pstmt.setString(3, newBankAccount.getBankAccountFriendlyName());

            pstmt.setInt(4, oldBankAccountID);

            execute = pstmt.executeUpdate();

            if (execute == 1)
                outputParam = true;

        } finally {

            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

}
