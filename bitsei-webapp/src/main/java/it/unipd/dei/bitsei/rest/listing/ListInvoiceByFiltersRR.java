package it.unipd.dei.bitsei.rest.listing;

import it.unipd.dei.bitsei.dao.listing.ListInvoiceByFiltersDAO;
import it.unipd.dei.bitsei.resources.*;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * A REST resource for listing {@link Invoice}s.
 */
public final class ListInvoiceByFiltersRR extends AbstractRR {

    private static final double FROM_DOUBLE = 0.00;
    private static final double TO_DOUBLE = 9E10;

    private static final Date FROM_DATE = Date.valueOf("1970-01-01");
    LocalDate currentDate = LocalDate.now();
    final Date TO_DATE = Date.valueOf(currentDate);

    private final int ownerId;
    private final Map<String, String> requestData;
    /**
     * Creates a new REST resource for listing {@code Invoice}s.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListInvoiceByFiltersRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int ownerId, Map<String, String> requestData) {
        super(Actions.LIST_INVOICES_BY_FILTERS, req, res, con);
        this.ownerId = ownerId;
        this.requestData = requestData;
    }

    @Override
    protected void doServe() throws IOException {

        List<Invoice> el = null;
        Message m = null;

        try {
            boolean filterByTotal = false;
            double fromTotal = FROM_DOUBLE;
            double toTotal = TO_DOUBLE;

            boolean filterByDiscount = false;
            double fromDiscount = FROM_DOUBLE;
            double toDiscount = TO_DOUBLE;

            boolean filterByPfr = false;
            double fromPfr = FROM_DOUBLE;
            double toPfr = TO_DOUBLE;

            boolean filterByInvoiceDate = false;
            Date fromInvoiceDate = FROM_DATE;
            Date toInvoiceDate = TO_DATE;

            boolean filterByWarningDate = false;
            Date fromWarningDate = FROM_DATE;
            Date toWarningDate = TO_DATE;

            boolean filterByBusinessName = false;
            List<String> fromBusinessName = new ArrayList<String>();

            boolean filterByProductTitle = false;
            List<String> fromProductTitle = new ArrayList<String>();

            //TODO: add checks on "Double.parseDouble" and on "Date.valueOf"
            for(String filter : requestData.keySet()) {
                if(filter.equals("fromTotal")) {
                    filterByTotal = true;
                    fromTotal = Double.parseDouble(requestData.get(filter));
                }
                if(filter.equals("toTotal")) {
                    filterByTotal = true;
                    toTotal = Double.parseDouble(requestData.get(filter));
                }
                if(filter.equals("fromDiscount")) {
                    filterByDiscount = true;
                    fromDiscount = Double.parseDouble(requestData.get(filter));
                }
                if(filter.equals("toDiscount")) {
                    filterByDiscount = true;
                    toDiscount = Double.parseDouble(requestData.get(filter));
                }
                if(filter.equals("fromPfr")) {
                    filterByPfr = true;
                    fromPfr = Double.parseDouble(requestData.get(filter));
                }
                if(filter.equals("toPfr")) {
                    filterByPfr = true;
                    toPfr = Double.parseDouble(requestData.get(filter));
                }
                if(filter.equals("fromInvoiceDate")) {
                    filterByInvoiceDate = true;
                    fromInvoiceDate = Date.valueOf(requestData.get(filter));
                }
                if(filter.equals("toInvoiceDate")) {
                    filterByInvoiceDate = true;
                    toInvoiceDate = Date.valueOf(requestData.get(filter));
                }
                if(filter.equals("fromWarningDate")) {
                    filterByWarningDate = true;
                    fromWarningDate = Date.valueOf(requestData.get(filter));
                }
                if(filter.equals("toWarningDate")) {
                    filterByWarningDate = true;
                    toWarningDate = Date.valueOf(requestData.get(filter));
                }
                if(filter.equals("fromBusinessName")) {
                    filterByBusinessName = true;
                    String businessNames = requestData.get(filter);
                    String[] parsedBusinessNames = businessNames.split("---");
                    for(String s : parsedBusinessNames)
                        fromBusinessName.add(s);
                }
                if(filter.equals("fromProductTitle")) {
                    filterByProductTitle = true;
                    String productTitles = requestData.get(filter);
                    String[] parsedProductTitles = productTitles.split("---");
                    for(String s : parsedProductTitles)
                        fromProductTitle.add(s);
                }
            }

            LOGGER.info("## ListInvoiceByFiltersRR: filterByTotal: " + filterByTotal + " fromTotal: " + fromTotal + " toTotal: " + toTotal + " filterByDiscount: " + filterByDiscount + " fromDiscount: " + fromDiscount + " toDiscount: " + toDiscount + " filterByPfr: " + filterByPfr + " fromPfr: " + fromPfr + " toPfr: " + toPfr + " filterByInvoiceDate: " + filterByInvoiceDate + " fromInvoiceDate: " + fromInvoiceDate + " toInvoiceDate: " + toInvoiceDate + " filterByWarningDate: " + filterByWarningDate + " fromWarningDate: " + fromWarningDate + " toWarningDate: " + toWarningDate + " fromBusinessName: " + fromBusinessName + " fromProductTitle: " + fromProductTitle);

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceByFiltersDAO(con, ownerId,
                    filterByTotal, fromTotal, toTotal,
                    filterByDiscount, fromDiscount, toDiscount,
                    filterByPfr, fromPfr, toPfr,
                    filterByInvoiceDate, fromInvoiceDate, toInvoiceDate,
                    filterByWarningDate, fromWarningDate, toWarningDate,
                    filterByBusinessName, fromBusinessName,
                    filterByProductTitle, fromProductTitle
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
