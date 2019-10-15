const html = $("html");

let jsonInput, jsonResponse, dialogImport, dialogExport, species, speciesNames;
let markers = {};

$(document).ready(function () {
    $.when(initializeMarkers()).then(function() {
        reset();
        parseURLVariables();
        bindAllEvents();
    });
});

function initializeMarkers() {
    let humanDefaultMarkers = [];
    let humanOptionalMarkers = [];
    $(".label-human").each(function() {humanDefaultMarkers.push(this.innerText.split(' ').join('_'))});
    $(".label-human-optional").each(function() {humanOptionalMarkers.push(this.innerText.split(' ').join('_'))});
    markers["Homo sapiens"] = {
        "default": humanDefaultMarkers,
        "optional": humanOptionalMarkers
    };
    let mouseDefaultMarkers = [];
    $(".label-mouse").each(function() {mouseDefaultMarkers.push(this.innerText.split(' ').join('_'))});
    markers["Mus musculus"] = {
        "default": mouseDefaultMarkers,
        "optional": []
    };
    let dogDefaultMarkers = [];
    $(".label-dog").each(function() {dogDefaultMarkers.push(this.innerText.split(' ').join('_'))});
    markers["Canis lupus familiaris"] = {
        "default": dogDefaultMarkers,
        "optional": []
    };
    speciesNames = {
        "human": "Homo sapiens",
        "Homo sapiens": "human",
        "mouse": "Mus musculus",
        "Mus musculus": "mouse",
        "dog": "Canis lupus familiaris",
        "Canis lupus familiaris": "dog"
    };
}

function reset() {
    resetAllMarkers();
    switchSpecies("human");

    document.getElementById("input-tanabe").checked = true;
    document.getElementById("input-nonempty").checked = true;
    document.getElementById("check-include-Amelogenin").checked = false;
    document.getElementById("filter-score").value = 60;
    document.getElementById("filter-markers").value = 8;
    document.getElementById("filter-size").value = 200;
    document.getElementById("results").style.display = "none";
    document.getElementById("warning").style.display = "none";
    document.getElementById("import-name").value = "CLASTR_Results";
    document.getElementById("export-name").value = "CLASTR_Results";
    document.getElementById("import-extension").value = "xlsx";
    document.getElementById("export-extension").value = "xlsx";
    document.getElementById("input-file").value = "";
    document.getElementById("import-help").innerHTML = "";
    document.getElementById("samples").innerHTML = "";
    document.getElementById("sample-human").innerHTML = "";
    document.getElementById("sample-human").style.display = "none";
    document.getElementById("sample-mouse").innerHTML = "";
    document.getElementById("sample-mouse").style.display = "none";
    document.getElementById("sample-dog").innerHTML = "";
    document.getElementById("sample-dog").style.display = "none";
    document.getElementById("warning").style.opacity = "0";
    document.getElementById("results").style.opacity = "0";

    $("#sib_header_small,#sib_footer").width("100%");
    $("#batch").button().attr('disabled', true).addClass('ui-state-disabled');

    switchIcon("import", "xlsx");
    switchIcon("export", "xlsx");
}

function example() {
    resetMarkers(species);
    switchSpecies(speciesNames[species]);

    if (species === "Homo sapiens") {
        document.getElementById("input-Amelogenin").value = "X";
        document.getElementById("input-CSF1PO").value = "11,12";
        document.getElementById("input-D2S1338").value = "19,23";
        document.getElementById("input-D3S1358").value = "15,17";
        document.getElementById("input-D5S818").value = "11,12";
        document.getElementById("input-D7S820").value = "10";
        document.getElementById("input-D8S1179").value = "10";
        document.getElementById("input-D13S317").value = "11,12";
        document.getElementById("input-D16S539").value = "11,12";
        document.getElementById("input-D18S51").value = "13";
        document.getElementById("input-D19S433").value = "14";
        document.getElementById("input-D21S11").value = "29,30";
        document.getElementById("input-FGA").value = "20,22";
        document.getElementById("input-Penta_D").value = "11,13";
        document.getElementById("input-Penta_E").value = "14,16";
        document.getElementById("input-TH01").value = "6,9";
        document.getElementById("input-TPOX").value = "8,9";
        document.getElementById("input-vWA").value = "17,19";
        document.getElementById("description").value = "HT-29";
        document.getElementById("sample-human").innerHTML = "Example <span class='text-purple'>HT-29</span> loaded";

        $("#sample-human").show("slide", 400);
    } else if (species === "Mus musculus") {
        document.getElementById("input-Mouse_STR_1-1").value = "10";
        document.getElementById("input-Mouse_STR_1-2").value = "16,17";
        document.getElementById("input-Mouse_STR_2-1").value = "9";
        document.getElementById("input-Mouse_STR_3-2").value = "14";
        document.getElementById("input-Mouse_STR_4-2").value = "13,21.3";
        document.getElementById("input-Mouse_STR_5-5").value = "14";
        document.getElementById("input-Mouse_STR_6-4").value = "18";
        document.getElementById("input-Mouse_STR_6-7").value = "12";
        document.getElementById("input-Mouse_STR_7-1").value = "26";
        document.getElementById("input-Mouse_STR_8-1").value = "16";
        document.getElementById("input-Mouse_STR_11-2").value = "16";
        document.getElementById("input-Mouse_STR_12-1").value = "16";
        document.getElementById("input-Mouse_STR_13-1").value = "17";
        document.getElementById("input-Mouse_STR_15-3").value = "26.3";
        document.getElementById("input-Mouse_STR_17-2").value = "15";
        document.getElementById("input-Mouse_STR_18-3").value = "16,17";
        document.getElementById("input-Mouse_STR_19-2").value = "12,14";
        document.getElementById("input-Mouse_STR_X-1").value = "26";
        document.getElementById("description").value = "P19";
        document.getElementById("sample-mouse").innerHTML = "Example <span class='text-purple'>P19</span> loaded";

        $("#sample-mouse").show("slide", 400);
    } else if (species === "Canis lupus familiaris") {
        document.getElementById("input-Dog_FHC2010").value = "235";
        document.getElementById("input-Dog_FHC2054").value = "156,164";
        document.getElementById("input-Dog_FHC2079").value = "271,275";
        document.getElementById("input-Dog_PEZ1").value = "115,119";
        document.getElementById("input-Dog_PEZ12").value = "274,285";
        document.getElementById("input-Dog_PEZ20").value = "176,184";
        document.getElementById("input-Dog_PEZ3").value = "121";
        document.getElementById("input-Dog_PEZ5").value = "107,111";
        document.getElementById("input-Dog_PEZ6").value = "179";
        document.getElementById("input-Dog_PEZ8").value = "228,232";
        document.getElementById("description").value = "STSA-1";
        document.getElementById("sample-dog").innerHTML = "Example <span class='text-purple'>STSA-1</span> loaded";

        $("#sample-dog").show("slide", 400);
    }
}

