<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <hr>
    <%-- Не могу раскопать как сделать тернарным оператором так не работает--%>
    <%--    <h2>${empty meal.getId() ? '<spring:message code="app.update"/>' : '<spring:message code="app.create"/>'}</h2>--%>
    <%--        <h2>${empty meal.getId() ? <spring:message code="app.update"/> : <spring:message code="app.create"/>}</h2>--%>
    <h2><c:choose>
        <c:when test="${empty meal.getId()}">
            <spring:message code="app.create"/>
        </c:when>
        <c:otherwise>
            <h2><spring:message code="app.update"/></h2>
        </c:otherwise>
    </c:choose></h2>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.dateTime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="app.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="app.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
