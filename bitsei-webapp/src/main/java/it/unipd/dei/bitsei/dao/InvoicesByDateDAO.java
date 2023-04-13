/*
 * Copyright 2018-2023 University of Padua, Italy
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

package it.unipd.dei.bitsei.dao;

import it.unipd.dei.bitsei.resources.InvoiceByDate;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 * Show the amount of invoices grouped by date.
 *
 * @author Christian Marchiori
 * @version 1.00
 * @since 1.00
 */
public final class InvoicesByDateDAO extends AbstractDAO<List<InvoiceByDate>> {

	/**
	 * The SQL statement to be executed
	 */
	private static final String STATEMENT = "SELECT to_char(invoice_date, 'Month YYYY') AS date, COUNT(*) AS amount FROM bitsei_schema.\"Invoice\" GROUP BY to_char(invoice_date, 'Month YYYY') ORDER BY to_date(to_char(invoice_date, 'Month YYYY'), 'Month YYYY')";

	/**
     * Start date for the period to be considered
     */
    private final Date startDate;
    /**
     * End date for the period to be considered
     */
    private final Date endDate;

	/**
	 * Creates a new object for show the amount of invoices grouped by date.
	 *
	 * @param con    the connection to the database.
	 * @param startDate     the date from which to start the filtering
     * @param endDate       the date from which to end the filtering
	 */
	public InvoicesByDateDAO(final Connection con, final Date startDate,final Date endDate) {
		super(con);
		this.startDate = endDate;
		this.endDate = endDate;
	}

	@Override
	public final void doAccess() throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// the results of the search
		final List<InvoiceByDate> invoices_by_date = new ArrayList<InvoiceByDate>();

		try {
			pstmt = con.prepareStatement(STATEMENT);

			//pstmt.setInt(1, invoice_date);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				invoices_by_date.add(new InvoiceByDate(rs.getString("date"), rs.getInt("amount")));
			}

			LOGGER.info("Amount of invoices grouped by date successfully listed.");
		} finally {
			if (rs != null) {
				rs.close();
			}

			if (pstmt != null) {
				pstmt.close();
			}

		}

		this.outputParam = invoices_by_date;
	}
}