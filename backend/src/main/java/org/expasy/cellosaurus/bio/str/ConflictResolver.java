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
            Haplotype haplotype = new Haplotype();
            for (List<Marker> markers : this.markersList) {
                haplotype.addMarker(markers.get(0));
            }
            haplotype.sort();
            this.haplotypes.add(haplotype);
        } else if (resolvable) {
            List<Set<String>> haplogroups = new ArrayList<>();

            for (List<Marker> markers : this.markersList) {
                if (markers.size() > 1) {
                    for (Marker marker : markers) {
                        Set<String> sourcess = marker.getSources();
                        if (!haplogroups.contains(sourcess)) {
                            haplogroups.add(sourcess);
                        }
                    }

                }
            }
            Set<Set<String>> curated = new LinkedHashSet<>();
            for (int i = 0; i < haplogroups.size(); i++) {
                for (int j = 0; j < haplogroups.size(); j++) {
                    if (i != j) {
                        Set<String> small;
                        Set<String> big;

                        if (haplogroups.get(i).size() >= haplogroups.get(j).size()) {
                            small = haplogroups.get(j);
                            big = haplogroups.get(i);
                        } else {
                            small = haplogroups.get(i);
                            big = haplogroups.get(j);
                        }
                        if (big.containsAll(small)) {
                            Set<String> diff = new LinkedHashSet<>(big);
                            diff.removeAll(small);
                            curated.add(diff);
                            curated.add(small);
                        }
                    }
                }
            }
            if (curated.isEmpty()) {
                curated.addAll(haplogroups);
            }
            for (Set<String> cur : curated) {
                Haplotype haplotype = new Haplotype();

                for (List<Marker> markers : this.markersList) {
                    if (markers.size() > 1) {
                        for (Marker marker : markers) {
                            if (marker.getSources().containsAll(cur)) {
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
        } else {
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

    public List<List<Marker>> getMarkersList() {
        return markersList;
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
