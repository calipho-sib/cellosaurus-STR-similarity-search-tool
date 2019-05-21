package org.expasy.cellosaurus.math;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PermutationTest {

    @Test
    public void getValuesTest1() {
        List<Integer> integers = Arrays.asList(0, 0, 0, 0);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(0, 0, 0, 0));

        Permutation permutation = new Permutation(integers);
        assertEquals(expected, permutation.getValues());
    }

    @Test
    public void getValuesTest2() {
        List<Integer> integers = Arrays.asList(1, 1, 1, 1);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(0, 0, 0, 0));
        expected.add(Arrays.asList(0, 0, 0, 1));
        expected.add(Arrays.asList(0, 0, 1, 0));
        expected.add(Arrays.asList(0, 0, 1, 1));
        expected.add(Arrays.asList(0, 1, 0, 0));
        expected.add(Arrays.asList(0, 1, 0, 1));
        expected.add(Arrays.asList(0, 1, 1, 0));
        expected.add(Arrays.asList(0, 1, 1, 1));
        expected.add(Arrays.asList(1, 0, 0, 0));
        expected.add(Arrays.asList(1, 0, 0, 1));
        expected.add(Arrays.asList(1, 0, 1, 0));
        expected.add(Arrays.asList(1, 0, 1, 1));
        expected.add(Arrays.asList(1, 1, 0, 0));
        expected.add(Arrays.asList(1, 1, 0, 1));
        expected.add(Arrays.asList(1, 1, 1, 0));
        expected.add(Arrays.asList(1, 1, 1, 1));

        Permutation permutation = new Permutation(integers);
        assertEquals(expected, permutation.getValues());
    }

    @Test
    public void getValuesTest3() {
        List<Integer> integers = Arrays.asList(0, 0, 0, 1);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(0, 0, 0, 0));
        expected.add(Arrays.asList(0, 0, 0, 1));

        Permutation permutation = new Permutation(integers);
        assertEquals(expected, permutation.getValues());
    }

    @Test
    public void getValuesTest4() {
        List<Integer> integers = Arrays.asList(0, 2, 0, 1);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(0, 0, 0, 0));
        expected.add(Arrays.asList(0, 0, 0, 1));
        expected.add(Arrays.asList(0, 1, 0, 0));
        expected.add(Arrays.asList(0, 1, 0, 1));
        expected.add(Arrays.asList(0, 2, 0, 0));
        expected.add(Arrays.asList(0, 2, 0, 1));

        Permutation permutation = new Permutation(integers);
        assertEquals(expected, permutation.getValues());
    }

    @Test
    public void getValuesTest5() {
        List<Integer> integers = Arrays.asList(3, 1, 2, 0);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(0, 0, 0, 0));
        expected.add(Arrays.asList(0, 0, 1, 0));
        expected.add(Arrays.asList(0, 0, 2, 0));
        expected.add(Arrays.asList(0, 1, 0, 0));
        expected.add(Arrays.asList(0, 1, 1, 0));
        expected.add(Arrays.asList(0, 1, 2, 0));
        expected.add(Arrays.asList(1, 0, 0, 0));
        expected.add(Arrays.asList(1, 0, 1, 0));
        expected.add(Arrays.asList(1, 0, 2, 0));
        expected.add(Arrays.asList(1, 1, 0, 0));
        expected.add(Arrays.asList(1, 1, 1, 0));
        expected.add(Arrays.asList(1, 1, 2, 0));
        expected.add(Arrays.asList(2, 0, 0, 0));
        expected.add(Arrays.asList(2, 0, 1, 0));
        expected.add(Arrays.asList(2, 0, 2, 0));
        expected.add(Arrays.asList(2, 1, 0, 0));
        expected.add(Arrays.asList(2, 1, 1, 0));
        expected.add(Arrays.asList(2, 1, 2, 0));
        expected.add(Arrays.asList(3, 0, 0, 0));
        expected.add(Arrays.asList(3, 0, 1, 0));
        expected.add(Arrays.asList(3, 0, 2, 0));
        expected.add(Arrays.asList(3, 1, 0, 0));
        expected.add(Arrays.asList(3, 1, 1, 0));
        expected.add(Arrays.asList(3, 1, 2, 0));

        Permutation permutation = new Permutation(integers);
        assertEquals(expected, permutation.getValues());
    }
}
