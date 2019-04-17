package org.expasy.cellosaurus.math.scoring;

import org.expasy.cellosaurus.genomics.str.Profile;

/**
 * Interface regulating the algorithm that performs the score computation between two profiles.
 */
public interface Algorithm {

    /**
     * @param mode              the mode defining which STR markers are included into the score computation
     * @param query             the profile submitted by the user to be searched
     * @param reference         the profile from the database to be searched against
     * @param includeAmelogenin define if Amelogenin needs to be included into the score computation
     * @return the similarity score between the query profile and reference profile
     */
    double computeScore(Mode mode, Profile query, Profile reference, boolean includeAmelogenin);
}
