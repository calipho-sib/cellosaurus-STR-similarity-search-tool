package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.bio.CellLine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class Search {
    private String description;
    private String cellosaurusRelease;
    private String runOn;
    private String softwareVersion;
    private Parameters parameters;

    private List<CellLine> results;

    public Search(String cellosaurusRelease, List<CellLine> cellLines) {
        this.description = "";
        this.cellosaurusRelease = cellosaurusRelease;
        this.runOn = utcDate();
        this.softwareVersion = "1.2-Beta";
        this.results = cellLines;
    }

    private String utcDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(new Date()) + " UTC+0";
    }

    public String getRunOn() {
        return runOn;
    }

    public void setRunOn(String runOn) {
        this.runOn = runOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCellosaurusRelease() {
        return cellosaurusRelease;
    }

    public void setCellosaurusRelease(String cellosaurusRelease) {
        this.cellosaurusRelease = cellosaurusRelease;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
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
                softwareVersion.equals(search.softwareVersion) &&
                parameters.equals(search.parameters) &&
                results.equals(search.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runOn, description, cellosaurusRelease, softwareVersion, parameters, results);
    }

    @Override
    public String toString() {
        return results.toString();
    }
}
