package org.expasy.cellosaurus.formats.csv;

import org.expasy.cellosaurus.formats.FormatsUtils;
import org.expasy.cellosaurus.genomics.str.Allele;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.expasy.cellosaurus.wrappers.Search;

import java.util.List;

public class CsvFormatter {

    public String toCsv(Search search) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"Accession\",\"Name\",\"Nº Markers\",\"Score\",");

        List<Marker> headerMarkers = FormatsUtils.makeHeaderMarkers(search.getParameters());
        for (Marker marker : headerMarkers) {
            sb.append('"');
            if (marker.getName().equals("Amelogenin")) {
                sb.append("Amel");
            } else {
                sb.append(marker.getName().replace("_", " "));
            }
            sb.append('"');
            sb.append(',');
        }
        sb.append("\"");
        sb.append(FormatsUtils.makeMetadata(search));
        sb.append("\"\r\n");
        sb.append("\"NA\",\"Query\",\"NA\",\"NA\",");

        for (Marker marker : headerMarkers) {
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
                sb.append(cellLine.getName());
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

                for (Marker marker : headerMarkers) {
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
