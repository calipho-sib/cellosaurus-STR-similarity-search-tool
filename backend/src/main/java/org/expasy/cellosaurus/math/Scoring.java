package org.expasy.cellosaurus.math;

import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;

public class Scoring {
    private int scoring;
    private int mode;
    private boolean includeAmelogenin;
    private int hits;
    private int querySize;
    private int referenceSize;

    public Scoring(int scoring, int mode, boolean includeAmelogenin) {
        this.scoring = scoring;
        this.mode = mode;
        this.includeAmelogenin = includeAmelogenin;
    }

    public void computeScore(Haplotype query, Haplotype reference) {
        switch (this.scoring) {
            case 1:
                tanabeAlgorithm(query, reference);
                break;
            case 2:
                mastersAlgorithmQuery(query, reference);
                break;
            case 3:
                mastersAlgorithmReference(query, reference);
                break;
        }
    }

    private void tanabeAlgorithm(Haplotype query, Haplotype reference) {
        computeHits(query, reference);

        double score = (double) this.hits * 2 / (this.querySize + this.referenceSize) * 100;
        reference.setScore(score);
    }

    private void mastersAlgorithmQuery(Haplotype query, Haplotype reference) {
        computeHits(query, reference);

        double score = (double) this.hits / this.querySize * 100;
        reference.setScore(score);
    }

    private void mastersAlgorithmReference(Haplotype query, Haplotype reference) {
        computeHits(query, reference);

        double score = (double) this.hits / this.referenceSize * 100;
        reference.setScore(score);
    }

    private void computeHits(Haplotype query, Haplotype reference) {
        this.hits = 0;
        this.querySize = 0;
        this.referenceSize = 0;

        int markerNumber = 0;

        switch (mode) {
            case 1:
                for (Marker queryMarker : query.getMarkers()) {
                    int idx = reference.getMarkers().indexOf(queryMarker);
                    if (idx > -1) {
                        Marker referenceMarker = reference.getMarkers().get(idx);

                        if (include(queryMarker)) {
                            this.hits += queryMarker.matchAgainst(referenceMarker);
                            this.querySize += queryMarker.size();
                            this.referenceSize += referenceMarker.size();
                            markerNumber++;
                        } else {
                            queryMarker.matchAgainst(referenceMarker);
                        }
                    }
                }
                break;
            case 2:
                for (Marker queryMarker : query.getMarkers()) {
                    if (include(queryMarker)) {
                        this.querySize += queryMarker.size();
                        markerNumber++;
                    }

                    int idx = reference.getMarkers().indexOf(queryMarker);
                    if (idx > -1) {
                        Marker referenceMarker = reference.getMarkers().get(idx);

                        if (include(referenceMarker)) {
                            this.hits += queryMarker.matchAgainst(referenceMarker);
                            this.referenceSize += referenceMarker.size();
                        } else {
                            queryMarker.matchAgainst(referenceMarker);
                        }
                    }
                }
                break;
            case 3:
                for (Marker referenceMarker : reference.getMarkers()) {
                    if (include(referenceMarker)) {
                        this.referenceSize += referenceMarker.size();
                        markerNumber++;
                    }

                    int idx = query.getMarkers().indexOf(referenceMarker);
                    if (idx > -1) {
                        Marker queryMarker = query.getMarkers().get(idx);

                        if (include(queryMarker)) {
                            this.hits += queryMarker.matchAgainst(referenceMarker);
                            this.querySize += queryMarker.size();
                        } else {
                            queryMarker.matchAgainst(referenceMarker);
                        }
                    }
                }
                break;
        }
        reference.setMarkerNumber(markerNumber);
    }

    private boolean include(Marker marker) {
        return this.includeAmelogenin || !marker.getName().equals("Amelogenin");
    }
}
