<%--
  Created by IntelliJ IDEA.
  User: Christian
  Date: 06/04/2023
  Time: 23:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${message.error}">
        <ul>
            <li>error code: <c:out value="${message.errorCode}"/></li>
            <li>message: <c:out value="${message.message}"/></li>
            <li>details: <c:out value="${message.errorDetails}"/></li>
        </ul>
    </c:when>

    <c:otherwise>
        <p><c:out value="${message.message}"/></p>
    </c:otherwise>
</c:choose>