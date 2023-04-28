package it.unipd.dei.bitsei.rest;

import it.unipd.dei.bitsei.dao.listing.ListInvoiceForChartsDAO;
import it.unipd.dei.bitsei.resources.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A REST resource for plotting charts.
 */
public final class PlotChartRR extends AbstractRR {

    private static final double FROM_DOUBLE = 0.00;
    private static final double TO_DOUBLE = 9E10;

    private static final Date FROM_DATE = Date.valueOf("1970-01-01");
    LocalDate currentDate = LocalDate.now();
    final Date TO_DATE = Date.valueOf(currentDate);

    private static final int CHART_TYPE = 1;
    private static final int CHART_PERIOD = 1;

    private final int company_id;
    private final Map<String, String> requestData;

    /**
     * Creates a new REST resource for plotting charts.
     *
     * @param req         the HTTP request.
     * @param res         the HTTP response.
     * @param con         the connection to the database.
     * @param company_id  the id of the company.
     * @param requestData the data of the request.
     */
    public PlotChartRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int company_id, Map<String, String> requestData) {
        super(Actions.PLOT_CHART, req, res, con);
        this.company_id = company_id;
        this.requestData = requestData;
    }

    @Override
    protected void doServe() throws IOException {

        final int owner_id;


        List<InvoiceCustomer> el = null;
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

            boolean filterByBusinessName = false;
            List<String> fromBusinessName = new ArrayList<String>();

            boolean filterByProductTitle = false;
            List<String> fromProductTitle = new ArrayList<String>();

            int chart_type = 1;
            int chart_period = 1;

            for (String filter : requestData.keySet()) {
                if (filter.equals("fromTotal")) {
                    filterByTotal = true;
                    try {
                        fromTotal = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromTotal = FROM_DOUBLE;
                    }
                }
                if (filter.equals("toTotal")) {
                    filterByTotal = true;
                    try {
                        toTotal = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toTotal = TO_DOUBLE;
                    }
                }
                if (filter.equals("fromDiscount")) {
                    filterByDiscount = true;
                    try {
                        fromDiscount = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromDiscount = FROM_DOUBLE;
                    }
                }
                if (filter.equals("toDiscount")) {
                    filterByDiscount = true;
                    try {
                        toDiscount = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toDiscount = TO_DOUBLE;
                    }
                }
                if (filter.equals("fromPfr")) {
                    filterByPfr = true;
                    try {
                        fromPfr = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromPfr = FROM_DOUBLE;
                    }
                }
                if (filter.equals("toPfr")) {
                    filterByPfr = true;
                    try {
                        toPfr = Double.parseDouble(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toPfr = TO_DOUBLE;
                    }
                }
                if (filter.equals("fromInvoiceDate")) {
                    filterByInvoiceDate = true;
                    try {
                        fromInvoiceDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromInvoiceDate = FROM_DATE;
                    }
                }
                if (filter.equals("toInvoiceDate")) {
                    filterByInvoiceDate = true;
                    try {
                        toInvoiceDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toInvoiceDate = TO_DATE;
                    }
                }
                if (filter.equals("fromWarningDate")) {
                    filterByWarningDate = true;
                    try {
                        fromWarningDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        fromWarningDate = FROM_DATE;
                    }
                }
                if (filter.equals("toWarningDate")) {
                    filterByWarningDate = true;
                    try {
                        toWarningDate = Date.valueOf(requestData.get(filter));
                    } catch (Exception e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for filter {" + filter + "}; setted at default value");
                        toWarningDate = TO_DATE;
                    }
                }
                if (filter.equals("fromBusinessName")) {
                    filterByBusinessName = true;
                    String businessNames = requestData.get(filter);
                    String[] parsedBusinessNames = businessNames.split("---");
                    for (String s : parsedBusinessNames)
                        fromBusinessName.add(s);
                }
                if (filter.equals("fromProductTitle")) {
                    filterByProductTitle = true;
                    String productTitles = requestData.get(filter);
                    String[] parsedProductTitles = productTitles.split("---");
                    for (String s : parsedProductTitles)
                        fromProductTitle.add(s);
                }
                if (filter.equals("chart_type")) {
                    try {
                        chart_type = Integer.parseInt(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for chart type {" + filter + "}; setted at default value");
                        chart_type = CHART_TYPE;
                    }
                }
                if (filter.equals("chart_period")) {
                    try {
                        chart_period = Integer.parseInt(requestData.get(filter));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("## PlotChartRR: Illegal value for chart period {" + filter + "}; setted at default value");
                        chart_period = CHART_PERIOD;
                    }
                }
            }

            LOGGER.info("## PlotChartRR: filterByTotal: " + filterByTotal + " fromTotal: " + fromTotal + " toTotal: " + toTotal + " filterByDiscount: " + filterByDiscount + " fromDiscount: " + fromDiscount + " toDiscount: " + toDiscount + " filterByPfr: " + filterByPfr + " fromPfr: " + fromPfr + " toPfr: " + toPfr + " filterByInvoiceDate: " + filterByInvoiceDate + " fromInvoiceDate: " + fromInvoiceDate + " toInvoiceDate: " + toInvoiceDate + " filterByWarningDate: " + filterByWarningDate + " fromWarningDate: " + fromWarningDate + " toWarningDate: " + toWarningDate + " fromBusinessName: " + fromBusinessName + " fromProductTitle: " + fromProductTitle + " chartType: " + chart_type + " chartType: " + chart_period);

            // creates a new DAO for accessing the database and lists the invoice(s)
            el = new ListInvoiceForChartsDAO(con, owner_id, company_id,
                    filterByTotal, fromTotal, toTotal,
                    filterByDiscount, fromDiscount, toDiscount,
                    filterByPfr, fromPfr, toPfr,
                    filterByInvoiceDate, fromInvoiceDate, toInvoiceDate,
                    filterByWarningDate, fromWarningDate, toWarningDate,
                    filterByBusinessName, fromBusinessName,
                    filterByProductTitle, fromProductTitle
            ).access().getOutputParam();

            if (el != null) {
                ArrayList<String> tmap_labels = new ArrayList<>();
                ArrayList<String> tmap_data = new ArrayList<>();

                switch (chart_type) {
                    case 1:
                        switch (chart_period) {
                            case 1:
                                //INVOICE BY DATE (MONTH)
                                TreeMap<Date, Integer> tmap_numb_month = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), 1);
                                        tmap_numb_month.put(d, 1 + tmap_numb_month.getOrDefault(d, 0));
                                    }
                                }
                                for (Map.Entry<Date, Integer> entry : tmap_numb_month.entrySet()) {
                                    String month = new SimpleDateFormat("MMMM yyyy").format(entry.getKey());
                                    tmap_labels.add(month.substring(0, 1).toUpperCase() + month.substring(1));
                                    tmap_data.add(entry.getValue().toString());
                                }
                                break;
                            case 2:
                                //INVOICE BY DATE (YEAR)
                                TreeMap<Date, Integer> tmap_numb_year = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), 0, 1);
                                        tmap_numb_year.put(d, 1 + tmap_numb_year.getOrDefault(d, 0));
                                    }
                                }
                                for (Map.Entry<Date, Integer> entry : tmap_numb_year.entrySet()) {
                                    String year = new SimpleDateFormat("yyyy").format(entry.getKey());
                                    tmap_labels.add(year);
                                    tmap_data.add(entry.getValue().toString());
                                }
                                break;
                            case 3:
                                //INVOICE BY DATE (DAY)
                                TreeMap<Date, Integer> tmap_numb_day = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), i.getInvoice_date().getDay());
                                        tmap_numb_day.put(d, 1 + tmap_numb_day.getOrDefault(d, 0));
                                    }
                                }
                                for (Map.Entry<Date, Integer> entry : tmap_numb_day.entrySet()) {
                                    String day = new SimpleDateFormat("d/MM/yyyy").format(entry.getKey());
                                    tmap_labels.add(day);
                                    tmap_data.add(entry.getValue().toString());
                                }
                                break;
                        }
                        break;
                    case 2:
                        switch (chart_period) {
                            case 1:
                                //TOTAL BY DATE (MONTH)
                                TreeMap<Date, Double> tmap_total_month = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), 1);
                                        tmap_total_month.put(d, i.getTotal() + tmap_total_month.getOrDefault(d, 0.0));
                                    }
                                }
                                for (Map.Entry<Date, Double> entry : tmap_total_month.entrySet()) {
                                    String month = new SimpleDateFormat("MMMM yyyy").format(entry.getKey());
                                    tmap_labels.add(month.substring(0, 1).toUpperCase() + month.substring(1));
                                    tmap_data.add(entry.getValue().toString());
                                }
                                break;
                            case 2:
                                //TOTAL BY DATE (YEAR)
                                TreeMap<Date, Double> tmap_total_year = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), 0, 1);
                                        tmap_total_year.put(d, i.getTotal() + tmap_total_year.getOrDefault(d, 0.0));
                                    }
                                }
                                for (Map.Entry<Date, Double> entry : tmap_total_year.entrySet()) {
                                    String year = new SimpleDateFormat("yyyy").format(entry.getKey());
                                    tmap_labels.add(year);
                                    tmap_data.add(entry.getValue().toString());
                                }
                                break;
                            case 3:
                                //TOTAL BY DATE (DAY)
                                TreeMap<Date, Double> tmap_total_day = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), i.getInvoice_date().getDay());
                                        tmap_total_day.put(d, i.getTotal() + tmap_total_day.getOrDefault(d, 0.0));
                                    }
                                }
                                for (Map.Entry<Date, Double> entry : tmap_total_day.entrySet()) {
                                    String day = new SimpleDateFormat("d/MM/yyyy").format(entry.getKey());
                                    tmap_labels.add(day);
                                    tmap_data.add(entry.getValue().toString());
                                }
                                break;
                        }
                        break;
                    case 3:
                        switch (chart_period) {
                            case 1:
                                //DISCOUNT BY DATE (MONTH)
                                TreeMap<Date, Double> tmap_discount_month_total = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), 1);
                                        tmap_discount_month_total.put(d, i.getTotal() + tmap_discount_month_total.getOrDefault(d, 0.0));
                                    }
                                }
                                TreeMap<Date, Integer> tmap_discount_month_numb = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), 1);
                                        tmap_discount_month_numb.put(d, 1 + tmap_discount_month_numb.getOrDefault(d, 0));
                                    }
                                }
                                for (Map.Entry<Date, Double> entry : tmap_discount_month_total.entrySet()) {
                                    String month = new SimpleDateFormat("MMMM yyyy").format(entry.getKey());
                                    tmap_labels.add(month.substring(0, 1).toUpperCase() + month.substring(1));
                                    tmap_data.add(String.valueOf(entry.getValue() / tmap_discount_month_numb.get(entry.getKey())));
                                }
                                break;
                            case 2:
                                //DISCOUNT BY DATE (YEAR)
                                TreeMap<Date, Double> tmap_discount_year_total = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), 0, 1);
                                        tmap_discount_year_total.put(d, i.getTotal() + tmap_discount_year_total.getOrDefault(d, 0.0));
                                    }
                                }
                                TreeMap<Date, Integer> tmap_discount_year_numb = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), 0, 1);
                                        tmap_discount_year_numb.put(d, 1 + tmap_discount_year_numb.getOrDefault(d, 0));
                                    }
                                }
                                for (Map.Entry<Date, Double> entry : tmap_discount_year_total.entrySet()) {
                                    String year = new SimpleDateFormat("yyyy").format(entry.getKey());
                                    tmap_labels.add(year);
                                    tmap_data.add(String.valueOf(entry.getValue() / tmap_discount_year_numb.get(entry.getKey())));
                                }
                                break;
                            case 3:
                                //DISCOUNT BY DATE (DAY)
                                TreeMap<Date, Double> tmap_discount_day_total = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), i.getInvoice_date().getDay());
                                        tmap_discount_day_total.put(d, i.getTotal() + tmap_discount_day_total.getOrDefault(d, 0.0));
                                    }
                                }
                                TreeMap<Date, Integer> tmap_discount_day_numb = new TreeMap<>();
                                for (InvoiceCustomer i : el) {
                                    if (i.getInvoice_date() != null) {
                                        Date d = new Date(i.getInvoice_date().getYear(), i.getInvoice_date().getMonth(), i.getInvoice_date().getDay());
                                        tmap_discount_day_numb.put(d, 1 + tmap_discount_day_numb.getOrDefault(d, 0));
                                    }
                                }
                                for (Map.Entry<Date, Double> entry : tmap_discount_day_total.entrySet()) {
                                    String day = new SimpleDateFormat("d/MM/yyyy").format(entry.getKey());
                                    tmap_labels.add(day);
                                    tmap_data.add(String.valueOf(entry.getValue() / tmap_discount_day_numb.get(entry.getKey())));
                                }
                                break;
                        }
                        break;
                    case 4:
                        //INVOICE BY CUSTOMER
                        TreeMap<String, Integer> tmap_numb_customer = new TreeMap<>();
                        for (InvoiceCustomer i : el) {

                            tmap_numb_customer.put(i.getCustomer_businessName().trim(),
                                    1 + tmap_numb_customer.getOrDefault(i.getCustomer_businessName().trim(), 0));

                        }
                        for (Map.Entry<String, Integer> entry : tmap_numb_customer.entrySet()) {
                            tmap_labels.add(entry.getKey() + "");
                            tmap_data.add(entry.getValue().toString());
                        }
                        
                        break;
                    case 5:
                        //TOTAL BY CUSTOMER
                        TreeMap<String, Double> tmap_total_customer = new TreeMap<>();
                        for (InvoiceCustomer i : el) {

                            tmap_total_customer.put(i.getCustomer_businessName().trim(),
                                    i.getTotal() + tmap_total_customer.getOrDefault(i.getCustomer_businessName().trim(), 0.0));

                        }
                        for (Map.Entry<String, Double> entry : tmap_total_customer.entrySet()) {

                            tmap_labels.add(entry.getKey() + "");
                            tmap_data.add(entry.getValue().toString());
                        }
                        break;
                    }


                LOGGER.info("## PlotChartRR: Chart successfully plotted. ##");

                res.setStatus(HttpServletResponse.SC_OK);

                new Chart(tmap_labels, tmap_data, chart_type, chart_period).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("## PlotChartRR: Fatal error while plotting chart. ##");

                m = new Message("## PlotChartRR: Cannot plot chart: unexpected error. ##", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("## PlotChartRR: Cannot plot chart: unexpected database error. ##", ex);

            m = new Message("## PlotChartRR: Cannot plot chart: unexpected database error. ##", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("## PlotChartRR: Owner not parsable. ##", "E5A1", ex.getMessage());
            LOGGER.info("## PlotChartRR: Owner not parsable. " + ex.getStackTrace() + " ##");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("## PlotChartRR: Runtime exception: " + e.getStackTrace() + " ##");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}
