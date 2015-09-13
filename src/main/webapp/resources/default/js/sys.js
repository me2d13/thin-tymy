/**
 * Created by win7 on 27.7.2015.
 */
function feedbackError(message) {
    closeCode='<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>';
    $("#feedback").append('<div class="alert alert-danger fade in">'+closeCode + message+'</div>');
}

var isTouchDevice = ("ontouchstart" in window) || window.DocumentTouch && document instanceof DocumentTouch;
//var isTouchDevice = true;


if(!String.prototype.startsWith){
    String.prototype.startsWith = function (str) {
        return !this.indexOf(str);
    }
}

function apiLink(serverPath) {
    var result = '/api/'+serverPath;
    if (tsid !== undefined && tsid != null && tsid.length > 0) {
        if (result.indexOf('?') > 0) {
            result += '&TSID=' + tsid;
        } else {
            result += '?TSID=' + tsid;
        }
    }
    return result;
}
