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

Author: Marco Martinelli
Version: 1.0
Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Get Invoices By Filters Form</title>
</head>

<body>
<h1>Get Invoices By Filters Form</h1>

<form method="POST" action="<c:url value="/get-invoices-by-filters-jsp"/>">
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

    <button type="submit">Submit</button><br/>
    <button type="reset">Reset the form</button>
</form>
</body>
</html>