function parseURLVariables() {
    if (window.location.search.length > 0) {
        let name;

        let a = decodeURIComponent(window.location.search).substring(1).split("&");
        for (let i = 0; i < a.length; i++) {
            if (a[i] === undefined || a[i].length === 0 || !a[i].includes('=')) continue;

            let s = a[i].split("=");
            let key = s[0].split(" ").join("_");
            let value = s[1];

            if (key === "name") {
                document.title = "CLASTR - " + value;
                document.getElementById("description").value = value;
                name = value;
            } else if (key.startsWith("Mouse")) {
                switchSpecies("mouse");
                if (key !== "Mouse_STR_9-2") document.getElementById("input-" + key).value = value;
            } else if (key.startsWith("Dog")) {
                switchSpecies("dog");
                document.getElementById("input-" + key).value = value;
            } else {
                if (markers["Homo sapiens"]["default"].indexOf(key) !== -1) {
                    document.getElementById("input-" + key).value = value;
                } else if (markers["Homo sapiens"]["optional"].indexOf(key) !== -1) {
                    document.getElementById("input-" + key).value = value;
                    document.getElementById("input-" + key).disabled = false;
                    document.getElementById("check-" + key).checked = true;
                    document.getElementById("label-" + key).style.color = "#107dac";
                }
            }
        }
        document.getElementById("sample-" + speciesNames[species]).innerHTML = "Cellosaurus entry <span class='text-purple'>" + name + "</span> loaded";
        $("#sample-" + speciesNames[species]).show("slide", 400);
    }
}

function bindAllEvents() {
    bindEvents("Homo sapiens");
    bindEvents("Mus musculus");
    bindEvents("Canis lupus familiaris");

    window.onscroll = scrollable;
}

function bindEvents(name) {
    let defaultMarkers = markers[name]["default"];
    for (let i = 0; i < defaultMarkers.length; i++){
        let e = document.getElementById("input-" + defaultMarkers[i]);
        if (e.id === "input-Amelogenin") {
            e.onkeypress = e.onpaste = restrictXYEvent;
        } else {
            e.onkeypress = e.onpaste = restrictSTREvent;
        }
        e.onblur = blurEvent;
        e.oninput = inputEvent;
    }
    let optionalMarkers = markers[name]["optional"];
    for (let i = 0; i < optionalMarkers.length; i++){
        let e = document.getElementById("input-" + optionalMarkers[i]);
        e.onkeypress = e.onpaste = restrictSTREvent;
        e.onblur = blurEvent;
        e.oninput = inputEvent;
    }
    $("#input-" + speciesNames[name]).click(function(){switchSpecies(speciesNames[name])});
}

function resetAllMarkers() {
    resetMarkers("Homo sapiens");
    resetMarkers("Mus musculus");
    resetMarkers("Canis lupus familiaris");
}

function resetMarkers(name) {
    let defaultMarkers = markers[name]["default"];
    for (let i = 0; i < defaultMarkers.length; i++){
        document.getElementById("input-" + defaultMarkers[i]).value = "";
        document.getElementById("input-" + defaultMarkers[i]).style.color = "#000000";
        document.getElementById("input-" + defaultMarkers[i]).style.borderColor = "#ccc";
    }
    let optionalMarkers = markers[name]["optional"];
    for (let i = 0; i < optionalMarkers.length; i++){
        document.getElementById("input-" + optionalMarkers[i]).value = "";
        document.getElementById("input-" + optionalMarkers[i]).style.color = "#000000";
        document.getElementById("input-" + optionalMarkers[i]).style.borderColor = "#ccc";
        document.getElementById("input-" + optionalMarkers[i]).disabled = true;
        document.getElementById("label-" + optionalMarkers[i]).style.color = "#7e7e7e";
        document.getElementById("check-" + optionalMarkers[i]).checked = false;
    }
}

