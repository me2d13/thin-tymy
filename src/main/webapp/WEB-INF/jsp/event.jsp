<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    var eventId = ${eventId};
</script>
<div class="row">
    <div class="col-sm-10 col-sm-push-2">
        <div id="attendance">
            Loading...
        </div>
        <div id="event_details">
            <div class="panel panel-default">
                <div class="panel-heading">${txt.event_detail}</div>
                <div class="panel-body">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td>${txt.type}</td>
                            <td id="ev_type"></td>
                        </tr>
                        <tr>
                            <td>${txt.caption}</td>
                            <td id="ev_caption"></td>
                        </tr>
                        <tr>
                            <td>${txt.descr}</td>
                            <td id="ev_descr"></td>
                        </tr>
                        <tr>
                            <td>${txt.start_time}</td>
                            <td id="ev_start_time"></td>
                        </tr>
                        <tr>
                            <td>${txt.end_time}</td>
                            <td id="ev_end_time"></td>
                        </tr>
                        <tr>
                            <td>${txt.close_time}</td>
                            <td id="ev_close_time"></td>
                        </tr>
                        <tr>
                            <td>${txt.place}</td>
                            <td id="ev_place"></td>
                        </tr>
                        <tr>
                            <td>${txt.link}</td>
                            <td id="ev_link"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-2 col-sm-pull-10">
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
</div>
