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
				const ctx = document.getElementById('myChart');
				
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
				const ctx = document.getElementById('myChart');
				
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
				const ctx = document.getElementById('myChart');
				
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
		</c:choose>
	</body>
</html>