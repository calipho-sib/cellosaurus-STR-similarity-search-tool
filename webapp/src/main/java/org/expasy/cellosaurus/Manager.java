package org.expasy.cellosaurus;

import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.genomics.str.*;
import org.expasy.cellosaurus.math.scoring.Algorithm;
import org.expasy.cellosaurus.math.scoring.Mode;
import org.expasy.cellosaurus.math.scoring.ScoringAlgorithm;
import org.expasy.cellosaurus.math.scoring.ScoringMode;
import org.expasy.cellosaurus.wrappers.Parameters;
import org.expasy.cellosaurus.wrappers.Search;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager {
    public static Database database;
    public static List<CellLine> cellLines;

    public static Search search(MultivaluedMap<String, String> map) throws IllegalArgumentException {
        int algorithm = 0;
        int mode = 0;
        int scoreFilter = 60;
        int maxResults = 200;
        boolean includeAmelogenin = false;
        String description = "";

        Profile query = new Profile();

        for (String key : map.keySet()) {
            String name = formatKey(key);

            switch (name) {
                case "ALGORITHM":
                    algorithm = Integer.valueOf(map.getFirst(key))-1;
                    if (algorithm < 0 || algorithm > 2) {
                        throw new IllegalArgumentException(name + '=' + map.getFirst(key));
                    }
                    break;
                case "SCORINGMODE":
                    mode = Integer.valueOf(map.getFirst(key))-1;
                    if (mode < 0 || mode > 2) {
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
                    Marker marker = new Marker(name);
                    if (HumanMarkers.CORE_MARKERS.contains(marker) || HumanMarkers.MINOR_MARKERS.contains(marker)) {
                        if (!map.getFirst(key).isEmpty()) {
                            for (String allele : map.getFirst(key).split(",")) {
                                marker.getAlleles().add(new Allele(allele.trim().toUpperCase()));
                            }
                        }
                        query.getMarkers().add(marker);
                    }
                    break;
            }
        }
        Algorithm scoringAlgorithm = ScoringAlgorithm.get(algorithm);
        Mode scoringMode = ScoringMode.get(mode);

        List<CellLine> matches = new ArrayList<>();
        for (CellLine cellLine : cellLines) {
            CellLine copy = new CellLine(cellLine);

            for (Profile profile : copy.getProfiles()) {
                double score = scoringAlgorithm.computeScore(scoringMode, query, profile, includeAmelogenin);
                profile.setScore(score);
            }
            copy.reduceProfiles();

            if (copy.getBestScore() >= scoreFilter) {
                matches.add(copy);
            }
        }
        Collections.sort(matches);
        if (matches.size() >= maxResults) {
            matches = matches.subList(0, maxResults);
        }

        for (Marker marker : query.getMarkers()) {
            marker.setConflicted(null);   // remove from the json
            marker.setSources(null);      // remove from the json

            for (Allele allele : marker.getAlleles()) {
                allele.setMatched(null);  // remove from the json
            }
        }
        Parameters parameters = new Parameters(algorithm, mode, scoreFilter, maxResults, includeAmelogenin);
        Collections.sort(query.getMarkers());
        parameters.setMarkers(query.getMarkers());

        Search search = new Search(matches, database.getVersion(), description);
        search.setParameters(parameters);

        return search;
    }

    private static String formatKey(String key) {
        String name = key.trim().toUpperCase().replace(" ", "_");

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
            case "PENTA_D":
            case "PENTA_E":
                return "Penta_" + name.charAt(name.length() - 1);
            case "THO1":
                return "TH01";
            case "VWA":
                return "vWA";
            default:
                return name;
        }
    }
}
