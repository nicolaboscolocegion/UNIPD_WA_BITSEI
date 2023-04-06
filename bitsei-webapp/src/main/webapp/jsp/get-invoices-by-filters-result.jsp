<!--
 Copyright 2018-2023 University of Padua, Italy
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Author: Nicola Ferro (ferro@dei.unipd.it)
 Version: 1.0
 Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Invoices Get by Filters</title>
	</head>

	<body>
		<h1>Invoices Get by Filters</h1>
		<hr/>
		
		<!-- display the message -->
		<c:import url="/jsp/include/show-message.jsp"/>

		<!-- display the list of found employees, if any -->
		<c:if test='${not empty invoiceList}'>
			<table>
				<thead>
					<tr>
						<th>Invoice id</th><th>Invoice Number</th><th>Customer Id</th><th>Status</th><th>Invoice Date</th><th>Warning Date</th><th>Total</th><th>Discount</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="invoice" items="${invoiceList}">
						<tr>
							<td><c:out value="${invoice.invoice_id}"/></td>
							<td><c:out value="${invoice.invoice_number}"/></td>
							<td><c:out value="${invoice.customer_id}"/></td>
							<td><c:out value="${invoice.status}"/></td>
							<td><c:out value="${invoice.invoice_date}"/></td>
							<td><c:out value="${invoice.warning_date}"/></td>
							<td><c:out value="${invoice.total}"/></td>
							<td><c:out value="${invoice.discount}"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</body>
</html>
