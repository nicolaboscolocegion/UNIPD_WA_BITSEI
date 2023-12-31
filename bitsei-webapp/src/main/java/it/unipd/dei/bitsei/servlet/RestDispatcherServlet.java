package it.unipd.dei.bitsei.servlet;

import it.unipd.dei.bitsei.dao.listing.ListInvoiceProductDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.*;
import it.unipd.dei.bitsei.rest.company.*;
import it.unipd.dei.bitsei.rest.customer.*;
import it.unipd.dei.bitsei.rest.listing.GetHomeDataRR;
import it.unipd.dei.bitsei.rest.product.CreateProductRR;
import it.unipd.dei.bitsei.rest.product.DeleteProductRR;
import it.unipd.dei.bitsei.rest.product.GetProductRR;
import it.unipd.dei.bitsei.rest.product.UpdateProductRR;
import it.unipd.dei.bitsei.rest.user.*;
import it.unipd.dei.bitsei.utils.RestURIParser;
import it.unipd.dei.bitsei.rest.documentation.*;
import it.unipd.dei.bitsei.rest.invoice.CreateInvoiceRR;
import it.unipd.dei.bitsei.rest.invoice.DeleteInvoiceRR;
import it.unipd.dei.bitsei.rest.invoice.GetInvoiceRR;
import it.unipd.dei.bitsei.rest.invoice.UpdateInvoiceRR;
import it.unipd.dei.bitsei.rest.invoiceproduct.CreateInvoiceProductRR;
import it.unipd.dei.bitsei.rest.invoiceproduct.DeleteInvoiceProductRR;
import it.unipd.dei.bitsei.rest.invoiceproduct.GetInvoiceProductRR;
import it.unipd.dei.bitsei.rest.invoiceproduct.UpdateInvoiceProductRR;
import it.unipd.dei.bitsei.rest.listing.*;
import it.unipd.dei.bitsei.rest.bankAccount.CreateBankAccountRR;
import it.unipd.dei.bitsei.rest.bankAccount.DeleteBankAccountRR;
import it.unipd.dei.bitsei.rest.bankAccount.GetBankAccountRR;
import it.unipd.dei.bitsei.rest.bankAccount.ListBankAccountsRR;
import it.unipd.dei.bitsei.rest.bankAccount.UpdateBankAccoutRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dispatches the request to the proper REST resource.
 */
public final class RestDispatcherServlet extends AbstractDatabaseServlet {
    /**
     * Default constructor.
     */
    public RestDispatcherServlet(){}

    /**
     * The JSON UTF-8 MIME media type
     */
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        final OutputStream out = res.getOutputStream();

