const def = ["Amelogenin", "CSF1PO", "D2S1338", "D3S1358", "D5S818", "D7S820", "D8S1179", "D13S317", "D16S539", "D18S51", "D19S433", "D21S11", "FGA", "Penta_D", "Penta_E", "TH01", "TPOX", "vWA"];
const opt = ["D10S1248", "D1S1656", "D2S441", "D6S1043", "D12S391", "D22S1045", "DXS101", "DYS391", "F13A01", "F13B", "FESFPS", "LPL", "Penta_C", "SE33"];

var jsonData;

$(document).ready(function () {
    var i, e;
    for (i = 0; i < def.length; i++){
        e = document.getElementById("input-" + def[i]);
        if (i === 0) {
            e.onkeypress = e.onpaste = restrictEventXY;
        } else {
            e.onkeypress = e.onpaste = restrictEventSTR;
        }
        e.onblur = validateEvent;
        validate(e);
    }
    for (i = 0; i < opt.length; i++){
        e = document.getElementById("input-" + opt[i]);
        e.onkeypress = e.onpaste = restrictEventSTR;
        e.onblur = validateEvent;
        validate(e);
        if (!document.getElementById("input-" + opt[i]).disabled) {
            document.getElementById("label-" + opt[i]).style.color = "#107dac";
        }
    }
    var qargs = window.location.search.substring(1).split("&");
    for (i = 0; i < qargs.length; i++) {
        var query = qargs[i].split("=");

        if (query.length > 1) {
            if (def.indexOf(query[0]) !== -1) {
                document.getElementById("input-" + query[0]).value = query[1];
            } else if (opt.indexOf(query[0]) !== -1) {
                document.getElementById("input-" + query[0]).value = query[1];
                document.getElementById("input-" + query[0]).disabled = false;
                document.getElementById("check-" + query[0]).checked = true;
                document.getElementById("label-" + query[0]).style.color = "#107dac";
            }
        }
    }
    $("#extension").val("csv");
    window.onscroll = function() {scrollable()};
});

$(function () {
    $(document).tooltip({
        content: function () {
            return $(this).prop('title');
        },
        position: {
            my: "left center",
            at: "right+5 center"
        },
        show: null,
        close: function (event, ui) {
            ui.tooltip.hover(
                function () {
                    $(this).stop(true).fadeTo(400, 1);
                },
                function () {
                    $(this).fadeOut("400", function () {
                        $(this).remove();
                    })
                }
            );
        }
    });
});

function search() {
    var i;
    var v;
    var query = [];
    var html = $("html");
    var c = 0;
    for (i = 0; i < def.length; i++){
        v = document.getElementById("input-" + def[i]).value.split(" ").join("");
        query.push(def[i] + "=" + v);
        if (v !== '') {
            c++;
        }
    }
    for (i = 0; i < opt.length; i++) {
        v = document.getElementById("input-" + opt[i]).value.split(" ").join("");
        if (v !== '') {
            query.push(opt[i] + "=" + v);
        }
    }
    if (c === 0) {
        document.getElementById("results").style.display = "none";
        document.getElementById("warning").style.display = "block";
        document.getElementById("warning").scrollIntoView({behavior: 'smooth', block: "end", inline: "nearest"});
        return;
    }
    html.addClass("waiting");

    query.push("scoring" + "=" + $("input[name=score]:checked").val());
    query.push("size" + "=" + document.getElementById("filter-number").value);
    query.push("filter" + "=" + document.getElementById("filter-score").value);

    $.ajax({
        type: "POST",
        url: "http://129.194.71.205:8080/str-sst/api/results",
        data: query.join("&"),
        contentType: "text/plain",
        dataType: "json",
        success: function (json) {
            if (json.results.length !== 0) {
                jsonData = json;
                new Table(json);
                document.getElementById("results").style.display = "table";
                document.getElementById("warning").style.display = "none";
                document.getElementById('caption').scrollIntoView({behavior: 'smooth', block: "start", inline: "nearest"});
            } else {
                document.getElementById("results").style.display = "none";
                document.getElementById("warning").style.display = "block";
                document.getElementById("warning").scrollIntoView({behavior: 'smooth', block: "end", inline: "nearest"});
            }
        },
        error: function(xhr, textStatus, error){
            alert("Error: The server is not responding\nPlease contact an administrator");
        },
        complete: function () {
            html.removeClass("waiting");
        }
    });
}

class Table {
    constructor(json) {
        this.build(json);
        this.makeSortable();
    }

