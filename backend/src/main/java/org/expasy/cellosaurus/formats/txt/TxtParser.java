package org.expasy.cellosaurus.formats.txt;

import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.utils.ConflictResolver;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parser for the TXT version of the Cellosaurus database.
 */
public class TxtParser {
    private Database database;

    private final List<CellLine> cellLines = new ArrayList<>();

    /**
     * Main constructor
     *
     * @param inputStream  the {@code InputStream} of the Cellosaurus TXT file
     * @throws IOException if the file cannot be read or closed
     */
    public TxtParser(InputStream inputStream) throws IOException {
        CellLine cellLine = new CellLine();
        ConflictResolver conflictResolver = new ConflictResolver();

        List<Marker> markers = new ArrayList<>();

        String previousMarker = "";
        String version = "";
        String updated = "";

        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            if (line.isEmpty() || !Character.isAlphabetic(line.charAt(0))) {
                if (line.startsWith(" Version:")) {
                    version = line.split(":\\s*")[1];
                } else if (line.startsWith(" Last update:")) {
                    updated = line.split(":\\s*")[1];
                }
                continue;
            }
            this.database = new Database(version, updated, 0, 0);

            String[] sline = line.trim().split("\\s{2,}");
            String[] xline = sline[1].split("(\\s*!\\s*)|(:\\s*)|(\\()|(\\))");

            String label = sline[0];
            switch (label) {
                case "ID":
                    cellLine = new CellLine();
                    conflictResolver = new ConflictResolver();

                    markers = new ArrayList<>();

                    previousMarker = "";

                    cellLine.setName(sline[1]);
                    break;
                case "AC":
                    cellLine.setAccession(sline[1]);
                    break;
                case "CC":
                    if (xline[0].equals("Problematic cell line")) {
                        cellLine.setProblematic(true);
                        cellLine.setProblem(xline[1]);
                    }
                    break;
                case "ST":
                    if (!xline[0].equals("Source")) {
                        Marker marker = new Marker(xline[0], xline[1].replace("Not_detected", "ND").split(","));

                        if (xline.length > 2) {
                            marker.setConflicted(true);
                            marker.getSources().addAll(Arrays.asList(xline[2].split(";\\s*")));
                        }
                        if (marker.getName().equals(previousMarker)) {
                            markers.add(marker);
                        } else {
                            if (!markers.isEmpty()) {
                                conflictResolver.getMarkersList().add(markers);
                                markers = new ArrayList<>();
                            }
                            markers.add(marker);
                        }
                        previousMarker = marker.getName();
                    }
                    break;
                case "OX":
                    cellLine.setSpecies(xline[1]);

                    if (!markers.isEmpty()) {
                        conflictResolver.getMarkersList().add(markers);
                    }
                    if (!conflictResolver.isEmpty()) {
                        cellLine.getProfiles().addAll(conflictResolver.resolve());
                        this.cellLines.add(cellLine);
                    }
                    break;
            }
        }
    }

    /**
     * Secondary constructor
     *
     * @param string       the TXT file path
     * @throws IOException if the file does not exist
     */
    public TxtParser(String string) throws IOException {
        this(new FileInputStream(new File(string)));
    }

    /**
     * Secondary constructor
     *
     * @param file         the TXT file
     * @throws IOException if the FileInputStream cannot be open
     */
    public TxtParser(File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Secondary constructor
     *
     * @param url          the url of the TXT file location
     * @throws IOException if the URL does not exist
     */
    public TxtParser(URL url) throws IOException {
        this(url.openConnection().getInputStream());
    }

    public Database getDatabase() {
        return database;
    }

    public List<CellLine> getCellLines() {
        return cellLines;
    }
}
