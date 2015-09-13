<%@ page import="java.util.Enumeration" %>
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
  <title>Debug page</title>

  <!-- Bootstrap -->
  <link rel='stylesheet' href='<c:url value="/webjars/bootstrap/3.3.5/css/bootstrap.min.css" />'>
  <link href="<c:url value="/static/css/main.css" />" rel="stylesheet">
  <script src="<c:url value="/webjars/jquery/2.1.4/jquery.min.js" />"></script>
  <script type="text/javascript">
    $(document).ready(function () {
    });
  </script>
</head>
<body class="container">
<header class="row">
  <div class="col-xs-6">${teamSysName}.tymy.cz</div>
  <div class="col-xs-6 text-right">logout</div>
</header>
<main>
  <h2>This is a list of the HTTP request headers sent by your browser.</h2>

  <c:forEach var="v" items="${header}">
    <b>${v.key}</b>: ${v.value}<br/>
  </c:forEach>

  <h2>Here are all the Available Cookies</h2>

  <c:forEach var="cookies" items="${cookie}">
    <strong><c:out value= "${cookies.key}"/></strong>: Object=<c:out value="${cookies.value}"/>, value=<c:out value="${cookies.value.value}"/><br/>
  </c:forEach>

  <h2>Here are all the available session vars</h2>

  <%
  Enumeration En = (Enumeration) (session.getAttributeNames());

  while ( En.hasMoreElements()){
  out.println("Attribute-->"+En.nextElement());
  out.println("<br/>");
  }
    %>

</main>
<jsp:useBean id="date" class="java.util.Date" />
<footer class="row">
  <div class="col-xs-12 text-right">&copy; <fmt:formatDate value="${date}" pattern="yyyy" /> tymy.cz</div>
</footer>
</body>
</html>