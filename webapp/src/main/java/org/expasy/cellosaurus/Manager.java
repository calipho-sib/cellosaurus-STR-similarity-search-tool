package org.expasy.cellosaurus;

import com.google.gson.Gson;
import org.expasy.cellosaurus.bio.CellLine;
import org.expasy.cellosaurus.bio.str.Allele;
import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;
import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.format.csv.Formatter;
import org.expasy.cellosaurus.math.Scoring;
import org.expasy.cellosaurus.wrappers.Parameters;
import org.expasy.cellosaurus.wrappers.Search;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager {
    public static Database database;
    public static List<CellLine> cellLines;

    public static String search(MultivaluedMap<String, String> map, String type) throws IllegalArgumentException {
        int algorithm = 1;
        int scoringMode = 1;
        int scoreFilter = 60;
        int maxResults = 200;
        boolean includeAmelogenin = false;
        String description = "";

        Haplotype query = new Haplotype();
        for (String key : map.keySet()) {
            String name = formatKey(key);

            switch (name) {
                case "ALGORITHM":
                    algorithm = Integer.valueOf(map.getFirst(key));
                    if (algorithm < 1 || algorithm > 3) {
                        throw new IllegalArgumentException(name + '=' + map.getFirst(key));
                    }
                    break;
                case "SCORINGMODE":
                    scoringMode = Integer.valueOf(map.getFirst(key));
                    if (scoringMode < 1 || scoringMode > 2) {
                        throw new IllegalArgumentException(name + '=' + map.getFirst(key));
                    }
                    break;
                case "SCOREFILTER":
                    scoreFilter = Integer.valueOf(map.getFirst(key));
                    break;
                case "MAXRESULTS":
                    maxResults = Integer.valueOf(map.getFirst(key));
                    break;
                case "INCLUDEAMELOGENIN":
                    includeAmelogenin = Boolean.valueOf(map.getFirst(key));
                    break;
                case "DESCRIPTION":
                    description = map.getFirst(key);
                    break;
                default:
                    if (Constants.CORE_MARKERS.contains(name) || Constants.MINOR_MARKERS.contains(name)) {
                        Marker marker = new Marker(name);
                        if (!map.getFirst(key).isEmpty()) {
                            for (String allele : map.getFirst(key).split(",")) {
                                marker.addAllele(allele.trim().toUpperCase());
                            }
                        }
                        query.addMarker(marker);
                    }
                    break;
            }
        }
        Scoring scoring = new Scoring(algorithm, scoringMode, includeAmelogenin);

        List<CellLine> matches = new ArrayList<>();
        for (CellLine cellLine : cellLines) {
            CellLine copy = new CellLine(cellLine);

            for (Haplotype haplotype : copy.getHaplotypes()) {
                scoring.computeScore(query, haplotype);
            }
            copy.initialize();

            if (copy.getBestScore() >= scoreFilter) {
                matches.add(copy);
            }
        }
        Collections.sort(matches);
        if (matches.size() >= maxResults) {
            matches = matches.subList(0, maxResults);
        }

        for (Marker marker : query.getMarkers()) {
            marker.setConflicted(null);
            marker.setSources(null);

            for (Allele allele : marker.getAlleles()) {
                allele.setMatched(null);
            }
        }
        Parameters parameters = new Parameters(algorithm, scoringMode, scoreFilter, maxResults, includeAmelogenin);
        Collections.sort(query.getMarkers());
        parameters.setMarkers(query.getMarkers());

        Search search = new Search(matches, database.getVersion(), description);
        search.setParameters(parameters);

        if (type.equals("application/json")) {
            Gson gson = new Gson();
            return gson.toJson(search);
        } else {
            Formatter formatter = new Formatter();
            return formatter.toCsv(search);
        }
    }

    private static String formatKey(String key) {
        String name = key.trim().toUpperCase().replace(" ", "_");

        switch (name) {
            case "AM":
            case "AMEL":
            case "AMELOGENIN":
                return "Amelogenin";
            case "THO1":
                return "TH01";
            case "CSF1P0":
                return "CSF1PO";
            case "VWA":
                return "vWA";
            case "PENTA_C":
            case "PENTA_D":
            case "PENTA_E":
                return "Penta_" + name.charAt(name.length()-1);
            default:
                return name;
        }
    }
}
