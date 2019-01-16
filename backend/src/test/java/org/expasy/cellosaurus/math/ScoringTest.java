package org.expasy.cellosaurus.math;

import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoringTest {
    private Haplotype referenceCcrfCem = new Haplotype(
            new Marker("D5S818", "12", "13"),
            new Marker("D13S317", "11"),
            new Marker("D7S820", "9", "13"),
            new Marker("D16S539", "10", "13"),
            new Marker("VWA", "17", "19"),
            new Marker("TH01", "6", "7"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "7", "8"),
            new Marker("CSF1PO", "10", "13")
    );

    private Haplotype queryCcrfCem = new Haplotype(
            new Marker("D5S818", "12", "13"),
            new Marker("D13S317", "11", "12"),
            new Marker("D7S820", "9", "13"),
            new Marker("D16S539", "10", "13"),
            new Marker("VWA", "17", "19"),
            new Marker("TH01", "6", "7"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8"),
            new Marker("CSF1PO", "10", "11")
    );

    private Haplotype referenceJurkat = new Haplotype(
            new Marker("D5S818", "9"),
            new Marker("D13S317", "8", "11"),
            new Marker("D7S820", "8", "10", "11"),
            new Marker("D16S539", "11"),
            new Marker("VWA", "18", "19", "17"),
            new Marker("TH01", "6", "9.3"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8", "10"),
            new Marker("CSF1PO", "10", "11")
    );

    private Haplotype queryJurkat = new Haplotype(
            new Marker("D5S818", "9"),
            new Marker("D13S317", "8", "11", "12"),
            new Marker("D7S820", "8", "10", "11"),
            new Marker("D16S539", "11"),
            new Marker("VWA", "16", "17", "18"),
            new Marker("TH01", "6", "9.3"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8", "10"),
            new Marker("CSF1PO", "11")
    );

    private Haplotype referenceKcl22 = new Haplotype(
            new Marker("D5S818", "10", "12"),
            new Marker("D13S317", "8", "11"),
            new Marker("D7S820", "12"),
            new Marker("D16S539", "12"),
            new Marker("VWA", "14"),
            new Marker("TH01", "7", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8"),
            new Marker("CSF1PO", "12")
    );

    private Haplotype queryKcl22 = new Haplotype(
            new Marker("D5S818", "10", "11"),
            new Marker("D13S317", "8", "12"),
            new Marker("D7S820", "11", "12"),
            new Marker("D16S539", "12"),
            new Marker("VWA", "14"),
            new Marker("TH01", "7", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8"),
            new Marker("CSF1PO", "12")
    );

    private Haplotype queryMt3 = new Haplotype(
            new Marker("D5S818", "12"),
            new Marker("D13S317", "10", "11"),
            new Marker("D7S820", "11", "12"),
            new Marker("D16S539", "11", "13"),
            new Marker("VWA", "14", "17"),
            new Marker("TH01", "6", "7"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8", "9"),
            new Marker("CSF1PO", "10", "13")
    );

    private Haplotype referenceMt3 = new Haplotype(
            new Marker("D5S818", "11"),
            new Marker("D13S317", "10", "11"),
            new Marker("D7S820", "10.3", "12"),
            new Marker("D16S539", "9", "11"),
            new Marker("VWA", "14", "17"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8", "9"),
            new Marker("CSF1PO", "10", "12")
    );

    @Test
    public void mastersAlgorithmTest1() {
        Scoring scoring = new Scoring(queryCcrfCem, 1);
        scoring.computeScore(referenceCcrfCem);
        assertEquals(87.5, referenceCcrfCem.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmTest2() {
        Scoring scoring = new Scoring(queryJurkat, 1);
        scoring.computeScore(referenceJurkat);
        assertEquals(88.9, referenceJurkat.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmTest3() {
        Scoring scoring = new Scoring(queryKcl22, 1);
        scoring.computeScore(referenceKcl22);
        assertEquals(76.9, referenceKcl22.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmTest4() {
        Scoring scoring = new Scoring(queryMt3, 1);
        scoring.computeScore(referenceMt3);
        assertEquals(62.5, referenceMt3.getScore(), 0.1);
    }
    
    @Test
    public void mastersAlgorithmTest5() {
        Scoring scoring = new Scoring(queryCcrfCem, 1);
        Haplotype copy = new Haplotype(queryCcrfCem);
        scoring.computeScore(copy);
        assertEquals(100.0, copy.getScore(), 0.1);
    }

    @Test
    public void reverseAlgorithmTest1() {
        Scoring scoring = new Scoring(queryCcrfCem, 2);
        scoring.computeScore(referenceCcrfCem);
        assertEquals(87.5, referenceCcrfCem.getScore(), 0.1);
    }

    @Test
    public void reverseAlgorithmTest2() {
        Scoring scoring = new Scoring(queryJurkat, 2);
        scoring.computeScore(referenceJurkat);
        assertEquals(88.9, referenceJurkat.getScore(), 0.1);
    }

    @Test
    public void reverseAlgorithmTest3() {
        Scoring scoring = new Scoring(queryKcl22, 2);
        scoring.computeScore(referenceKcl22);
        assertEquals(83.3, referenceKcl22.getScore(), 0.1);
    }

    @Test
    public void reverseAlgorithmTest4() {
        Scoring scoring = new Scoring(queryMt3, 2);
        scoring.computeScore(referenceMt3);
        assertEquals(62.5, referenceMt3.getScore(), 0.1);
    }

    @Test
    public void reverseAlgorithmTest5() {
        Scoring scoring = new Scoring(queryCcrfCem, 2);
        Haplotype copy = new Haplotype(queryCcrfCem);
        scoring.computeScore(copy);
        assertEquals(100.0, copy.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest1() {
        Scoring scoring = new Scoring(queryCcrfCem, 3);
        scoring.computeScore(referenceCcrfCem);
        assertEquals(87.5, referenceCcrfCem.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest2() {
        Scoring scoring = new Scoring(queryJurkat, 3);
        scoring.computeScore(referenceJurkat);
        assertEquals(88.9, referenceJurkat.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest3() {
        Scoring scoring = new Scoring(queryKcl22, 3);
        scoring.computeScore(referenceKcl22);
        assertEquals(80, referenceKcl22.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest4() {
        Scoring scoring = new Scoring(queryMt3, 3);
        scoring.computeScore(referenceMt3);
        assertEquals(62.5, referenceMt3.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest5() {
        Scoring scoring = new Scoring(queryCcrfCem, 3);
        Haplotype copy = new Haplotype(queryCcrfCem);
        scoring.computeScore(copy);
        assertEquals(100.0, copy.getScore(), 0.1);
    }
}
