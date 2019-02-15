package org.expasy.cellosaurus.bio;

import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CellLine implements Comparable<CellLine> {
    private String accession;
    private String name;
    private String species;
    private double score;
    private boolean problematic;
    private String problem;

    private List<Haplotype> haplotypes = new ArrayList<>();

    public CellLine() {
    }

    public CellLine(String accession, String name, String species, boolean problematic) {
        this.accession = accession;
        this.name = name;
        this.species = species;
        this.problematic = problematic;
    }

    public CellLine(Marker... markers) {
        this.haplotypes.add(new Haplotype(markers));
    }

    public CellLine(CellLine that) {
        this.accession = that.accession;
        this.name = that.name;
        this.species = that.species;
        this.score = that.score;
        this.problematic = that.problematic;
        this.problem = that.problem;

        for (Haplotype haplotype : that.haplotypes) {
            this.haplotypes.add(new Haplotype(haplotype));
        }
    }

    public void initialize() {
        Collections.sort(this.haplotypes);
        score = this.haplotypes.get(0).getScore();

        if (this.haplotypes.size() > 2) {
            List<Haplotype> haplotypes = new ArrayList<>();
            haplotypes.add(this.haplotypes.get(0));
            haplotypes.add(this.haplotypes.get(this.haplotypes.size() - 1));

            this.haplotypes = haplotypes;
        }
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getScore() {
        return score;
    }

    public boolean isProblematic() {
        return problematic;
    }

    public void setProblematic(boolean problematic) {
        this.problematic = problematic;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public List<Haplotype> getHaplotypes() {
        return haplotypes;
    }

    public void setHaplotypes(List<Haplotype> haplotypes) {
        this.haplotypes = haplotypes;
    }

    @Override
    public int compareTo(CellLine that) {
        int c = Double.compare(that.score, this.score);
        if (c != 0) return c;

        return Integer.compare(that.haplotypes.get(0).getNumber(), this.haplotypes.get(0).getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellLine cellLine = (CellLine) o;
        return accession.equals(cellLine.accession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accession);
    }

    @Override
    public String toString() {
        return accession + "=" + haplotypes + '\n';
    }
}
