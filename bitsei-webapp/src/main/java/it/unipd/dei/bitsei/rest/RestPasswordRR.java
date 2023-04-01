package it.unipd.dei.bitsei.rest;

import it.unipd.dei.bitsei.dao.GetUserDAO;
import it.unipd.dei.bitsei.resources.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RestPasswordRR extends AbstractRR {
    public RestPasswordRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.REST_PASSWORD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        User user = null;
        Message m = null;

        try {
            final StringValue email = StringValue.fromJSON(req.getInputStream(), "email");
            user = new GetUserDAO(con, email.getValue()).access().getOutputParam();

            if (user != null) {
                LOGGER.info("User successfully found.");

                // TODO: Generate a hash and send it to the user's email to reset password using it.

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

