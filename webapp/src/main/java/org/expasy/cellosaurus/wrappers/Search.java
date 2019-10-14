package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.genomics.str.CellLine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Class used to wrap all the search metadata in a single object to make it easily convertible to the different export
 * formats.
 */
public class Search {
    private final String description;
    private final String cellosaurusRelease;
    private final String runOn;
    private final String toolVersion;
    private final int searchSpace;
    private final Parameters parameters;

    private final List<CellLine> results;

    /**
     * Main constructor
     *
     * @param cellLines          the resulting cell line matches from the STR similarity search
     * @param description        the user-defined description of the query
     * @param cellosaurusRelease the release version of the Cellosaurus data being used
     * @param searchSpace        the number of cell lines with STR profiles that were searched
     */
    public Search(List<CellLine> cellLines, Parameters parameters, String description, String cellosaurusRelease,
                  int searchSpace) {
        this.results = cellLines;
        this.parameters = parameters;
        this.description = description;
        this.cellosaurusRelease = cellosaurusRelease;
        this.runOn = utcDate();
        this.toolVersion = "1.4.3";
        this.searchSpace = searchSpace;
    }

    /**
     * Secondary constructor
     *
     * @param cellLines          the resulting cell line matches from the STR similarity search
     * @param description        the user-defined description of the query
     * @param cellosaurusRelease the release version of the Cellosaurus data being used
     * @param runOn              the date at which the search was performed
     * @param toolVersion        the release version of the STR Similarity Search Tool being used
     * @param searchSpace        the number of cell lines with STR profiles that were searched
     */
    public Search(List<CellLine> cellLines,  Parameters parameters, String description, String cellosaurusRelease,
                  String runOn, String toolVersion, int searchSpace ) {
        this.results = cellLines;
        this.parameters = parameters;
        this.description = description;
        this.cellosaurusRelease = cellosaurusRelease;
        this.runOn = runOn;
        this.toolVersion = toolVersion;
        this.searchSpace = searchSpace;
    }

    /**
     * Format the date and time at which the search was performed to be used as tracking metadata in the export formats.
     *
     * @return the search date and time in the UTC+0 format
     */
    private String utcDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(new Date()) + " UTC+0";
    }

    public String getDescription() {
        return description;
    }

    public String getCellosaurusRelease() {
        return cellosaurusRelease;
    }

    public String getRunOn() {
        return runOn;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public List<CellLine> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Search search = (Search) o;
        return searchSpace == search.searchSpace &&
                description.equals(search.description) &&
                cellosaurusRelease.equals(search.cellosaurusRelease) &&
                runOn.equals(search.runOn) &&
                toolVersion.equals(search.toolVersion) &&
                parameters.equals(search.parameters) &&
                results.equals(search.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, cellosaurusRelease, runOn, toolVersion, searchSpace, parameters, results);
    }

    @Override
    public String toString() {
        return results.toString();
    }
}
