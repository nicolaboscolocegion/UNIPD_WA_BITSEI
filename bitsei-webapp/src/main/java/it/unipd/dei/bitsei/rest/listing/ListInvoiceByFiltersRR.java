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

    private final int company_id;
    private final Map<String, String> requestData;

    /**
     * Creates a new REST resource for listing {@code Invoice}s.
     *
     * @param req         the HTTP request.
     * @param res         the HTTP response.
     * @param con         the connection to the database.
     * @param company_id  the company id to be used for getting the invoice(s).
     * @param requestData the request data to be used for getting the invoice(s).
     */
    public ListInvoiceByFiltersRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int company_id, Map<String, String> requestData) {
        super(Actions.LIST_INVOICES_BY_FILTERS, req, res, con);
        this.company_id = company_id;
        this.requestData = requestData;
    }

    @Override
    protected void doServe() throws IOException {
        final int owner_id;
        List<InvoiceContainer> el = null;
        Message m = null;

        try {
            owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());

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

            boolean filterByCustomerId = false;
            List<Integer> fromCustomerId = new ArrayList<Integer>();

            boolean filterByProductId= false;
            List<Integer> fromProductId = new ArrayList<Integer>();

            boolean filterByStatus = false;
            List<Integer> fromStatus = new ArrayList<Integer>();

            for (String filter : requestData.keySet()) {
                if (filter.equals("fromTotal")) {
                    filterByTotal = true;
                    try {
                        fromTotal = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromTotal = FROM_DOUBLE;
                    }
                }
                if (filter.equals("toTotal")) {
                    filterByTotal = true;
                    try {
                        toTotal = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toTotal = TO_DOUBLE;
                    }
                }
                if (filter.equals("fromDiscount")) {
                    filterByDiscount = true;
                    try {
                        fromDiscount = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromDiscount = FROM_DOUBLE;
                    }
                }
                if (filter.equals("toDiscount")) {
                    filterByDiscount = true;
                    try {
                        toDiscount = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toDiscount = TO_DOUBLE;
                    }
                }
                if (filter.equals("fromPfr")) {
                    filterByPfr = true;
                    try {
                        fromPfr = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromPfr = FROM_DOUBLE;
                    }
                }
                if (filter.equals("toPfr")) {
                    filterByPfr = true;
                    try {
                        toPfr = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toPfr = TO_DOUBLE;
                    }
                }
                if (filter.equals("fromInvoiceDate")) {
                    filterByInvoiceDate = true;
                    try {
                        fromInvoiceDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromInvoiceDate = FROM_DATE;
                    }
                }
                if (filter.equals("toInvoiceDate")) {
                    filterByInvoiceDate = true;
                    try {
                        toInvoiceDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toInvoiceDate = TO_DATE;
                    }
                }
                if (filter.equals("fromWarningDate")) {
                    filterByWarningDate = true;
                    try {
                        fromWarningDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromWarningDate = FROM_DATE;
                    }
                }
                if (filter.equals("toWarningDate")) {
                    filterByWarningDate = true;
                    try {
                        toWarningDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## ListInvoiceByFilterRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toWarningDate = TO_DATE;
                    }
                }
                if (filter.equals("fromCustomerId")) {
                    filterByCustomerId = true;
                    String customerIds = requestData.get(filter);
                    String[] parsedcustomerIds = customerIds.split("-");
                    for (String s : parsedcustomerIds)
                        fromCustomerId.add(Integer.parseInt(s));
                }
                if (filter.equals("fromProductId")) {
                    filterByProductId = true;
                    String productIds = requestData.get(filter);
                    String[] parsedProductIds = productIds.split("-");
                    for (String s : parsedProductIds)
                        fromProductId.add(Integer.parseInt(s));
                }
                if (filter.equals("fromStatus")) {
                    filterByStatus = true;
                    String status = requestData.get(filter);
                    String[] parsedStatus = status.split("-");
                    for (String s : parsedStatus)
                        fromStatus.add(Integer.parseInt(s));
                }
            }

            LOGGER.info("## ListInvoiceByFiltersRR: filterByTotal: " + filterByTotal + " fromTotal: " + fromTotal + " toTotal: " + toTotal + " filterByDiscount: " + filterByDiscount + " fromDiscount: " + fromDiscount + " toDiscount: " + toDiscount + " filterByPfr: " + filterByPfr + " fromPfr: " + fromPfr + " toPfr: " + toPfr + " filterByInvoiceDate: " + filterByInvoiceDate + " fromInvoiceDate: " + fromInvoiceDate + " toInvoiceDate: " + toInvoiceDate + " filterByWarningDate: " + filterByWarningDate + " fromWarningDate: " + fromWarningDate + " toWarningDate: " + toWarningDate + " fromCustomerId: " + fromCustomerId + " fromProductId: " + fromProductId);

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceByFiltersDAO(con, owner_id, company_id,
                    filterByTotal, fromTotal, toTotal,
                    filterByDiscount, fromDiscount, toDiscount,
                    filterByPfr, fromPfr, toPfr,
                    filterByInvoiceDate, fromInvoiceDate, toInvoiceDate,
                    filterByWarningDate, fromWarningDate, toWarningDate,
                    filterByCustomerId, fromCustomerId,
                    filterByProductId, fromProductId,
                    filterByStatus, fromStatus
            ).access().getOutputParam();

            if (el != null) {
                LOGGER.info("## ListInvoiceByFiltersRR: Invoice(s) successfully listed. ##");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList(el).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## ListInvoiceByFiltersRR: Fatal error while listing invoice(s). ##");

                m = new Message("## ListInvoiceByFiltersRR: Cannot list invoice(s): unexpected error. ##", "E5A1", "");
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## ListInvoiceByFiltersRR: Cannot list invoice(s): unexpected database error. ##", ex);

            m = new Message("## ListInvoiceByFiltersRR: Cannot list invoice(s): unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("## ListInvoiceByFiltersRR: Owner not parsable. ##", "E5A1", "");
            LOGGER.info("## ListInvoiceByFiltersRR: Owner not parsable. " + ex.getStackTrace() + " ##");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("## ListInvoiceByFiltersRR: Runtime exception: " + e.getStackTrace() + " ##");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}
