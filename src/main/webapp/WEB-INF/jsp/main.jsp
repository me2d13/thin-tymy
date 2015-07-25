<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="panel panel-default">
    <div class="panel-heading">Diskuze</div>
    <ul class="panel-body list-group">
        <c:forEach var="dis" items="${discussions.data}">
            <li class="list-group-item"><span class="badge">?</span>${dis.caption}</li>
        </c:forEach>
    </ul>
</div>
