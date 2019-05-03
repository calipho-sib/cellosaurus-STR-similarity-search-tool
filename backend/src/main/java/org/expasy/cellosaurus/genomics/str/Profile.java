package org.expasy.cellosaurus.genomics.str;

import java.util.*;

/**
 * Class representing a STR profile, defined as a set of STR markers. The profile is characterized by a score and number
 * of markers used after the score computation step.
 */
public class Profile implements Comparable<Profile> {
    private double score;
    private int markerNumber;
    private int alleleNumber;

    private final List<Marker> markers = new ArrayList<>();

    /**
     * Main constructor
     *
     * @param markers a collection of markers
     */
    public Profile(Collection<Marker> markers) {
        this.markers.addAll(markers);
    }

    /**
     * Secondary constructor
     *
     * @param markers an array of markers
     */
    public Profile(Marker... markers) {
        this(Arrays.asList(markers));
    }

    /**
     * Copy constructor
     *
     * @param that another instance of {@code Profile}
     */
    public Profile(Profile that) {
        this.score = that.score;
        this.markerNumber = that.markerNumber;
        this.alleleNumber = that.alleleNumber;

        for (Marker marker : that.markers) {
            this.markers.add(new Marker(marker));
        }
    }

    /**
     * Sorts the list of markers
     */
    public void sort() {
        Collections.sort(this.markers);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getMarkerNumber() {
        return markerNumber;
    }

    public void setMarkerNumber(int markerNumber) {
        this.markerNumber = markerNumber;
    }

    public int getAlleleNumber() {
        return alleleNumber;
    }

    public void setAlleleNumber(int alleleNumber) {
        this.alleleNumber = alleleNumber;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void addMarker(Marker marker) {
        this.markers.add(marker);
    }

    public void addMarkers(Collection<Marker> markers) {
        this.markers.addAll(markers);
    }

    @Override
    public int compareTo(Profile other) {
        return Double.compare(other.score, score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Double.compare(profile.score, score) == 0 &&
                markers.equals(profile.markers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, markers);
    }

    @Override
    public String toString() {
        return markers.toString();
    }
}
