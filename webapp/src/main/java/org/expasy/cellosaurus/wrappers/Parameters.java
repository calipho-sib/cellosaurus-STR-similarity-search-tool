package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.bio.str.Marker;

import java.util.List;
import java.util.Objects;

public class Parameters {
    private String algorithm;
    private int scoreFilter;
    private int sizeFilter;

    private List<Marker> markers;

    public Parameters(int scoring, int scoreFilter, int sizeFilter) {
        if (scoring == 1) {
            this.algorithm = "Masters Algorithm (vs. query)";
        } else if (scoring == 2) {
            this.algorithm = "Masters Algorithm (vs. reference)";
        } else {
            this.algorithm = "Tanabe Algorithm";
        }
        this.scoreFilter = scoreFilter;
        this.sizeFilter = sizeFilter;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getScoreFilter() {
        return scoreFilter;
    }

    public void setScoreFilter(int scoreFilter) {
        this.scoreFilter = scoreFilter;
    }

    public int getSizeFilter() {
        return sizeFilter;
    }

    public void setSizeFilter(int sizeFilter) {
        this.sizeFilter = sizeFilter;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameters that = (Parameters) o;
        return scoreFilter == that.scoreFilter &&
                sizeFilter == that.sizeFilter &&
                algorithm.equals(that.algorithm) &&
                markers.equals(that.markers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, scoreFilter, sizeFilter, markers);
    }

    @Override
    public String toString() {
        return markers.toString();
    }
}
