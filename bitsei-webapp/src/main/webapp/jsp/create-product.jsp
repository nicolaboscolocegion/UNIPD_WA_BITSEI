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
    <title>Insert new Product</title>
</head>

<body>
<h1>Insert new Product</h1>
<hr/>

<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>

<c:if test = "${message != null}">
    <p>Response:</p>
    <p><c:out value = "${message}"/></p>
    <hr>
</c:if>

<form method="POST" action="<c:url value="/productinsert"/>">

    <label for="company_ID">ID Company:</label>
    <input id="company_ID" name="company_id" type="number"/><br/><br/>

    <label for="title_ID">Title:</label>
    <input id="title_ID" name="title" type="text"/><br/><br/>

    <label for="defaultPrice_ID">Default Price:</label>
    <input id="defaultPrice_ID" name="default_price" type="number"/><br/><br/>

    <label for="logo_ID">Logo:</label>
    <input id="logo_ID" name="logo" type="text"/><br/><br/>

    <label for="measurement_unit_ID">Measurement Unit:</label>
    <input id="measurement_unit_ID" name="measurement_unit" type="text"/><br/><br/>

    <label for="description_ID">Description:</label>
    <input id="description_ID" name="description" type="text"/><br/><br/>
    <hr>

    <button type="submit">Submit</button><br/>
    <button type="reset">Reset the form</button>
</form>


</body>
</html>