    build(json) {
        var a, b, c, i, j, t, v, w, map;

        var tr = "";
        var html = "<tr><th class='unselectable b0'>Accession</th><th class='unselectable'>Name</th><th class='unselectable'>Score</th></th>";
        for (i = 0; i < def.length; i++) {
            a = def[i].split("_").join(" ");
            if (a === 'Amelogenin') a = 'Amel';
            html += "<th class='unselectable'>" + a + "</th>";
            tr += "<td>" + document.getElementById("input-" + def[i]).value + "</td>";
        }
        for (i = 0; i < opt.length; i++) {
            if (document.getElementById("check-" + opt[i]).checked === true) {
                html += "<th class='unselectable'>" + opt[i].split("_").join(" ") + "</th>";
                tr += "<td>" + document.getElementById("input-" + opt[i]).value + "</td>";
            }
        }
        html += "</tr><tr><td class='b0'>NA</td><td>Query</td><td>NA</td>" + tr + "</tr>";

        for (i = 0; i < json.results.length; i++) {
            map = {};
            for (a in json.results[i].haplotypes) {
                for (b in json.results[i].haplotypes[a].markers) {
                    t = [];
                    for (c in json.results[i].haplotypes[a].markers[b].alleles) {
                        v = json.results[i].haplotypes[a].markers[b].alleles[c];
                        if (v.matched === false) {
                            t.push("<span style='color:red'>" + v.value + "</span>");
                        } else {
                            t.push(v.value);
                        }
                    }
                    var key = json.results[i].haplotypes[a].markers[b].name.split(" ").join("_");
                    if (json.results[i].haplotypes[a].markers[b].conflicted) {
                        map[key] = "<a class=\"as\" title=\"" + Table.formatSources(json.results[i].haplotypes[a].markers[b].sources) + "\">" + t.join(",") + "</a>";
                    } else {
                        map[key] = t.join(",");
                    }
                }
                html += "<tr>";

                var cls;
                if (json.results[i].haplotypes[a].score >= 80.0) {
                    cls = "b1";
                } else if (json.results[i].haplotypes[a].score < 50.0) {
                    cls = "b2";
                } else {
                    cls = "b3";
                }
                var ver;
                if (a == 0 && json.results[i].haplotypes.length === 2) {
                    ver = "&nbsp;<span style='color:#373434'><i>Best</i></span>";
                } else if (a == 1) {
                    ver = "&nbsp;<span style='color:#373434'><i>Worst</i></span>";
                } else {
                    ver = "";
                }

                if (json.results[i].problematic) {
                    html += "<td class='" + cls + "'><a title='Problematic cell line' style='color:red' href=\"https://web.expasy.org/cellosaurus/" + json.results[i].accession + "\" target=\"_blank\">" + json.results[i].accession + "</a>" + ver + "</td>"
                } else {
                    html += "<td class='" + cls + "'><a href=\"https://web.expasy.org/cellosaurus/" + json.results[i].accession + "\" target=\"_blank\">" + json.results[i].accession + "</a>" + ver + "</td>"
                }

                html += "<td>" + json.results[i].name + "</td>";
                html += "<td>" + json.results[i].haplotypes[a].score.toFixed(2) + "%</td>";

                for (j = 0; j < def.length; j++){
                    v = map[def[j]];
                    if (v === undefined) v = "";
                    html += "<td>" + v + "</td>";
                }
                for (j = 0; j < opt.length; j++){
                    if (document.getElementById("check-" + opt[j]).checked === true) {
                        w = map[opt[j]];
                        if (w === undefined) {
                            w = "";
                        }
                        html += "<td>" + w + "</td>";
                    }
                }
                html += "</tr>";
            }
        }
        var tableResults = $("#table-results");
        tableResults.empty();
        tableResults.append(html);
    }

    static formatSources(sources) {
        var dom = "<b class='unselectable'>Source";
        if (sources.length > 1) {
            dom += "s";
        }
        dom += ":</b><br>";

        for (var i = 0; i < sources.length ; i++) {
            if (sources[i].startsWith("PubMed")) {
                dom += sources[i].substring(0, 7);
                dom += "<a class='ab' href='https://www.ncbi.nlm.nih.gov/pubmed/";
                dom += sources[i].substring(7);
                dom += "' target='_blank'>";
                dom += sources[i].substring(7);
                dom += "</a><br>";
            } else if (sources[i].startsWith("DOI")) {
                dom += sources[i].substring(0, 4);
                dom += "<a class='ab' href='https://www.doi.org/";
                dom += sources[i].substring(4);
                dom += "' target='_blank'>";
                dom += sources[i].substring(4);
                dom += "</a><br>";
            } else {
                dom += sources[i];
                dom += "<br>";
            }
        }
        return dom;
    }

