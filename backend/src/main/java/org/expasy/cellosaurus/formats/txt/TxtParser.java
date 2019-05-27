package org.expasy.cellosaurus.formats.txt;

import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Species;
import org.expasy.cellosaurus.genomics.str.utils.ConflictResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Parser for the TXT version of the Cellosaurus database.
 */
public class TxtParser implements Parser {
    private Database database;

    private final Map<String, Species> speciesMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse(InputStream inputStream) throws IOException {
        List<String> speciesNames = new ArrayList<>();
        List<Marker> markers = new ArrayList<>();
        Set<String> origins = new HashSet<>();

        ConflictResolver conflictResolver = new ConflictResolver();

        String version = "";
        String updated = "";

        String accession = "";
        String name = "";
        String parent = "";
        String problem = "";

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
            String[] sline = line.trim().split("\\s{2,}");
            String[] xline = sline.length == 1 ? new String[]{} : sline[1].split("(\\s*!\\s*)|(:\\s*)");

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
                        problem = xline[1];
                    }
                    break;
                case "ST":
                    if (!xline[0].equals("Source(s)")) {
                        String[] zline = xline[1].replace(" ", "").split("(\\()|(\\))");

                        String[] alleles = zline[0].replace("Not_detected", "ND").split(",");
                        Marker marker = new Marker(xline[0], alleles);

                        if (zline.length > 1) {
                            marker.setConflicted(true);
                            marker.addSources(Arrays.asList(zline[1].split(";\\s*")));
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
                    parent = xline[1];
                    break;
                case "OI":
                    origins.add(xline[0]);
                    break;
                case "OX":
                    speciesNames.add(xline[1]);
                    break;
                case "//":
                    // Add the last STR marker
                    if (!markers.isEmpty()) {
                        conflictResolver.addMarkers(markers);
                        markers = new ArrayList<>();
                    }
                    Collections.sort(speciesNames);
                    String speciesName = String.join("/", speciesNames);

                    // Manual fix for CVCL_1875 as it possesses human STR markers only
                    if (accession.equals("CVCL_1875")) speciesName = Species.Name.HUMAN.toString();
                    if (!speciesMap.containsKey(speciesName)) {
                        speciesMap.put(speciesName, new Species(speciesName));
                    }

                    if (!origins.isEmpty()) origins.add(accession);
                    Species species = speciesMap.get(speciesName);
                    species.addOrigins(origins);
                    species.addHierarchy(parent, accession);

                    if (!conflictResolver.isEmpty()) {
                        CellLine cellLine = new CellLine(accession, name, speciesName);
                        cellLine.setProblematic(!problem.isEmpty());
                        if (!problem.isEmpty()) cellLine.setProblem(problem);
                        cellLine.addProfiles(conflictResolver.resolve());
                        species.addCellLine(cellLine);
                    }
                    conflictResolver = new ConflictResolver();
                    accession = name = parent = problem = previousMarker = "";
                    speciesNames = new ArrayList<>();
                    origins = new HashSet<>();
                    break;
            }
        }
        br.close();
        this.database = new Database(version, updated, 0, 0);
        speciesMap.entrySet().removeIf(x -> x.getValue().isEmpty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Database getDatabase() {
        return database;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Species getSpecies(String name) {
        if (!this.speciesMap.containsKey(name)) return null;
        return speciesMap.get(name);
    }
}
