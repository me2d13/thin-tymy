/**
 * Created by win7 on 27.7.2015.
 */
function feedbackError(message) {
    closeCode='<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>';
    $("#feedback").append('<div class="alert alert-danger fade in">'+closeCode + message+'</div>');
}

var isTouchDevice = ("ontouchstart" in window) || window.DocumentTouch && document instanceof DocumentTouch;
//var isTouchDevice = true;

function apiLink(serverPath) {
    //return serverPath;

    return "http://192.168.1.5:8180" + serverPath + (serverPath.indexOf("?") >= 0 ? "&" : "?") + "login=admin&password=6c850d3aa3584fe052f32b25b79d2c65";
}
