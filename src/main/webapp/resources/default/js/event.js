var dimensions = ["postStatus", "preStatus", "sex", "status", "preNote"];

var dimCaptions = {};
dimCaptions[dimensions[0]] = 'Docházka';
dimCaptions[dimensions[1]] = 'Plán';
dimCaptions[dimensions[2]] = 'Pohlaví';
dimCaptions[dimensions[3]] = 'Status';
dimCaptions[dimensions[4]] = 'Poznámka';

var statusCaptions = {
    'PLAYER' : 'Hráč',
    'SICK' : 'Marod',
    'MEMBER' : 'Člen'
};

var attendanceData = {};

var emptyKey = '___$$@&^@(&Yasc382ltbiucvocp8yr8374yrclakfu';

$(document).ready(function () {
    data = loadData(eventId);

    var sAv = Sortable.create($('#available')[0], {
        group: "dim"
    });
    var sUsed = Sortable.create($('#used')[0], {
        group: "dim", onSort: onDimMove
    });
})

function loadData(eventId) {
    $.ajax({
        url: apiLink('/api/event/' + eventId),
        dataType: "json",
        success: function (data) {
            if (data.status == "OK") {
                processData(data.data);
            } else {
                feedbackError(data.status);
            }
        },
        error: function (data) {
            feedbackError(data.statusText);
        }
    });
}

function processData(data) {
    //console.log(data);
    // count distinct values of all dimensions
    attendanceData = data;
    distCount = countDistinct(data, dimensions);
    refreshDetails(data);
    addDimensions(distCount);
    onDimMove();
}

function countDistinct(data, dimensions) {
    var res = {};
    var distVals = {};
    for (dim of dimensions) {
        distVals[dim] = [];
    }
    for (att of data.attendance) {
        for (dim of dimensions) {
            // get current value
            val = getAttDimension(att, dim);
            if (val !== undefined) {
                exists = false;
                // check in array of existing values
                for (distVal of distVals[dim]) {
                    if (val == distVal) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    distVals[dim].push(val);
                }
            }
        }
    }
    for (dim of dimensions) {
        res[dim] = distVals[dim].length;
    }
    return res;
}

function getAttDimension(att, dim) {
    switch (dim) {
        case "postStatus":
            return att.postStatus;
        case "preStatus":
            return att.preStatus;
        //case "sex":
        //    return att.user.sex;
        case "status":
            return att.user.status;
        case "preNote":
            return (att.preDescription === undefined || att.preDescription.trim().length == 0) ? undefined : att.preDescription;
    }
}

function addDimensions(distinctCounts) {
    var el = $('#used');
    for (dim of dimensions) {
        if (distinctCounts[dim] > 0) {
            // add ul
            el.append($('<li>').attr("id", "dim-" + dim).addClass("list-group-item").append(dimCaptions[dim]));
            el = $('#available');
        }
    }
}

// returns simple string array with ordered dimension names
function getUsedDimensions() {
    var result = [];
    $('#used li').each(function () {
        result.push(this.id.substring(4));
    });
    return result;
}

function onDimMove(event) {
    var used = getUsedDimensions();
    var attMap = organizeByDimmensions(used, attendanceData.attendance.slice());
    refreshGui(attMap, attendanceData.eventType);
    //console.log(attMap);
}

function organizeByDimmensions(dims, attData) {
    var result = {};
    result.data = attData;
    groupByFirstDimmension(result, dims);
    return result;
}

function groupByFirstDimmension(result, dims) {
    if (dims.length > 0 && result.data !== undefined) {
        var dim = dims.shift();
        result.map = {};
        result.dimension = dim;
        while (result.data.length > 0) {
            att = result.data.shift();
            var val = getAttDimension(att, dim);
            if (val === undefined) {
                val = emptyKey;
            }
            if (result.map[val] === undefined) {
                result.map[val] = {};
                result.map[val].data = [];
            }
            // move att item to new map
            result.map[val].data.push(att);
        }
        // now take next dimension
        for (val in result.map) {
            groupByFirstDimmension(result.map[val], dims.slice(0));
        }
    }
}

function refreshGui(attMap, eventTypeData) {
    $("#attendance").fadeOut('fast', function () {
        $("#attendance").empty();
        //console.log("Building from");
        //console.log(attMap);
        buildDom(attMap, eventTypeData, $("#attendance"));
        $('[data-toggle="tooltip"]').tooltip();
        $("#attendance").fadeIn('fast');
    })
}

