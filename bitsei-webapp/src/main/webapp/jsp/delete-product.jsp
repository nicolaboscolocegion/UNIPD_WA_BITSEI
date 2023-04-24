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
<h1>Delete Product by Id</h1>
<hr/>

<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>

<c:if test = "${message != null}">
    <p>Response:</p>
    <p><c:out value = "${message}"/></p>
    <hr>
</c:if>

<form method="POST" action="<c:url value="/productinsert"/>">

    <label for="product_ID">ID Product:</label>
    <input id="product_ID" name="product_id" type="number"/><br/><br/>
    <hr>

    <button type="submit">Submit</button><br/>
    <button type="reset">Reset the form</button>
</form>


</body>
</html>