package org.expasy.cellosaurus.analyzes;

import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.formats.xml.XmlParser;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.expasy.cellosaurus.genomics.str.Species;
import org.expasy.cellosaurus.math.scoring.Algorithm;
import org.expasy.cellosaurus.math.scoring.Mode;
import org.expasy.cellosaurus.math.scoring.ScoringAlgorithm;
import org.expasy.cellosaurus.math.scoring.ScoringMode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CellosaurusAnalysis {
    private final static String URL = "ftp://ftp.expasy.org/databases/cellosaurus/cellosaurus.xml";

    private double scoreLimit = 90;
    private double markerLimit = 8;
    private String filename = "AllAgainstAll_Results.txt";

    public static void main(String[] args) {
        try {
            new CellosaurusAnalysis(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CellosaurusAnalysis() {
    }

    private CellosaurusAnalysis(String[] args) throws IOException {
        parseArgs(args);

        System.out.println("------------------------------------------------------------------------");
        System.out.println("Cellosaurus Analysis - All Against All");
        System.out.println("------------------------------------------------------------------------\n");
        System.out.println("Reading the Cellosaurus XML file... ");

        Parser parser = new XmlParser();
        parser.parse(new URL(URL));
        System.out.println("Done");

        Database cellosaurus = Database.CELLOSAURUS;
        System.out.println("Cellosaurus version " + cellosaurus.getVersion() + " (" + cellosaurus.getUpdated() +") loaded" );
        System.out.println("------------------------------------------------------------------------\n");

        System.out.println("Resolving related cell line groups... ");
        Species human = Species.HUMAN;
        Map<String, List<String>> hierarchy = human.getHierarchy();
        Set<Set<String>> sameOrigins = human.getSameOrigins();
        Map<String, Set<String>> related = makeGroups(hierarchy, sameOrigins);
        System.out.println("Done");
        System.out.println("------------------------------------------------------------------------\n");

        System.out.println("Performing the smilarity search against all cell lines... ");
        List<CellLine> cellLines = human.getCellLines();
        Map<String, Set<String>> conflicts = run(cellLines, related);
        System.out.println("Done");
        System.out.println("------------------------------------------------------------------------\n");

        System.out.println("Writing the results as " + this.filename + " ... ");
        write(conflicts);
        System.out.println("Done");
        System.out.println("------------------------------------------------------------------------\n");
    }

    private void parseArgs(String[] args) {
        if (args.length == 0) return;
        if (args[0].equalsIgnoreCase("-H") || args[0].equalsIgnoreCase("-HELP")) {
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Cellosaurus Analysis - Help");
            System.out.println("------------------------------------------------------------------------\n");
            System.out.println("-f\t--file\t\tthe name/path of the result file");
            System.out.println("-s\t--score\t\tthe minimum score for conflicts to be reported");
            System.out.println("-m\t--markers\tthe minimum number of markers for conflicts to be reported");
            System.exit(0);
        }
        for (int i = 0; i < args.length-1; i+=2) {
            switch (args[i].toUpperCase()) {
                case "-F":
                case "-FILE":
                    this.filename = args[i+1];
                    break;
                case "-S":
                case "-SCORE":
                    this.scoreLimit = Double.parseDouble(args[i+1]);
                    break;
                case "-M":
                case "-MARKERS":
                    this.markerLimit = Double.parseDouble(args[i+1]);
                    break;
            }
        }
    }

    private void resolveMap(Set<String> ids, Map<String, List<String>> hierarchy, String id) {
        ids.add(id);

        if (hierarchy.containsKey(id)) {
            for (String value : hierarchy.get(id)) {
                ids.add(value);
                resolveMap(ids, hierarchy, value);
            }
        }

    }

    private void addGroupsToMap(Map<String, Set<String>> map, Set<Set<String>> groups) {
        for (Set<String> group : groups) {
            for (String id : group) {
                Set<String> others = new HashSet<>(group);
                others.remove(id);

                if (map.containsKey(id)) {
                    map.get(id).addAll(others);
                } else {
                    map.put(id, others);
                }
            }
        }
    }

    public Map<String, Set<String>> makeGroups(Map<String, List<String>> hierarchy, Set<Set<String>> sameOrigins) {
        Map<String, Set<String>> related = new HashMap<>();
        addGroupsToMap(related, sameOrigins);

        Set<Set<String>> groups = new HashSet<>();
        for (String id : hierarchy.keySet()) {
            Set<String> group = new HashSet<>(hierarchy.get(id));
            resolveMap(group, hierarchy, id);

            for (Set<String> origins : sameOrigins) {
                Set<String> copy = new HashSet<>(origins);
                copy.retainAll(hierarchy.get(id));

                if (origins.contains(id) || !copy.isEmpty()) {
                    group.addAll(origins);
                }
            }
            groups.add(group);
        }
        addGroupsToMap(related, groups);

        return related;
    }

    private Map<String, Set<String>> run(List<CellLine> cellLines, Map<String, Set<String>> related) {
        Map<String, Set<String>> conflicts = new HashMap<>();

        Algorithm algorithm = ScoringAlgorithm.TANABE;
        Mode mode = ScoringMode.NON_EMPTY;

        int size = cellLines.size();
        for (int i = 0; i < size-1; i++) {
            CellLine query = cellLines.get(i);
            for (int j = i+1; j < cellLines.size(); j++) {
                CellLine reference = cellLines.get(j);

                for (Profile queryProfile : query.getProfiles()) {
                    for (Profile referenceProfile : reference.getProfiles()) {
                        String queryId = query.getAccession();
                        String referenceId = reference.getAccession();

                        if (!(related.containsKey(queryId) && related.get(queryId).contains(referenceId))) {
                            double score = algorithm.computeScore(mode, queryProfile, referenceProfile, false);
                            if (score >= this.scoreLimit && referenceProfile.getMarkerNumber() >= this.markerLimit) {
                                if (!conflicts.containsKey(queryId)) {
                                    conflicts.put(queryId, new HashSet<>());
                                }
                                conflicts.get(queryId).add(referenceId);
                            }
                        }
                    }
                }
            }
            if (i % 100 == 0) System.out.print(i + "/" + size + " cell lines searched\r");
        }
        System.out.println(size + "/" + size + " cell lines searched");
        return conflicts;
    }

    private void write(Map<String, Set<String>> conflicts) throws IOException{
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename))) {
            for (String key : conflicts.keySet()) {
                bw.write("QR   ");
                bw.write(key);
                bw.write("\n");

                for (String value : conflicts.get(key)) {
                    bw.write("AC   ");
                    bw.write(value);
                    bw.write("\n");
                }
                bw.write("//\n");
            }
        }
    }
}
