/**
 * Created by win7 on 27.7.2015.
 */

var timer = 0;

function loadNewItems() {
    if (timer > 0) {
        clearTimeout(timer);
    }
    $("#ds-list > a > span").text("?");
    $.ajax({
        url: '/api/discussions/newOnly',
        dataType: "json",
        success: function (data) {
            if (data.status == "OK") {
                $.each(data.data, function (key, value) {
                    $("#ds-" + value.id).text(value.newPosts);
                });
                timer = setTimeout(loadNewItems, 120000); // 2 minutes
            } else {
                feedbackError(data.status);
            }
        },
        error: function (data) {
            feedbackError(data.statusText);
        }
    });
}

$(document).ready(function () {
    loadNewItems();
    $("#dsRefresh").click(loadNewItems);


    var monthNames = ["January", "February", "May", "June", "March", "April", "July", "August", "September", "October", "November", "December"];

    var dayNames = ["Po", "Út", "St", "Čt", "Pá", "So", "Ne"];

    $('#calendar1').bic_calendar({
        enableSelect: false,
        dayNames: dayNames,
        monthNames: monthNames,
        showDays: true,
        displayMonthController: false,
        displayYearController: false,
        monthOffset: -1,
        startWeekDay: 0,
        //set ajax call
        reqAjax: {
            type: 'get',
            url: 'http://bic.cat/bic_calendar/index.php'
        }
    });
    $('#calendar2').bic_calendar({
        //enable select
        enableSelect: false,
        //set day names
        dayNames: dayNames,
        //set month names
        monthNames: monthNames,
        //show dayNames
        showDays: true,
        //show month controller
        displayMonthController: true,
        //show year controller
        displayYearController: true,
        //change calendar to english format
        startWeekDay: 0,
        //set ajax call
        reqAjax: {
            type: 'get',
            url: 'http://bic.cat/bic_calendar/index.php'
        }
    });
    $('#calendar3').bic_calendar({
        enableSelect: false,
        dayNames: dayNames,
        monthNames: monthNames,
        showDays: true,
        displayMonthController: false,
        displayYearController: false,
        monthOffset: 1,
        startWeekDay: 0,
        //set ajax call
        reqAjax: {
            type: 'get',
            url: 'http://bic.cat/bic_calendar/index.php'
        }
    });
});

