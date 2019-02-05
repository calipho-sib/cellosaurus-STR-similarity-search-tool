package org.expasy.cellosaurus.format.csv;

import org.expasy.cellosaurus.bio.CellLine;
import org.expasy.cellosaurus.bio.str.Allele;
import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;
import org.expasy.cellosaurus.wrappers.Search;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Formatter {
    private Set<Marker> cores = new TreeSet<>();

    public Formatter() {
        this.cores.add(new Marker("Amelogenin"));
        this.cores.add(new Marker("CSF1PO"));
        this.cores.add(new Marker("D2S1338"));
        this.cores.add(new Marker("D3S1358"));
        this.cores.add(new Marker("D5S818"));
        this.cores.add(new Marker("D7S820"));
        this.cores.add(new Marker("D8S1179"));
        this.cores.add(new Marker("D13S317"));
        this.cores.add(new Marker("D16S539"));
        this.cores.add(new Marker("D18S51"));
        this.cores.add(new Marker("D19S433"));
        this.cores.add(new Marker("D21S11"));
        this.cores.add(new Marker("FGA"));
        this.cores.add(new Marker("Penta_D"));
        this.cores.add(new Marker("Penta_E"));
        this.cores.add(new Marker("TH01"));
        this.cores.add(new Marker("TPOX"));
        this.cores.add(new Marker("vWA"));
    }

    private String metadata(Search search) {
        return "\"#" +
                "Scoring: '" +
                search.getParameters().getScoring() +
                "';Mode: '" +
                search.getParameters().getMode() +
                "';Score Filter: '" +
                search.getParameters().getScoreFilter() +
                "';Size Filter: '" +
                search.getParameters().getSizeFilter() +
                "';Include Amelogenin: '" +
                search.getParameters().isIncludeAmelogenin() +
                "';Description: '" +
                search.getDescription() +
                "';Data set: 'Cellosaurus release " +
                search.getCellosaurusRelease() +
                "';Run on: '" +
                search.getRunOn() +
                "';STR-SST version: '" +
                search.getSoftwareVersion() +
                "'\"";
    }

    public String toCsv(Search search) {
        Map<String, String> map = new LinkedHashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("\"Accession\",\"Name\",\"Nº\",\"Score\",");
        for (Marker marker : search.getParameters().getMarkers()) {
            if (!marker.getName().equalsIgnoreCase("amelogenin") && !marker.getName().equalsIgnoreCase("amel")) {
                this.cores.add(new Marker(marker.getName()));
            }
        }
        for (Marker marker : this.cores) {
            sb.append('"');
            if (marker.getName().equals("Amelogenin")) {
                sb.append("Amel");
            } else {
                sb.append(marker.getName());
            }
            map.put(marker.getName(), "");
            sb.append('"');
            sb.append(',');
        }
        sb.append(metadata(search));
        sb.append("\r\n");
        sb.append("\"NA\",\"Query\",\"NA\",\"NA\",");

        for (Marker marker : search.getParameters().getMarkers()) {
            if (this.cores.contains(new Marker(marker.getName()))) {
                sb.append('"');

                for (Allele allele : marker.getAlleles()) {
                    sb.append(allele.toString());
                    sb.append(',');
                }
                sb.setLength(sb.length() - 1);
                sb.append('"');
                sb.append(',');
            }
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
                sb.append(haplotype.getNumber());
                sb.append('"');
                sb.append(',');
                sb.append('"');
                sb.append(String.format("%.2f", haplotype.getScore()));
                sb.append('%');
                sb.append('"');
                sb.append(',');

                for (Marker marker : haplotype.getMarkers()) {
                    StringBuilder alleles = new StringBuilder();

                    for (Allele allele : marker.getAlleles()) {
                        alleles.append(allele.toString());
                        alleles.append(',');
                    }
                    alleles.setLength(alleles.length() - 1);
                    map.put(marker.getName(), alleles.toString());
                }
                for (String key : map.keySet()) {
                    sb.append('"');
                    sb.append(map.get(key));
                    map.put(key, "");
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
