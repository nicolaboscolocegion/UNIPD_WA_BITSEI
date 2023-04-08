package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.resources.User;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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

            // if the requested resource was an User, delegate its processing and return
            if (processUser(req, res)) {
                return;
            }

            // if the requested resource was an Invoice, delegate its processing and return
            if (processFilterInvoices(req, res)) {
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

        // the requested resource was not an user
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
        }

        return true;

    }

    /**
     * Checks whether the request is for an {@link it.unipd.dei.bitsei.resources.Invoice} resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for an {@code Invoice}; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processFilterInvoices(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;

        // the requested resource was not a filter-invoices
        if (path.lastIndexOf("rest/filter-invoices") <= 0) {
            return false;
        }

        // strip everything until after the /filter-invoices
        path = path.substring(path.lastIndexOf("filter-invoices") + "filter-invoices".length());

        // the request URI is: /filter-invoices
        // if method GET, list all invoices where CompanyId == currentUser_CompanyId
        // if method POST, list invoices by filters
        if (path.length() == 0 || path.equals("/")) {

            switch (method) {
                case "GET":
                    int companyId = -1; //TODO: replace with currentUser_CompanyId, ask to autent. subgroup
                    new ListInvoicesRR(req, res, getConnection(), companyId).serve();
                    break;
                case "POST":
                    //new ListFilteredInvoicesRR(req, res, getConnection()).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /user: %s.", method);

                    m = new Message("Unsupported operation for URI /user.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        } else {
            List<String> filterList = List.of("fromTotal", "toTotal", "fromDiscount", "toDiscount", "fromPfr", "toPfr", "fromInvoiceDate", "toInvoiceDate", "fromWarningDate", "toWarningDate");
            // the request URI contains filter(s)
            boolean checkFilters = false;
            for(String filter : filterList) {
                boolean tmp = checkPath(path, filter, req, res, m);
                if(tmp)
                    path = path.substring(path.lastIndexOf(filter) + filter.length());
                checkFilters = checkFilters || tmp;
            }
            if (!checkFilters) {
                LOGGER.error("## REST DISPATCHER SERVLET: ERROR IN \"if(!checkFilters)\" ##");
                m = new Message("## REST DISPATCHER SERVLET: ERROR IN \"if(!checkFilters)\" ##", "E4A8",
                        String.format("Requested URI: %s.", req.getRequestURI()));
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
            }
            else {
                switch (method) {
                    case "GET":
                        new ListInvoicesRR(req, res, getConnection(), -1).serve();

                        break;
                    default:
                        LOGGER.warn("Unsupported operation for URI /employee/salary/{salary}: %s.", method);

                        m = new Message("Unsupported operation for URI /employee/salary/{salary}.", "E4A5",
                                String.format("Requested operation %s.", method));
                        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        m.toJSON(res.getOutputStream());
                        break;
                }
            }

        }

        return true;
    }

    private boolean checkPath(String path, String filter, HttpServletRequest req, HttpServletResponse res, Message m) throws IOException {
        try {
            if(path.contains(filter)) {
                path = path.substring(path.lastIndexOf(filter) + filter.length() + 1);
                path = path.substring(0, path.indexOf("/"));

                if (path.length() == 0 || path.equals("/")) {
                    LOGGER.warn("Wrong format for URI /filter-invoices/" + filter + "/{" + filter + "}: no {" + filter + "} specified. Requested URI: %s.", req.getRequestURI());

                    m = new Message("Wrong format for URI /filter-invoices/" + filter + "/{" + filter + "}: no {" + filter + "} specified.", "E4A7",
                            String.format("Requested URI: %s.", req.getRequestURI()));
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    m.toJSON(res.getOutputStream());
                    return false;
                }
            }
            else {
                return false;
            }
        } catch (IOException ex) {
            LOGGER.error("Unexpected error while processing the checkPath function.");

            m = new Message("Unexpected error in function checkPath.", "E5A1",
                    String.format("Requested URI: %s.", req.getRequestURI()));
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
            return false;
        }
        LOGGER.info("##  checkPath func: filter: " + filter + " found! Value: " + path + " ##");
        return true;
    }
}
