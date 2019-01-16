package org.expasy.cellosaurus.bio.str;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Marker implements Comparable<Marker> {
    private String name;
    private Boolean conflicted;

    private Set<String> sources = new LinkedHashSet<>();
    private Set<Allele> alleles = new LinkedHashSet<>();

    public Marker(String name) {
        this.name = name.replace(" ", "_");
    }

    public Marker(String name, boolean conflicted) {
        this(name);
        this.conflicted = conflicted;
    }

    public Marker(String name, String... alleles) {
        this(name, false);

        for (String allele : alleles) {
            this.alleles.add(new Allele(allele));
        }
    }

    public Marker(Marker that) {
        this.name = that.name;
        this.conflicted = that.conflicted;
        this.sources.addAll(that.sources);

        for (Allele allele : that.alleles) {
            this.alleles.add(new Allele(allele));
        }
    }

    public int size() {
        return this.alleles.size();
    }

    public int matchAgainst(Marker that) {
        if (this.alleles.equals(that.alleles)) {
            that.alleles.forEach(x -> x.setMatched(true));
            return this.size();
        }

        int c = 0;
        for (Allele allele : that.alleles) {
            if (this.alleles.contains(allele)) {
                allele.setMatched(true);
                c++;
            } else {
                allele.setMatched(false);
            }
        }
        return c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isConflicted() {
        return conflicted;
    }

    public void setConflicted(Boolean conflicted) {
        this.conflicted = conflicted;
    }

    public Set<Allele> getAlleles() {
        return alleles;
    }

    public void setAlleles(Set<Allele> alleles) {
        this.alleles = alleles;
    }

    public void addAllele(String allele) {
        this.alleles.add(new Allele(allele));
    }

    public void addAlleles(Collection<Allele> alleles) {
        for (Allele allele : alleles) {
            this.alleles.add(new Allele(allele));
        }
    }

    public Set<String> getSources() {
        return sources;
    }

    public void setSources(Set<String> sources) {
        this.sources = sources;
    }

    public void addSource(String source) {
        this.sources.add(source);
    }

    public void addSources(Collection<String> sources) {
        this.sources.addAll(sources);
    }

    @Override
    public int compareTo(Marker that) {
        if (this.name.charAt(0) == 'D' && Character.isDigit(this.name.charAt(1))) {
            if (that.name.charAt(0) == 'D' && Character.isDigit(that.name.charAt(1))) {
                Integer c1;
                Integer c2;

                if (this.name.charAt(2) == 'S') {
                    c1 = Integer.valueOf(this.name.substring(1, 2));
                } else {
                    c1 = Integer.valueOf(this.name.substring(1, 3));
                }
                if (that.name.charAt(2) == 'S') {
                    c2 = Integer.valueOf(that.name.substring(1, 2));
                } else {
                    c2 = Integer.valueOf(that.name.substring(1, 3));
                }
                return c1.compareTo(c2);
            }
        }
        return this.name.compareTo(that.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marker marker = (Marker) o;
        return conflicted == marker.conflicted &&
                name.equals(marker.name) &&
                sources.equals(marker.sources) &&
                alleles.equals(marker.alleles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, conflicted, sources, alleles);
    }

    @Override
    public String toString() {
        return name + alleles;
    }
}
