package org.expasy.cellosaurus.genomics.str.utils;

import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.expasy.cellosaurus.math.Permutation;

import java.util.*;

/**
 * Class in charge of resolving the profiles with conflicted STR markers. If possible the conflicts will be resolved by
 * grouping the STR markers with common sources. If the data does not allows it, a brute-force permutation approach is
 * used.
 */
public class ConflictResolver {
    private static final int MAX_PERMUTATIONS = 150;

    private final List<List<Marker>> markersList = new ArrayList<>();

    /**
     * Determines if the list of STR markers contains conflicted elements and if it can be resolved. Then, the conflicts
     * are resolved following the appropriate approach and the corresponding profile generated.
     *
     * @return the list of resolved profile
     */
    public List<Profile> resolve() {
        boolean conflicted = false;
        boolean resolvable = true;

        Set<String> referenceSources = new HashSet<>();
        Set<String> sources = new HashSet<>();

        for (List<Marker> markers : this.markersList) {
            if (markers.size() > 1) {
                conflicted = true;

                for (Marker marker : markers) {
                    sources.addAll(marker.getSources());
                }
                if (referenceSources.isEmpty()) {
                    referenceSources.addAll(sources);
                }
                if (!referenceSources.equals(sources)) {
                    resolvable = false;
                }
                sources.clear();
            }
        }

        if (!conflicted) {
            Profile profile = new Profile();
            for (List<Marker> markers : this.markersList) {
                profile.getMarkers().add(markers.get(0));
            }
            profile.sort();

            List<Profile> profiles = new ArrayList<>();
            profiles.add(profile);

            return profiles;
        } else if (resolvable) {
            return resolveByGroups();
        } else {
            return resolveByPermutations();
        }
    }

    /**
     * Resolve the conflicts by grouping the conflicted STR markers with common sources. This is the preferred approach
     * and should be used all the time if possible.
     *
     * @return the list of resolved profile
     */
    private List<Profile> resolveByGroups() {
        List<Profile> profiles = new ArrayList<>();

        List<Set<String>> groups = computeGroups();
        for (Set<String> group : groups) {
            Profile profile = new Profile();

            for (List<Marker> markers : this.markersList) {
                if (markers.size() > 1) {
                    for (Marker marker : markers) {
                        if (marker.getSources().containsAll(group)) {
                            profile.getMarkers().add(marker);
                        }
                    }
                } else {
                    profile.getMarkers().add(markers.get(0));
                }
            }
            profile.sort();
            profiles.add(profile);
        }
        return profiles;
    }

    /**
     * Resolve the conflicts by making all the possible permutations of conflicted STR markers. This is a less preferred
     * approach as it makes permutations that do not always respect the sources and can produce a high amount of
     * possibilities. The {@code MAX_PERMUTATIONS} constant regulates the maximum number of permutations as their number
     * can slow down the search due to a few extreme cases.
     *
     * @return the List of resolved profiles
     */
    private List<Profile> resolveByPermutations() {
        List<Profile> profiles = new ArrayList<>();

        List<Integer> sizes = new ArrayList<>();
        for (List<Marker> markers : this.markersList) {
            sizes.add(markers.size() - 1);
        }
        Permutation permutation = new Permutation(sizes);
        List<List<Integer>> permutations = permutation.getValues();
        if (permutations.size() > ConflictResolver.MAX_PERMUTATIONS) {
            permutations = permutations.subList(0, ConflictResolver.MAX_PERMUTATIONS);
        }

        for (List<Integer> choice : permutations) {
            Profile profile = new Profile();

            for (int i = 0; i < choice.size(); i++) {
                profile.getMarkers().add(this.markersList.get(i).get(choice.get(i)));
            }
            profile.sort();
            profiles.add(profile);
        }
        return profiles;
    }

    /**
     * Define the different groups of sources based on the conflicted STR markers.
     *
     * @return a list of grouped sources
     */
    private List<Set<String>> computeGroups() {
        List<Set<String>> groups = new ArrayList<>();

        for (List<Marker> markers : this.markersList) {
            if (markers.size() > 1) {
                if (groups.isEmpty()) {
                    for (Marker marker : markers) {
                        groups.add(marker.getSources());
                    }
                } else {
                    List<Set<String>> newGroups = new ArrayList<>(groups);

                    for (Set<String> sources : groups) {
                        List<Set<String>> matches = new ArrayList<>();

                        for (Marker marker : markers) {
                            if (!sources.equals(marker.getSources()) && sources.containsAll(marker.getSources())) {
                                matches.add(marker.getSources());
                            }
                        }
                        if (!matches.isEmpty()) {
                            Set<String> delta = new LinkedHashSet<>(sources);

                            for (Set<String> match : matches) {
                                delta.removeAll(match);
                            }
                            if (!delta.isEmpty()) {
                                newGroups.add(delta);
                            }
                            newGroups.addAll(matches);
                            newGroups.remove(sources);
                        }
                    }
                    groups.clear();
                    groups.addAll(newGroups);
                }
            }
        }
        return groups;
    }

    /**
     * @return if the list of markers is empty
     */
    public boolean isEmpty() {
        return this.markersList.isEmpty();
    }

    public void addMarkers(List<Marker> markers) {
        this.markersList.add(markers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConflictResolver that = (ConflictResolver) o;
        return markersList.equals(that.markersList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(markersList);
    }

    @Override
    public String toString() {
        return this.markersList.toString();
    }
}
