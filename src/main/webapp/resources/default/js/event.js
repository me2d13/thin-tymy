var dimensions = ["postStatus", "preStatus", "sex", "status"];

var attendanceData = {};

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
        case "status": return att.user.status;
    }
}

function addDimensions(distinctCounts) {
    var el = $('#used');
    for (dim of dimensions) {
        if (distinctCounts[dim] > 0) {
            // add ul
            el.append($('<li>').attr("id", "dim-" + dim).addClass("list-group-item").append(dim));
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
    refreshGui(attMap);
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
        while (result.data.length > 0) {
            att = result.data.shift();
            var val = getAttDimension(att, dim);
            if (val !== undefined) {
                if (result.map[val] === undefined) {
                    result.map[val] = {};
                    result.map[val].data = [];
                }
                // move att item to new map
                result.map[val].data.push(att);
            } else {
                if (result.empty === undefined) {
                    result.empty = [];
                }
                result.empty.push(att);
            }
        }
        // now take next dimension
        for (val in result.map) {
            groupByFirstDimmension(result.map[val], dims.slice(0));
        }
    }
}

function refreshGui(attMap) {
    $("#attendance").fadeOut('fast', function() {
        $("#attendance").empty();
        console.log("Building from");
        console.log(attMap);
        buildDom(attMap, $("#attendance"));
        $("#attendance").fadeIn('fast');
    })
}

function buildDom(attMap, parent) {
    if (attMap.map === undefined && attMap.data !== undefined && attMap.data.length > 0) {
        buildUsers(attMap.data, parent);
    } else if (attMap.map !== undefined) {
        buildGroups(attMap.map, attMap.empty, parent);
    } else {
        console.log("Neither data nor attMap:");
        console.log(attMap);
    }
}

function buildUsers(attDataArray, parent) {
    for (var attData of attDataArray) {
        //container
        var uDiv = $('<div>').addClass('ev_det_user');
        //user image
        var uImg = $('<img>').attr('src', attData.user.pictureUrl);
        uDiv.append(uImg);
        //plan image
        var pImg = $('<img>').attr('src', '/attend_pics/2/YES.gif');
        uDiv.append(pImg);
        //result image
        var rImg = $('<img>').attr('src', '/attend_pics/2/YES.gif');
        uDiv.append(rImg);

        parent.append(uDiv);
    }
}

function buildGroups(attMap, empty, parent) {
    for (var key in attMap) {
        var panel = $('<div>').addClass("panel").addClass("panel-default");
        panel.append($('<div>').addClass("panel-heading").append(key + " (" + countUsersBelow(attMap[key])+ ")"));
        var panelBody = $('<div>').addClass("panel-body");
        buildDom(attMap[key], panelBody);
        panel.append(panelBody);
        parent.append(panel);
    }
    if (empty !== undefined && empty.length > 0) {
        var panel = $('<div>').addClass("panel").addClass("panel-default");
        panel.append($('<div>').addClass("panel-heading").append("? (" + empty.length+ ")"));
        var panelBody = $('<div>').addClass("panel-body");
        buildDom(empty, panelBody);
        panel.append(panelBody);
        parent.append(panel);
    }
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
    if (node.empty !== undefined) {
        result += node.empty.length;
    }
    return result;
}