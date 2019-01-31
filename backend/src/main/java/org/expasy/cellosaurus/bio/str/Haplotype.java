package org.expasy.cellosaurus.bio.str;

import java.util.*;

public class Haplotype implements Comparable<Haplotype> {
    private double score;

    private List<Marker> markers = new ArrayList<>();

    public Haplotype(Marker... markers) {
        for (Marker marker : markers) {
            addMarker(marker);
        }
    }

    public Haplotype(Haplotype that) {
        this.score = that.score;

        for (Marker marker : that.markers) {
            this.markers.add(new Marker(marker));
        }
    }

    public int size() {
        return this.markers.size();
    }

    public void sort() {
        Collections.sort(this.markers);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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
    public int compareTo(Haplotype other) {
        return Double.compare(other.score, score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Haplotype haplotype = (Haplotype) o;
        return Double.compare(haplotype.score, score) == 0 &&
                markers.equals(haplotype.markers);
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
