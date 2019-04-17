package org.expasy.cellosaurus.math.scoring;

import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;

/**
 * Implementation of the {@code Mode} interface as an enum}.
 */
public enum ScoringMode implements Mode {

    NON_EMPTY {
        /**
         * Implementation of the non-empty markers mode, in which only the STR markers that have allele data for both
         * the query profile and reference profile are included in the score computation.
         *
         * {@inheritDoc}
         */
        @Override
        public int computeHits(Profile query, Profile reference, boolean includeAmelogenin) {
            int hits = 0, querySize = 0, referenceSize = 0, markerNumber = 0;

            for (Marker queryMarker : query.getMarkers()) {
                int idx = reference.getMarkers().indexOf(queryMarker);
                if (idx > -1) {
                    Marker referenceMarker = reference.getMarkers().get(idx);

                    if (needsInclusion(queryMarker, includeAmelogenin)) {
                        hits += queryMarker.matchAgainst(referenceMarker);
                        querySize += queryMarker.countAlleles();
                        referenceSize += referenceMarker.countAlleles();
                        markerNumber++;
                    } else {
                        queryMarker.matchAgainst(referenceMarker);
                    }
                }
            }
            query.setSize(querySize);
            reference.setSize(referenceSize);
            reference.setMarkerNumber(markerNumber);

            return hits;
        }
    },
    QUERY {
        /**
         * Implementation of the query markers mode, in which all the STR markers that have allele data for the query
         * profile are included in the score computation.
         *
         * {@inheritDoc}
         */
        @Override
        public int computeHits(Profile query, Profile reference, boolean includeAmelogenin) {
            int[] results = relativeHits(query, reference, includeAmelogenin);

            query.setSize(results[0]);
            reference.setSize(results[1]);
            reference.setMarkerNumber(results[2]);

            return results[3];
        }
    },
    REFERENCE {
        /**
         * Implementation of the query markers mode, in which all the STR markers that have allele data for the
         * reference profile are included in the score computation.
         *
         * {@inheritDoc}
         */
        @Override
        public int computeHits(Profile query, Profile reference, boolean includeAmelogenin) {
            int[] results = relativeHits(reference, query, includeAmelogenin);

            query.setSize(results[1]);
            reference.setSize(results[0]);
            reference.setMarkerNumber(results[2]);

            return results[3];
        }
    };

    /**
     * Common method for the query markers and reference markers modes, as the two mode are the reverse of each other
     * and share the same algorithm.
     *
     * @param primary           the primary profile that determine the selected STR markers
     * @param secondary         the secondary profile
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     * @return                  an array of int values representing the sizes, marker number and hits
     */
    private static int[] relativeHits(Profile primary, Profile secondary, boolean includeAmelogenin) {
        int hits = 0, primarySize = 0, secondarySize = 0, markerNumber = 0;

        for (Marker primaryMarker : primary.getMarkers()) {
            if (needsInclusion(primaryMarker, includeAmelogenin)) {
                primarySize += primaryMarker.countAlleles();
                markerNumber++;
            }

            int idx = secondary.getMarkers().indexOf(primaryMarker);
            if (idx > -1) {
                Marker secondaryMarker = secondary.getMarkers().get(idx);

                if (needsInclusion(secondaryMarker, includeAmelogenin)) {
                    hits += secondaryMarker.matchAgainst(primaryMarker);
                    secondarySize += secondaryMarker.countAlleles();
                } else {
                    secondaryMarker.matchAgainst(primaryMarker);
                }
            }
        }
        int[] results = new int[4];
        results[0] = primarySize;
        results[1] = secondarySize;
        results[2] = markerNumber;
        results[3] = hits;

        return results;
    }

    /**
     * Filters the Amelogenin marker if the {@code includeAmelogenin} parameter is false
     *
     * @param marker            a STR marker
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     * @return                  if the marker needs to be included
     */
    private static boolean needsInclusion(Marker marker, boolean includeAmelogenin) {
        return includeAmelogenin || !marker.getName().equals("Amelogenin");
    }

    /**
     * @param i the index of the {@code ScoringMode} instance
     * @return  the corresponding {@code ScoringMode} instance
     */
    public static ScoringMode get(int i) {
        return ScoringMode.values()[i];
    }
}
