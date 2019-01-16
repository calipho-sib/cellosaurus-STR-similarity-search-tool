package org.expasy.cellosaurus;

import com.google.gson.Gson;
import org.expasy.cellosaurus.bio.CellLine;
import org.expasy.cellosaurus.bio.str.Allele;
import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;
import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.math.Scoring;
import org.expasy.cellosaurus.wrappers.Parameters;
import org.expasy.cellosaurus.wrappers.Search;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager {
    static Database database;
    static List<CellLine> cellLines;

    public static String search(MultivaluedMap<String, String> map) {
        int iScoring = 1;
        int iScoreFilter = 40;
        int iSizeFilter = 200;

        Haplotype query = new Haplotype();
        for (String key : map.keySet()) {
            if (key.equalsIgnoreCase("scoring")) {
                iScoring = Integer.valueOf(map.getFirst(key));
            } else if (key.equalsIgnoreCase("filter")) {
                iScoreFilter = Integer.valueOf(map.getFirst(key));
            } else if (key.equalsIgnoreCase("size")) {
                iSizeFilter = Integer.valueOf(map.getFirst(key));
            } else {
                Marker marker = new Marker(key.trim());
                if (!map.getFirst(key).isEmpty()) {
                    for (String allele : map.getFirst(key).split(",")) {
                        marker.addAllele(allele.trim().toUpperCase());
                    }
                }
                query.addMarker(marker);
            }
        }
        Scoring scoring = new Scoring(query, iScoring);

        List<CellLine> matches = new ArrayList<>();
        for (CellLine cellLine : cellLines) {
            CellLine copy = new CellLine(cellLine);

            for (Haplotype haplotype : copy.getHaplotypes()) {
                scoring.computeScore(haplotype);
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
        Parameters parameters = new Parameters(iScoring, iScoreFilter, iSizeFilter);
        Collections.sort(query.getMarkers());
        parameters.setMarkers(query.getMarkers());

        Search search = new Search(database.getVersion(), matches);
        search.setParameters(parameters);

        Gson gson = new Gson();
        return gson.toJson(search);
    }
}
