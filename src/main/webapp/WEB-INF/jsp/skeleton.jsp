<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cs">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>${pageTitle}</title>

    <!-- Bootstrap -->
    <link rel='stylesheet' href='<c:url value="/webjars/bootstrap/3.3.5/css/bootstrap.min.css" />'/>
    <link href="<c:url value="/static/css/main.css" />" rel="stylesheet"/>
    <c:if test="${! empty cssFiles}">
        <c:forEach var="cssFile" items="${cssFiles}">
            <link href="<c:url value="${cssFile}" />" rel="stylesheet" />
        </c:forEach>
    </c:if>
    <script src="<c:url value="/webjars/jquery/2.1.4/jquery.min.js" />"></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.5/js/bootstrap.min.js" />"></script>
    <script>
        var contextPath = "${pageContext.request.contextPath}";
        var tsid = "${sessionScope.TSID}";
        var sysPageTitle = "${pageTitle}";
    </script>
    <script src="<c:url value="/static/js/sys.js" />"></script>
    <c:if test="${! empty jsFiles}">
        <c:forEach var="jsFile" items="${jsFiles}">
            <script src="<c:url value="${jsFile}" />"></script>
        </c:forEach>
    </c:if>
</head>
<body class="container">
<header class="row">
    <div class="col-xs-6">
        <c:choose>
            <c:when test="${! empty sessionScope.loginName}">${sessionScope.loginName}</c:when>
            <c:otherwise><a href="http://${teamSysName}.tymy.cz">${teamSysName}.tymy.cz</a></c:otherwise>
        </c:choose> : <a href="<c:url value="/main" />">main</a></div>
    <div class="col-xs-6 text-right"><a href="<c:url value="/logout" />">logout</a></div>
</header>
<div class="row" id="feedbackRow">
    <div class="col-xs-12 text-center" id="feedback">
        <c:forEach var="error" items="${errors}">
            <div class="alert alert-danger fade in">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>${error}
            </div>
        </c:forEach>
    </div>
</div>
<main>
    <jsp:include page="/${includePage}"></jsp:include>
</main>
<jsp:useBean id="date" class="java.util.Date" />
<footer class="row">
    <div class="col-xs-12 text-right">&copy; <fmt:formatDate value="${date}" pattern="yyyy" /> tymy.cz</div>
</footer>
</body>
</html>