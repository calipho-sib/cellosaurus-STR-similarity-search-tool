package org.expasy.cellosaurus.formats.csv;

import org.expasy.cellosaurus.Constants;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Allele;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.wrappers.Search;

import java.util.Set;
import java.util.TreeSet;

public class CsvFormatter {
    private Set<Marker> markers = new TreeSet<>();

    public CsvFormatter() {
        for (String marker : Constants.CORE_MARKERS) {
            this.markers.add(new Marker(marker));
        }
    }

    private String metadata(Search search) {
        return "\"#" +
                "Description: '" +
                search.getDescription() +
                "';Data set: 'Cellosaurus release " +
                search.getCellosaurusRelease() +
                "';Run on: '" +
                search.getRunOn() +
                "';Tool version: '" +
                search.getToolVersion() +
                "';Algorithm: '" +
                search.getParameters().getAlgorithm() +
                "';Scoring mode: '" +
                search.getParameters().getScoringMode() +
                "';Score filter: '" +
                search.getParameters().getScoreFilter() +
                "';Max results: '" +
                search.getParameters().getMaxResults() +
                "';Include Amelogenin: '" +
                search.getParameters().isIncludeAmelogenin() +
                "'\"";
    }

    public String toCsv(Search search) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"Accession\",\"Name\",\"Nº Markers\",\"Score\",");

        for (Marker marker : search.getParameters().getMarkers()) {
            if (!marker.getName().equalsIgnoreCase("amelogenin") && !marker.getName().equalsIgnoreCase("amel")) {
                this.markers.add(new Marker(marker.getName()));
            }
        }
        for (Marker marker : this.markers) {
            sb.append('"');
            if (marker.getName().equals("Amelogenin")) {
                sb.append("Amel");
            } else {
                sb.append(marker.getName().replace("_", " "));
            }
            sb.append('"');
            sb.append(',');
        }
        sb.append(metadata(search));
        sb.append("\r\n");
        sb.append("\"NA\",\"Query\",\"NA\",\"NA\",");

        for (Marker marker : this.markers) {
            sb.append('"');

            int i = search.getParameters().getMarkers().indexOf(marker);
            if (i > -1) {
                for (Allele allele : search.getParameters().getMarkers().get(i).getAlleles()) {
                    sb.append(allele.toString());
                    sb.append(',');
                }
                sb.setLength(sb.length() - 1);
            }
            sb.append('"');
            sb.append(',');

        }
        sb.setLength(sb.length() - 1);
        sb.append("\r\n");

        for (CellLine cellLine : search.getResults()) {
            boolean best = true;

            for (Profile profile : cellLine.getProfiles()) {
                sb.append('"');
                sb.append(cellLine.getAccession());
                if (cellLine.getProfiles().size() > 1) {
                    if (best) {
                        sb.append(" Best");
                        best = false;
                    } else {
                        sb.append(" Worst");
                    }
                }
                if (cellLine.isProblematic()) {
                    sb.append(" (Problematic cell line)");
                }
                sb.append('"');
                sb.append(',');
                sb.append('"');
                sb.append(cellLine.getName().replace("_", " "));
                sb.append('"');
                sb.append(',');
                sb.append('"');
                sb.append(profile.getMarkerNumber());
                sb.append('"');
                sb.append(',');
                sb.append('"');
                sb.append(String.format("%.2f", profile.getScore()));
                sb.append('%');
                sb.append('"');
                sb.append(',');

                for (Marker marker : this.markers) {
                    sb.append('"');

                    int i = profile.getMarkers().indexOf(marker);
                    if (i > -1) {
                        for (Allele allele : profile.getMarkers().get(i).getAlleles()) {
                            sb.append(allele.toString());
                            sb.append(',');
                        }
                        sb.setLength(sb.length() - 1);
                    }
                    sb.append('"');
                    sb.append(',');
                }
                sb.setLength(sb.length() - 1);
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }
}
