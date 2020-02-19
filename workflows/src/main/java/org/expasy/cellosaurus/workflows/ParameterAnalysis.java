package org.expasy.cellosaurus.workflows;

import org.expasy.cellosaurus.analyzes.CellosaurusAnalysis;
import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.formats.xml.XmlParser;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.expasy.cellosaurus.genomics.str.Species;
import org.expasy.cellosaurus.math.scoring.*;

import java.io.*;
import java.util.*;

public class ParameterAnalysis {
    private static final List<List<Marker>> MARKERS_LIST = new ArrayList<>();
    private final Map<String, Set<String>> related;

    private ParameterAnalysis() throws IOException {
        String[] names = {"D5S818", "D7S820", "D13S317", "D16S539", "CSF1PO", "TH01", "TPOX", "vWA", "D3S1358",
                "D8S1179", "D18S51", "D21S11", "FGA", "Penta D", "Penta E", "D2S1338", "D19S433"};

        List<Marker> markers = new ArrayList<>();
        markers.add(new Marker("Amelogenin"));
        for (String name : names) {
            markers.add(new Marker(name));
            MARKERS_LIST.add(new ArrayList<>(markers));
        }

        Set<String> accessions = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("Table_S1.csv")))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                accessions.add(line.split(",")[0]);
            }
        }

        Parser parser = new XmlParser();
        parser.parse("data/704f826d-e3bd-48d2-9fc4-4cae3a3a0e24/researchdata/cellosaurus_xml.rel33");

        Species species = Species.HUMAN;
        List<CellLine> cellLines = new ArrayList<>();
        for (CellLine cellLine : species.getCellLines()) {
            if (accessions.contains(cellLine.getAccession())) {
                cellLines.add(cellLine);
            }
        }

        Map<String, List<String>> hierarchy = species.getHierarchy();
        Set<Set<String>> sameOrigins = species.getSameOrigins();
        CellosaurusAnalysis cellosaurusAnalysis = new CellosaurusAnalysis();
        this.related = cellosaurusAnalysis.makeGroups(hierarchy, sameOrigins);

        List<CellLine> cellLines8 = filter(cellLines, MARKERS_LIST.get(7), MARKERS_LIST.get(7));
        List<CellLine> cellLines13 = filter(cellLines, MARKERS_LIST.get(12), MARKERS_LIST.get(12));
        
        compute(cellLines8, "Run_Algorithms-8_STR_Markers-Tanabe", 0, 0, false);
        compute(cellLines8, "Run_Algorithms-8_STR_Markers-Masters", 1, 0, false);
        compute(cellLines8, "Run_Algorithms-8_STR_Markers-Reverse", 2, 0, false);
        compute(cellLines13, "Run_Algorithms-13_STR_Markers-Tanabe", 0, 0, false);
        compute(cellLines13, "Run_Algorithms-13_STR_Markers-Masters", 1, 0, false);
        compute(cellLines13, "Run_Algorithms-13_STR_Markers-Reverse", 2, 0, false);

        compute(cellLines8, "Run_Homozygotes-8_STR_Markers-As_One", 0, 0, false);
        compute(cellLines8, "Run_Homozygotes-8_STR_Markers-As_Two", 0, 1, false);
        compute(cellLines13, "Run_Homozygotes-13_STR_Markers-As_One", 0, 0, false);
        compute(cellLines13, "Run_Homozygotes-13_STR_Markers-As_Two", 0, 1, false);

        compute(cellLines8, "Run_Amelogenin-8_STR_Markers-Not_Included", 0, 0, false);
        compute(cellLines8, "Run_Amelogenin-8_STR_Markers-Included", 0, 0, true);
        compute(cellLines13, "Run_Amelogenin-13_STR_Markers-Not_Included", 0, 0, false);
        compute(cellLines13, "Run_Amelogenin-13_STR_Markers-Included", 0, 0, true);

        compute(filter(cellLines, MARKERS_LIST.get(0), MARKERS_LIST.get(0)), "Run_Markers-1_STR_Markers-1", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(1), MARKERS_LIST.get(1)), "Run_Markers-2_STR_Markers-2", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(2), MARKERS_LIST.get(2)), "Run_Markers-3_STR_Markers-3", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(3), MARKERS_LIST.get(3)), "Run_Markers-4_STR_Markers-4", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(4), MARKERS_LIST.get(4)), "Run_Markers-5_STR_Markers-5", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(5), MARKERS_LIST.get(5)), "Run_Markers-6_STR_Markers-6", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(6), MARKERS_LIST.get(6)), "Run_Markers-7_STR_Markers-7", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(7), MARKERS_LIST.get(7)), "Run_Markers-8_STR_Markers-8", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(8), MARKERS_LIST.get(8)), "Run_Markers-9_STR_Markers-9", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(9), MARKERS_LIST.get(9)), "Run_Markers-10_STR_Markers-10", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(10), MARKERS_LIST.get(10)), "Run_Markers-11_STR_Markers-11", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(11), MARKERS_LIST.get(11)), "Run_Markers-12_STR_Markers-12", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(12), MARKERS_LIST.get(12)), "Run_Markers-13_STR_Markers-13", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(13), MARKERS_LIST.get(13)), "Run_Markers-14_STR_Markers-14", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(14), MARKERS_LIST.get(14)), "Run_Markers-15_STR_Markers-15", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(15), MARKERS_LIST.get(15)), "Run_Markers-16_STR_Markers-16", 0, 0, false);
        compute(filter(cellLines, MARKERS_LIST.get(16), MARKERS_LIST.get(16)), "Run_Markers-17_STR_Markers-17", 0, 0, false);
    }
    
    private List<CellLine> filter(List<CellLine> allCellLines, List<Marker> markers, List<Marker> references) {
        List<CellLine> cellLines = new ArrayList<>();

        for (CellLine cellLine : allCellLines) {
            CellLine copyCellLine = new CellLine(cellLine);
            copyCellLine.getProfiles().clear();

            List<Profile> copyProfiles = new ArrayList<>();
            for (Profile profile : cellLine.getProfiles()) {
                Profile copyProfile = new Profile();

                if (profile.getMarkers().containsAll(references)) {
                    for (Marker marker : profile.getMarkers()) {
                        if (markers.contains(marker)) {
                            copyProfile.getMarkers().add(new Marker(marker));
                        }
                    }
                    copyProfiles.add(copyProfile);
                }
            }
            if (!copyProfiles.isEmpty()) {
                copyCellLine.getProfiles().addAll(copyProfiles);
                cellLines.add(copyCellLine);
            }
        }
        return cellLines;
    }

    private void compute(List<CellLine> cellLines, String run, int iAlgorithm, int iMode, boolean amelogenin) throws IOException {
        compute(cellLines, cellLines, run, iAlgorithm, iMode, amelogenin);
    }

    private void compute(List<CellLine> cellLines1, List<CellLine> cellLines2, String run, int iAlgorithm, int iMode, boolean amelogenin) throws IOException {
        System.out.println(run);

        Algorithm algorithm = ScoringAlgorithm.get(iAlgorithm);
        Mode mode = iMode == 0 ? ScoringMode.NON_EMPTY : ExtendedMode.NON_EMPTY_HOMOZYGOTES_AS_TWO;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("results/data/" + run + ".tsv"))) {
            bw.write("sample1\tsample2\trelationship\tmarkers\tparameter\tscore\n");
            for (int i = 0; i < cellLines1.size() - 1; i++) {
                CellLine query = cellLines1.get(i);
                for (int j = i + 1; j < cellLines2.size(); j++) {
                    CellLine reference = cellLines2.get(j);
                    write(bw, query, reference, run, algorithm, mode, amelogenin);
                }
            }
        }
    }

    private void write(BufferedWriter bw, CellLine query, CellLine reference, String run, Algorithm algorithm, Mode mode, boolean amelogenin) throws IOException {
        double bestScore = 0;

        for (Profile queryProfile : query.getProfiles()) {
            for (Profile referenceProfile : reference.getProfiles()) {
                double score = algorithm.computeScore(mode, queryProfile, referenceProfile, amelogenin);
                if (score > bestScore) {
                    bestScore = score;
                }
            }
        }

        String relationship;
        if (this.related.containsKey(query.getAccession()) && this.related.get(query.getAccession()).contains(reference.getAccession())) {
            relationship = "Related";
        } else {
            relationship = "Unrelated";
        }

        String[] srun = run.replace("_", " ").split("-");
        bw.write(query.getAccession());
        bw.write("\t");
        bw.write(reference.getAccession());
        bw.write("\t");
        bw.write(relationship);
        bw.write("\t");
        bw.write(srun[1]);
        bw.write("\t");
        bw.write(srun[2]);
        bw.write("\t");
        bw.write(String.format("%.2f", bestScore));
        bw.write("\n");
    }

    public static void main(String[] args) {
        try {
            new ParameterAnalysis();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private enum ExtendedMode implements Mode {
        NON_EMPTY_HOMOZYGOTES_AS_TWO {
            @Override
            public int computeHits(Profile query, Profile reference, boolean includeAmelogenin) {
                int hits = 0;
                int querySize = 0;
                int referenceSize = 0;
                int markerNumber = 0;

                for (Marker queryMarker : query.getMarkers()) {
                    int idx = reference.getMarkers().indexOf(queryMarker);
                    if (idx > -1) {
                        Marker referenceMarker = reference.getMarkers().get(idx);

                        if (includeAmelogenin || !queryMarker.getName().equals("Amelogenin")) {
                            int queryAlleles = queryMarker.countAlleles();
                            querySize += queryAlleles == 1 ? 2 : queryAlleles;

                            int referenceAlleles = referenceMarker.countAlleles();
                            referenceSize += referenceAlleles == 1 ? 2 : referenceAlleles;

                            int commonAlleles = queryMarker.matchAgainst(referenceMarker);
                            if (commonAlleles == 1 && queryAlleles == 1 && referenceAlleles == 1) {
                                hits += 2;
                            } else {
                                hits += commonAlleles;
                            }
                            markerNumber++;
                        } else {
                            queryMarker.matchAgainst(referenceMarker);
                        }
                    }
                }
                query.setAlleleNumber(querySize);
                reference.setAlleleNumber(referenceSize);
                reference.setMarkerNumber(markerNumber);

                return hits;
            }
        }
    }
}
