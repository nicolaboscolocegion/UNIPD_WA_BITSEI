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
package it.unipd.dei.bitsei.dao.documentation;

import it.unipd.dei.bitsei.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Get the selected document name from the database.
 *
 * @author Mirco Cazzaro
 * @version 1.00
 * @since 1.00
 */
public final class GetDocumentDAO extends AbstractDAO<String> {

    /**
     * SQL statement to be executed to check ownership for security reasons.
     */
    private static final String CHECK_OWNERSHIP_STMT = "SELECT COUNT(*) AS c FROM bitsei_schema.\"Company\" INNER JOIN bitsei_schema.\"Customer\" ON bitsei_schema.\"Company\".company_id = bitsei_schema.\"Customer\".company_id INNER JOIN bitsei_schema.\"Invoice\" ON bitsei_schema.\"Customer\".customer_id = bitsei_schema.\"Invoice\".customer_id WHERE bitsei_schema.\"Company\".company_id = ? AND bitsei_schema.\"Company\".owner_id = ? AND bitsei_schema.\"Invoice\".invoice_id = ?;";

    /**
     * The SQL statement to be executed
     *
     */
    private static final String STATEMENT = "SELECT bitsei_schema.\"Invoice\".warning_pdf_file, bitsei_schema.\"Invoice\".invoice_pdf_file, bitsei_schema.\"Invoice\".invoice_xml_file FROM bitsei_schema.\"Invoice\" WHERE bitsei_schema.\"Invoice\".invoice_id = ?;";

    // the id of the owner
    private final int owner_id;

    // the id of the company
    private final int company_id;

    private final int invoice_id;
    private final int document_type;

    String doc_name = "";



    /**
     * Creates a new object for listing all the users.
     *
     * @param con        the connection to the database.
     * @param company_id the id of the company to delete
     * @param owner_id   the id of the owner
     */
    public GetDocumentDAO(final Connection con, final int company_id, final int owner_id, final int invoice_id, final int document_type) {
        super(con);
        this.owner_id = owner_id;
        this.company_id = company_id;
        this.invoice_id = invoice_id;
        this.document_type = document_type;
    }

    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        byte[] image;

        try {

            pstmt = con.prepareStatement(CHECK_OWNERSHIP_STMT);
            pstmt.setInt(1, company_id);
            pstmt.setInt(2, owner_id);
            pstmt.setInt(3, invoice_id);
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
            rs = pstmt.executeQuery();


            while (rs.next()) {
                if (document_type == 0) {
                    rs.getString("warning_pdf_file");
                }
                else if (document_type == 1) {
                    rs.getString("invoice_pdf_file");
                }
                else if (document_type == 2) {
                    rs.getString("invoice_xml_file");
                }
            }

            LOGGER.info("Document name successfully fetched.");

        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        outputParam = doc_name;
    }
}
