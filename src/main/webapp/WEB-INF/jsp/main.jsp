<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
ahoj

<h2>This is a list of the HTTP request headers sent by your browser.</h2>

<c:forEach var="v" items="${header}">
    <b>${v.key}</b>: ${v.value}<br />
</c:forEach>

<h2>Here are all the Available Cookies</h2>

<c:forEach var="cookies" items="${cookie}">

    <strong><c:out value=
                           "${cookies.key}"/></strong>: Object=<c:out value="${cookies.value}"/>, value=<c:out value="${cookies.value.value}"/><br />

</c:forEach>
