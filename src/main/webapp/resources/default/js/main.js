/**
 * Created by win7 on 27.7.2015.
 */

var timer = 0;

var month = new Date().getMonth() + 1;
var year = new Date().getFullYear();

var calEvents = [];


function fetchEvents() {
    var lMonth = month - 1;
    var lYear = year;
    if (lMonth == 0) {
        lMonth = 12;
        lYear--;
    }
    var lMonthEnd = month + 2;
    var lYearEnd = year;
    if (lMonthEnd > 12) {
        lMonthEnd = lMonthEnd - 12;
        lYearEnd++;
    }
    var lFilter = "startTime>1." + lMonth + "." + lYear + "~startTime<1." + lMonthEnd + "." + lYearEnd;
    var lUrl = "/thin/events?filter=" + lFilter;

    $.ajax({
        url: apiLink(lUrl),
        dataType: "json",
        success: function (data) {
            if (data.status == "OK") {
                convertApiEventsToCalEvents(lMonth, lYear, data.data);
            } else {
                feedbackError(data.status);
            }
        },
        error: function (data) {
            feedbackError(data.statusText);
        }
    });
}

function convertApiEventsToCalEvents(pMonth, pYear, events) {
    var lEvents = [];
    $.each(events, function (key, value) {
        lEvent = {};
        var lDate = new Date(parseZuluDateTime(value.startTime))
        lEvent["date"] = "" + lDate.getDate() + "/" + (lDate.getMonth()+1) + "/" + lDate.getFullYear();
        lEvent["title"] = value.caption;
        lEvent["content"] = value.caption;
        lEvent["id"] = value.id;
        lEvent["link"] = contextPath + "/event/" + value.id;
        lEvents.push(lEvent);
    });
    var eventDate = new CustomEvent("bicCalendarMarkEvents", {
        detail: {
            month: pMonth,
            year: pYear,
            events: lEvents
        }
    });
    document.dispatchEvent(eventDate);
}

function loadNewItems() {
    if (timer > 0) {
        clearTimeout(timer);
    }
    $("#ds-list > a > span").text("?");
    $.ajax({
        url: apiLink('/thin/discussions/newOnly'),
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

function myDateChangeCallback(pMonth, pYear) {
    month = pMonth + 1; // zero based
    year = pYear;
    fetchEvents();
}

$(document).ready(function () {
    loadNewItems();
    $("#dsRefresh").click(loadNewItems);


    var monthNames = ["Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"];

    var dayNames = ["Po", "Út", "St", "Čt", "Pá", "So", "Ne"];

    $('#calendar1').bic_calendar({
        events: calEvents,
        enableSelect: false,
        dayNames: dayNames,
        monthNames: monthNames,
        showDays: true,
        displayMonthController: false,
        displayYearController: false,
        monthOffset: -1,
        startWeekDay: 0
    });
    $('#calendar2').bic_calendar({
        events: calEvents,
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
        dateChangeCallback: myDateChangeCallback
    });
    $('#calendar3').bic_calendar({
        events: calEvents,
        enableSelect: false,
        dayNames: dayNames,
        monthNames: monthNames,
        showDays: true,
        displayMonthController: false,
        displayYearController: false,
        monthOffset: 1,
        startWeekDay: 0
    });
    fetchEvents();
});

function parseZuluDateTime(v) {
    return Date.parse(v);
}