<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>${pageTitle}</title>

    <!-- Bootstrap -->
    <link rel='stylesheet' href='/webjars/bootstrap/3.3.5/css/bootstrap.min.css'>
    <link href="<c:url value="/static/css/main.css" />" rel="stylesheet">
    <script src="/webjars/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('p').animate({
                fontSize: '48px'
            }, "slow");
        });
    </script>
</head>
<body class="container-fluid">
<header class="row">
    <div class="col-sm-2">tymy.cz</div>
    <div class="col-sm-10"></div>
</header>
<main>
    <jsp:include page="/${includePage}"></jsp:include>
</main>
<jsp:useBean id="date" class="java.util.Date" />
<footer>&copy; <fmt:formatDate value="${date}" pattern="yyyy" /> tymy.cz</footer>
</body>
</html>