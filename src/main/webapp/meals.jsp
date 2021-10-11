<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
    <title>Show All Meals</title>
    <style type="text/css">
        .excess {
            color: red;
            font-size: 100%;
        }

        .normal {
            color: darkgreen;
            font-size: 100%;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<p><a href="meals?action=insert">Добавить</a></p>
<table border=1>
    <thead>
    <tr>
        <th>№</th>
        <th>Описание</th>
        <th>Каллории</th>
        <th>ДатаВремя</th>
        <th colspan=2>Действие</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${userMeals}" var="mealTo">
        <c:set var="className" scope="page" value="normal"/>
        <c:if test="${mealTo.excess}">
            <c:set var="className" scope="page" value="excess"/>
        </c:if>
        <tr class="<c:out value="${mealTo.excess ? 'excess' : 'normal'}"/>">
            <td><c:out value="${mealTo.id}"/></td>
            <td><c:out value="${mealTo.description}"/></td>
            <td><c:out value="${mealTo.calories}"/></td>
            <td><fmt:parseDate value="${mealTo.dateTime}" pattern="${dateTimeFormatFromStorage}" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="${dateTimeFormatForView}" value="${ parsedDateTime }"/></td>
            <td><a href="meals?action=edit&mealId=<c:out value="${mealTo.id}"/>">Обновить</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${mealTo.id}"/>">Удалить</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>