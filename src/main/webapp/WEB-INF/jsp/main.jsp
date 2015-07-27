<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="panel panel-default">
    <div class="panel-heading">Diskuze <a href="#" id="dsRefresh"><span class="glyphicon glyphicon-refresh"></span></a></div>
    <ul class="panel-body list-group" id="ds-list">
        <c:forEach var="dis" items="${discussions.data}">
            <a href="<c:url value="/ds/${dis.id}" />" class="list-group-item"><span class="badge" id="ds-${dis.id}">?</span>${dis.caption}</a>
        </c:forEach>
    </ul>
</div>
