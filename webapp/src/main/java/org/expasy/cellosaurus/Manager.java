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
        int iScoring = 1;
        int iMode = 1;
        int iScoreFilter = 60;
        int iSizeFilter = 200;
        boolean iIncludeAmelogenin = false;
        String iDescription = "";

        Haplotype query = new Haplotype();
        for (String key : map.keySet()) {
            String name = key.trim().replace(" ", "_");
            if (name.equalsIgnoreCase("amel")) name = "Amelogenin";

            if (name.equalsIgnoreCase("scoring")) {
                iScoring = Integer.valueOf(map.getFirst(key));
                if (iScoring < 1 || iScoring > 3) {
                    throw new IllegalArgumentException(name + '=' + map.getFirst(key));
                }
            } else if (name.equalsIgnoreCase("mode")) {
                iMode = Integer.valueOf(map.getFirst(key));
                if (iMode < 1 || iMode > 2) {
                    throw new IllegalArgumentException(name + '=' + map.getFirst(key));
                }
            } else if (name.equalsIgnoreCase("filter")) {
                iScoreFilter = Integer.valueOf(map.getFirst(key));
            } else if (name.equalsIgnoreCase("size")) {
                iSizeFilter = Integer.valueOf(map.getFirst(key));
            } else if (name.equalsIgnoreCase("includeAmelogenin")) {
                iIncludeAmelogenin = Boolean.valueOf(map.getFirst(key));
            } else if (name.equalsIgnoreCase("description")) {
                iDescription = map.getFirst(key);
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
        Scoring scoring = new Scoring(iScoring, iMode, iIncludeAmelogenin);

        List<CellLine> matches = new ArrayList<>();
        for (CellLine cellLine : cellLines) {
            CellLine copy = new CellLine(cellLine);

            for (Haplotype haplotype : copy.getHaplotypes()) {
                scoring.computeScore(query, haplotype);
            }
            copy.initialize();

            if (copy.getScore() >= iScoreFilter) {
                matches.add(copy);
            }
        }
        Collections.sort(matches);
        if (matches.size() >= iSizeFilter) {
            matches = matches.subList(0, iSizeFilter);
        }

        for (Marker marker : query.getMarkers()) {
            marker.setConflicted(null);
            marker.setSources(null);

            for (Allele allele : marker.getAlleles()) {
                allele.setMatched(null);
            }
        }
        Parameters parameters = new Parameters(iScoring, iMode, iScoreFilter, iSizeFilter, iIncludeAmelogenin);
        Collections.sort(query.getMarkers());
        parameters.setMarkers(query.getMarkers());

        Search search = new Search(matches, database.getVersion(), iDescription);
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
