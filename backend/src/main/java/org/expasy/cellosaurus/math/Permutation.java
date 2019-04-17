package org.expasy.cellosaurus.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Class generating permutations from a list of integers.
 */
public class Permutation {
    private final List<List<Integer>> values;

    /**
     * Main constructor
     *
     * @param integers a list of integers representing ranges
     */
    public Permutation(List<Integer> integers) {
        this.values = compute(integers, new ArrayList<>());
    }

    /**
     * @param integers  a list of integer representing ranges
     * @param recursive the list of integer permutations getting recursively built
     * @return          the list of generated permutations
     */
    private List<List<Integer>> compute(List<Integer> integers, List<Integer> recursive) {
        List<List<Integer>> permutationsList = new ArrayList<>();

        if (recursive.size() == integers.size()) {
            permutationsList.add(recursive);

            return permutationsList;
        }

        List<Integer> permutations;
        for (int i = 0; i <= integers.get(recursive.size()); i++) {
            permutations = new ArrayList<>(recursive);
            permutations.add(i);
            permutationsList.addAll(compute(integers, permutations));
        }
        return permutationsList;
    }

    /**
     * @return the list of generated permutations
     */
    public List<List<Integer>> getValues() {
        return values;
    }
}
