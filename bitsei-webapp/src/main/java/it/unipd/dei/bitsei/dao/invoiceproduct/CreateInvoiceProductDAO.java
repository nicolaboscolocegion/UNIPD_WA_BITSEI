package it.unipd.dei.bitsei.dao.invoiceproduct;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.InvoiceProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a new invoice product inside the database.
 *
 * @author Fabio Zanini (fabio.zanini@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class CreateInvoiceProductDAO extends AbstractDAO<InvoiceProduct> {
    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" WHERE company_id = ? and owner_id = ?";

    /**
     * SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO bitsei_schema.\"Invoice_Product\" (invoice_id, product_id, quantity, unit_price, related_price, related_price_description, purchase_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String FETCH_INVOICE_PRODUCTS = "SELECT * FROM bitsei_schema.\"Invoice_Product\" WHERE invoice_id = ?;";
    private static final String INVOICE_TOTAL_STATEMENT = "UPDATE bitsei_schema.\"Invoice\" SET total = ? WHERE invoice_id = ?;";

    /**
     * The invoice product to be stored into the database.
     */
    private final InvoiceProduct invoiceProduct;

    /**
     * The owner_id of this session, to be checked for security reasons.
     */
    private final int owner_id;

    /**
     * The company_id of this session, to be checked for security reasons.
     */
    private final int company_id;

    /**
     * Creates a new object for storing an invoice product into the database.
     *
     * @param con            the connection to the database.
     * @param invoiceProduct the invoice product to be stored into the database.
     * @param owner_id       the id of the owner of the session.
     * @param company_id     the id of the company of the owner of the session.
     */
    public CreateInvoiceProductDAO(final Connection con, final InvoiceProduct invoiceProduct, final int owner_id, final int company_id) {
        super(con);

        if (invoiceProduct == null) {
            LOGGER.error("The invoice product cannot be null.");
            throw new NullPointerException("The invoice product cannot be null.");
        }
        this.owner_id = owner_id;
        this.company_id = company_id;

        this.invoiceProduct = invoiceProduct;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
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

            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, invoiceProduct.getInvoice_id());
            pstmt.setInt(2, invoiceProduct.getProduct_id());
            pstmt.setInt(3, invoiceProduct.getQuantity());
            pstmt.setDouble(4, invoiceProduct.getUnit_price());
            pstmt.setDouble(5, invoiceProduct.getRelated_price());
            pstmt.setString(6, invoiceProduct.getRelated_price_description());
            pstmt.setDate(7, invoiceProduct.getPurchase_date());
            pstmt.execute();


            pstmt = con.prepareStatement(FETCH_INVOICE_PRODUCTS);
            pstmt.setInt(1, invoiceProduct.getInvoice_id());
            rs = pstmt.executeQuery();
            double total = 0;

            while (rs.next()) {
                total = total + (rs.getInt("quantity") * rs.getDouble("unit_price")) + rs.getDouble("related_price");
            }


            pstmt = con.prepareStatement(INVOICE_TOTAL_STATEMENT);
            pstmt.setDouble(1, total);
            pstmt.setInt(2, invoiceProduct.getInvoice_id());

            pstmt.execute();


            LOGGER.info("Invoice product successfully stored in the database.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
