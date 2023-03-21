package org.expasy.cellosaurus.genomics.str;

import java.util.*;

/**
 * Class representing a species. It is used to encapsulate all the relevant information about the cell lines of a
 * species and enable to retrieve them more easily.
 */
public enum Species {
    HUMAN("Homo sapiens (Human)",
            Arrays.asList(
                    new Marker("Amelogenin"),
                    new Marker("CSF1PO"),
                    new Marker("D2S1338"),
                    new Marker("D3S1358"),
                    new Marker("D5S818"),
                    new Marker("D7S820"),
                    new Marker("D8S1179"),
                    new Marker("D13S317"),
                    new Marker("D16S539"),
                    new Marker("D18S51"),
                    new Marker("D19S433"),
                    new Marker("D21S11"),
                    new Marker("FGA"),
                    new Marker("Penta D"),
                    new Marker("Penta E"),
                    new Marker("TH01"),
                    new Marker("TPOX"),
                    new Marker("vWA")
            ),
            Arrays.asList(
                    new Marker("D10S1248"),
                    new Marker("D1S1656"),
                    new Marker("D2S441"),
                    new Marker("D6S1043"),
                    new Marker("D12S391"),
                    new Marker("D22S1045"),
                    new Marker("DXS101"),
                    new Marker("DYS391"),
                    new Marker("F13A01"),
                    new Marker("F13B"),
                    new Marker("FESFPS"),
                    new Marker("LPL"),
                    new Marker("Penta C"),
                    new Marker("SE33")
            )),
    MOUSE("Mus musculus (Mouse)",
            Arrays.asList(
                    new Marker("STR 1-1"),
                    new Marker("STR 1-2"),
                    new Marker("STR 2-1"),
                    new Marker("STR 3-2"),
                    new Marker("STR 4-2"),
                    new Marker("STR 5-5"),
                    new Marker("STR 6-4"),
                    new Marker("STR 6-7"),
                    new Marker("STR 7-1"),
                    new Marker("STR 8-1"),
                    new Marker("STR 11-2"),
                    new Marker("STR 12-1"),
                    new Marker("STR 13-1"),
                    new Marker("STR 15-3"),
                    new Marker("STR 17-2"),
                    new Marker("STR 18-3"),
                    new Marker("STR 19-2"),
                    new Marker("STR X-1")
            ),
            new ArrayList<>()),
    DOG("Canis lupus familiaris (Dog)",
            Arrays.asList(
                    new Marker("FHC2010"),
                    new Marker("FHC2054"),
                    new Marker("FHC2079"),
                    new Marker("PEZ1"),
                    new Marker("PEZ3"),
                    new Marker("PEZ5"),
                    new Marker("PEZ6"),
                    new Marker("PEZ8"),
                    new Marker("PEZ12"),
                    new Marker("PEZ20")
            ),
            new ArrayList<>());

    private final String name;
    private final List<Marker> defaultMarkers;
    private final List<Marker> optionalMarkers;

    private final List<CellLine> cellLines = new ArrayList<>();
    private final Set<Set<String>> sameOrigins = new HashSet<>();
    private final Map<String, List<String>> hierarchy = new HashMap<>();

    /**
     * Main constructor
     */
    Species(String name, List<Marker> defaultMarkers, List<Marker> optionalMarkers) {
        this.name = name;
        this.defaultMarkers = defaultMarkers;
        this.optionalMarkers = optionalMarkers;
    }

    public static Species get(String name) {
        switch (name.toUpperCase()) {
            case "HUMAN":
            case "HOMO SAPIENS":         // found in frontend as a value of parameter 'species' for search query
            case "HOMO SAPIENS (HUMAN)": // found in xml
                return HUMAN;
            case "MOUSE":
            case "MUS MUSCULUS":
            case "MUS MUSCULUS (MOUSE)":
                return MOUSE;
            case "DOG":
            case "CANIS LUPUS FAMILIARIS":
            case "CANIS LUPUS FAMILIARIS (DOG)":
                return DOG;
            default:
                return null;
        }
    }

    public String getName() {
        return name;
    }

    public List<Marker> getDefaultMarkers() {
        return defaultMarkers;
    }

    public List<Marker> getOptionalMarkers() {
        return optionalMarkers;
    }

    public List<CellLine> getCellLines() {
        return cellLines;
    }

    public void addCellLine(CellLine cellLine) {
        this.cellLines.add(cellLine);
    }

    public Set<Set<String>> getSameOrigins() {
        return sameOrigins;
    }

    public void addOrigins(Set<String> orgins) {
        if (orgins.isEmpty()) return;
        this.sameOrigins.add(orgins);
    }

    public Map<String, List<String>> getHierarchy() {
        return hierarchy;
    }

    public void addHierarchy(String key, String value) {
        if (key.isEmpty()) return;
        if (!this.hierarchy.containsKey(key)) {
            this.hierarchy.put(key, new ArrayList<>());
        }
        this.hierarchy.get(key).add(value);
    }
}
