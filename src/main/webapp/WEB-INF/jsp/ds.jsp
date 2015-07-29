<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:if test="${ds.data.discussion.canWrite}">
    <div class="row">
        <div class="col-xs-1"></div>
        <div class="col-xs-10">
            <div class="panel panel-default">
                <div class="panel-heading">${txt.new_item}</div>
                <div class="panel-body">
                    <c:url var="post_url"  value="/ds/${ds.data.discussion.id}" />
                    <form:form role="form" method="POST" modelAttribute="post" action="${post_url}">
                        <div class="form-group">
                            <form:textarea class="form-control" rows="5" path="post"></form:textarea>
                        </div>
                        <div class="container-fluid">
                            <div class="row">
                                <div class="col-xs-10">
                                    <button type="submit" class="btn btn-primary" name="save" value="save">${txt.insert}</button>
                                    <button type="submit" class="btn btn-default btn-sm"
                                            name="preview" value="preview">${txt.preview}</button>
                                </div>
                                <div class="col-xs-2 text-right">
                                    <a href="#"><span class="glyphicon glyphicon-question-sign"></span></a>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <div class="col-xs-1"></div>
    </div>
</c:if>
<c:forEach var="post" items="${ds.data.posts}">
    <div class="ds-row <c:if test="${post.newPost}">new-post</c:if>">
        <div class="post-picture"><img src="${post.createdBy.pictureUrl}"/></div>
        <div class="post-container post-header">${post.createdBy.callName} - ${post.createdAtStr}</div>
        <div class="post-container">${post.post}</div>
    </div>
</c:forEach>

