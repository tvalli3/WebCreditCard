<%-- 
    Document   : History
    Created on : Oct 1, 2019, 9:38:10 PM
    Author     : Tom Valli
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Log</title>
    </head>
    <body>
        <h1>Account Log for: ${card.accountId}</h1>
        <br>
        <c:forEach var="s" items="${card.creditHistory}">
            <p>${s}</p>
        </c:forEach>
    </body>
</html>
