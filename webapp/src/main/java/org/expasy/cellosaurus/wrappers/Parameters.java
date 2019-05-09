package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.genomics.str.Marker;

import java.util.List;
import java.util.Objects;

/**
 * Class used to wrap all the search parameter information and metadata in a single object to make it easily convertible
 * to the different export formats.
 */
public class Parameters {
    private String algorithm;
    private String scoringMode;
    private int scoreFilter;
    private int maxResults;
    private boolean includeAmelogenin;

    private List<Marker> markers;

    /**
     * Main constructor
     *
     * @param algorithm         define the scoring algorithm used for the score computation
     * @param scoringMode       define the scoring mode used for the score computation
     * @param scoreFilter       filter defining the minimum score for matches to be reported
     * @param maxResults        filter defining the maximum number of results to be returned
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     */
    public Parameters(int algorithm, int scoringMode, int scoreFilter, int maxResults, boolean includeAmelogenin) {
        if (algorithm == 0) {
            this.algorithm = "Tanabe";
        } else if (algorithm == 1) {
            this.algorithm = "Masters (vs. query)";
        } else {
            this.algorithm = "Masters (vs. reference)";
        }
        if (scoringMode == 0) {
            this.scoringMode = "Non-empty makers";
        } else if (scoringMode == 1) {
            this.scoringMode = "Query markers";
        } else {
            this.scoringMode = "Reference markers";
        }
        this.scoreFilter = scoreFilter;
        this.maxResults = maxResults;
        this.includeAmelogenin = includeAmelogenin;
    }

    /**
     * Secondary constructor
     *
     * @param algorithm         define the scoring algorithm used for the score computation
     * @param scoringMode       define the scoring mode used for the score computation
     * @param scoreFilter       filter defining the minimum score for matches to be reported
     * @param maxResults        filter defining the maximum number of results to be returned
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     */
    public Parameters(String algorithm, String scoringMode, int scoreFilter, int maxResults, boolean includeAmelogenin){
        this.algorithm = algorithm;
        this.scoringMode = scoringMode;
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
