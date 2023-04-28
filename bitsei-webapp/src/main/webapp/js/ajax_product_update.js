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

Author: BITSEI GROUP
Version: 1.0
Since: 1.0
*/

// Add an event listener to the button,
// to invoke the function making the AJAX call
document.getElementById("updateProductButton").addEventListener("click", update_product);


/**
 * Searches for employee above the given salary.
 *
 * @returns {boolean} true if the HTTP request was successful; false otherwise.
 */
function update_product() {

    // get the value of the token and parameters from the form field
    const token = document.getElementById("token").value;
    const id_product = document.getElementById("product_ID").value;
    const id_company = document.getElementById("company_ID").value;
    const id_title = document.getElementById("title_ID").value;
    const id_defaultPrice = document.getElementById("defaultPrice_ID").value;
    const id_logo = document.getElementById("logo_ID").value;
    const id_measurement_unit = document.getElementById("measurement_unit_ID").value;
    const id_description = document.getElementById("description_ID").value;


    console.log("token: %d.", token);

    const url = "http://localhost:8080/bitsei-1.0/productupdate";

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
        processResponse(this);
    };

    // define params to send
    const params = {
        product_id: id_product,
        company_id: id_company,
        title: id_title,
        default_price_: id_defaultPrice,
        logo: id_logo,
        measurement_unit: id_measurement_unit,
        description: id_description
    }

    // perform the request
    console.log("Performing the HTTP GET request.");

    xhr.open("PUT", url);
    xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8'");
    xhr.setRequestHeader("Authorization", token);
    xhr.send(JSON.stringify(params));

    console.log("HTTP PUT request sent.");


    // listen for `load` event
    xhr.onload = () => {
        console.log(xhr.responseText)
    }
}

/**
 * Processes the HTTP response and writes the results back to the HTML page.
 *
 * @param xhr the XMLHttpRequest object performing the request.
 */

function processResponse(xhr) {

    // not finished yet
    if (xhr.readyState !== XMLHttpRequest.DONE) {
        console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
            xhr.readyState);
        return;
    }

    const div = document.getElementById("results");

    // remove all the children of the result div, appended by a previous call, if any
    div.replaceChildren();

    if (xhr.status !== 200) {
        console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
        console.log(xhr.response);

        div.appendChild(document.createTextNode("Unable to perform the AJAX request."));

        return;
    }

    // request successful
    console.log("Request successful: HTTP status = %d.", xhr.status);
    // get the JSON response and pass the message to display in jsp results div
    const response = JSON.parse(xhr.response);
    div.appendChild(document.createTextNode(response.message));
}
