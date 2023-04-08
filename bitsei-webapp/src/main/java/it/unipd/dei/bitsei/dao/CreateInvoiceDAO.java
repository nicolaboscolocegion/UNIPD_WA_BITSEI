package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.Invoice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates a new invoice inside the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CreateInvoiceDAO extends AbstractDAO {
    /*
    CREATE TABLE bitsei_schema."Invoice" (
                                         invoice_id serial NOT NULL,
                                         customer_id integer NOT NULL,
                                         status smallint DEFAULT 0 NOT NULL,
                                         warning_number character(255),
                                         warning_date date,
                                         warning_pdf_file character(255),
                                         invoice_number character(255),
                                         invoice_date date,
                                         invoice_pdf_file character(255),
                                         invoice_xml_file character(255),  -- relative file path, extension must be xml
                                         total double precision,   -- must be positive
                                         discount double precision,  -- must be positive
                                         pension_fund_refund double precision NOT NULL, -- must be within 0 and 4 (%)
                                         has_stamp boolean DEFAULT false
);
    */
    /**
     * SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Invoice\" (customer_id, status, warning_number, warning_date, warning_pdf_file, invoice_number, invoice_date, invoice_pdf_file, invoice_xml_file, total, discount, pension_fund_refund, has_stamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * The invoice to be stored into the database.
     */
    private final Invoice invoice;

    /**
     * Creates a new object for storing an invoice into the database.
     *
     * @param con the connection to the database.
     *
     * @param invoice the invoice to be stored into the database.
     */
    public CreateInvoiceDAO(final Connection con, final Invoice invoice) {
        super(con);

        if (invoice == null) {
            LOGGER.error("The invoice cannot be null.");
            throw new NullPointerException("The invoice cannot be null.");
        }

        this.invoice = invoice;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice.getCustomer_id());
            pstmt.setInt(2, invoice.getStatus());
            pstmt.setInt(3, invoice.getWarning_number());
            pstmt.setDate(4, invoice.getWarning_date());
            pstmt.setString(5, invoice.getWarning_pdf_file());
            pstmt.setString(6, invoice.getInvoice_number());
            pstmt.setDate(7, invoice.getInvoice_date());
            pstmt.setString(8, invoice.getInvoice_pdf_file());
            pstmt.setString(9, invoice.getInvoice_xml_file());
            pstmt.setDouble(10, invoice.getTotal());
            pstmt.setDouble(11, invoice.getDiscount());
            pstmt.setDouble(12, invoice.getPension_fund_refund());
            pstmt.setBoolean(13, invoice.hasStamp());
            pstmt.execute();

            LOGGER.info("Invoice successfully stored in the database.");
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
