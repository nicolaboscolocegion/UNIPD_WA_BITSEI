package it.unipd.dei.bitsei.rest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import it.unipd.dei.bitsei.dao.CreateBankAccountDAO;
import it.unipd.dei.bitsei.dao.DeleteBankAccountDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.BankAccount;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteBankAccountRR extends AbstractRR{

    
    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    protected DeleteBankAccountRR(String action, HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.DELETE_BANK_ACCOUNT, req, res, con);
    }


    /**
     * creates bank a new bank account
     */
    @Override
    protected void doServe() throws IOException {
        Message m;
        InputStream requestStream = req.getInputStream();

        try{
            //find if there is a new bank accoount in the response
            BankAccount bankAccount= BankAccount.fromJSON(requestStream);
            
            //try to change the bank accoutn
            boolean delited = new DeleteBankAccountDAO(con, bankAccount).access().getOutputParam();

            if(delited){
                res.setStatus(HttpServletResponse.SC_OK);
                LOGGER.info("delete bank account with iban: " + bankAccount.getIban());
            }else{
                LOGGER.error("Fatal error while creating.");

                m = new Message("Cannot delete the bank account", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
            }
        }catch(SQLException e){
            LOGGER.error("Cannot dlete bank account: unexpected error while accessing the database.", e);

            m = new Message("Cannot delete bank account: unexpected error while accessing the database.", "E5A1", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
    

