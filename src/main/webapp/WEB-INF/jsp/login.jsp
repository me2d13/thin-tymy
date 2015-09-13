<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <script src="<c:url value="/webjars/jquery/2.1.4/jquery.min.js" />"></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.5/js/bootstrap.min.js" />"></script>
    <script src="<c:url value="/static/js/md5.js" />"></script>
    <script>
        $(document).ready(function () {
            $('form').submit(function () {
                var txt = $('#password');
                for (var i = 0; i < Math.floor((Math.random() * 10) + 2); i++) {
                    txt.val(md5(txt.val()));
                }
            });
        });
    </script>
</head>
<body class="container">
<header class="row">
    <a href="http://www.tymy.cz">www.tymy.cz</a>
</header>
<div class="row" id="feedbackRow">
    <div class="col-xs-12 text-center" id="feedback">
        <c:forEach var="error" items="${errors}">
            <div class="alert alert-danger fade in">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>${error}
            </div>
        </c:forEach>
        <c:forEach var="info" items="${infos}">
            <div class="alert alert-success fade in">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>${info}
            </div>
        </c:forEach>
    </div>
</div>
<main>

    <div class="col-xs-1 col-sm-3"></div>
    <div class="col-xs-10 col-sm-6">
        <div class="row">
            <c:url var="post_url"  value="/doLogin" />
            <form:form class="form-horizontal" role="form" method="POST" action="${post_url}" modelAttribute="lf">
                <div class="form-group">
                    <label class="control-label col-xs-5" for="login">Login:</label>

                    <div class="col-xs-7">
                        <form:input path="login" type="text" class="form-control" id="login"
                                    placeholder="Přihlašovací jméno"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-xs-5" for="password">Heslo:</label>

                    <div class="col-xs-7">
                        <form:password path="password" class="form-control" id="password" placeholder="Zadejte heslo"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-offset-5 col-xs-7">
                        <button type="submit" class="btn btn-default">Přihlásit</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <div class="col-xs-1 col-sm-3"></div>
</main>
<jsp:useBean id="date" class="java.util.Date"/>
<footer class="row">
    <div class="col-xs-12 text-right">&copy; <fmt:formatDate value="${date}" pattern="yyyy"/> tymy.cz</div>
</footer>
</body>
</html>