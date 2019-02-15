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
            String name = key.trim().replace(" ", "_");
            if (name.equalsIgnoreCase("amel")) name = "Amelogenin";

            if (name.equalsIgnoreCase("algorithm")) {
                algorithm = Integer.valueOf(map.getFirst(key));
                if (algorithm < 1 || algorithm > 3) {
                    throw new IllegalArgumentException(name + '=' + map.getFirst(key));
                }
            } else if (name.equalsIgnoreCase("scoringMode")) {
                scoringMode = Integer.valueOf(map.getFirst(key));
                if (scoringMode < 1 || scoringMode > 2) {
                    throw new IllegalArgumentException(name + '=' + map.getFirst(key));
                }
            } else if (name.equalsIgnoreCase("scoreFilter")) {
                scoreFilter = Integer.valueOf(map.getFirst(key));
            } else if (name.equalsIgnoreCase("maxResults")) {
                maxResults = Integer.valueOf(map.getFirst(key));
            } else if (name.equalsIgnoreCase("includeAmelogenin")) {
                includeAmelogenin = Boolean.valueOf(map.getFirst(key));
            } else if (name.equalsIgnoreCase("description")) {
                description = map.getFirst(key);
            } else if (Constants.CORE_MARKERS.contains(name) || Constants.MINOR_MARKERS.contains(name)) {
                Marker marker = new Marker(name);
                if (!map.getFirst(key).isEmpty()) {
                    for (String allele : map.getFirst(key).split(",")) {
                        marker.addAllele(allele.trim().toUpperCase());
                    }
                }
                query.addMarker(marker);
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

            if (copy.getScore() >= scoreFilter) {
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
}