    // Adapted from Nick Grealy
    // https://stackoverflow.com/questions/14267781/sorting-html-table-with-javascript/49041392#49041392
    makeSortable() {
        const getCellValue = (tr, idx) => tr.children[idx].innerText || tr.children[idx].textContent;
        const comparer = (idx, asc) => (a, b) => ((v1, v2) => v1 !== '' && v2 !== '' && !isNaN(v1) && !isNaN(v2) ? v1 - v2 : compare(v1, v2))
        (getCellValue(asc ? a : b, idx), getCellValue(asc ? b : a, idx));

        function compare(v1, v2) {
            if (v1.endsWith("%")) {
                while (v1.length < 7) {
                    v1 = "0" + v1;
                }
                while (v2.length < 7) {
                    v2 = "0" + v2;
                }
            }
            return v1.toString().localeCompare(v2)
        }

        document.querySelectorAll('th').forEach(th => th.addEventListener('click', (() => {
            const table = th.closest('table');
            Array.from(table.querySelectorAll('tr:nth-child(n+3)'))
                .sort(comparer(Array.from(th.parentNode.children).indexOf(th), this.asc = !this.asc))
                .forEach(tr => table.appendChild(tr) );
        })));
    }
}

function restrictEventXY(e) {
    if (e.ctrlKey || e.key === " " || e.key === "Backspace" || e.key === "Delete") return true;
    var char = e.type === "keypress" ? String.fromCharCode(e.keyCode || e.which) : (e.clipboardData || window.clipboardData).getData("Text");
    return !/[^\sxy,]/i.test(char);
}
function restrictEventSTR(e) {
    if (e.ctrlKey || e.key === " " || e.key === "Backspace" || e.key === "Delete")  return true;
    var char = e.type === "keypress" ? String.fromCharCode(e.keyCode || e.which) : (e.clipboardData || window.clipboardData).getData("Text");
    return !/[^\s\d,.]/.test(char);
}
function validateEvent(e) {
    validate(document.getElementById(e.target.id));
}
function validate(a) {
    if (/[,.]{2,}|\d{3,}|(\.\d{2,}|[,.]$|^[,.])/.test(a.value.split(" ").join(""))) {
        a.style.color = "#ff0000";
    } else {
        a.style.color = "#000000";
    }
}

function loadExample() {
    var i;
    for (i = 0; i < def.length; i++){
        document.getElementById("input-" + def[i]).style.color = "#000000";
    }
    for (i = 0; i < opt.length; i++){
        document.getElementById("input-" + opt[i]).value = "";
        document.getElementById("input-" + opt[i]).style.color = "#000000";
    }
    document.getElementById("input-Amelogenin").value = "X";
    document.getElementById("input-CSF1PO").value = "11,12";
    document.getElementById("input-D13S317").value = "11,12";
    document.getElementById("input-D16S539").value = "11,12";
    document.getElementById("input-D18S51").value = "13";
    document.getElementById("input-D19S433").value = "14";
    document.getElementById("input-D21S11").value = "29,30";
    document.getElementById("input-D2S1338").value = "19,23";
    document.getElementById("input-D3S1358").value = "15,17";
    document.getElementById("input-D5S818").value = "11,12";
    document.getElementById("input-D7S820").value = "10";
    document.getElementById("input-D8S1179").value = "10";
    document.getElementById("input-FGA").value = "20,22";
    document.getElementById("input-Penta_D").value = "11,13";
    document.getElementById("input-Penta_E").value = "14,16";
    document.getElementById("input-TH01").value = "6,9";
    document.getElementById("input-TPOX").value = "8,9";
    document.getElementById("input-vWA").value = "17,19";
}
function clearInput() {
    var i;
    for (i = 0; i < def.length; i++){
        document.getElementById("input-" + def[i]).value = "";
        document.getElementById("input-" + def[i]).style.color = "#000000";
    }
    for (i = 0; i < opt.length; i++){
        document.getElementById("input-" + opt[i]).value = "";
        document.getElementById("input-" + opt[i]).style.color = "#000000";
        document.getElementById("check-" + opt[i]).checked = false;
        document.getElementById("input-" + opt[i]).disabled = true;
        document.getElementById("input-" + opt[i]).value = "";
        document.getElementById("label-" + opt[i]).style.color = "#7e7e7e";
    }
    document.getElementById("results").style.display = "none";
    document.getElementById("filter-number").value = 200;
    document.getElementById("filter-score").value = 40;
    document.getElementById("input-masters").checked = true;
    document.getElementById("warning").style.display = "none";
    document.getElementById("information").style.display = "none";
}
function disable(i) {
    if (document.getElementById("input-" + i).disabled) {
        document.getElementById("input-" + i).disabled = false;
        document.getElementById("label-" + i).style.color = "#107dac";
    } else {
        document.getElementById("input-" + i).disabled = true;
        document.getElementById("input-" + i).value = "";
        document.getElementById("label-" + i).style.color = "#7e7e7e";
    }
}