function markerComparator(x, y) {
    if (x.charAt(0) === 'D' && !isNaN(x.charAt(1)) && y.charAt(0) === 'D' && !isNaN(y.charAt(1))) {
        let c1 = parseInt(x.substring(1, (x.charAt(2) === 'S') ? 2 : 3), 10);
        let c2 = parseInt(y.substring(1, (y.charAt(2) === 'S') ? 2 : 3), 10);

        return c1 - c2;
    }
    return x.localeCompare(y);
}

function switchSpecies(name) {
    species = speciesNames[name];
    document.getElementById("markers-human").style.display = "none";
    document.getElementById("markers-mouse").style.display = "none";
    document.getElementById("markers-dog").style.display = "none";
    document.getElementById("markers-" + name).style.display = "block";
    $("#input-human").removeClass("active");
    $("#input-mouse").removeClass("active");
    $("#input-dog").removeClass("active");
    $("#input-" + name).addClass("active");
    if (name === "human") {
        $("#label-include-Amelogenin").removeClass("off");
    } else {
        $("#label-include-Amelogenin").addClass("off");
    }
}

function switchIcon(name, value) {
    document.getElementById(name + "-xlsx").style.display = "none";
    document.getElementById(name + "-csv").style.display = "none";
    document.getElementById(name + "-json").style.display = "none";

    switch (value) {
        case "xlsx":
            document.getElementById(name + "-xlsx").style.display = "block";
            break;
        case "csv":
            document.getElementById(name + "-csv").style.display = "block";
            break;
        case "json":
            document.getElementById(name + "-json").style.display = "block";
            break;
    }
}

function restrictXYEvent(e) {
    if (e.ctrlKey || e.key === " " || e.key === "Backspace" || e.key === "Delete") return true;
    let char = e.type === "keypress" ? String.fromCharCode(e.keyCode || e.which) : (e.clipboardData || window.clipboardData).getData("Text");
    return !/[^\sxy,]/i.test(char);
}

function restrictSTREvent(e) {
    if (e.ctrlKey || e.key === " " || e.key === "Backspace" || e.key === "Delete")  return true;
    let char = e.type === "keypress" ? String.fromCharCode(e.keyCode || e.which) : (e.clipboardData || window.clipboardData).getData("Text");
    return !/[^\s\d,.]/.test(char);
}

function inputEvent() {
    if (species === "Homo sapiens") {
        document.getElementById("sample-human").innerHTML = "";
    } else if (species === "Mus musculus") {
        document.getElementById("sample-mouse").innerHTML = "";
    } else if (species === "Canis lupus familiaris") {
        document.getElementById("sample-dog").innerHTML = "";
    }
}

function blurEvent(e) {
    validateElement(document.getElementById(e.target.id));
}

function validateElement(e) {
    let s = e.value.split(" ").join("");
    let wrong = "color:#ff0000;border-color:#ff0000";
    let right = "color:#000000;border-color:#ccc";

    if (s.length === 0) {
        e.style = right;
    } else if (/[,.]{2,}|\d{3,}|(\.\d{2,}|[,.]$|^[,.])/.test(s)) {
        if (e.id.indexOf("Dog") === -1 || /[,.]{2,}|\d{4,}|(\.\d{2,}|[,.]$|^[,.])/.test(s)) {
            e.style = wrong;
        }
    } else if (e.id === "input-Amelogenin") {
        e.style = /^[xyXY](,[xyXY])?$/.test(s) ? right : wrong;
    } else if (/^[0-9,.]+$/.test(s)) {
        e.style = right;
    } else {
        e.style = wrong;
    }
}

function scrollable() {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        document.getElementById("scroller").style.display = "block";
    } else {
        document.getElementById("scroller").style.display = "none";
    }
}

function scrollUp() {
    document.body.scrollIntoView({behavior: 'instant', block: "start", inline: "nearest"});
}

function jsonParameters() {
    let map = {};

    map["species"] = species;
    map["algorithm"] = $("input[name=score]:checked").val();
    map["scoringMode"] = $("input[name=mode]:checked").val();
    map["scoreFilter"] = document.getElementById("filter-score").value;
    map["minMarkers"] = document.getElementById("filter-markers").value;
    map["maxResults"] = document.getElementById("filter-size").value;
    if (species === "Homo sapiens") map["includeAmelogenin"] = document.getElementById("check-include-Amelogenin").checked;

    return map;
}

function launchAlert(response, status, xhr) {
    console.log(response);
    console.log(status);
    console.log(xhr);

    let alertText = status.toUpperCase() + ' ' + response.status + ' - ' + response.statusText + '\n';
    if (response.status !== 404) {
        let split = response.responseText.split('\n', 2);
        alertText += '\n' + split[0] + '\n' + split[1] + '\n\n';
    }
    alertText += 'Please inform an administrator';
    alert(alertText);
}

