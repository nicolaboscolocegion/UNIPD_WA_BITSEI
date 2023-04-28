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
 
 Author: Christian Marchiori
 Version: 1.0
 Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Chart using jsp</title>
	</head>

	<body>
		<h1>Chart using jsp</h1>
		<hr/>
		
		<!-- display the message -->
		<c:import url="/jsp/include/show-message.jsp"/>

		<!-- display the list of found employees, if any -->
		<div class="chart-container" style="position: relative; height:60vh; width:100vw">
			<canvas id="myChart"></canvas>
		</div>
		  
		<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
		<c:choose>
			<c:when test="${chart_type == 1}">
			<script>
				ctx = document.getElementById('myChart');

				new Chart(ctx, {
					type: 'bar',
					data: {
					labels: [<c:forEach var="element" items="${tmap_labels}">'<c:out value="${element}"/>'<c:if test ="${tmap_labels.indexOf(element)!=tmap_labels.size()-1}">,</c:if></c:forEach>],
					datasets: [{
						label: '# of Invoices',
						data: [<c:forEach var="element" items="${tmap_data}"><c:out value="${element}"/><c:if test ="${tmap_data.indexOf(element)!=tmap_data.size()-1}">,</c:if></c:forEach>],
						borderWidth: 1
					}]
					},
					options: {
					scales: {
						y: {
						beginAtZero: true
						}
					}
					}
				});
			</script>
			</c:when>
			
			<c:when test="${chart_type == 2}">
			<script>
				ctx = document.getElementById('myChart');
				
				new Chart(ctx, {
					type: 'line',
					data: {
					labels: [<c:forEach var="element" items="${tmap_labels}">'<c:out value="${element}"/>'<c:if test ="${tmap_labels.indexOf(element)!=tmap_labels.size()-1}">,</c:if></c:forEach>],
					datasets: [{
						label: 'Total profit',
						data: [<c:forEach var="element" items="${tmap_data}"><c:out value="${element}"/><c:if test ="${tmap_data.indexOf(element)!=tmap_data.size()-1}">,</c:if></c:forEach>],
						borderWidth: 1,
						borderColor: 'rgb(75, 192, 192)'
					}]
					},
					options: {
					scales: {
						y: {
						beginAtZero: true
						}
					}
					}
				});
			</script>
			</c:when>

			<c:when test="${chart_type == 3}">
			<script>
				ctx = document.getElementById('myChart');
				
				new Chart(ctx, {
					type: 'doughnut',
					data: {
					labels: [<c:forEach var="element" items="${tmap_labels}">'<c:out value="${element}"/>'<c:if test ="${tmap_labels.indexOf(element)!=tmap_labels.size()-1}">,</c:if></c:forEach>],
					datasets: [{
						label: '# of Invoices',
						data: [<c:forEach var="element" items="${tmap_data}"><c:out value="${element}"/><c:if test ="${tmap_data.indexOf(element)!=tmap_data.size()-1}">,</c:if></c:forEach>],
						borderWidth: 2,
						hoverOffset: 4
					}]
					},
					options: {
					scales: {
						y: {
						beginAtZero: true
						}
					}
					}
				});
			</script>
			</c:when>

			<c:when test="${chart_type == 4}">
			<script>
				ctx = document.getElementById('myChart');
				
				new Chart(ctx, {
					type: 'doughnut',
					data: {
					labels: [<c:forEach var="element" items="${tmap_labels}">'<c:out value="${element}"/>'<c:if test ="${tmap_labels.indexOf(element)!=tmap_labels.size()-1}">,</c:if></c:forEach>],
					datasets: [{
						label: 'Total profit',
						data: [<c:forEach var="element" items="${tmap_data}"><c:out value="${element}"/><c:if test ="${tmap_data.indexOf(element)!=tmap_data.size()-1}">,</c:if></c:forEach>],
						borderWidth: 2,
						hoverOffset: 4
					}]
					},
					options: {
					scales: {
						y: {
						beginAtZero: true
						}
					}
					}
				});
			</script>
			</c:when>

			<c:when test="${chart_type == 5}">
			<script>
				ctx = document.getElementById('myChart');
				
				new Chart(ctx, {
					type: 'line',
					data: {
					labels: [<c:forEach var="element" items="${tmap_labels}">'<c:out value="${element}"/>'<c:if test ="${tmap_labels.indexOf(element)!=tmap_labels.size()-1}">,</c:if></c:forEach>],
					datasets: [{
						label: 'Average discount',
						data: [<c:forEach var="element" items="${tmap_data}"><c:out value="${element}"/><c:if test ="${tmap_data.indexOf(element)!=tmap_data.size()-1}">,</c:if></c:forEach>],
						borderWidth: 1,
						borderColor: 'rgb(75, 192, 192)'
					}]
					},
					options: {
					scales: {
						y: {
						beginAtZero: true
						}
					}
					}
				});
			</script>
			</c:when>
		</c:choose>
		<hr>
		
		<form method="POST" action="./chart-form">
			
			<label for="chartPeriodID">Period of chart:  </label>
			<select id="chartPeriodID" name="chartPeriod" onchange="this.form.submit()">
			<option value="1"<c:if test="${chart_period==1}"><c:out value=" selected"/></c:if>>Month</option>
			<option value="2"<c:if test="${chart_period==2}"><c:out value=" selected"/></c:if>>Year</option>
			<option value="3"<c:if test="${chart_period==3}"><c:out value=" selected"/></c:if>>Day</option>
			</select><br/><br/>
			<hr>


			<label for="filterByTotalID">Filter by Total:</label>
			<input id="filterByTotalID" name="filterByTotal" type="checkbox"/><br/><br/>
			
			<label for="startTotalID">Start Total:</label>
			<input id="startTotalID" name="startTotal" type="number"/><br/><br/>
	
			<label for="endTotalID">End Total:</label>
			<input id="endTotalID" name="endTotal" type="number"/><br/><br/>
			<hr>
	
	
			<label for="filterByDiscountID">Filter by Discount:</label>
			<input id="filterByDiscountID" name="filterByDiscount" type="checkbox"/><br/><br/>
			
			<label for="startDiscountID">Start Discount:</label>
			<input id="startDiscountID" name="startDiscount" type="number"/><br/><br/>
	
			<label for="endDiscountID">End Discount:</label>
			<input id="endDiscountID" name="endDiscount" type="number"/><br/><br/>
			<hr>
	
	
			<label for="filterByPfrID">Filter by Pension Fund Refund:</label>
			<input id="filterByPfrID" name="filterByPfr" type="checkbox"/><br/><br/>
			
			<label for="startPfrID">Start Pension Fund Refund:</label>
			<input id="startPfrID" name="startPfr" type="number"/><br/><br/>
	
			<label for="endPfrID">End Pension Fund Refund:</label>
			<input id="endPfrID" name="endPfr" type="number"/><br/><br/>
			<hr>
	
	
			<label for="filterByInvoiceDateID">Filter by Invoice Date:</label>
			<input id="filterByInvoiceDateID" name="filterByInvoiceDate" type="checkbox"/><br/><br/>
	
			<label for="startInvoiceDateID">Start Invoice Date:</label>
			<input id="startInvoiceDateID" name="startInvoiceDate" type="date"/><br/><br/>
	
			<label for="endInvoiceDateID">End Invoice Date:</label>
			<input id="endInvoiceDateID" name="endInvoiceDate" type="date"/><br/><br/>
			<hr>
	
	
			<label for="filterByWarningDateID">Filter by Warning Date:</label>
			<input id="filterByWarningDateID" name="filterByWarningDate" type="checkbox"/><br/><br/>
	
			<label for="startWarningDateID">Start Warning Date:</label>
			<input id="startWarningDateID" name="startWarningDate" type="date"/><br/><br/>
	
			<label for="endWarningDateID">End Warning Date:</label>
			<input id="endWarningDateID" name="endWarningDate" type="date"/><br/><br/>
			<hr>
	
			<label for="chartTypeID">Type of chart:</label><br/><br/>
			<select id="chartTypeID" name="chartType" onchange="this.form.submit()">
			<option value="1"<c:if test="${chart_type==1}"><c:out value=" selected"/></c:if>>Invoices by period</option>
			<option value="2"<c:if test="${chart_type==2}"><c:out value=" selected"/></c:if>>Total profit by period</option>
			<option value="3"<c:if test="${chart_type==3}"><c:out value=" selected"/></c:if>>Invoices by customer</option>
			<option value="4"<c:if test="${chart_type==4}"><c:out value=" selected"/></c:if>>Total by customer</option>
			<option value="5"<c:if test="${chart_type==5}"><c:out value=" selected"/></c:if>>Discount by period</option>
			</select><br/><br/>
			<hr>
	
			<button type="submit">Submit</button><br/>
			<button type="reset">Reset the form</button>
		</form>
		
	</body>
</html>