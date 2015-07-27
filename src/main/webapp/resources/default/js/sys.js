/**
 * Created by win7 on 27.7.2015.
 */
function feedbackError(message) {
    closeCode='<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>';
    $("#feedback").append('<div class="alert alert-danger fade in">'+closeCode + message+'</div>');
}
