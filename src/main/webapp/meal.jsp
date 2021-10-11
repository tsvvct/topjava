<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="webjars/datetimepicker/2.5.20-1/jquery.datetimepicker.css"/>
    <style type="text/css">
        table {
            border-spacing: 10px 10px;
        }

        .button {
            padding: 5px;
            margin: 5px
        }

        div {
            padding: 5px;
        }
    </style>
    <title>Add new user meal</title>
</head>
<body>
<form method="POST" action='meals' name="frmAddMeal">
    <table>
        <tr>
            <td>
                <label for="datetimepicker">Дата : </label>
            </td>
            <td>
                <input
                        type="text" name="dateTime" id="datetimepicker"
                        value="<fmt:parseDate value="${meal.dateTime}" pattern="${dateTimeFormatFromStorage}" var="parsedDateTime" type="both" />
<fmt:formatDate pattern="${dateTimeFormatForView}" value="${not empty parsedDateTime ? parsedDateTime : now}" />"/>
            </td>
        </tr>
        <tr>
            <td>
                <label>Описание :</label>
            </td>
            <td>
                <input type="text" name="description" value="${meal.description}"/>
            </td>
        </tr>
        <tr>
            <td>
                <label>Каллории :</label>
            </td>
            <td>
                <input type="number" name="calories" step="1" value="${meal.calories}"/>
            </td>
        </tr>
    </table>
    <div>
        <input type="hidden" readonly="readonly" name="mealId" value="${meal.id}" />
        <input type="submit" class="button" value="Сохранить"/>
        <input type="button" class="button" name="cancel" value="Отмена" onClick="window.location.href='meals';"/>
    </div>
</form>
</body>
<script src="webjars/datetimepicker/2.5.20-1/jquery.js" type="text/javascript"></script>
<script src="webjars/datetimepicker/2.5.20-1/build/jquery.datetimepicker.full.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        $('#datetimepicker').datetimepicker({
            format: '${dateTimePickerFormat}'
        });
    });
</script>
</html>