function buildDom(attMap, eventTypeData, parent) {
    if (attMap.map === undefined && attMap.data !== undefined && attMap.data.length > 0) {
        buildUsers(attMap.data, eventTypeData, parent);
    } else if (attMap.map !== undefined) {
        buildGroups(attMap, eventTypeData, parent);
    } else {
        console.log("Neither data nor attMap:");
        console.log(attMap);
    }
}

function isBlank(val){
    return (val === undefined || val == null || val.length <= 0) ? true : false;
}

function buildUsers(attDataArray, eventTypeData, parent) {
    for (var attData of attDataArray) {
        //container
        var uDiv = $('<div>').addClass('ev_det_user');
        //user image
        var uImg = $('<img>')
            .attr('src', attData.user.pictureUrl)
            .attr('title', attData.user.displayName)
            .attr('data-toggle', 'tooltip');
        uDiv.append(uImg);
        //plan image
        var pImg = $('<img>').attr('src', getPictureUrl(eventTypeData.preStatusSetId, attData.preStatus));
        if (!isBlank(attData.preDescription)) {
            pImg.attr('title', attData.preDescription).attr('data-toggle', 'tooltip');
            pImg.addClass('att_title');
        }
        uDiv.append(pImg);
        //result image
        var rImg = $('<img>').attr('src', getPictureUrl(eventTypeData.postStatusSetId, attData.postStatus));
        if (!isBlank(attData.postDescription)) {
            pImg.attr('title', attData.postDescription).attr('data-toggle', 'tooltip');
            pImg.addClass('att_title');
        }
        uDiv.append(rImg);

        parent.append(uDiv);
    }
}

function getPictureUrl(setId, code) {
    if (code === undefined || code == '') {
        return '/attend_pics/empty.gif';
    }
    return '/attend_pics/' +
        setId + '/' +
        code + '.gif';
}

function buildGroups(attMap, eventTypeData, parent) {
    for (var key in attMap.map) {
        var panel = $('<div>').addClass("panel").addClass("panel-default");
        panel.append($('<div>').addClass("panel-heading").append(getGroupCaption(key, attMap.dimension, eventTypeData)
            + " (" + countUsersBelow(attMap.map[key]) + ")"));
        var panelBody = $('<div>').addClass("panel-body");
        buildDom(attMap.map[key], eventTypeData, panelBody);
        panel.append(panelBody);
        parent.append(panel);
    }
}

function getGroupCaption(value, dimension, eventTypeData) {
    switch (dimension) {
        case "postStatus":
            if (value == emptyKey) {
                return 'Nevyplněno';
            }
            for (st of eventTypeData.postStatusSet) {
                if (st.code == value) {
                    return st.caption;
                }
            }
            break;
        case "preStatus":
            if (value == emptyKey) {
                return 'Nezadáno';
            }
            for (st of eventTypeData.preStatusSet) {
                if (st.code == value) {
                    return st.caption;
                }
            }
            break;
        case "preNote":
            if (value == emptyKey) {
                return '[prázdná]';
            }
            break;
        //case "sex":
        //    return att.user.sex;
        case "status":
            return statusCaptions[value];
    }

    return (value == emptyKey) ? '?' : value;
}

function countUsersBelow(node) {
    var result = 0;
    if (node.data !== undefined) {
        result += node.data.length;
    }
    if (node.map !== undefined) {
        for (var key in node.map) {
            result += countUsersBelow(node.map[key]);
        }
    }
    return result;
}

function refreshDetails(data) {
    $('#ev_type').text(data.eventType.caption);
    $('#ev_caption').text(data.caption);
    $('#ev_descr').text(data.description);
    $('#ev_start_time').text(data.startTime);
    $('#ev_end_time').text(data.endTime);
    $('#ev_close_time').text(data.closeTime);
    $('#ev_place').text(data.place);
    $('#ev_link').empty();
    if (data.link) {
        var lLink = data.link;
        var lCaption = data.link;
        if (!lLink.startsWith('http://') &&
            !lLink.startsWith('https://') &&
            !lLink.startsWith('ftp://') &&
            !lLink.startsWith('mailto://')) {
            lLink = 'http://' + lLink;
        } else {
            // strip protocol from caption to look nice
            lCaption = lLink.replace(/^[a-z]+:\/\/(.*)/, "$1")
        }
        var anchor = $('<a>').attr('href', lLink).text(lCaption);
        $('#ev_link').append(anchor);
    }
}