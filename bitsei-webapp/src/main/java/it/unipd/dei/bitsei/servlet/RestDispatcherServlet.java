package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.resources.User;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Dispatches the request to the proper REST resource.
 */
public final class RestDispatcherServlet extends AbstractDatabaseServlet {

    /**
     * The JSON UTF-8 MIME media type
     */
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        final OutputStream out = res.getOutputStream();

        try {

            // if the requested resource was a User, delegate its processing and return
            if (processUser(req, res)) {
                return;
            }
            if (processLogin(req, res)) {
                return;
            }
            if (processCompany(req, res)) {
                return;
            }

            // if none of the above process methods succeeds, it means an unknown resource has been requested
            LOGGER.warn("Unknown resource requested: %s.", req.getRequestURI());

            final Message m = new Message("Unknown resource requested.", "E4A6",
                    String.format("Requested resource is %s.", req.getRequestURI()));
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.setContentType(JSON_UTF_8_MEDIA_TYPE);
            m.toJSON(out);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error while processing the REST resource.", t);

            final Message m = new Message("Unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(out);
        } finally {

            // ensure to always flush and close the output stream
            if (out != null) {
                out.flush();
                out.close();
            }

            LogContext.removeIPAddress();
        }
    }


    /**
     * Checks whether the request if for an {@link User} resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for an {@code User}; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processUser(final HttpServletRequest req, final HttpServletResponse res) throws Exception {

        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a user
        if (path.lastIndexOf("rest/user") <= 0) {
            return false;
        }

        // strip everything until after the /user
        path = path.substring(path.lastIndexOf("user") + 4);

        // the request URI is: /user
        // if method GET, list users
        // if method POST, create user
        if (path.length() == 0 || path.equals("/")) {

            switch (method) {
                case "GET":
                    new ListUserRR(req, res, getConnection()).serve();
                    break;
                case "POST":
//                    new CreateUserRR(req, res, getConnection()).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /user: %s.", method);

                    m = new Message("Unsupported operation for URI /user.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else if (path.equals("/reset-password")) {
            if (method.equals("POST")) {
                new RestPasswordRR(req, res, getConnection()).serve();
            }
        } else if (path.equals("/change-password")) {
            if (method.equals("POST")) {
                new ChangePasswordRR(req, res, getConnection()).serve();
            }
        }


        return true;

    }

    private boolean processLogin(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a login
        if (path.lastIndexOf("rest/login") <= 0) {
            return false;
        }

        // strip everything until after the /login
        path = path.substring(path.lastIndexOf("login") + 5);

        // the request URI is: /login
        // if method POST, authenticate the user
        if (path.length() == 0 || path.equals("/")) {
            if (method.equals("POST")) {
                new LoginUserRR(req, res, getConnection()).serve();
            } else {
                LOGGER.warn("Unsupported operation for URI /user: {}.", method);

                m = new Message("Unsupported operation for URI /user.", "E4A5",
                        String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
            }
        } else if (path.equals("/reset-password")){
            if (method.equals("POST")){
                new RestPasswordRR(req, res, getConnection()).serve();
            }
        } else if (path.equals("/change-password")){
            if (method.equals("POST")){
                new ChangePasswordRR(req, res, getConnection()).serve();
            }
        }


        return true;

    }


    private boolean processCompany(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a company
        if (path.lastIndexOf("rest/company") <= 0) {
            return false;
        }

        // strip everything until after the /company
        path = path.substring(path.lastIndexOf("company") + 7);

        // the request URI is: /company
        // if method GET, list companies
        // if method POST, create company
        if (path.length() == 0 || path.equals("/")) {
            switch (method) {
                case "GET" -> new ListCompanyRR(req, res, getConnection()).serve();
                case "POST" -> new CreateCompanyRR(req, res, getConnection()).serve();
                default -> {
                    LOGGER.warn("Unsupported operation for URI /user: {}.", method);
                    m = new Message("Unsupported operation for URI /company.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                }
            }
        } else if (path.matches("/image/\\d+")) {
            // the request URI is: /company/image/{id}
            // if method GET, get company image
            // TODO: change the sql query to get the image from the database table company not company2
            if (method.equals("GET")) {
                new GetCompanyImageRR(req, res, getConnection()).serve();
            }
        } else if (path.matches("/\\d+")) {
            // the request URI is: /company/{id}
            // if method GET, get company
            // if method PUT, update company
            // if method DELETE, delete company
            switch (method) {
                case "GET" -> new GetCompanyRR(req, res, getConnection()).serve();
                // TODO: change the sql query to get the image from the database table company not company2
                case "PUT" -> new UpdateCompanyRR(req, res, getConnection()).serve();
                // TODO: implement delete company -> as there is a lot of constraints we should delete them first in the right order
                // after that we can delete the company itself. -> use atomic transactions
                case "DELETE" -> new DeleteCompanyRR(req, res, getConnection()).serve();
                default -> {
                    LOGGER.warn("Unsupported operation for URI /company: {}.", method);
                    m = new Message("Unsupported operation for URI /company.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                }
            }
        }
        return true;
    }
}
