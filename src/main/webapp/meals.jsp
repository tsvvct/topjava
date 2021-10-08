<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Show All Meals</title>
    <style type="text/css">
        .excess {
            color: red;
            font-size: 120%;
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
<table border=1>
    <thead>
    <tr>
        <th>№</th>
        <th>Описание</th>
        <th>Каллории</th>
        <th>ДатаВремя</th>
    </tr>
    </thead>
    <tbody>
    <%
        int i = 1;
        DateTimeFormatter dateTimeFormatter = (DateTimeFormatter) request.getAttribute("dateTimeFormatter");
        List<MealTo> mealList = (List<MealTo>) request.getAttribute("userMeals");
        for (MealTo mealTo : mealList) {
            String rowClass = mealTo.isExcess() ? "excess" : "normal";
    %>
    <tr class="<%=rowClass%>">
        <td><%=i++%>
        </td>
        <td><%=mealTo.getDescription()%>
        </td>
        <td><%=mealTo.getCalories()%>
        </td>
        <td><%=mealTo.getDateTime().format(dateTimeFormatter)%>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>