<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    var eventId = ${eventId};
</script>
<div class="row">
    <div class="col-sm-2">
        <div class="panel panel-primary">
            <div class="panel-heading">Seskupeno</div>
            <div class="panel-body">
                <ul class="list-group sortable-container" id="used">

                </ul>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">Skupiny</div>
            <div class="panel-body">
                <ul class="list-group sortable-container" id="available">

                </ul>
            </div>
        </div>
    </div>
    <div class="col-sm-10" id="attendance">
        Loading...
    </div>
</div>
