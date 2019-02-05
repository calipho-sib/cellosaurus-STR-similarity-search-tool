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

    private void computeHits(Haplotype query, Haplotype reference) {
        this.hits = 0;
        this.querySize = 0;
        this.referenceSize = 0;

        int number = 0;

        for (Marker marker1 : query.getMarkers()) {
            int idx = reference.getMarkers().indexOf(marker1);
            boolean include = this.includeAmelogenin || !marker1.getName().equals("Amelogenin");

            if (mode == 2 && include) {
                number++;
                this.querySize += marker1.size();
            }
            if (idx > -1) {
                Marker marker2 = reference.getMarkers().get(idx);

                if (include) {
                    this.hits += marker1.matchAgainst(marker2);
                    if (mode == 1) {
                        number++;
                        this.querySize += marker1.size();
                    }
                    this.referenceSize += marker2.size();
                } else {
                    marker1.matchAgainst(marker2);
                }
            }
        }
        reference.setNumber(number);
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
}