function search() {
    let jsonQuery = {};

    let defaultMarkers = markers[species]["default"];
    for (let i = 0; i < defaultMarkers.length; i++){
        let v = document.getElementById("input-" + defaultMarkers[i]).value.split(" ").join("");
        if (v !== '') {
            jsonQuery[defaultMarkers[i].split("_").join(" ")] = v;
        }
    }
    let optionalMarkers = markers[species]["optional"];
    for (let i = 0; i < optionalMarkers.length; i++) {
        let v = document.getElementById("input-" + optionalMarkers[i]).value.split(" ").join("");
        if (v !== '') {
            jsonQuery[optionalMarkers[i].split("_").join(" ")] = v;
        }
    }
    if (Object.keys(jsonQuery).length === 0) {
        document.getElementById("results").style.display = "none";
        document.getElementById("warning").style.display = "block";
        $("#warning").animate({opacity: 1}, 400, "swing");
        document.getElementById("warning").scrollIntoView({behavior: 'smooth', block: "end", inline: "nearest"});
        return;
    }
    jsonQuery["outputFormat"] = "json";
    jQuery.extend(jsonQuery, jsonParameters());
    html.addClass("waiting");
    $.ajax({
        type: "POST",
        url: "/cellosaurus-str-search/api/query",
        data: JSON.stringify(jsonQuery),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            if (response.results.length !== 0) {
                jsonResponse = response;
                table.build(response);

                let results = $("#results");
                if (results.width() > $("#content").width()) {
                    $("#sib_header_small,#sib_footer").width(results.width())
                } else {
                    $("#sib_header_small,#sib_footer").width("100%");
                }
                document.getElementById("caption-count").innerText = response.searchSpace;
                document.getElementById("caption-species").innerText = speciesNames[response.parameters.species];
                document.getElementById("caption-release").innerText = response.cellosaurusRelease;

                document.getElementById("results").style.display = "table";
                results.animate({opacity: 1}, 1000, "swing");
                document.getElementById("warning").style.display = "none";
                document.getElementById('caption').scrollIntoView({behavior: "smooth", block: "start", inline: "nearest"});
            } else {
                document.getElementById("results").style.display = "none";
                document.getElementById("warning").style.display = "block";
                $("#warning").animate({opacity: 1}, 400, "swing");
                document.getElementById("warning").scrollIntoView({behavior: "smooth", block: "end", inline: "nearest"});
            }
        },
        error: function (response, status, xhr) {
            launchAlert(response, status, xhr);
        },
        complete: function () {
            html.removeClass("waiting");
        }
    });
}

