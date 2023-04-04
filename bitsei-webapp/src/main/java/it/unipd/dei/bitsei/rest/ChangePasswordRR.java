package it.unipd.dei.bitsei.rest;

import it.unipd.dei.bitsei.dao.UpdateUserPasswordDAO;
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
        Message m = null;
        boolean is_done = false;

        try {
            final ChangePassword data = ChangePassword.fromJSON(req.getInputStream());

            is_done = new UpdateUserPasswordDAO(con, data).access().getOutputParam();

            if (is_done) {
                LOGGER.info("User successfully found.");
                m = new Message("Successfully done, login", null, null);
                res.setStatus(HttpServletResponse.SC_OK);
                m.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while getting user.");

                m = new Message("Cannot get user: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            if(con != null){
                try {
                    con.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.error("Cannot get user: unexpected database error.", ex);

            m = new Message("Cannot get user: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}

