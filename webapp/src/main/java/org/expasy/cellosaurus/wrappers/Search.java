package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.genomics.str.CellLine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class Search {
    private String description;
    private String cellosaurusRelease;
    private String runOn;
    private String toolVersion;
    private Parameters parameters;

    private List<CellLine> results;

    public Search(String description, String cellosaurusRelease, String runOn, String toolVersion ) {
        this.description = description;
        this.cellosaurusRelease = cellosaurusRelease;
        this.runOn = runOn;
        this.toolVersion = toolVersion;
    }

    public Search(List<CellLine> cellLines, String cellosaurusRelease, String description) {
        this.description = description;
        this.cellosaurusRelease = cellosaurusRelease;
        this.runOn = utcDate();
        this.toolVersion = "1.1.0";
        this.results = cellLines;
    }

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

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public List<CellLine> getResults() {
        return results;
    }

    public void setResults(List<CellLine> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Search search = (Search) o;
        return runOn.equals(search.runOn) &&
                Objects.equals(description, search.description) &&
                cellosaurusRelease.equals(search.cellosaurusRelease) &&
                toolVersion.equals(search.toolVersion) &&
                parameters.equals(search.parameters) &&
                results.equals(search.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runOn, description, cellosaurusRelease, toolVersion, parameters, results);
    }

    @Override
    public String toString() {
        return results.toString();
    }
}