let table = {
    build: function (json) {
        let currentMarkers = [...markers[species]["default"]];

        let optionalMarkers = markers[species]["optional"];
        if (optionalMarkers.length > 0) {
            for (let i = 0; i < optionalMarkers.length; i++) {
                if (document.getElementById("check-" + optionalMarkers[i]).checked === true) {
                    currentMarkers.push(optionalMarkers[i]);
                }
            }
            currentMarkers.sort(markerComparator);
        }
        let tr = "";
        let htmlContent = "<tr><th class='unselectable b0'><p class=\"sort-by\">Accession</p></th><th class='unselectable'><p class=\"sort-by\">Name</p></th><th class='unselectable'><p class=\"sort-by\">Nº Markers</p></th></th><th class='unselectable'><p class=\"sort-by\">Score</p></th>";
        for (let i = 0; i < currentMarkers.length; i++) {
            let a = currentMarkers[i].split("_").join(" ");
            if (a.startsWith("Mouse")) {
                a = a.substring(6);
            } else if (a.startsWith("Dog")) {
                a = a.substring(4);
            } else if (a === 'Amelogenin') {
                a = 'Amel';
            }
            htmlContent += "<th class='unselectable'><p class=\"sort-by\">" + a + "</p></th>";
            tr += "<td>" + document.getElementById("input-" + currentMarkers[i]).value + "</td>";
        }
        htmlContent += "</tr><tr><td class='b0'>NA</td><td>Query</td><td>NA</td><td>NA</td></td>" + tr + "</tr>";

        for (let i = 0; i < json.results.length; i++) {
            let map = {};
            for (let a in json.results[i].profiles) {
                for (let b in json.results[i].profiles[a].markers) {
                    let m = json.results[i].profiles[a].markers[b];
                    let t = [];
                    for (let c in json.results[i].profiles[a].markers[b].alleles) {
                        let v = json.results[i].profiles[a].markers[b].alleles[c];
                        if (m.searched === true) {
                            if (v.matched === true) {
                                t.push(v.value);
                            } else {
                                t.push("<span style='color:red'>" + v.value + "</span>");
                            }
                        } else {
                            t.push(v.value);
                        }
                    }
                    let key = json.results[i].profiles[a].markers[b].name.split(" ").join("_");
                    if (json.parameters.species === "Mus musculus") {
                        key = "Mouse_" + key;
                    } else if (json.parameters.species === "Canis lupus familiaris") {
                        key = "Dog_" + key;
                    }
                    if (currentMarkers.indexOf(key) > -1) {
                        let s = m.searched === true ? t.join(","): "<span style='color:gray'>" + t.join(",") + "</span>";
                        if (json.results[i].profiles[a].markers[b].conflicted) {
                            map[key] = "<a class=\"as\" title=\"" + table._formatSources(json.results[i].profiles[a].markers[b].sources) + "\">" + s + "</a>";
                        } else {
                            map[key] = s;
                        }
                    }
                }
                htmlContent += "<tr>";

                let cls;
                if ($("input[name=score]:checked").val() === "1") {
                    if (json.results[i].profiles[a].score >= 90.0) {
                        cls = "b1";
                    } else if (json.results[i].profiles[a].score < 80.0) {
                        cls = "b2";
                    } else {
                        cls = "b3";
                    }
                } else {
                    if (json.results[i].profiles[a].score >= 80.0) {
                        cls = "b1";
                    } else if (json.results[i].profiles[a].score < 60.0) {
                        cls = "b2";
                    } else {
                        cls = "b3";
                    }
                }
                let ver;
                if (a == 0 && json.results[i].profiles.length === 2) {
                    ver = " <span style='color:#373434'><i>Best</i></span>";
                } else if (a == 1) {
                    ver = " <span style='color:#373434'><i>Worst</i></span>";
                } else {
                    ver = "";
                }

                if (json.results[i].problematic) {
                    htmlContent += "<td class='" + cls + "'><a title=\"" + table._formatDescription(json.results[i].problem) + "\" style='color:red' href=\"https://web.expasy.org/cellosaurus/" + json.results[i].accession + "\" target=\"_blank\">" + json.results[i].accession + "</a>" + ver + "</td>"
                } else {
                    htmlContent += "<td class='" + cls + "'><a href=\"https://web.expasy.org/cellosaurus/" + json.results[i].accession + "\" target=\"_blank\">" + json.results[i].accession + "</a>" + ver + "</td>"
                }

                htmlContent += "<td>" + json.results[i].name + "</td>";
                if ((document.getElementById("check-include-Amelogenin").checked && json.results[i].profiles[a].markerNumber < 9) || (!document.getElementById("check-include-Amelogenin").checked && json.results[i].profiles[a].markerNumber < 8)) {
                    htmlContent += "<td><span style='color:red' title='A minimum of eight STR markers (excluding Amelogenin) is recommended'>" + json.results[i].profiles[a].markerNumber + "</span></td>";
                } else {
                    htmlContent += "<td>" + json.results[i].profiles[a].markerNumber + "</td>";
                }
                htmlContent += "<td>" + json.results[i].profiles[a].score.toFixed(2) + "%</td>";

                for (let j = 0; j < currentMarkers.length; j++){
                    let v = map[currentMarkers[j]];
                    if (v === undefined) v = "";
                    htmlContent += "<td>" + v + "</td>";
                }
                htmlContent += "</tr>";
            }
        }
        let tableResults = $("#table-results");
        tableResults.empty();
        tableResults.append(htmlContent);
        table._makeSortable();
    },
    _formatDescription: function (description) {
        let dom = "<b>Problematic cell line:</b><br><span style='color:red;'>";

        dom += description
            .replace(".","</span>.")
            .replace(/(PubMed=)(\d+)/g, "$1<a class='ab' href='https://www.ncbi.nlm.nih.gov/pubmed/$2' target='_blank'>$2</a>")
            .replace(/(DOI=)([\w/.]+)/g, "$1<a class='ab' href='https://www.doi.org/$2' target='_blank'>$2</a>");

        return dom;
    },
    _formatSources: function (sources) {
        let dom = "<b>Source";
        if (sources.length > 1) {
            dom += "s";
        }
        dom += ":</b><br>";

        for (let i = 0; i < sources.length ; i++) {
            if (sources[i].startsWith("PubMed")) {
                let underscore = sources[i].indexOf("_");
                let end = underscore === -1 ? sources[i].length : underscore;
                dom += sources[i].substring(0, 7);
                dom += "<a class='ab' href='https://www.ncbi.nlm.nih.gov/pubmed/";
                dom += sources[i].substring(7, end);
                dom += "' target='_blank'>";
                dom += sources[i].substring(7, end);
                dom += "</a>";
                if (underscore !== -1) dom += sources[i].substring(underscore);
                dom += "<br>";
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
    },
    // Adapted from Nick Grealy
    // https://stackoverflow.com/questions/14267781/sorting-html-table-with-javascript/49041392#49041392
    _makeSortable: function () {
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
            let ths = document.querySelectorAll('th');
            for (let i = 0; i < ths.length; i++) {
                ths[i].getElementsByClassName('sort-by')[0].className = "sort-by";
            }
            if (this.asc) {
                th.getElementsByClassName('sort-by')[0].className = "sort-by asc";
            } else {
                th.getElementsByClassName('sort-by')[0].className = "sort-by des";
            }
            const table = th.closest('table');
            Array.from(table.querySelectorAll('tr:nth-child(n+3)'))
                .sort(comparer(Array.from(th.parentNode.children).indexOf(th), this.asc = !this.asc))
                .forEach(tr => table.appendChild(tr) );
        })));
    }
};

function check(i) {
    if (document.getElementById("input-" + i).disabled) {
        document.getElementById("input-" + i).disabled = false;
        document.getElementById("label-" + i).style.color = "#107dac";
    } else {
        document.getElementById("input-" + i).disabled = true;
        document.getElementById("input-" + i).value = "";
        document.getElementById("label-" + i).style.color = "#7e7e7e";
    }
}

