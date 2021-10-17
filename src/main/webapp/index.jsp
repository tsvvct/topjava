<%@ page import="ru.javawebinar.topjava.web.SecurityUtil" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Topjava)</title>
</head>
<body>

<h3>Проект <a href="https://github.com/JavaWebinar/topjava" target="_blank">Java Enterprise (Topjava)</a></h3>
<hr>
<form action="users" method="post" >
    <p><label for="selectedUser">Select user:</label>
        <select id="selectedUser" name="authUser">
            <option selected disabled>Select user</option>
            <option value="1">1</option>
            <option value="2">2</option>
        </select>
        <input type="submit" value="Save">
    </p>
</form>
<hr>
<ul style="font-size: large">
    <li><a href="users">Users</a></li>
    <li><a href="meals">Meals</a></li>
</ul>
</body>
<script>function setValueByElementId(id, valueToSelect)
{
    const element = document.getElementById(id);
    element.value = valueToSelect;
}
setValueByElementId("selectedUser", "${SecurityUtil.authUserId()}")</script>
</html>
