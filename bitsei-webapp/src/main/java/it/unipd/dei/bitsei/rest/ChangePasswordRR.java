package it.unipd.dei.bitsei.rest;

import it.unipd.dei.bitsei.dao.GetUserIDFromTokenDAO;
import it.unipd.dei.bitsei.resources.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ChangePasswordRR extends AbstractRR {
    public ChangePasswordRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.CHANGE_PASSWORD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        User user = null;
        Message m = null;
        int user_id = -1;

        try {
            final ChangePassword data = ChangePassword.fromJSON(req.getInputStream());
            user_id = new GetUserIDFromTokenDAO(con, data.getReset_token()).access().getOutputParam();
            // TODO: Check the user id existence
            // TODO: Hash the password and create user modified object
            // TODO: Create Modify User DAO
            user = new ModifyUserDAO(con, data).access().getOutputParam();

            if (user != null) {
                LOGGER.info("User successfully found.");
                // TODO: send the user object to the client
                m = new Message("Successfully done.", null, null);
                res.setStatus(HttpServletResponse.SC_OK);
                m.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while getting user.");

                m = new Message("Cannot get user: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot get user: unexpected database error.", ex);

            m = new Message("Cannot get user: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}

