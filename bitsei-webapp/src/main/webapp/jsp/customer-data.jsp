<%--
  Created by IntelliJ IDEA.
  User: Mirco
  Date: 04/04/2023
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>


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
