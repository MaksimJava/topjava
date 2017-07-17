<%--
  Created by IntelliJ IDEA.
  User: Максим
  Date: 17.07.2017
  Time: 22:34
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://mycompany.com" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit</title>
</head>
<body>
<form method="post" action="controller" name="save">
    ID : <input type="text" readonly="readonly" name="id"
                value="<c:out value="${meal.id}" />"/> <br/>

    Date : <input required
                  type="datetime" name="date"
                  value="<c:out value="${f:matches(meal.dateTime, 'dd-MM-yyyy HH:mm')}" />"/> <br/>

    Description : <input required
                         type="text" name="description"
                         value="<c:out value="${meal.description}" />"/> <br/>

    Calories : <input required type="text" name="calories"
                      value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
