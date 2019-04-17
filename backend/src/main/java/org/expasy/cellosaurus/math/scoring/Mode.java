package org.expasy.cellosaurus.math.scoring;

import org.expasy.cellosaurus.genomics.str.Profile;

/**
 * Interface regulating the mode that defines which STR markers are included into the score computation
 */
public interface Mode {

    /**
     * @param query             the profile submitted by the user to be searched
     * @param reference         the profile from the database to be searched against
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     * @return the number of allele hits between the two STR profiles
     */
    int computeHits(Profile query, Profile reference, boolean includeAmelogenin);
}