let importFile = {
    read: function (files) {
        if (files[0] === undefined) return;

        switch (files[0].name.split(".").pop()) {
            case undefined:
                break;
            case "csv":
            case "tsv":
            case "txt":
                importFile._csv(files[0]);
                break;
            case "xls":
                importFile._xls(files[0]);
                break;
            case "xlsx":
                importFile._xlsx(files[0]);
                break;
        }
    },
    load: function (value) {
        for (let i = 0; i < jsonInput.length; i++) {
            if (jsonInput[i].description === value) {
                resetAllMarkers();
                document.getElementById("description").value = value;

                for (let property in jsonInput[i]) {
                    if (jsonInput[i].hasOwnProperty(property)) {
                        let name = importFile._format(property);

                        if (markers[species]["default"].includes(name)) {
                            let e = document.getElementById("input-" + name);
                            e.value = jsonInput[i][property].split(" ").join("");
                            validateElement(e)
                        }
                        if (markers[species]["optional"].includes(name)) {
                            document.getElementById("check-" + markers[species]["optional"][i]).checked = true;
                            document.getElementById("label-" + i).style.color = "#107dac";
                            let e = document.getElementById("input-" + i);
                            e.disabled = false;
                            e.value = jsonInput[i][property].split(" ").join("");
                            validateElement(e)
                        }
                    }
                }
                document.getElementById("sample-" + speciesNames[species]).innerHTML = "Sample <span class='text-purple'>" + value + "</span> loaded";
                $("#sample-" + speciesNames[species]).show("slide", 400);
                dialogImport.dialog("close");
                break;
            }
        }
    },
    reload: function(json) {
        for (let i = 0; i < json.length; i++) {
            json[i] = $.extend(json[i], jsonParameters());
            json[i]["outputFormat"] = $("#import-extension").val()
        }
        return json;
    },
    _callback: function (results) {
        if (results.length === 0) {
            document.getElementById("samples").innerHTML = "<p style='color:red;'><b>Error:</b><br>The input file is empty.</p>";
            jsonInput = {};
            $("#batch").button().attr('disabled', true).addClass('ui-state-disabled');
            return;
        }
        if (results[0].Marker) {
            jsonInput = importFile._genemapper(results);
        } else {
            jsonInput = results;
        }
        jsonInput = importFile._clean(jsonInput);

        let status = importFile._validate(jsonInput);
        if (status > 0) {
            if (jsonInput.length === 1) {
                importFile.load(jsonInput[0].description);
            } else {
                document.getElementById("import-help").innerHTML =  "<b>" + jsonInput.length + " samples detected:</b>";
                let samples = "<i>Click on a sample to load its values in the form or use the <b>Batch Query</b> option to search them all</i><br><br>";
                for (let i = 0; i < jsonInput.length; i++) {
                    samples += "<a class='sample' onclick='importFile.load(this.innerText)'>" + jsonInput[i].description + "</a><br>"
                }
                samples += "<br>";
                document.getElementById("samples").innerHTML = samples;
                $("#batch").button().attr('disabled', false).removeClass('ui-state-disabled');
            }
        } else if (status === -2) {
            document.getElementById("import-help").innerHTML = "<b style='color:red;'>Error:</b>";
            document.getElementById("samples").innerHTML = "<span style='color:red;'>No sample could be detected in the input file. Please check that your file contains a \"Name\", \"Sample\" or \"Sample Name\" column.</span>";
            jsonInput = {};
            $("#batch").button().attr('disabled', true).addClass('ui-state-disabled') ;
        } else if (status === -1) {
            document.getElementById("import-help").innerHTML = "<b style='color:red;'>Error:</b>";
            document.getElementById("samples").innerHTML = "<span style='color:red;'>Not all samples are named in the input file. Please name all your samples.</span>";
            jsonInput = {};
            $("#batch").button().attr('disabled', true).addClass('ui-state-disabled') ;
        } else if (status === 0) {
            document.getElementById("import-help").innerHTML = "<b style='color:red;'>Error:</b>";
            document.getElementById("samples").innerHTML = "<span style='color:red;'>No compatible marker was detected in the input file. Please check that you have selected the correct species.</span>";
            jsonInput = {};
            $("#batch").button().attr('disabled', true).addClass('ui-state-disabled') ;
        }
    },
    _genemapper: function (results) {
        let array = [];
        let object = {};

        let sample = results[0]["Sample Name"];
        for (let i = 0; i < results.length; i++) {
            if (results[i]["Sample Name"] !== sample) {
                object.description = sample;
                array.push(object);
                object = {};
                sample = results[i]["Sample Name"];
            }
            let alleles = [];
            let j = 1;
            while(results[i]["Allele " + j]) {
                if (results[i]["Allele " + j] !== "") {
                    alleles.push(results[i]["Allele " + j])
                }
                j++;
            }
            object[results[i].Marker] = alleles.join(",");
        }
        object.description = sample;
        array.push(object);

        return array;
    },
    _format: function (key) {
        let name = key.trim().toUpperCase().replace(/  +/g, "_") ;

        switch (name) {
            case "AM":
            case "AMEL":
            case "AMELOGENIN":
                return "Amelogenin";
            case "CSF1P0":
                return "CSF1PO";
            case "F13A1":
                return "F13A01";
            case "FES/FPS":
                return "FESFPS";
            case "PENTA_C":
            case "PENTAC":
            case "PENTA_D":
            case "PENTAD":
            case "PENTA_E":
            case "PENTAE":
                return "Penta_" + name[name.length - 1];
            case "THO1":
                return "TH01";
            case "VWA":
                return "vWA";
            default:
                let names = name.split(/[ _]+/);
                if (species === "Mus musculus") {
                    return "Mouse_STR_" + names[names.length - 1];
                } else if (species === "Canis lupus familiaris") {
                    return "Dog_" + names[names.length - 1];
                }
                return name;
        }
    },
    _clean: function (json) {
        let jsonClean = [];
        let names = [];

        for (let i = 0; i < json.length; i++) {
            if (json[i]["SampleReferenceNbr"] !== undefined) {
                json[i].description = importFile._renameCheck(names, json[i]["SampleReferenceNbr"]);
                delete json[i]["SampleReferenceNbr"];
            } else if (json[i]["Sample"] !== undefined) {
                json[i].description = importFile._renameCheck(names, json[i]["Name"]);
                delete json[i]["Sample"];
            } else if (json[i]["Sample Name"] !== undefined) {
                json[i].description = importFile._renameCheck(names, json[i]["Sample Name"]);
                delete json[i]["Sample Name"];
            } else if (json[i]["Name"] !== undefined) {
                json[i].description = importFile._renameCheck(names, json[i]["Name"]);
                delete json[i]["Name"];
            }
            let c = 0;
            for (let key in json[i]) {
                if (json[i].hasOwnProperty(key) && json[i][key] !== null && json[i][key] !== "") c++;
            }
            console.log(json[i]);
            console.log(c);
            if (c > 1) jsonClean.push(json[i])
        }
        return jsonClean;
    },
    _renameCheck: function (names, value) {
        if (!names.includes(value)) {
            names.push(value);
            return value;
        } else {
            let i = 1;
            while (names.includes(value + '(' + i + ')')) {
                i++;
            }
            names.push(value + '(' + i + ')');
            return value + '(' + i + ')';
        }
    },
    _validate: function(json) {
        if (json.some(e => !e.hasOwnProperty("description"))) return -2;
        if (json.some(e => e["description"].length === 0)) return -1;

        let c = 0;
        for (let i = 0; i < json.length; i++) {
            for (let property in json[i]) {
                if (json[i].hasOwnProperty(property)) {
                    let name = this._format(property);
                    if (name !== property) {
                        json[name] = property;
                        delete json[property]
                    }
                    if (markers[species]["default"].includes(name) || markers[species]["optional"].includes(name)) c++;
                }
            }
        }
        return c;
    },
    _csv: function (file) {
        Papa.parse(file, {
            header: true,
            skipEmptyLines: true,
            complete: function (results) {
                importFile._callback(results.data);
            }
        });
    },
    _xls: function (file) {
        let reader = new FileReader();
        reader.onload = event => {
            const bstr = event.target.result;
            const wb = XLS.read(bstr, { type: "binary" });
            const wsname = wb.SheetNames[0];
            const ws = wb.Sheets[wsname];
            const results =  XLS.utils.sheet_to_json(ws, { raw: false, defval: "" });
            importFile._callback(results);
        };
        reader.readAsBinaryString(file);
    },
    _xlsx: function (file) {
        let reader = new FileReader();
        reader.onload = event => {
            const bstr = event.target.result;
            const wb = XLSX.read(bstr, { type: "binary" });
            const wsname = wb.SheetNames[0];
            const ws = wb.Sheets[wsname];
            const results = XLSX.utils.sheet_to_json(ws, { raw: false, defval: ""});
            importFile._callback(results);
        };
        reader.readAsBinaryString(file);
    },
    openDialog: function () {
        dialogImport.dialog("open");
    }
};

