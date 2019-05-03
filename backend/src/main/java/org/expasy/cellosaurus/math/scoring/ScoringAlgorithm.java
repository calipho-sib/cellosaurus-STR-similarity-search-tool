package org.expasy.cellosaurus.math.scoring;

import org.expasy.cellosaurus.genomics.str.Profile;

/**
 * Implementation of the {@code Algorithm} interface as an enum.
 */
public enum ScoringAlgorithm implements Algorithm {

    TANABE {
        /**
         * Implementation of the Tanabe algorithm, defined as the two times the number of shared alleles divided by
         * the sum of the number of query alleles and the number of reference alleles.
         *
         * {@inheritDoc}
         */
        @Override
        public double computeScore(Mode mode, Profile query, Profile reference, boolean includeAmelogenin) {
            int hits = mode.computeHits(query, reference, includeAmelogenin);

            return (double) hits * 2 / (query.getAlleleNumber() + reference.getAlleleNumber()) * 100;
        }
    },
    MASTERS_QUERY {
        /**
         * Implementation of the default version of the Masters algorithm, defined as the number of shared alleles
         * divided by the number of query alleles.
         *
         * {@inheritDoc}
         */
        @Override
        public double computeScore(Mode mode, Profile query, Profile reference, boolean includeAmelogenin) {
            int hits = mode.computeHits(query, reference, includeAmelogenin);

            return (double) hits / query.getAlleleNumber() * 100;
        }
    },
    MASTERS_REFERENCE {
        /**
         * Implementation of the alternate version of the Masters algorithm, defined as the number of shared alleles
         * divided by the number of reference alleles.
         *
         * {@inheritDoc}
         */
        @Override
        public double computeScore(Mode mode, Profile query, Profile reference, boolean includeAmelogenin) {
            int hits = mode.computeHits(query, reference, includeAmelogenin);

            return (double) hits / reference.getAlleleNumber() * 100;
        }
    };

    /**
     * @param i the index of the {@code ScoringAlgorithm} instance
     * @return the corresponding {@code ScoringAlgorithm} instance
     */
    public static ScoringAlgorithm get(int i) {
        return ScoringAlgorithm.values()[i];
    }
}
