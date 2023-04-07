package it.unipd.dei.bitsei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.dei.bitsei.resources.BankAccount;

public class CreateBankAccountDAO extends AbstractDAO<Boolean>{

    private static String STATEMENT = "INSERT INTO \"BankAccount\"  (\"IBAN\" , bank_name , bankaccount_friendly_name ,company_id) VALUES ('?', '?', '?', '?');";

    /**
     * new bank account for the user
     */
    private BankAccount newBankAccount;

    protected CreateBankAccountDAO(Connection con, BankAccount ba) {
        super(con);
        newBankAccount = ba;
    }

    @Override
    protected void doAccess() throws Exception {
        outputParam = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{

            pstmt= con.prepareStatement(STATEMENT);

            pstmt.setString(1, newBankAccount.getIban());
            pstmt.setString(2, newBankAccount.getBankName());
            pstmt.setString(3, newBankAccount.getBankAccountFriendlyName());
            pstmt.setString(4, newBankAccount.getCompanyId());

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
