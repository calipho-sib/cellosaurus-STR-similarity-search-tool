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
            query.setAlleleNumber(querySize);
            reference.setAlleleNumber(referenceSize);
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
            return relativeHits(query, reference, includeAmelogenin, false);
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
            return relativeHits(query, reference, includeAmelogenin, true);
        }
    };

    /**
     * Common method for the query markers and reference markers modes, as the two mode are the reverse of each other
     * and share the same algorithm.
     *
     * @param query             the profile submitted by the user to be searched
     * @param reference         the profile from the database to be searched against
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     * @param reversed          define if the reference is the primary profile
     * @return the number of allele hits between the two STR profiles
     */
    private static int relativeHits(Profile query, Profile reference, boolean includeAmelogenin, boolean reversed) {
        int hits = 0, primarySize = 0, secondarySize = 0, markerNumber = 0;

        Profile primary;
        Profile secondary;
        if (reversed) {
            primary = reference;
            secondary = query;
        } else {
            primary = query;
            secondary = reference;
        }

        for (Marker primaryMarker : primary.getMarkers()) {
            if (needsInclusion(primaryMarker, includeAmelogenin)) {
                primarySize += primaryMarker.countAlleles();
                markerNumber++;
            }

            int idx = secondary.getMarkers().indexOf(primaryMarker);
            if (idx > -1) {
                Marker secondaryMarker = secondary.getMarkers().get(idx);

                if (needsInclusion(secondaryMarker, includeAmelogenin)) {
                    if (reversed) {
                        hits += secondaryMarker.matchAgainst(primaryMarker);
                    } else {
                        hits += primaryMarker.matchAgainst(secondaryMarker);
                    }
                    secondarySize += secondaryMarker.countAlleles();
                } else {
                    if (reversed) {
                        secondaryMarker.matchAgainst(primaryMarker);
                    } else {
                        primaryMarker.matchAgainst(secondaryMarker);
                    }
                }
            }
        }
        if (reversed) {
            query.setAlleleNumber(secondarySize);
            reference.setAlleleNumber(primarySize);
            reference.setMarkerNumber(markerNumber);
        } else {
            query.setAlleleNumber(primarySize);
            reference.setAlleleNumber(secondarySize);
            reference.setMarkerNumber(markerNumber);
        }
        return hits;
    }

    /**
     * Filters the Amelogenin marker if the {@code includeAmelogenin} parameter is false
     *
     * @param marker            a STR marker
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     * @return if the marker needs to be included
     */
    private static boolean needsInclusion(Marker marker, boolean includeAmelogenin) {
        return includeAmelogenin || !marker.getName().equals("Amelogenin");
    }

    /**
     * @param i the index of the {@code ScoringMode} instance
     * @return the corresponding {@code ScoringMode} instance
     */
    public static ScoringMode get(int i) {
        return ScoringMode.values()[i];
    }
}
