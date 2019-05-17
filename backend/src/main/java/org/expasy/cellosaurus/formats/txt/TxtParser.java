package org.expasy.cellosaurus.formats.txt;

import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.utils.ConflictResolver;

import java.io.*;
import java.util.*;

/**
 * Parser for the TXT version of the Cellosaurus database.
 */
public class TxtParser implements Parser {
    private Database database;

    private final List<CellLine> cellLines = new ArrayList<>();
    private final Set<Set<String>> sameOrigins = new HashSet<>();
    private final Map<String, List<String>> hierarchy = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse(InputStream inputStream) throws IOException {
        List<Marker> markers = new ArrayList<>();
        Set<String> origin = new HashSet<>();

        ConflictResolver conflictResolver = new ConflictResolver();

        String version = "";
        String updated = "";

        String accession = "";
        String name = "";
        String species = "";
        boolean problematic = false;
        String problem = "";
        String stability = "";

        String previousMarker = "";

        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            if (line.isEmpty() || line.charAt(0) == ' ' || line.charAt(0) == '-'  || line.charAt(0) == '_') {
                if (line.startsWith(" Version:")) {
                    version = line.split(":\\s*")[1];
                } else if (line.startsWith(" Last update:")) {
                    updated = line.split(":\\s*")[1];
                }
                continue;
            }
            this.database = new Database(version, updated, 0, 0);

            String[] sline = line.trim().split("\\s{2,}");
            String[] xline = sline.length == 1 ? new String[]{} : sline[1].split("(\\s*!\\s*)|(:\\s*)|(\\()|(\\))");

            String label = sline[0];
            switch (label) {
                case "ID":
                    name = sline[1];
                    break;
                case "AC":
                    accession = sline[1];
                    break;
                case "CC":
                    if (xline[0].equals("Problematic cell line")) {
                        problematic = true;
                        problem = xline[1];
                    } else if (xline[0].equals("Microsatellite instability")) {
                        stability = xline[1];
                    }
                    break;
                case "ST":
                    if (!xline[0].equals("Source")) {
                        String[] alleles = xline[1].replace("Not_detected", "ND").split(",");
                        Marker marker = new Marker(xline[0], alleles);

                        if (xline.length > 2) {
                            marker.setConflicted(true);
                            marker.addSources(Arrays.asList(xline[2].split(";\\s*")));
                        }
                        if (marker.getName().equals(previousMarker)) {
                            markers.add(marker);
                        } else {
                            if (!markers.isEmpty()) {
                                conflictResolver.addMarkers(markers);
                                markers = new ArrayList<>();
                            }
                            markers.add(marker);
                        }
                        previousMarker = marker.getName();
                    }
                    break;
                case "HI":
                    if (!this.hierarchy.containsKey(xline[1])) {
                        this.hierarchy.put(xline[1], new ArrayList<>());
                    }
                    this.hierarchy.get(xline[1]).add(accession);
                    break;
                case "OI":
                    origin.add(accession);
                    origin.add(xline[0]);
                    break;
                case "SX":
                    if (!origin.isEmpty()) {
                        this.sameOrigins.add(origin);
                        origin = new HashSet<>();
                    }
                    break;
                case "OX":
                    species = xline[1];
                    break;
                case "//":
                    if (!markers.isEmpty()) {
                        conflictResolver.addMarkers(markers);
                        markers = new ArrayList<>();
                    }
                    if (!conflictResolver.isEmpty()) {
                        CellLine cellLine = new CellLine(accession, name, species);
                        cellLine.setProblematic(problematic);
                        cellLine.setProblem(problem);
                        cellLine.setStability(stability);
                        cellLine.addProfiles(conflictResolver.resolve());
                        this.cellLines.add(cellLine);
                    }
                    conflictResolver = new ConflictResolver();
                    accession = name = species = problem = stability = previousMarker = "";
                    problematic = false;
                    break;
            }
        }
        br.close();
    }

    public Database getDatabase() {
        return database;
    }

    public List<CellLine> getCellLines() {
        return cellLines;
    }

    public Set<Set<String>> getSameOrigins() {
        return sameOrigins;
    }

    public Map<String, List<String>> getHierarchy() {
        return hierarchy;
    }
}
