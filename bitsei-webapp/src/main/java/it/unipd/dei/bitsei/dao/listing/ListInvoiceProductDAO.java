/*
 * Copyright 2022-2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.bitsei.dao.listing;

import it.unipd.dei.bitsei.dao.AbstractDAO;
import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.Invoice;
import it.unipd.dei.bitsei.resources.InvoiceProduct;
import it.unipd.dei.bitsei.resources.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * List invoice rows of a specific invoice.
 *
 * @author Mirco Cazzaro
 * @version 1.00
 * @since 1.00
 */
public class ListInvoiceProductDAO extends AbstractDAO<List<InvoiceProduct>> {


    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Customer\".customer_id = bitsei_schema.\"Invoice\".customer_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ?;";

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT i.* FROM bitsei_schema.\"Invoice\" AS i JOIN bitsei_schema.\"Customer\" AS c ON i.customer_id = c.customer_id JOIN bitsei_schema.\"Company\" AS cmp ON c.company_id = cmp.company_id WHERE ((cmp.company_id = ?) OR 1=1)";

    private final int ownerId;
    private final int companyId;
    private final int invoiceId;
    /**
     /**
     * Creates a new object for searching the invoices from startTotal to endTotal
     *
     * @param con           the connection to the database.
     */
    public ListInvoiceProductDAO(final Connection con, int ownerId ,int companyId, int invoiceId){
        super(con);
        this.ownerId = ownerId;
        this.companyId = companyId;
        this.invoiceId = invoiceId;
    }

    /**
     * search the invoices associated to the company having company_id = currentUser_companyId
     */
    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        List<InvoiceProduct> rows = new ArrayList<InvoiceProduct>();

        try {

            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, companyId);
            pstmt.setInt(2, ownerId);
            pstmt.setInt(3, invoiceId);
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
            pstmt.setInt(1, invoiceId);
            rs = pstmt.executeQuery();

            rows = parseInvoiceRS(rs);

            LOGGER.info("## ListInvoiceDAO: Invoices of ownerId: %d and companyId: %d succesfully listed ##", ownerId, companyId);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = rows;
    }

    private List<InvoiceProduct> parseInvoiceRS(ResultSet rs) throws SQLException {
        final List<InvoiceProduct> rows = new ArrayList<InvoiceProduct>();

        while (rs.next()) {
            rows.add(new InvoiceProduct(rs.getInt("invoice_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getDouble("unit_price"), rs.getDouble("related_price"), rs.getString("related_price_description"), rs.getDate("purchase_date")));
        }

        return rows;
    }


}
