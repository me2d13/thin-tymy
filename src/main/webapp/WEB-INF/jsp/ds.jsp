<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <div class="col-xs-1"></div>
    <div class="col-xs-10">input</div>
    <div class="col-xs-1"></div>
</div>
<c:forEach var="post" items="${ds.data.posts}">
    <div class="flex-row">
        <div class="post-picture"><img src="${post.createdBy.pictureUrl}"/></div>
        <div class="post-container">${post.post}</div>
    </div>
</c:forEach>

