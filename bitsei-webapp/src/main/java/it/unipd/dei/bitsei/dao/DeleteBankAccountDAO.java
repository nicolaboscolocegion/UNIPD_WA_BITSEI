package it.unipd.dei.bitsei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.dei.bitsei.resources.BankAccount;

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
            pstmt.setString(2, bankAccount.getCompanyId());

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
