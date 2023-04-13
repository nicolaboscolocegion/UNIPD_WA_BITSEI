<%--
  Created by IntelliJ IDEA.
  User: Mirco
  Date: 04/04/2023
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>

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

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Search Employee</title>
</head>

<body>
<h1>Search Employee</h1>
<hr/>

<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>

<!-- display the list of found employees, if any -->
    <table>
        <thead>
        <tr>
            <th>BNAME</th><th>P.IVA</th><th>CF</th>
        </tr>
        </thead>

        <tbody>

            <tr>
                <td><c:out value="${customer.businessName}"/></td>
                <td><c:out value="${employee.vatNumber}"/></td>
                <td><c:out value="${employee.taxCode}"/></td>

            </tr>
        </tbody>
    </table>

</body>
</html>
