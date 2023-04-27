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
<html>
<head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <title>Get Invoices By Filters Form</title>
    <style>
        * {
            box-sizing: border-box;
        }

        /* Create three equal columns that floats next to each other */
        .column {
            float: left;
            width: 33.33%;
            padding: 10px;
            height: 300px; /* Should be removed. Only for demonstration */
        }

        /* Clear floats after the columns */
        .row:after {
            content: "";
            display: table;
            clear: both;
        }
    </style>
</head>
<body>

<h1>Get Invoices By Filters Form</h1>

<div class="row">
    <div id="listInvoicesByFilters-div" class="column">
        <h3>Filter Invoices By Invoice Parameters</h3>
        <hr>
        <label for="filterByTotalID">Filter by Total:</label>
        <input id="filterByTotalID" name="filterByTotal" type="checkbox"/><br/><br/>

        <label for="fromTotalID">from Total:</label>
        <input id="fromTotalID" name="fromTotal" type="number"/><br/><br/>

        <label for="toTotalID">to Total:</label>
        <input id="toTotalID" name="toTotal" type="number"/><br/><br/>
        <hr>


        <label for="filterByDiscountID">Filter by Discount:</label>
        <input id="filterByDiscountID" name="filterByDiscount" type="checkbox"/><br/><br/>

        <label for="fromDiscountID">from Discount:</label>
        <input id="fromDiscountID" name="fromDiscount" type="number"/><br/><br/>

        <label for="toDiscountID">to Discount:</label>
        <input id="toDiscountID" name="toDiscount" type="number"/><br/><br/>
        <hr>


        <label for="filterByPfrID">Filter by Pension Fund Refund:</label>
        <input id="filterByPfrID" name="filterByPfr" type="checkbox"/><br/><br/>

        <label for="fromPfrID">from Pension Fund Refund:</label>
        <input id="fromPfrID" name="fromPfr" type="number"/><br/><br/>

        <label for="toPfrID">to Pension Fund Refund:</label>
        <input id="toPfrID" name="toPfr" type="number"/><br/><br/>
        <hr>


        <label for="filterByInvoiceDateID">Filter by Invoice Date:</label>
        <input id="filterByInvoiceDateID" name="filterByInvoiceDate" type="checkbox"/><br/><br/>

        <label for="fromInvoiceDateID">from Invoice Date:</label>
        <input id="fromInvoiceDateID" name="fromInvoiceDate" type="date"/><br/><br/>

        <label for="toInvoiceDateID">to Invoice Date:</label>
        <input id="toInvoiceDateID" name="toInvoiceDate" type="date"/><br/><br/>
        <hr>


        <label for="filterByWarningDateID">Filter by Warning Date:</label>
        <input id="filterByWarningDateID" name="filterByWarningDate" type="checkbox"/><br/><br/>

        <label for="fromWarningDateID">from Warning Date:</label>
        <input id="fromWarningDateID" name="fromWarningDate" type="date"/><br/><br/>

        <label for="toWarningDateID">to Warning Date:</label>
        <input id="toWarningDateID" name="toWarningDate" type="date"/><br/><br/>
        <hr>

        <button type="submit" id="listInvoicesByFilters-button">Search with filters</button><br/>
        <button type="reset">Reset filters</button>
    </div>

    <div id="listCustomersByCompanyID-div" class="column">
        <h3>Filter Invoices By Customer(s)</h3>
        <button type="button" id="listCustomersByCompanyID-button">List all the customers</button><br/>
    </div>

    <div id="listProductsByCompanyID-div" class="column">
        <h3>Filter Invoices By Product(s)</h3>
        <button type="button" id="listProductsByCompanyID-button">List all the products</button><br/>
    </div>
</div>

<script type="text/javascript" src="<c:url value="../js/ajax_list_customers_by_companyId.js"/>"></script>
<script type="text/javascript" src="<c:url value="../js/ajax_list_products_by_companyId.js"/>"></script>
<script type="text/javascript" src="<c:url value="../js/ajax_list_invoices_by_filters.js"/>"></script>

</body>
</html>
