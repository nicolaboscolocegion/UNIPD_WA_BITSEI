package it.unipd.dei.bitsei.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import it.unipd.dei.bitsei.resources.BankAccount;
import it.unipd.dei.bitsei.resources.Company;

/**
 * Gives all the bank accounts of a company
 */
public class ListBankAccountsDAO extends AbstractDAO<List<BankAccount>>{

    private static final String STATEMENT="select * from \"BankAccount\" where company_id=?";
    private final Company company;

    /**
     * constructor for: retrives all bank account in a list by givin a certain company
     * @param con connettion of the database
     * @param c company to search
     */
    public ListBankAccountsDAO(Connection con, Company c) {
        super(con);
        this.company = c;
    }

    /**
     * retrives all bank account in a list by givin a certain company
     */
    @Override
    public void doAccess() throws SQLException {
        outputParam = null;
        List<BankAccount> bankAccountList =new LinkedList<BankAccount>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            //execute the query
            pstmt= con.prepareStatement(STATEMENT);
            
            pstmt.setInt(1, company.getCompany_id());
            

            rs = pstmt.executeQuery();

            while (rs.next()) {
                bankAccountList.add(
                    new BankAccount(
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
