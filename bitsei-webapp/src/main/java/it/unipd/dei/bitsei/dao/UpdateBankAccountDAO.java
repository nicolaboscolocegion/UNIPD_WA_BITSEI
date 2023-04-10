package it.unipd.dei.bitsei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.dei.bitsei.resources.BankAccount;

/**
 * updates the bank account
 */
public class UpdateBankAccountDAO extends AbstractDAO<Boolean>{

    private static String STATEMENT ="UPDATE \"BankAccount\" SET \"IBAN\" ='?', bank_name ='?', bankaccount_friendly_name='?' WHERE company_id ='?' and \"IBAN\" ='?';";

    private BankAccount oldBankAccount;
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
            
            pstmt.setString(4, oldBankAccount.getCompanyId());
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
