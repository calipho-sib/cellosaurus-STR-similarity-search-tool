package org.expasy.cellosaurus.bio.str;

import org.expasy.cellosaurus.math.Permutation;

import java.util.*;

public class ConflictResolver {
    private List<Haplotype> haplotypes = new ArrayList<>();
    private List<List<Marker>> markersList = new ArrayList<>();

    public void resolve() {
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
            addHaplotype();
        } else if (resolvable) {
            resolveByGroups();
        } else {
            resolveByPermutations();
        }
    }

    private void addHaplotype() {
        Haplotype haplotype = new Haplotype();
        for (List<Marker> markers : this.markersList) {
            haplotype.addMarker(markers.get(0));
        }
        haplotype.sort();
        this.haplotypes.add(haplotype);
    }

    private void resolveByGroups() {
        List<Set<String>> groups = computeGroups();

        for (Set<String> group : groups) {
            Haplotype haplotype = new Haplotype();

            for (List<Marker> markers : this.markersList) {
                if (markers.size() > 1) {
                    for (Marker marker : markers) {
                        if (marker.getSources().containsAll(group)) {
                            haplotype.addMarker(marker);
                        }
                    }
                } else {
                    haplotype.addMarker(markers.get(0));
                }
            }
            haplotype.sort();
            this.haplotypes.add(haplotype);
        }
    }

    private void resolveByPermutations() {
        List<Integer> sizes = new ArrayList<>();
        for (List<Marker> markers : this.markersList) {
            sizes.add(markers.size() - 1);
        }
        Permutation permutation = new Permutation(sizes);
        List<List<Integer>> permutations = permutation.getValues();
        if (permutations.size() > 150) permutations = permutations.subList(0, 150);

        for (List<Integer> choice : permutations) {
            Haplotype haplotype = new Haplotype();

            for (int i = 0; i < choice.size(); i++) {
                haplotype.addMarker(this.markersList.get(i).get(choice.get(i)));
            }
            haplotype.sort();
            this.haplotypes.add(haplotype);
        }
    }

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

    public int size() {
        return this.markersList.size();
    }

    public List<Haplotype> getHaplotypes() {
        return haplotypes;
    }

    public void addHaplotypes(Haplotype haplotype) {
        this.haplotypes.add(haplotype);
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