        try {

            if(processHomeData(req, res)) {
                return;
            }

            // if the requested resource was an invoice, delegate its processing and return
            if(processCustomersReport(req, res)) {
                return;
            }
            // if the requested resource was an invoice, delegate its processing and return
            if(processProductsReport(req, res)) {
                return;
            }
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
            if(processBank(req, res)){
                return;
            }

            // if the requested resource was an User, delegate its processing and return
            if (processCustomer(req, res)) {
                return;
            }

            // if the requested resource was a product, delegate its processing and return
            if (processProduct(req, res)) {
                return;
            }

            // if the requested resource was an invoice, delegate its processing and return
            if (processInvoice(req, res)) {
                return;
            }



            // if the requested resource was an invoice, delegate its processing and return
            if (processInvoiceProduct(req, res)) {
                return;
            }

            if(processListInvoiceProduct(req, res)){
                return;
            }

            // if the requested resource was an invoice, delegate its processing and return
            if(processGetDocument(req, res)) {
                return;
            }

            // if the requested resource was a list of invoices, delegate its processing and return
            if (processListInvoice(req, res)) {
                return;
            }

            // if the requested resource was a list of customers, delegate its processing and return
            if (processListCustomer(req, res)) {
                return;
            }

            // if the requested resource was a list of products, delegate its processing and return
            if (processListProduct(req, res)) {
                return;
            }

            // if the requested resource was a list of filtered invoices, delegate its processing and return
            if(processListInvoiceByFilters(req, res)) {
                return;
            }

            // if the requested resource was a list of filtered invoices, delegate its processing and return
            if(processChartInvoiceByFilters(req, res)) {
                return;
            }

            // if the requested resource was an invoice, delegate its processing and return
            if(processCloseInvoice(req, res)) {
                return;
            }

            // if the requested resource was an invoice, delegate its processing and return
            if(processGenerateInvoice(req, res)) {
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

    private boolean processCloseInvoice(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;
        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }

        if (!r.getResource().equals("closeinvoice")) {
            return false;
        }
        // the request URI is: /user
        // if method GET, list users
        // if method POST, create user
        if (r.getResourceID() == -1) {

            switch (method) {
                default:
                    LOGGER.warn("Unsupported operation for URI /closeinvoice: %s.", method);

                    m = new Message("Unsupported operation for URI /closeinvoice.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        else {
            switch (method) {

                case "PUT":
                    new CloseInvoiceRR(req, res, getConnection(), super.getServletContext().getRealPath("/"), r).serve();
                    break;


                default:
                    LOGGER.warn("Unsupported operation for URI /closeinvoice: %s.", method);

                    m = new Message("Unsupported operation for URI /closeinvoice.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;


    }

    private boolean processGenerateInvoice(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }

        if (!r.getResource().equals("generateinvoice")) {
            LOGGER.info("Risorsa richiesta: " + r.getResource());
            return false;
        }
        // the request URI is: /user
        // if method GET, list users
        // if method POST, create user
        if (r.getResourceID() == -1) {

            switch (method) {
                default:
                    LOGGER.warn("Unsupported operation for URI /generateinvoice: %s.", method);

                    m = new Message("Unsupported operation for URI /generateinvoice.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        else {
            switch (method) {

                case "PUT":
                    new GenerateInvoiceRR(req, res, getConnection(), super.getServletContext().getRealPath("/"), r).serve();
                    break;


                default:
                    LOGGER.warn("Unsupported operation for URI /generateinvoice: %s.", method);

                    m = new Message("Unsupported operation for URI /generateinvoice.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;


    }


    private boolean processGetDocument(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }



        if (!r.getResource().equals("getdocument")) {
            LOGGER.info("Risorsa richiesta: " + r.getResource());
            return false;
        }


        String URI = req.getRequestURI();
        String[] parts = URI.split("/");
        String tmp = "\"URI: \" + URI + \" Parts:";
        for(int i=0; i < parts.length; i++)
            tmp += i + " " + parts[i] + " ";
        LOGGER.info(tmp);
        Integer document_type = Integer.parseInt(parts[4]);
        Integer company_id = Integer.parseInt(parts[6]);
        Integer invoice_id = Integer.parseInt(parts[8]);
        int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
        if (document_type < 0 || document_type > 2) {
            LOGGER.error("Document type not valid: " + parts[3]);
            return false;
        }

        // the request URI is: /user
        // if method GET, list users
        // if method POST, create user

        switch (method) {

            case "GET":
                new GetDocumentRR(req, res, getConnection(), super.getServletContext().getRealPath("/"), company_id, invoice_id, document_type).serve();
                break;


            default:
                LOGGER.warn("Unsupported operation for URI /getdocument: %s.", method);
                m = new Message("Unsupported operation for URI /getdocument.", "E4A5", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
                break;
        }


        return true;


    }

    private boolean processCustomersReport (final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }



        if (!r.getResource().equals("customerreport")) {
            LOGGER.info("Risorsa richiesta: " + r.getResource());
            return false;
        }


        if (r.getResourceID() == -1) {

            switch (method) {
                case "GET":
                    LOGGER.info("qui" + r.toString());
                    new GenerateCustomersReportRR(req, res, getConnection(), super.getServletContext().getRealPath("/"), r).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /customerreport: %s.", method);

                    m = new Message("Unsupported operation for URI /customerreport.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }



        return true;


    }


    private boolean processProductsReport (final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }



        if (!r.getResource().equals("productsreport")) {
            LOGGER.info("Risorsa richiesta: " + r.getResource());
            return false;
        }


        if (r.getResourceID() == -1) {

            switch (method) {
                case "GET":
                    new GenerateProductsReportRR(req, res, getConnection(), super.getServletContext().getRealPath("/"), r).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /productsreport: %s.", method);

                    m = new Message("Unsupported operation for URI /productsreport.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }



        return true;


    }

    /**
     * Checks whether the request if for an {@link User} resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for an {@code User}; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processBank(HttpServletRequest req, HttpServletResponse res) throws Exception{
        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;
        RestURIParser uri;

        LOGGER.info(path);
        try{
            uri = new RestURIParser(path);

        }catch(Exception e){
            return false;
        }

        if(!uri.getResource().equals("bankaccount")){
            return false;
        }

        if(uri.getResourceID()==-1){
            /*
            * GET: gets all the bank accounts of a given company
            * POST: creates a BANK account
            */
            switch (method) {
                case "GET":
                    new ListBankAccountsRR(req, res, getConnection()).serve();
                break;
                case "POST":
                    new CreateBankAccountRR(req, res, getConnection()).serve();
                break;
                default:
                    LOGGER.warn("Unsupported operation for URI /bankaccounts: {}.", method);

                    m = new Message("Unsupported operation for URI /bankaccounts.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    res.setContentType("text/plain");
                    m.toJSON(res.getOutputStream());
                break;
            }
        }else{
            switch (method) {
                /*
                * GET: gives a bank account
                * PUT: updates a bank account
                * DELETE: deletes bank account
                */
                case "GET":
                    new GetBankAccountRR(req, res, getConnection()).serve();
                break;
                case "PUT":
                    new UpdateBankAccoutRR(req, res, getConnection()).serve();
                break;
                case "DELETE":
                    new DeleteBankAccountRR(req, res, getConnection()).serve();
                break;
                default:
                    LOGGER.warn("Unsupported operation for URI /bankaccount: {}.", method);

                    m = new Message("Unsupported operation for URI /bankaccount.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    res.setContentType("text/plain");
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;
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
                    new GetUserRR(req, res, getConnection()).serve();
                    break;
                case "POST":
//                    new CreateUserRR(req, res, getConnection()).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /user: {}.", method);

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


    /**
     * Checks whether the request if for an {@link User} resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for an {@code User}; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processCustomer(final HttpServletRequest req, final HttpServletResponse res) throws Exception {

        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }



        if (!r.getResource().equals("customer")) {
            LOGGER.info("Risorsa richiesta: " + r.getResource());
            return false;
        }


        if (r.getResourceID() == -1) {

            switch (method) {

                case "POST":
                    new CreateCustomerRR(req, res, getConnection(), r).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /customer: %s.", method);

                    m = new Message("Unsupported operation for URI /customer.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        else {
            switch (method) {
                case "GET":
                    new GetCustomerRR(req, res, getConnection(), r).serve();
                    break;
                case "DELETE":
                    new DeleteCustomerRR(req, res, getConnection(), r).serve();
                    break;
                case "PUT":
                    new UpdateCustomerRR(req, res, getConnection(), r).serve();
                    break;


                default:
                    LOGGER.warn("Unsupported operation for URI /customer: %s.", method);

                    m = new Message("Unsupported operation for URI /customer.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;

    }

    /**
     * Checks whether the request if for an {@link User} resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for an {@code User}; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processProduct(final HttpServletRequest req, final HttpServletResponse res) throws Exception {

        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }



        if (!r.getResource().equals("product")) {
            LOGGER.info("Risorsa richiesta: " + r.getResource());
            return false;
        }


        if (r.getResourceID() == -1) {

            switch (method) {

                case "POST":
                    new CreateProductRR(req, res, getConnection(), r).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /product: %s.", method);

                    m = new Message("Unsupported operation for URI /product.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        else {
            switch (method) {
                case "GET":
                    new GetProductRR(req, res, getConnection(), r).serve();
                    break;
                case "DELETE":
                    new DeleteProductRR(req, res, getConnection(), r).serve();
                    break;
                case "PUT":
                    new UpdateProductRR(req, res, getConnection(), r).serve();
                    break;


                default:
                    LOGGER.warn("Unsupported operation for URI /product: %s.", method);

                    m = new Message("Unsupported operation for URI /product.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;

    }

    /**
     * Checks whether the request if for an {@link User} resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for an {@code User}; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processInvoice(final HttpServletRequest req, final HttpServletResponse res) throws Exception {

        Message m = null;

        final String method = req.getMethod();
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }



        if (!r.getResource().equals("invoice")) {
            LOGGER.info("Risorsa richiesta: " + r.getResource());
            return false;
        }


        if (r.getResourceID() == -1) {

            switch (method) {

                case "POST":
                    new CreateInvoiceRR(req, res, getConnection(), r).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /invoice: %s.", method);

                    m = new Message("Unsupported operation for URI /invoice.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        else {
            switch (method) {
                case "GET":
                    new GetInvoiceRR(req, res, getConnection(), r).serve();
                    break;
                case "DELETE":
                    new DeleteInvoiceRR(req, res, getConnection(), r).serve();
                    break;
                case "PUT":
                    new UpdateInvoiceRR(req, res, getConnection(), r).serve();
                    break;


                default:
                    LOGGER.warn("Unsupported operation for URI /invoice: %s.", method);

                    m = new Message("Unsupported operation for URI /invoice.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;

    }

    private boolean processHomeData(HttpServletRequest req, HttpServletResponse res) throws Exception{
        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message m = null;
        RestURIParser uri;

        LOGGER.info(path);
        try{
            uri = new RestURIParser(path);

        }catch(Exception e){
            return false;
        }

        if(!uri.getResource().equals("home-data")){
            return false;
        }

        /*
         * GET: gets all the bank accounts of a given company
         * POST: creates a BANK account
         */
        switch (method) {
            case "GET":
                new GetHomeDataRR(req, res, getConnection(), uri).serve();
                break;
            default:
                LOGGER.warn("Unsupported operation for URI /home-data: {}.", method);

                m = new Message("Unsupported operation for URI /home-data.", "E4A5",
                        String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                res.setContentType("text/plain");
                m.toJSON(res.getOutputStream());
                break;
        }




        return true;
    }

    /**
     * Checks whether the request if for an {@link User} resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for an {@code User}; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processInvoiceProduct(final HttpServletRequest req, final HttpServletResponse res) throws Exception {

        Message m = null;

        final String method = req.getMethod();
        String [] parts = req.getRequestURI().split("/");   ///    /rest/invoiceproduct/1/10/company/1
        String resource = parts[3];

        if (!resource.equals("invoiceproduct")) {
            LOGGER.info("Risorsa richiesta: " + resource);
            return false;
        }

        Integer companyID = null;
        Integer invoiceID = null;
        Integer productID = null;


        invoiceID = Integer.parseInt(parts[4]);
        productID = Integer.parseInt(parts[5]);

        if (!parts[6].equals("company")) {
            LOGGER.error("Bad URI format for invoiceproduct (company tag not specified)");
            return false;
        }

        companyID = Integer.parseInt(parts[7]);

        LOGGER.warn(String.valueOf(companyID), invoiceID, productID);

        switch (method) {
            case "GET":
                new GetInvoiceProductRR(req, res, getConnection(), companyID, invoiceID, productID).serve();
                break;
            case "POST":
                new CreateInvoiceProductRR(req, res, getConnection(), companyID, invoiceID, productID).serve();
                break;
            case "DELETE":
                new DeleteInvoiceProductRR(req, res, getConnection(), companyID, invoiceID, productID).serve();
                break;
            case "PUT":
                new UpdateInvoiceProductRR(req, res, getConnection(), companyID, invoiceID, productID).serve();
                break;


            default:
                LOGGER.warn("Unsupported operation for URI /invoiceproduct: %s.", method);

                m = new Message("Unsupported operation for URI /invoiceproduct.", "E4A5",
                        String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
                break;
        }


        return true;

    }


    /**
     * Checks whether the request is for a list of {@link Invoice}s resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for a list of {@code Invoice}s; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
        private boolean processListInvoiceProduct(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
            final String method = req.getMethod();

            String path = req.getRequestURI();
            Message m = null;

            // the requested resource was not a company
            if (path.lastIndexOf("rest/invoice") <= 0) {
                return false;
            }

            // strip everything until after the /invoice
            path = path.substring(path.lastIndexOf("invoice") + 7);

            // the request URI is: /company
            // if method GET, list companies
            // if method POST, create company
            if (path.matches("/\\d+/\\d+")) {
                // the request URI is: /company/image/{id}
                // if method GET, get company image
                if (method.equals("GET")) {
                    new ListInvoiceProductRR(req, res, getConnection()).serve();
                }
            }
            return true;

        }

    /**
     * Checks whether the request is for a list of {@link Invoice}s resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for a list of {@code Invoice}s; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processListInvoice(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String method = req.getMethod();
        Message m = null;
        RestURIParser r = null;


        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }

        if(!r.getResource().equals("list-invoice")) {
            return false;
        }
        final int company_id = r.getCompanyID();

        // the request URI is: /list-invoice
        // if method GET, list all invoices where CompanyId == currentUser_CompanyId
        if (r.getResourceID() == -1) {
            switch (method) {
                case "GET":
                    new ListInvoiceRR(req, res, getConnection(), company_id).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /list-invoice: %s.", method);

                    m = new Message("Unsupported operation for URI /list-invoice.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;
    }

    /**
     * Checks whether the request is for a list of {@link Customer}s resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for a list of {@code Customer}s; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processListCustomer(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String method = req.getMethod();
        Message m = null;
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }
        if(!r.getResource().equals("list-customer")) {
            return false;
        }
        final int company_id = r.getCompanyID();

        // the request URI is: /list-customer
        // if method GET, list all customers where CompanyId == currentUser_CompanyId
        if (r.getResourceID() == -1) {
            switch (method) {
                case "GET":
                    new ListCustomerRR(req, res, getConnection(), company_id).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /list-customer: %s.", method);

                    m = new Message("Unsupported operation for URI /list-customer.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;
    }

    /**
     * Checks whether the request is for a list of {@link Product}s resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for a list of {@code Product}s; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processListProduct(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String method = req.getMethod();
        Message m = null;
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }
        if(!r.getResource().equals("list-product")) {
            return false;
        }
        final int company_id = r.getCompanyID();

        // the request URI is: /list-product
        // if method GET, list all products where CompanyId == currentUser_CompanyId
        if (r.getResourceID() == -1) {
            switch (method) {
                case "GET":
                    new ListProductRR(req, res, getConnection(), company_id).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /list-product: %s.", method);

                    m = new Message("Unsupported operation for URI /list-product.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }

        return true;
    }


    /**
     * Checks whether the request is for a list of filtered {@link Invoice}s resource and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for a filtered list of {@code Invoice}s; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processListInvoiceByFilters(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String method = req.getMethod();
        Message m = null;
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }
        if(!r.getResource().equals("filter-invoices")) {
            return false;
        }
        final int company_id = r.getCompanyID();

        List<String> filterList = List.of("fromTotal", "toTotal", "fromDiscount", "toDiscount", "fromPfr", "toPfr", "fromInvoiceDate", "toInvoiceDate", "fromWarningDate", "toWarningDate", "fromCustomerId", "fromProductId", "fromStatus");
        // the request URI contains filter(s)
        Map<String, String> requestData = checkFilterPath(filterList, req, res, m);;

        // it enters here if the user parsed illegal filters
        if (requestData == null) {
            // instead of throwing error list all the invoices
            new ListInvoiceRR(req, res, getConnection(), company_id).serve();
        }
        else {
            switch (method) {
                case "POST":
                    new ListInvoiceByFiltersRR(req, res, getConnection(), company_id, requestData).serve();
                    break;
                default:
                    LOGGER.warn("Unsupported operation for URI /filter-invoices %s.", method);

                    m = new Message("Unsupported operation for URI /filter-invoices.", "E4A5",
                            String.format("Requested operation %s.", method));
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    m.toJSON(res.getOutputStream());
                    break;
            }
        }


        return true;
    }


    private Map<String, String> checkFilterPath(List<String> filterList, HttpServletRequest req, HttpServletResponse res, Message m) throws IOException {
        String request = req.getReader().lines().collect(Collectors.joining());

        // Remove escape characters from the request
        request = request.replaceAll("\\\\", "");

        LOGGER.info("## checkPath func: Request: " + request + " ##");
        Map<String, String> requestData = new HashMap<>();

        // Split the request in a list of key-value pairs
        String[] requestLines = request.split(",");

        try {
            // Parse every key-value pair and add it to the map
            for(String line : requestLines) {
                String[] keyValue = line.split(":");
                // Clean the data before parsing
                String key = keyValue[0].trim().replaceAll("\"", "");
                key = key.replaceAll("\\s+", "");
                key = key.replaceAll("[\\[\\](){}]","");

                if(keyValue.length > 1) {
                    String value = keyValue[1].trim().replaceAll("\"", "");
                    value = value.replaceAll("[\\[\\](){}]","");

                    if(!filterList.contains(key)) {
                        LOGGER.warn("## checkPath func: Filter {" + key + "} Not Supported. Requested URI: %s. ##", req.getRequestURI());
                        LOGGER.warn("## checkPath func: Available filters: " + filterList + " ##");
                        m = new Message("## checkPath func: Filter {" + key + "} Not Supported. ##", "E4A7",
                                String.format("Requested URI: %s.", req.getRequestURI()));
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        m.toJSON(res.getOutputStream());
                        return null;
                    }
                    else {
                        requestData.put(key, value);
                        LOGGER.info("##  checkPath func: Filter {" + key + "} found! Value {" + value + "} ##");
                    }
                }
                else {
                    LOGGER.warn("## checkPath func: KeyValue split for {" + key + "} has length < 1. Requested URI: %s. ##", req.getRequestURI());
                }

            }
        } catch (Exception e) {
            LOGGER.error("## checkPath func: Unexpected exception thrown: " + e + " ##");

            m = new Message("## checkPath func: Unexpected exception thrown: " + e + " ##", "E4A7",
                    String.format("Requested URI: %s.", req.getRequestURI()));
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
            return null;
        }
        return requestData;
    }


    /**
     * Checks whether the request is for plot a filtered chart and, in case, processes it.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request was for plot a filtered chart; {@code false} otherwise.
     * @throws Exception if any error occurs.
     */
    private boolean processChartInvoiceByFilters(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String method = req.getMethod();
        Message m = null;
        RestURIParser r = null;

        try {
            r = new RestURIParser(req.getRequestURI());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("URI INVALID: \n" + req.getRequestURI());
            return false;
        }
        if(!r.getResource().equals("charts")) {
            return false;
        }
        final int company_id = r.getCompanyID();

        List<String> filterList = List.of("fromTotal", "toTotal", "fromDiscount", "toDiscount", "fromPfr", "toPfr", "fromInvoiceDate", "toInvoiceDate", "fromWarningDate", "toWarningDate", "fromCustomerId", "fromProductId", "fromStatus", "chart_type", "chart_period");
        // the request URI contains filters or the chart type/period
        Map<String, String> requestData = checkFilterPath(filterList, req, res, m);

        switch (method) {
            case "POST":
                new PlotChartRR(req, res, getConnection(), company_id, requestData).serve();
                break;
            default:
                LOGGER.warn("Unsupported operation for URI /charts %s.", method);

                m = new Message("Unsupported operation for URI /charts.", "E4A5",
                        String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(res.getOutputStream());
                break;
        }

        return true;
    }






}