let exportTable = {
    toXlsx: function (name) {
        jsonResponse["outputFormat"] = "xlsx";
        $.ajax({
            type: "POST",
            url: "/cellosaurus-str-search/api/conversion",
            data: JSON.stringify(jsonResponse),
            contentType: "application/json",
            dataType: 'text',
            mimeType: 'text/plain; charset=x-user-defined',
            success: function (response, status, xhr) {
                let filename = name + ".xlsx";

                let newContent = "";
                for (let i = 0; i < response.length; i++) {
                    newContent += String.fromCharCode(response.charCodeAt(i) & 0xFF);
                }
                let bytes = new Uint8Array(newContent.length);
                for (let i = 0; i < newContent.length; i++) {
                    bytes[i] = newContent.charCodeAt(i);
                }
                let type = xhr.getResponseHeader('Content-Type');
                let blob = new Blob([bytes], { type: type });

                let URL = window.URL || window.webkitURL;
                let downloadUrl = URL.createObjectURL(blob);

                if (filename) {
                    let a = document.createElement("a");
                    if (typeof a.download === 'undefined') {
                        window.location = downloadUrl;
                    } else {
                        a.href = downloadUrl;
                        a.download = filename;
                        document.body.appendChild(a);
                        a.click();
                    }
                } else {
                    window.location = downloadUrl;
                }
                setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100);
            },
            error: function (response, status, xhr) {
                launchAlert(response, status, xhr);
            },
        });
    },
    toCsv: function (name) {
        jsonResponse["outputFormat"] = "csv";
        $.ajax({
            type: "POST",
            url: "/cellosaurus-str-search/api/conversion",
            data: JSON.stringify(jsonResponse),
            contentType: "application/json",
            dataType: 'text',
            mimeType: 'text/csv',
            success: function (response, status, xhr) {
                let blob = new Blob([response], {type: "text/csv"});

                if (navigator.msSaveOrOpenBlob) {
                    navigator.msSaveOrOpenBlob(blob, name + ".csv");
                } else {
                    downloadAnchor(URL.createObjectURL(blob), name + ".csv");
                }
            },
            error: function (response, status, xhr) {
                launchAlert(response, status, xhr);
            },
        });
    },
    toJson: function (name) {
        delete jsonResponse["outputFormat"];

        let blob = new Blob([JSON.stringify(jsonResponse, undefined, 2)], {type: "text/plain"});

        if (navigator.msSaveOrOpenBlob) {
            navigator.msSaveOrOpenBlob(blob, name + ".json");
        } else {
            downloadAnchor(URL.createObjectURL(blob), name + ".json");
        }
    },
    openDialog: function () {
        dialogExport.dialog("open");
    }
};

