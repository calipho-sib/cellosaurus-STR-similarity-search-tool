package org.expasy.cellosaurus.genomics.str;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a cell line entry. In addition to the providing of basic information such as the accession number,
 * name or species, its purpose is to store the different STR profiles resolved by the ConflictResolver class.
 */
public class CellLine implements Comparable<CellLine> {
    private final String accession;
    private final String name;
    private final String species;
    private double bestScore;
    private boolean problematic;
    private String problem;

    private final List<Profile> profiles = new ArrayList<>();

    /**
     * Main constructor
     *
     * @param accession the accession number of the cell line
     * @param name      the name of the cell line
     * @param species   the species of the cell line
     */
    public CellLine(String accession, String name, String species) {
        this.accession = accession;
        this.name = name;
        this.species = species;
    }

    /**
     * Copy constructor
     *
     * @param that another instance of {@code CellLine}
     */
    public CellLine(CellLine that) {
        this.accession = that.accession;
        this.name = that.name;
        this.species = that.species;
        this.bestScore = that.bestScore;
        this.problematic = that.problematic;
        this.problem = that.problem;

        for (Profile profile : that.profiles) {
            this.profiles.add(new Profile(profile));
        }
    }

    /**
     * Sorts the profiles by score and only retain the best and worst ones.
     */
    public void reduceProfiles() {
        Collections.sort(this.profiles);
        if(this.profiles.size()!=0) {
            this.bestScore = this.profiles.get(0).getScore();
        }
        // if there is more than two profiles stored, only keep the best and the worst ones.
        if (this.profiles.size() > 2) {
            Profile bestProfile = this.profiles.get(0);
            Profile worstProfile = this.profiles.get(this.profiles.size() - 1);

            this.profiles.clear();
            this.profiles.add(bestProfile);
            this.profiles.add(worstProfile);
        }
    }

    public String getAccession() {
        return accession;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public double getBestScore() {
        return bestScore;
    }

    public void setBestScore(double bestScore) {
        this.bestScore = bestScore;
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

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void addProfiles(List<Profile> profiles) {
        this.profiles.addAll(profiles);
    }

    @Override
    public int compareTo(CellLine that) {
        int c = Double.compare(that.bestScore, this.bestScore);
        if (c != 0) return c;

        return Integer.compare(that.profiles.get(0).getMarkerNumber(), this.profiles.get(0).getMarkerNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellLine cellLine = (CellLine) o;
        return accession.equals(cellLine.accession) &&
                name.equals(cellLine.name) &&
                species.equals(cellLine.species);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accession, name, species);
    }

    @Override
    public String toString() {
        return accession + "=" + profiles + '\n';
    }
}
