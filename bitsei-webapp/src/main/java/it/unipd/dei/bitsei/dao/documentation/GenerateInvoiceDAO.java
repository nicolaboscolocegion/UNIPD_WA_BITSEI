package it.unipd.dei.bitsei.dao.documentation;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.taglibs.standard.functions.Functions.trim;


/**
 * Updates invoice status and fetch data for invoice output.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GenerateInvoiceDAO extends AbstractDAO<List<Object>> {

    /**
     * The SQL statement to be executed
     */
    //private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";

    private static final String COUNT_INVOICES = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Invoice\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Invoice\".customer_id = bitsei_schema.\"Customer\".customer_id WHERE bitsei_schema.\"Customer\".company_id = ? AND bitsei_schema.\"Invoice\".status = 2;"; // AND YEAR = CURYEAR
    private static final String STATEMENT_UPDATE = "UPDATE bitsei_schema.\"Invoice\" SET status = ?, invoice_date = ?, invoice_pdf_file = ?, invoice_number = ? WHERE invoice_id = ?;";
    private static final String STATEMENT_SELECT_INVOICE = "SELECT * FROM bitsei_schema.\"Invoice\" WHERE invoice_id = ?;";
    private static final String STATEMENT_SELECT_CUSTOMER = "SELECT * FROM bitsei_schema.\"Customer\" WHERE customer_id = ?;";
    private static final String STATEMENT_SELECT_COMPANY = "SELECT * FROM bitsei_schema.\"Company\" WHERE company_id = ?;";
    private static final String STATEMENT_SELECT_INVOICE_PRODUCT = "SELECT bitsei_schema.\"Product\".title, bitsei_schema.\"Product\".description, bitsei_schema.\"Invoice_Product\".quantity, bitsei_schema.\"Product\".measurement_unit, bitsei_schema.\"Invoice_Product\".unit_price, bitsei_schema.\"Invoice_Product\".related_price, bitsei_schema.\"Invoice_Product\".related_price_description,  bitsei_schema.\"Invoice_Product\".purchase_date FROM bitsei_schema.\"Invoice_Product\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Product\".product_id = bitsei_schema.\"Invoice_Product\".product_id WHERE bitsei_schema.\"Invoice_Product\".invoice_id = ?;";

    private int owner_id;
    private int invoice_id;
    private Date today;
    private  String fileName;

    private Customer c;
    private Invoice i;

    private String company_name;
    private String company_address;
    private String company_city_postalcode_prov;
    private String company_mail;
    private String company_vat;
    private String company_tax;
    private String company_pec;
    private String company_unique_code;
    private Integer fiscal_company_type;



    private List<DetailRow> ldr = new ArrayList<>();
    private List<Object> output = new ArrayList<>();



    /**
     * Closes the invoice.
     *
     * @param con
     *        the connection to the database.
     * @param invoice_id
     *        the id of the invoice to be closed.
     */
    public GenerateInvoiceDAO(final Connection con, int invoice_id, Date today, String fileName, int owner_id) {
        super(con);
        this.invoice_id = invoice_id;
        this.today = today;
        this.fileName = fileName;
        this.owner_id = owner_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search

        try {

  /*          pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, c.getCompanyID());
            pstmt.setInt(2, owner_id);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs.getInt("c") == 0) {
                LOGGER.error("Company selected does not belong to logged user.");
                throw new IllegalAccessException();
            }
*/
            int invoiceNumber = 0;
            pstmt = con.prepareStatement(COUNT_INVOICES);
            pstmt.setInt(1, c.getCompanyID());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                invoiceNumber = rs.getInt("c");
            }


            pstmt = con.prepareStatement(STATEMENT_UPDATE);
            pstmt.setInt(1, 2);
            pstmt.setDate(2, (java.sql.Date) today);
            pstmt.setString(3, this.fileName);
            pstmt.setInt(4, this.invoice_id);
            pstmt.executeUpdate();
            LOGGER.info("Invoice status successfully set to 1.");



            pstmt = con.prepareStatement(STATEMENT_SELECT_INVOICE);
            pstmt.setInt(1, this.invoice_id);
            rs = pstmt.executeQuery();
            int customer_id = 0;
            while (rs.next()) {
                i = new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("status"),
                        rs.getInt("warning_number"),
                        rs.getDate("warning_date"),
                        rs.getString("warning_pdf_file"),
                        rs.getString("invoice_number"),
                        rs.getDate("invoice_date"),
                        rs.getString("invoice_pdf_file"),
                        rs.getString("invoice_xml_file"),
                        rs.getDouble("total"),
                        rs.getDouble("discount"),
                        rs.getDouble("pension_fund_refund"),
                        rs.getBoolean("has_stamp")
                );

                customer_id =  rs.getInt("customer_id");
            }
            LOGGER.info("Invoice data successfully fetched.");


            pstmt = con.prepareStatement(STATEMENT_SELECT_CUSTOMER);
            pstmt.setInt(1, customer_id);
            rs = pstmt.executeQuery();
            int company_id = 0;
            while (rs.next()) {
                c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("business_name"),
                        rs.getString("vat_number"),
                        rs.getString("tax_code"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("province"),
                        rs.getString("postal_code"),
                        rs.getString("email"),
                        rs.getString("pec"),
                        rs.getString("unique_code"),
                        rs.getInt("company_id")
                );
                company_id = rs.getInt("company_id");
            }
            LOGGER.info("Customer data successfully fetched.");


            pstmt = con.prepareStatement(STATEMENT_SELECT_COMPANY);
            pstmt.setInt(1, company_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.company_name = rs.getString("business_name");
                this.company_address = rs.getString("address");
                this.company_city_postalcode_prov = rs.getString("city") + " " + rs.getString("postal_code") + " (" + rs.getString("province") + ")";
                this.company_mail = "todo@gmail.com";
                this.company_vat = rs.getString("vat_number");
                this.company_tax = rs.getString("tax_code");
                this.company_pec = "todo@pec.it";
                this.company_unique_code = rs.getString("unique_code");
                this.fiscal_company_type = rs.getInt("fiscal_company_type");
            }
            LOGGER.info("Customer data successfully fetched.");


            SimpleDateFormat italianFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat psqlFormat = new SimpleDateFormat("yyyy-MM-dd");
            pstmt = con.prepareStatement(STATEMENT_SELECT_INVOICE_PRODUCT);
            pstmt.setInt(1, this.invoice_id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String purchaseDate = italianFormat.format(psqlFormat.parse(rs.getString("purchase_date")));
                ldr.add(new DetailRow(trim(rs.getString("title")) + " - " + rs.getString("description"), purchaseDate, rs.getInt("quantity"), rs.getString("measurement_unit"), rs.getFloat("unit_price"), rs.getFloat("related_price"), rs.getString("related_price_description")));
            }

            output.add(ldr);
            output.add(c);
            output.add(i);
            output.add(this.company_name);
            output.add(this.company_address);
            output.add(this.company_city_postalcode_prov);
            output.add(this.company_mail);
            output.add(this.company_vat);
            output.add(this.company_tax);
            output.add(this.company_pec);
            output.add(this.company_unique_code);
            output.add(this.fiscal_company_type);


        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = output;
    }
}