var convert = {
    openDialog: function () {
        dialog.dialog("open");
    },
    toCSV: function (name) {
        var csv = this._tableToCSV(document.getElementById('table-results'));
        var blob = new Blob([csv], {type: "text/csv"});

        if (navigator.msSaveOrOpenBlob) {
            navigator.msSaveOrOpenBlob(blob, name + ".csv");
        } else {
            this._downloadAnchor(URL.createObjectURL(blob), name + ".csv");
        }
    },
    toJson: function (name) {
        var blob = new Blob([JSON.stringify(jsonData, undefined, 2)], {type: "text/plain"});

        if (navigator.msSaveOrOpenBlob) {
            navigator.msSaveOrOpenBlob(blob, name + ".json");
        } else {
            this._downloadAnchor(URL.createObjectURL(blob), name + ".json");
        }
    },
    _downloadAnchor: function (content, name) {
        var anchor = document.createElement("a");
        anchor.style = "display:none !important";
        anchor.id = "downloadanchor";
        document.body.appendChild(anchor);

        if ("download" in anchor) {
            anchor.download = name;
        }
        anchor.href = content;
        anchor.click();
        anchor.remove();
    },
    _tableToCSV: function (table) {
        var rows = [];
        var cells = [];

        var metadata = ",#";
        metadata += "Algorithm: \"";
        metadata += jsonData.parameters.algorithm;
        metadata += "\";";
        metadata += "Score Filter: \"";
        metadata += jsonData.parameters.scoreFilter;
        metadata += "\";";
        metadata += "Size Filter: \"";
        metadata += jsonData.parameters.sizeFilter;
        metadata += "\";";
        metadata += "Description: \"";
        metadata += jsonData.description;
        metadata += "\";";
        metadata += "Data set: \"";
        metadata += "Cellosaurus release " + jsonData.cellosaurusRelease;
        metadata += "\";";
        metadata += "Run on: \"";
        metadata += jsonData.runOn;
        metadata += "\";";
        metadata += "STR-SST version: \"";
        metadata += jsonData.softwareVersion;
        metadata += "\"";

        for (var i = 0; i < table.rows.length; i++) {
            for (var j = 0; j < table.rows[0].cells.length; j++) {
                var cell = '"';
                cell += table.rows[i].cells[j].textContent;
                if (j === 0 &&  table.rows[i].cells[j].innerHTML.startsWith('<a title="Problematic')) {
                    cell += " (Problematic cell line)"
                }
                cell += '"';
                cells.push(cell);
            }
            if (i === 0) {
                rows.push(cells.join(",") + metadata);
            } else {
                rows.push(cells.join(","));
            }
            cells = [];
        }
        return rows.join("\r\n");
    }
};

var dialog, form;
$( function() {
    dialog = $("#dialog-form").dialog({
        autoOpen: false,
        height: 350,
        width: 450,
        modal: true,
        resizable: false,
        closeText: null,
        buttons: {
            Save: function() {
                jsonData.description = $("#description").val();
                if (document.getElementById("extension").value === "csv") {
                    convert.toCSV($("#name").val());
                } else {
                    convert.toJson($("#name").val());
                }
                dialog.dialog("close");
            },
            Cancel: function() {
                dialog.dialog("close");
            }
        },
        open: function(event, ui) {
            $("#form").addClass("blur-filter");
            $("#results").addClass("blur-filter");
            $("#warning").addClass("blur-filter");
        },
        close: function(event, ui) {
            $("#form").removeClass("blur-filter");
            $("#results").removeClass("blur-filter");
            $("#warning").removeClass("blur-filter");
        }

    });
    form = dialog.find("form").on("submit", function( event ) {
        event.preventDefault();
    });
});

function changeIcon(val) {
    if (val === "csv") {
        document.getElementById("imgCsv").style.display = "block";
        document.getElementById("imgJson").style.display = "none";
    } else {
        document.getElementById("imgCsv").style.display = "none";
        document.getElementById("imgJson").style.display = "block";
    }
}

function scrollable() {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        document.getElementById("scroll-up").style.display = "block";
    } else {
        document.getElementById("scroll-up").style.display = "none";
    }
}
function scrollUp() {
    document.getElementById("body").scrollIntoView({behavior: 'smooth', block: "start", inline: "nearest"});
}