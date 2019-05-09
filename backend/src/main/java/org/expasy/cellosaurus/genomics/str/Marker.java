package org.expasy.cellosaurus.genomics.str;

import java.util.*;

/**
 * Class representing a STR marker, defined as a specific loci on DNA that can possess several alleles. If the marker is
 * conflicted, the sources are listed to indicate the allele provenance.
 */
public class Marker implements Comparable<Marker> {
    private final String name;
    private Boolean conflicted;

    private Set<String> sources = new LinkedHashSet<>();
    private final Set<Allele> alleles = new LinkedHashSet<>();

    /**
     * Main constructor
     *
     * @param name    the name of the STR marker
     * @param values  an array of allele values
     */
    public Marker(String name, String... values) {
        this.name = name.replace(" ", "_");

        for (String value : values) {
            this.alleles.add(new Allele(value));
        }
    }

    /**
     * Secondary constructor
     *
     * @param name    the name of the STR marker
     * @param alleles a collection of alleles
     */
    public Marker(String name, Collection<Allele> alleles) {
        this.name = name.replace(" ", "_");
        this.alleles.addAll(alleles);
    }

    /**
     * Copy constructor
     *
     * @param that another instance of {@code Marker}
     */
    public Marker(Marker that) {
        this.name = that.name;
        this.conflicted = that.conflicted;
        this.sources.addAll(that.sources);

        that.alleles.forEach(allele -> this.alleles.add(new Allele(allele)));
    }

    /**
     * @return the number of alleles possessed by the STR marker. "ND" does not count as an allele.
     */
    public int countAlleles() {
        int c = 0;
        for (Allele allele : alleles) {
            if (!allele.getValue().equals("ND")) {
                c++;
            }
        }
        return c;
    }

    /**
     * Matches the alleles of this {@code Marker} instance against another {@code Marker} instance and returns the
     * number of common alleles. The matching alleles of the other {@code Marker} instance are marked as matched.
     *
     * @param that another instance of {@code Marker}
     * @return the number of alleles in common between the two markers
     */
    public int matchAgainst(Marker that) {
        int c = 0;
        for (Allele allele : that.alleles) {
            if (!allele.getValue().equals("ND")) {
                if (this.alleles.contains(allele)) {
                    allele.setMatched(true);
                    c++;
                } else {
                    allele.setMatched(false);
                }
            }
        }
        return c;
    }

    public String getName() {
        return name;
    }

    public Boolean getConflicted() {
        return conflicted;
    }

    public void setConflicted(Boolean conflicted) {
        this.conflicted = conflicted;
    }

    public Set<Allele> getAlleles() {
        return alleles;
    }

    public Set<String> getSources() {
        return sources;
    }

    public void setSources(Set<String> sources) {
        this.sources = sources;
    }

    @Override
    public int compareTo(Marker that) {
        // Sort the "DS" markers based on the int value of the chromosome
        if (this.name.charAt(0) == 'D' && Character.isDigit(this.name.charAt(1))) {
            if (that.name.charAt(0) == 'D' && Character.isDigit(that.name.charAt(1))) {
                int c1 = Integer.parseInt(this.name.substring(1, (this.name.charAt(2) == 'S') ? 2 : 3));
                int c2 = Integer.parseInt(that.name.substring(1, (that.name.charAt(2) == 'S') ? 2 : 3));

                return Integer.compare(c1, c2);
            }
        }
        return this.name.compareTo(that.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marker marker = (Marker) o;
        return Objects.equals(name, marker.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + alleles;
    }
}
