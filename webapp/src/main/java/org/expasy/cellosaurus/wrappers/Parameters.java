package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.genomics.str.Marker;

import java.util.List;
import java.util.Objects;

/**
 * Class used to wrap all the search parameter information and metadata in a single object to make it easily convertible
 * to the different export formats.
 */
public class Parameters {
    private final String species;
    private final String algorithm;
    private final String scoringMode;
    private final int scoreFilter;
    private final int minMarkers;
    private final int maxResults;
    private final boolean includeAmelogenin;

    private final List<Marker> markers;

    /**
     * Main constructor
     *
     * @param algorithm         define the scoring algorithm used for the score computation
     * @param scoringMode       define the scoring mode used for the score computation
     * @param scoreFilter       filter defining the minimum score for matches to be reported
     * @param minMarkers        filter defining the minimum number of markers for matches to be reported
     * @param maxResults        filter defining the maximum number of results to be returned
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     */
    public Parameters(List<Marker> markers, String species, int algorithm, int scoringMode, int scoreFilter,
                      int minMarkers, int maxResults, boolean includeAmelogenin) {
        this.markers = markers;
        this.species = species;
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
        this.minMarkers = minMarkers;
        this.maxResults = maxResults;
        this.includeAmelogenin = includeAmelogenin;
    }

    /**
     * Secondary constructor
     *
     * @param algorithm         define the scoring algorithm used for the score computation
     * @param scoringMode       define the scoring mode used for the score computation
     * @param scoreFilter       filter defining the minimum score for matches to be reported
     * @param minMarkers        filter defining the minimum number of markers for matches to be reported
     * @param maxResults        filter defining the maximum number of results to be returned
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     */
    public Parameters(List<Marker> markers, String species, String algorithm, String scoringMode, int scoreFilter,
                      int minMarkers, int maxResults, boolean includeAmelogenin) {
        this.markers = markers;
        this.species = species;
        this.algorithm = algorithm;
        this.scoringMode = scoringMode;
        this.scoreFilter = scoreFilter;
        this.minMarkers = minMarkers;
        this.maxResults = maxResults;
        this.includeAmelogenin = includeAmelogenin;
    }

    public String getSpecies() {
        return species;
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

    public int getMinMarkers() {
        return minMarkers;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameters that = (Parameters) o;
        return scoreFilter == that.scoreFilter &&
                minMarkers == that.minMarkers &&
                maxResults == that.maxResults &&
                includeAmelogenin == that.includeAmelogenin &&
                species.equals(that.species) &&
                algorithm.equals(that.algorithm) &&
                scoringMode.equals(that.scoringMode) &&
                markers.equals(that.markers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(species, algorithm, scoringMode, scoreFilter, minMarkers, maxResults, includeAmelogenin, markers);
    }

    @Override
    public String toString() {
        return markers.toString();
    }
}
