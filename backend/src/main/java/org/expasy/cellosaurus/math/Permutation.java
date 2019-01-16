package org.expasy.cellosaurus.math;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
    private final List<Integer> integers;
    private final List<List<Integer>> values;

    public Permutation(List<Integer> integers) {
        this.integers = integers;
        this.values = compute(new ArrayList<>());
    }

    private List<List<Integer>> compute(List<Integer> recursive) {
        List<List<Integer>> permutationsList = new ArrayList<>();

        if (recursive.size() == this.integers.size()) {
            permutationsList.add(recursive);

            return permutationsList;
        }

        List<Integer> permutations;
        for (int i = 0; i <= integers.get(recursive.size()); i++) {
            permutations = new ArrayList<>(recursive);
            permutations.add(i);
            permutationsList.addAll(compute(permutations));
        }
        return permutationsList;
    }

    public List<List<Integer>> getValues() {
        return values;
    }
}