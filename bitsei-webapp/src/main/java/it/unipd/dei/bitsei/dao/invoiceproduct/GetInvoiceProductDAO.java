package it.unipd.dei.bitsei.dao.invoiceproduct;



import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Searches invoice product by ids.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GetInvoiceProductDAO extends AbstractDAO<InvoiceProduct> {

    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Product\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Product\".company_id INNER JOIN bitsei_schema.\"Invoice_Product\" ON bitsei_schema.\"Product\".product_id = bitsei_schema.\"Invoice_Product\".product_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Invoice_Product\".invoice_id = bitsei_schema.\"Invoice\".invoice_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ? AND bitsei_schema.\"Product\".product_id = ?;";


    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM bitsei_schema.\"Invoice_Product\" WHERE invoice_id = ? AND product_id = ?;";

    /**
     * The invoice_id of the invoice product to be deleted from the database.
     */
    private final int invoice_id;

    /**
     * The product_id of the invoice product to be deleted from the database.
     */
    private final int product_id;
    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id of this session, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for searching invoice product by ids.
     *
     * @param con    the connection to the database.
     * @param invoice_id the id of the invoice linked to the invoice product to be deleted from the database.
     * @param product_id the id of the product linked to the invoice product to be deleted from the database.
     * @param owner_id the owner_id of the session, to be checked for security reasons.
     * @param company_id the company_id of the session, to be checked for security reasons.
     */
    public GetInvoiceProductDAO(final Connection con, final int invoice_id, final int product_id, final int owner_id, final int company_id) {
        super(con);
        this.invoice_id = invoice_id;
        this.product_id = product_id;
        this.owner_id = owner_id;
        this.company_id = company_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_check = null;

        // the results of the search
        InvoiceProduct ip = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, invoice_id);
            pstmt.setInt(4, product_id);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                LOGGER.error("Error on fetching data from database");
                throw new SQLException();
            }

            if (rs.getInt("c") == 0) {
                LOGGER.error("Data access violation");
                throw new IllegalAccessException();
            }

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoice_id);
            pstmt.setInt(2, product_id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ip = new InvoiceProduct(rs.getInt("invoice_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getDouble("unit_price"), rs.getDouble("related_price"), rs.getString("related_price_description"), rs.getDate("purchase_date"));
            }

            LOGGER.info("Invoice product with invoice_id %d and product_id %d successfully listed.", invoice_id, product_id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (rs_check != null) {
                rs_check.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = ip;
    }
}