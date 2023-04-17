package it.unipd.dei.bitsei.rest;

import it.unipd.dei.bitsei.dao.ListInvoiceByFiltersDAO;
import it.unipd.dei.bitsei.dao.ListInvoiceDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.ResourceList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A REST resource for listing {@link Invoice}s.
 */
public final class ListInvoiceByFiltersRR extends AbstractRR {
    private final int companyId;
    /**
     * Creates a new REST resource for listing {@code Invoice}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     * @param
     */
    public ListInvoiceByFiltersRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int companyId) {
        super(Actions.LIST_INVOICES_BY_FILTERS, req, res, con);
        this.companyId = companyId;
    }

    @Override
    protected void doServe() throws IOException {

        List<Invoice> el = null;
        Message m = null;

        try {
            boolean filterByTotal = false;
            double fromTotal = -1;
            double toTotal = -1;

            boolean filterByDiscount = false;
            double fromDiscount = -1;
            double toDiscount = -1;

            boolean filterByPfr = false;
            double fromPfr = -1;
            double toPfr = -1;

            boolean filterByInvoiceDate = false;
            Date fromInvoiceDate = null;
            Date toInvoiceDate = null;

            boolean filterByWarningDate = false;
            Date fromWarningDate = null;
            Date toWarningDate = null;


            String path = req.getRequestURI();
            String[] parsePath = path.split("/");
            
            for(int i=0; i<parsePath.length-1; i++) {
                if(parsePath[i].equals("filterByTotal"))
                    filterByTotal = true;
                if(parsePath[i].equals("fromTotal"))
                    fromTotal = Double.parseDouble(parsePath[++i]);
                if(parsePath[i].equals("toTotal"))
                    toTotal = Double.parseDouble(parsePath[++i]);
                
                if(parsePath[i].equals("filterByDiscount"))
                    filterByDiscount = true;
                if(parsePath[i].equals("fromDiscount"))
                    fromDiscount = Double.parseDouble(parsePath[++i]);
                if(parsePath[i].equals("toDiscount"))
                    toDiscount = Double.parseDouble(parsePath[++i]);
                
                if(parsePath[i].equals("filterByPfr"))
                    filterByPfr = true;
                if(parsePath[i].equals("fromPfr"))
                    fromPfr = Double.parseDouble(parsePath[++i]);
                if(parsePath[i].equals("toPfr"))
                    toPfr = Double.parseDouble(parsePath[++i]);
                
                if(parsePath[i].equals("filterByInvoiceDate"))
                    filterByInvoiceDate = true;
                if(parsePath[i].equals("fromInvoiceDate"))
                    fromInvoiceDate = Date.valueOf(parsePath[++i]);
                if(parsePath[i].equals("toInvoiceDate"))
                    toInvoiceDate = Date.valueOf(parsePath[++i]);

                if(parsePath[i].equals("filterByWarningDate"))
                    filterByTotal = true;
                if(parsePath[i].equals("fromWarningDate"))
                    fromWarningDate = Date.valueOf(parsePath[++i]);
                if(parsePath[i].equals("toWarningDate"))
                    toWarningDate = Date.valueOf(parsePath[++i]);
            }

            LOGGER.info("## ListInvoiceByFiltersRR: filterByTotal: " + filterByTotal + " fromTotal: " + fromTotal + " toTotal: " + toTotal + " filterByDiscount: " + filterByDiscount + " fromDiscount: " + fromDiscount + " toDiscount: " + toDiscount + " filterByPfr: " + filterByPfr + " fromPfr: " + fromPfr + " toPfr: " + toPfr + " filterByInvoiceDate: " + filterByInvoiceDate + " fromInvoiceDate: " + fromInvoiceDate + " toInvoiceDate: " + toInvoiceDate + " filterByWarningDate: " + filterByWarningDate + " fromWarningDate: " + fromWarningDate + " toWarningDate: " + toWarningDate);

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceByFiltersDAO(con, companyId,
                    filterByTotal, fromTotal, toTotal,
                    filterByDiscount, fromDiscount, toDiscount,
                    filterByPfr, fromPfr, toPfr,
                    filterByInvoiceDate, fromInvoiceDate, toInvoiceDate,
                    filterByWarningDate, fromWarningDate, toWarningDate
                    ).access().getOutputParam();

            if (el != null) {
                LOGGER.info("## ListInvoiceByFiltersRR: Invoice(s) successfully listed. ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## ListInvoiceByFiltersRR: Fatal error while listing invoice(s). ##");

                m = new Message("## ListInvoiceByFiltersRR: Cannot list invoice(s): unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## ListInvoiceByFiltersRR: Cannot list invoice(s): unexpected database error. ##", ex);

            m = new Message("## ListInvoiceByFiltersRR: Cannot list invoice(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}
