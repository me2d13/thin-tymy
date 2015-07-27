/**
 * Created by win7 on 27.7.2015.
 */

var timer = 0;

function loadNewItems() {
    if (timer > 0) {
        clearTimeout(timer);
    }
    $("#ds-list > a > span").text("?");
    $.getJSON("/api/discussions/newOnly", function(data, status){
        if (status=="success" && data.status=="OK") {
            $.each(data.data, function(key, value) {
                $("#ds-" + value.id).text(value.newPosts);
            })
        }
    });
    timer = setTimeout(loadNewItems, 120000); // 2 minutes
}

$(document).ready(function () {
    loadNewItems();
    $("#dsRefresh").click(loadNewItems);
});

