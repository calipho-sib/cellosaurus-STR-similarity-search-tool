package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.genomics.str.Marker;

import java.util.List;
import java.util.Objects;

public class Parameters {
    private String algorithm;
    private String scoringMode;
    private int scoreFilter;
    private int maxResults;
    private boolean includeAmelogenin;

    private List<Marker> markers;

    public Parameters(int algorithm, int scoringMode, int scoreFilter, int maxResults, boolean includeAmelogenin) {
        if (algorithm == 1) {
            this.algorithm = "Tanabe";
        } else if (algorithm == 2) {
            this.algorithm = "Masters (vs. query)";
        } else {
            this.algorithm = "Masters (vs. reference)";
        }
        if (scoringMode == 1) {
            this.scoringMode = "Non-empty makers";
        } else if (scoringMode == 2) {
            this.scoringMode = "Query markers";
        } else {
            this.scoringMode = "Reference markers";
        }
        this.scoreFilter = scoreFilter;
        this.maxResults = maxResults;
        this.includeAmelogenin = includeAmelogenin;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getScoringMode() {
        return scoringMode;
    }

    public int getScoreFilter() {
        return scoreFilter;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public boolean isIncludeAmelogenin() {
        return includeAmelogenin;
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
                maxResults == that.maxResults &&
                includeAmelogenin == that.includeAmelogenin &&
                algorithm.equals(that.algorithm) &&
                scoringMode.equals(that.scoringMode) &&
                markers.equals(that.markers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, scoringMode, scoreFilter, maxResults, includeAmelogenin, markers);
    }

    @Override
    public String toString() {
        return markers.toString();
    }
}
