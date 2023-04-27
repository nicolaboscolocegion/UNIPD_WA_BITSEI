/*
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
*/

// Add an event listener to the button,
// to invoke the function making the AJAX call
document.getElementById("listInvoicesByFilters-button")
    .addEventListener("click", listInvoicesByFilters);
console.log("Event listener added to listInvoicesByFilters ajaxButton.")

/**
 * List invoices by the specified filters
 * @returns {boolean} true if the HTTP request was successful; false otherwise.
 */
function listInvoicesByFilters() {

    // get the values of the filters from the form field
    const filterByTotal = document.getElementById("filterByTotalID").checked;
    let fromTotal = document.getElementById("fromTotalID").value;
    let toTotal = document.getElementById("toTotalID").value;

    const filterByDiscount = document.getElementById("filterByDiscountID").checked;
    let fromDiscount = document.getElementById("fromDiscountID").value;
    let toDiscount = document.getElementById("toDiscountID").value;

    const filterByPfr = document.getElementById("filterByPfrID").checked;
    let fromPfr = document.getElementById("fromPfrID").value;
    let toPfr = document.getElementById("toPfrID").value;

    const filterByInvoiceDate = document.getElementById("filterByInvoiceDateID").checked;
    let fromInvoiceDate = document.getElementById("fromInvoiceDateID").value;
    let toInvoiceDate = document.getElementById("toInvoiceDateID").value;

    const filterByWarningDate = document.getElementById("filterByWarningDateID").checked;
    let fromWarningDate = document.getElementById("fromWarningDateID").value;
    let toWarningDate = document.getElementById("toWarningDateID").value;

    console.log("## ajax_list_invoices_by_filters.js: Filters parsed ##");

    let url = "http://localhost:8080/bitsei-1.0/rest/filter-invoices/with-filters/";
    if(filterByTotal) {
        if(fromTotal.length === 0)
            fromTotal = 0.00;
        if(toTotal.length === 0)
            toTotal = 10E9;
        url += "filterByTotal/" + filterByTotal + "/fromTotal/" + fromTotal + "/toTotal/" + toTotal + "/";
    }
    if(filterByDiscount) {
        if(fromDiscount.length === 0)
            fromDiscount = 0.00;
        if(toDiscount.length === 0)
            toDiscount = 10E9;
        url += "filterByDiscount/" + filterByDiscount + "/fromDiscount/" + fromDiscount + "/toDiscount/" + toDiscount + "/";
    }
    if(filterByPfr) {
        if(fromPfr.length === 0)
            fromPfr = 0.00;
        if(toPfr.length === 0)
            toPfr = 10E9;
        url += "filterByPfr/" + filterByPfr + "/fromPfr/" + fromPfr + "/toPfr/" + toPfr + "/"
    }
    if(filterByInvoiceDate) {
        if(fromInvoiceDate.length === 0)
            fromInvoiceDate = new Date("1970-01-01");
        if(toInvoiceDate.length === 0)
            toInvoiceDate = new Date().toJSON().slice(0, 10);
        url += "filterByInvoiceDate/" + filterByInvoiceDate + "/fromInvoiceDate/" + fromInvoiceDate + "/toInvoiceDate/" + toInvoiceDate + "/";
    }
    if(filterByWarningDate) {
        if(fromWarningDate.length === 0)
            fromWarningDate = new Date("1970-01-01");
        if(toWarningDate.length === 0)
            toWarningDate = new Date().toJSON().slice(0, 10);
        url += "filterByWarningDate/" + filterByWarningDate + "/fromWarningDate/" + fromWarningDate + "/toWarningDate/" + toWarningDate;
    }
    console.log("Request URL: %s.", url)

    // the XMLHttpRequest object
    const xhr = new XMLHttpRequest();

    if (!xhr) {
        console.log("Cannot create an XMLHttpRequest instance.")

        alert("Giving up :( Cannot create an XMLHttpRequest instance");
        return false;
    }

    // set up the call back for handling the request
    xhr.onreadystatechange = function () {
        processResponseListInvoicesByFilters(this);
    };

    // perform the request
    console.log("Performing the HTTP GET request.");

    xhr.open("GET", url, true);
    xhr.send();

    console.log("HTTP GET request sent.");
}

