package org.expasy.cellosaurus.math;

import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;

public class Scoring {
    private Haplotype query;
    private int scoring;

    public Scoring(Haplotype haplotype, int scoring) {
        this.query = haplotype;
        this.scoring = scoring;
    }

    public void computeScore(Haplotype reference) {
        switch (this.scoring) {
            case 1:
                mastersVsQueryAlgorithm(reference);
                break;
            case 2:
                mastersVsReferenceAlgorithm(reference);
                break;
            case 3:
                tanabeAlgorithm(reference);
                break;
        }
    }

    private void mastersVsQueryAlgorithm(Haplotype reference) {
        int hits = hits(reference);

        double score = (double) hits / (double) this.query.sum() * 100;
        reference.setScore(score);
    }

    private void mastersVsReferenceAlgorithm(Haplotype reference) {
        int hits = hits(reference);

        double score = (double) hits / (double) reference.sum() * 100;
        reference.setScore(score);
    }

    private void tanabeAlgorithm(Haplotype reference) {
        int hits = hits(reference);

        double score = ((double) hits * 2) / ((double) this.query.sum() + (double) reference.sum()) * 100;
        reference.setScore(score);
    }

    private int hits(Haplotype reference) {
        int hits = 0;
        for (Marker marker : this.query.getMarkers()) {
            for (Marker other : reference.getMarkers()) {
                if (marker.getName().equals(other.getName())) {
                    hits += marker.matchAgainst(other);
                    break;
                }
            }
        }
        return hits;
    }
}
