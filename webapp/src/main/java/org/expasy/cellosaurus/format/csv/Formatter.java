package org.expasy.cellosaurus.format.csv;

import org.expasy.cellosaurus.Constants;
import org.expasy.cellosaurus.bio.CellLine;
import org.expasy.cellosaurus.bio.str.Allele;
import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;
import org.expasy.cellosaurus.wrappers.Search;

import java.util.Set;
import java.util.TreeSet;

public class Formatter {
    private Set<Marker> markers = new TreeSet<>();

    public Formatter() {
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
                "';STR-SST version: '" +
                search.getSoftwareVersion() +
                "';Algorithm: '" +
                search.getParameters().getAlgorithm() +
                "';Scoring Mode: '" +
                search.getParameters().getScoringMode() +
                "';Score Filter: '" +
                search.getParameters().getScoreFilter() +
                "';Max Results: '" +
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

            for (Haplotype haplotype : cellLine.getHaplotypes()) {
                sb.append('"');
                sb.append(cellLine.getAccession());
                if (cellLine.getHaplotypes().size() > 1) {
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
                sb.append(haplotype.getMarkerNumber());
                sb.append('"');
                sb.append(',');
                sb.append('"');
                sb.append(String.format("%.2f", haplotype.getScore()));
                sb.append('%');
                sb.append('"');
                sb.append(',');

                for (Marker marker : this.markers) {
                    sb.append('"');

                    int i = haplotype.getMarkers().indexOf(marker);
                    if (i > -1) {
                        for (Allele allele : haplotype.getMarkers().get(i).getAlleles()) {
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
