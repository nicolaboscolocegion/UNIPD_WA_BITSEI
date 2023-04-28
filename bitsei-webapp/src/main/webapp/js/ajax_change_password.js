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
document.getElementById("ajaxButton").addEventListener("click", reset_password);
console.log("Event listener added to ajaxButton.")

/**
 * Searches for employee above the given salary.
 *
 * @returns {boolean} true if the HTTP request was successful; false otherwise.
 */
function reset_password() {

    // get the value of the token and new password from the form field
    const token = document.getElementById("token").value;
    const password = document.getElementById("password").value;

    console.log("token: %d.", token);

    const url = "localhost:8080/bitsei-1.0/rest/user/change-password";

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

    // perform the request
    console.log("Performing the HTTP GET request.");

    xhr.open("POST", url);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify({ "token": token, "password": password }));

    console.log("HTTP POST request sent.");
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
