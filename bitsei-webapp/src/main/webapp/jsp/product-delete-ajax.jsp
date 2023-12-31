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

Author: Andrea Costa (andrea.costa.15@studenti.unipd.it)
Version: 1.0
Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!DOCTYPE html>
<html lang="en">
    <head>
        <title>Delete Product</title>
    </head>

    <body>
    <h1>Delete Product</h1>
    <hr/>

    <!-- display the message -->
    <c:import url="/jsp/include/show-message.jsp"/>
    <br>


    <div>

        <label for="token">Token:</label>
        <input id="token" type="text"/><br/><br/>

        <hr>

        <label for="product_ID">ID Product:</label>
        <input id="product_ID" name="product_id" type="number"/><br/><br/>

        <label for="company_ID">ID Company:</label>
        <input id="company_ID" name="company_id" type="number"/><br/><br/>

        <button type="submit" id="deleteProductButton">Submit</button><br/>
        <button type="reset">Reset the form</button>

    </div>

    <div id="results" style="margin: 2em;"></div>


    <script type="text/javascript" src="<c:url value="/js/ajax_product_delete.js"/>"></script>

    </body>
</html>