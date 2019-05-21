package org.expasy.cellosaurus.genomics.str;

import java.util.*;

/**
 * Class representing a species. It is used to encapsulate all the relevant information about the cell lines of a
 * species and enable to retrieve them more easily.
 */
public class Species {
    private final String name;
    private final List<CellLine> cellLines = new ArrayList<>();
    private final Set<Set<String>> sameOrigins = new HashSet<>();
    private final Map<String, List<String>> hierarchy = new HashMap<>();

    /**
     * Main constructor
     *
     * @param name the name of the species
     */
    public Species(String name) {
        this.name = name;
    }

    public boolean isEmpty() {
        return this.cellLines.isEmpty();
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Species species = (Species) o;
        return name.equals(species.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
