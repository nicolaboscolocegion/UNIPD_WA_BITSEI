package it.unipd.dei.bitsei.rest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import it.unipd.dei.bitsei.dao.UpdateBankAccountDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.BankAccount;
import it.unipd.dei.bitsei.resources.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ChangeBankAccoutRR extends AbstractRR{
    /**
     * Control the authentication of the user
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ChangeBankAccoutRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CHANGE_BANK_ACCOUNT, req, res, con);
    }

    /**
     * updates a bank account 
     */
    @Override
    protected void doServe() throws IOException {
        Message m;
        InputStream requestStream = req.getInputStream();

        try{
            //find the old and the new bank account to update
            BankAccount oldBankAccount= BankAccount.fromJSON(requestStream);
            BankAccount newBankAccount= BankAccount.fromJSON(requestStream);

            //try to change the bank accoutn
            boolean updated = new UpdateBankAccountDAO(con, oldBankAccount, newBankAccount).access().getOutputParam();

            if(updated){
                res.setStatus(HttpServletResponse.SC_OK);
                LOGGER.info("changed " + oldBankAccount.getIban() +  " to "  + newBankAccount.getIban());
            }else{
                LOGGER.error("Fatal error while getting user.");

                m = new Message("Cannot change password: the bank account", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        }catch(SQLException e){
            LOGGER.error("Cannot search for user: unexpected error while accessing the database.", e);

            m = new Message("Cannot search for user: unexpected error while accessing the database.", "E5A1", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
    
}