function downloadAnchor(content, name) {
    let anchor = document.createElement("a");
    anchor.style = "display:none !important";
    anchor.id = "downloadanchor";
    document.body.appendChild(anchor);

    if ("download" in anchor) {
        anchor.download = name;
    }
    anchor.href = content;
    anchor.click();
    anchor.remove();
}

$(function () {
    dialogImport = $("#dialog-import").dialog({
        autoOpen: false,
        height: 575,
        width: 500,
        modal: false,
        resizable: false,
        closeText: null,
        hide: {effect: "fold", duration: 300},
        show: {effect: "fold", duration: 300},
        buttons: [{
                text: "Batch Query",
                disabled: true,
                id: "batch",
                click: function() {
                    html.addClass("waiting");
                    $("#import-progressbar").addClass("animate");
                    $.ajax({
                        type: "POST",
                        url: "/cellosaurus-str-search/api/batch",
                        data: JSON.stringify(importFile.reload(jsonInput)),
                        contentType: "application/json",
                        dataType: 'text',
                        mimeType: 'text/plain; charset=x-user-defined',
                        success: function (response, status, xhr) {
                            let importExtension = $("#import-extension").val();
                            let extension = importExtension === "csv" ? "zip": importExtension;
                            let filename = $("#import-name").val() + "." + extension;

                            let newContent = "";
                            for (let i = 0; i < response.length; i++) {
                                newContent += String.fromCharCode(response.charCodeAt(i) & 0xFF);
                            }
                            let bytes = new Uint8Array(newContent.length);
                            for (let i = 0; i < newContent.length; i++) {
                                bytes[i] = newContent.charCodeAt(i);
                            }
                            let type = xhr.getResponseHeader('Content-Type');
                            let blob = new Blob([bytes], { type: type });

                            let URL = window.URL || window.webkitURL;
                            let downloadUrl = URL.createObjectURL(blob);

                            if (filename) {
                                let a = document.createElement("a");
                                if (typeof a.download === 'undefined') {
                                    window.location = downloadUrl;
                                } else {
                                    a.href = downloadUrl;
                                    a.download = filename;
                                    document.body.appendChild(a);
                                    a.click();
                                }
                            } else {
                                window.location = downloadUrl;
                            }
                            setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100);
                        },
                        error: function (response, status, xhr) {
                            launchAlert(response, status, xhr);
                        },
                        complete: function () {
                            dialogImport.dialog("close");
                            html.removeClass("waiting");
                            $("#import-progressbar").removeClass("animate");
                        }
                    });
                }
            },
            {
                text: "Close",
                click: function() {
                    dialogImport.dialog("close");
                }
            },
        ],
    });
    dialogImport.find("form").on("submit", function (event) {
        event.preventDefault();
    });
    dialogExport = $("#dialog-export").dialog({
        autoOpen: false,
        height: 350,
        width: 450,
        modal: true,
        resizable: false,
        closeText: null,
        hide: {effect: "fold", duration: 300},
        show: {effect: "fold", duration: 300},
        buttons: {
            Save: function () {
                let exportName = $("#export-name").val();
                let exportProgressbar = $("#export-progressbar");

                html.addClass("waiting");
                exportProgressbar.addClass("animate");
                
                jsonResponse.description = $("#description").val();
                let val = document.getElementById("export-extension").value;
                switch (val) {
                    case "xlsx":
                        exportTable.toXlsx(exportName);
                        break;
                    case "csv":
                        exportTable.toCsv(exportName);
                        break;
                    case "json":
                        exportTable.toJson(exportName);
                        break;
                }
                dialogExport.dialog("close");
                exportProgressbar.removeClass("animate");
                html.removeClass("waiting");
            },
            Close: function () {
                dialogExport.dialog("close");
            }
        },
        open: function () {
            $("#content").addClass("blur");
        },
        close: function () {
            $("#content").removeClass("blur");
        }
    });
    dialogExport.find("form").on("submit", function (event ) {
        event.preventDefault();
    });
    $(document).tooltip({
        content: function () {
            return $(this).prop('title');
        },
        position: {
            my: "left center",
            at: "right+15 center"
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
