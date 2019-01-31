package org.expasy.cellosaurus.wrappers;

import org.expasy.cellosaurus.bio.str.Marker;

import java.util.List;
import java.util.Objects;

public class Parameters {
    private String scoring;
    private String mode;
    private int scoreFilter;
    private int sizeFilter;
    private boolean includeAmelogenin;

    private List<Marker> markers;

    public Parameters(int scoring, int mode, int scoreFilter, int sizeFilter, boolean includeAmelogenin) {
        if (scoring == 1) {
            this.scoring = "Masters Algorithm (vs. query)";
        } else if (scoring == 2) {
            this.scoring = "Masters Algorithm (vs. reference)";
        } else {
            this.scoring = "Tanabe Algorithm";
        }
        if (mode == 1) {
            this.mode = "Non-empty makers";
        } else {
            this.mode = "Query markers";
        }
        this.scoreFilter = scoreFilter;
        this.sizeFilter = sizeFilter;
        this.includeAmelogenin = includeAmelogenin;
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
                includeAmelogenin == that.includeAmelogenin &&
                scoring.equals(that.scoring) &&
                mode.equals(that.mode) &&
                markers.equals(that.markers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoring, mode, scoreFilter, sizeFilter, includeAmelogenin, markers);
    }

    @Override
    public String toString() {
        return markers.toString();
    }
}