/**
 * Processes the HTTP response and writes the results back to the HTML page.
 *
 * @param xhr the XMLHttpRequest object performing the request.
 */
function processResponseListInvoicesByFilters(xhr) {

    // not finished yet
    if (xhr.readyState !== XMLHttpRequest.DONE) {
        console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
            xhr.readyState);
        return;
    }

    const div = document.getElementById("listInvoicesByFilters-div");

    // remove all the children of the result div, appended by a previous call, if any
    div.replaceChildren();

    if (xhr.status !== 200) {
        console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
        console.log(xhr.response);

        div.appendChild(document.createTextNode("Unable to perform the AJAX request."));

        return;
    }

    // generate the table, appending node-by-node
    const table = document.createElement("table");
    div.appendChild(table)

    // placeholders for generic DOM nodes
    let e, ee, eee;


    // table header
    e = document.createElement("thead");
    table.appendChild(e); // append the table header to the table

    // the row in the table header
    ee = document.createElement("tr");
    e.appendChild(ee); // append the row to the table header

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Invoice Number"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Warning Date"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Invoice Date"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Total"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Discount"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Pension Fund Refund"));
    ee.appendChild(eee); // append the cell to the row

    // table body
    e = document.createElement("tbody");
    table.appendChild(e); // append the table body to the table

    // parse the response as JSON and extract the resource-list array
    const resourceList = JSON.parse(xhr.responseText)["resource-list"];

    for (let i = 0; i < resourceList.length; i++) {

        // extract the i-th invoice and create a table row for it
        let invoice = resourceList[i].invoice;

        ee = document.createElement("tr");
        e.appendChild(ee); // append the row to the table body

        // create a cell for the invoice_number of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["invoice_number"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the warning_date of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["warning_date"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the invoice_date of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["invoice_date"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the total of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["total"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the discount of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["discount"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the pension_fund_refund of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["pension_fund_refund"]));
        ee.appendChild(eee); // append the cell to the row

        console.log("HTTP GET request successfully performed and processed.");
    }

/*
function processResponseListInvoicesByFilters(xhr) {

    // not finished yet
    if (xhr.readyState !== XMLHttpRequest.DONE) {
        console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
            xhr.readyState);
        return;
    }

    const div = document.getElementById("listInvoicesByFilters-div");

    // remove all the children of the result div, appended by a previous call, if any
    div.replaceChildren();

    if (xhr.status !== 200) {
        console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
        console.log(xhr.response);

        div.appendChild(document.createTextNode("Unable to perform the AJAX request."));

        return;
    }

    // generate the table, appending node-by-node
    const table = document.createElement("table");
    div.appendChild(table)

    // placeholders for generic DOM nodes
    let e, ee, eee;


    // table header
    e = document.createElement("thead");
    table.appendChild(e); // append the table header to the table

    // the row in the table header
    ee = document.createElement("tr");
    e.appendChild(ee); // append the row to the table header

    // a generic element of the table header row
    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Invoice ID"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Customer ID"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Status"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Warning Number"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Warning Date"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Invoice Number"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Invoice Date"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Total"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Discount"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Pension Fund Refund"));
    ee.appendChild(eee); // append the cell to the row

    // table body
    e = document.createElement("tbody");
    table.appendChild(e); // append the table body to the table

    // parse the response as JSON and extract the resource-list array
    const resourceList = JSON.parse(xhr.responseText)["resource-list"];

    for (let i = 0; i < resourceList.length; i++) {

        // extract the i-th invoice and create a table row for it
        let invoice = resourceList[i].invoice;

        ee = document.createElement("tr");
        e.appendChild(ee); // append the row to the table body

        // create a cell for the invoice_id of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["invoice_id"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the customer_id of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["customer_id"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the status of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["status"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the warning_number of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["warning_number"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the warning_date of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["warning_date"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the invoice_number of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["invoice_number"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the invoice_date of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["invoice_date"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the total of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["total"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the discount of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["discount"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the pension_fund_refund of the invoice
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(invoice["pension_fund_refund"]));
        ee.appendChild(eee); // append the cell to the row

        console.log("HTTP GET request successfully performed and processed.");
    }
*/

}
