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
document.getElementById("listProductsByCompanyID-button")
    .addEventListener("click", listProductsByCompanyId);
console.log("Event listener added to listProductsByCompanyId ajaxButton.")

/**
 * List products associated to the specified company Id
 * @returns {boolean} true if the HTTP request was successful; false otherwise.
 */
function listProductsByCompanyId() {

    // get the values of the filters from the form field

    console.log("## ajax_list_products_by_companyId.js: Filters parsed ##");

    const url = "http://localhost:8080/bitsei-1.0/rest/list-product";

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
        processResponseListProductsByCompanyId(this);
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
function processResponseListProductsByCompanyId(xhr) {

    // not finished yet
    if (xhr.readyState !== XMLHttpRequest.DONE) {
        console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
            xhr.readyState);
        return;
    }

    const div = document.getElementById("listProductsByCompanyID-div");

    // remove all the children of the result div, appended by a previous call, if any
    div.replaceChildren();

    if (xhr.status !== 200) {
        console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
        console.log(xhr.response);

        div.appendChild(document.createTextNode("Unable to perform the AJAX request."));

        return;
    }

    const header = document.createElement("h3");
    header.appendChild(document.createTextNode("Filter Invoices By Product(s)"));
    div.appendChild(header);
    const hr = document.createElement("hr");
    div.appendChild(hr);

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
    eee.appendChild(document.createTextNode("Title"));
    ee.appendChild(eee); // append the cell to the row

    eee = document.createElement("th");
    eee.appendChild(document.createTextNode("Default Price"));
    ee.appendChild(eee); // append the cell to the row

    // table body
    e = document.createElement("tbody");
    table.appendChild(e); // append the table body to the table

    // parse the response as JSON and extract the resource-list array
    const resourceList = JSON.parse(xhr.responseText)["resource-list"];

    for (let i = 0; i < resourceList.length; i++) {

        // extract the i-th customer and create a table row for it
        let product = resourceList[i].product;

        ee = document.createElement("tr");
        e.appendChild(ee); // append the row to the table body

        // create a cell for the businessName of the customer
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(product["title"]));
        ee.appendChild(eee); // append the cell to the row

        // create a cell for the emailAddress of the customer
        eee = document.createElement("td");
        eee.appendChild(document.createTextNode(product["default_price"].toString()));
        ee.appendChild(eee); // append the cell to the row

        console.log("HTTP GET request successfully performed and processed.");
    }

}
