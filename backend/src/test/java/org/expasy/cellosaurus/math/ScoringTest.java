package org.expasy.cellosaurus.math;

import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.bio.str.Marker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoringTest {
    private Haplotype query1 = new Haplotype(
            new Marker("D5S818", "12", "13"),
            new Marker("D13S317", "11", "12"),
            new Marker("D7S820", "9", "13"),
            new Marker("D16S539", "10", "13"),
            new Marker("VWA", "17", "19"),
            new Marker("TH01", "6", "7"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8"),
            new Marker("FGA", "20"),
            new Marker("CSF1PO", "10", "11")
    );

    private Haplotype reference1 = new Haplotype(
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

    private Haplotype query2 = new Haplotype(
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

    private Haplotype reference2 = new Haplotype(
            new Marker("D5S818", "9"),
            new Marker("D13S317", "8", "11"),
            new Marker("D7S820", "8", "10", "11"),
            new Marker("D16S539", "11"),
            new Marker("VWA", "18", "19", "17"),
            new Marker("TH01", "6", "9.3"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8", "10"),
            new Marker("FGA", "20", "22"),
            new Marker("CSF1PO", "10", "11")
    );

    private Haplotype query3 = new Haplotype(
            new Marker("D5S818", "10", "11"),
            new Marker("D13S317", "8", "12"),
            new Marker("D7S820", "11", "12"),
            new Marker("D16S539", "12"),
            new Marker("VWA", "14"),
            new Marker("TH01", "7", "9"),
            new Marker("TPOX", "8"),
            new Marker("CSF1PO", "12")
    );

    private Haplotype reference3 = new Haplotype(
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

    private Haplotype reference4 = new Haplotype(
            new Marker("D5S818", "11"),
            new Marker("D13S317", "10", "11"),
            new Marker("D7S820", "10.3", "12"),
            new Marker("D16S539", "9", "11"),
            new Marker("VWA", "14", "17"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8", "9"),
            new Marker("LPL", "12"),
            new Marker("F13B", "11"),
            new Marker("CSF1PO", "10", "12")
    );

    private Haplotype query4 = new Haplotype(
            new Marker("D5S818", "12"),
            new Marker("D13S317", "10", "11"),
            new Marker("D7S820", "11", "12"),
            new Marker("D16S539", "11", "13"),
            new Marker("VWA", "14", "17"),
            new Marker("TH01", "6", "7"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8", "9"),
            new Marker("LPL", "12"),
            new Marker("CSF1PO", "10", "13")
    );

    @Test
    public void tanabeAlgorithmTest1() {
        Scoring scoring1 = new Scoring(1, 1, true);
        scoring1.computeScore(query1, reference1);
        assertEquals(87.5, reference1.getScore(), 0.1);

        Scoring scoring2 = new Scoring(1, 2, true);
        scoring2.computeScore(query1, reference1);
        assertEquals(84.8, reference1.getScore(), 0.1);

        Scoring scoring3 = new Scoring(1, 1, false);
        scoring3.computeScore(query1, reference1);
        assertEquals(86.6, reference1.getScore(), 0.1);

        Scoring scoring4 = new Scoring(1, 2, false);
        scoring4.computeScore(query1, reference1);
        assertEquals(83.9, reference1.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest2() {
        Scoring scoring1 = new Scoring(1, 1, true);
        scoring1.computeScore(query2, reference2);
        assertEquals(88.9, reference2.getScore(), 0.1);

        Scoring scoring2 = new Scoring(1, 2, true);
        scoring2.computeScore(query2, reference2);
        assertEquals(88.9, reference2.getScore(), 0.1);

        Scoring scoring3 = new Scoring(1, 1, false);
        scoring3.computeScore(query2, reference2);
        assertEquals(87.5, reference2.getScore(), 0.1);

        Scoring scoring4 = new Scoring(1, 2, false);
        scoring4.computeScore(query2, reference2);
        assertEquals(87.5, reference2.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest3() {
        Scoring scoring1 = new Scoring(1, 1, true);
        scoring1.computeScore(query3, reference3);
        assertEquals(78.3, reference3.getScore(), 0.1);

        Scoring scoring2 = new Scoring(1, 2, true);
        scoring2.computeScore(query3, reference3);
        assertEquals(78.3, reference3.getScore(), 0.1);

        Scoring scoring3 = new Scoring(1, 1, false);
        scoring3.computeScore(query3, reference3);
        assertEquals(78.3, reference3.getScore(), 0.1);

        Scoring scoring4 = new Scoring(1, 2, false);
        scoring4.computeScore(query3, reference3);
        assertEquals(78.3, reference3.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest4() {
        Scoring scoring1 = new Scoring(1, 1, true);
        scoring1.computeScore(query4, reference4);
        assertEquals(64.7, reference4.getScore(), 0.1);

        Scoring scoring2 = new Scoring(1, 2, true);
        scoring2.computeScore(query4, reference4);
        assertEquals(64.7, reference4.getScore(), 0.1);

        Scoring scoring3 = new Scoring(1, 1, false);
        scoring3.computeScore(query4, reference4);
        assertEquals(64.5, reference4.getScore(), 0.1);

        Scoring scoring4 = new Scoring(1, 2, false);
        scoring4.computeScore(query4, reference4);
        assertEquals(64.5, reference4.getScore(), 0.1);
    }

    @Test
    public void tanabeAlgorithmTest5() {
        Haplotype copy = new Haplotype(query1);

        Scoring scoring1 = new Scoring(1, 1, true);
        scoring1.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring2 = new Scoring(1, 2, true);
        scoring2.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring3 = new Scoring(1, 1, false);
        scoring3.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring4 = new Scoring(1, 2, false);
        scoring4.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmQueryTest1() {
        Scoring scoring1 = new Scoring(2, 1, true);
        scoring1.computeScore(query1, reference1);
        assertEquals(87.5, reference1.getScore(), 0.1);

        Scoring scoring2 = new Scoring(2, 2, true);
        scoring2.computeScore(query1, reference1);
        assertEquals(82.4, reference1.getScore(), 0.1);

        Scoring scoring3 = new Scoring(2, 1, false);
        scoring3.computeScore(query1, reference1);
        assertEquals(86.7, reference1.getScore(), 0.1);

        Scoring scoring4 = new Scoring(2, 2, false);
        scoring4.computeScore(query1, reference1);
        assertEquals(81.3, reference1.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmQueryTest2() {
        Scoring scoring1 = new Scoring(2, 1, true);
        scoring1.computeScore(query2, reference2);
        assertEquals(88.9, reference2.getScore(), 0.1);

        Scoring scoring2 = new Scoring(2, 2, true);
        scoring2.computeScore(query2, reference2);
        assertEquals(88.9, reference2.getScore(), 0.1);

        Scoring scoring3 = new Scoring(2, 1, false);
        scoring3.computeScore(query2, reference2);
        assertEquals(87.5, reference2.getScore(), 0.1);

        Scoring scoring4 = new Scoring(2, 2, false);
        scoring4.computeScore(query2, reference2);
        assertEquals(87.5, reference2.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmQueryTest3() {
        Scoring scoring1 = new Scoring(2, 1, true);
        scoring1.computeScore(query3, reference3);
        assertEquals(75.0, reference3.getScore(), 0.1);

        Scoring scoring2 = new Scoring(2, 2, true);
        scoring2.computeScore(query3, reference3);
        assertEquals(75.0, reference3.getScore(), 0.1);

        Scoring scoring3 = new Scoring(2, 1, false);
        scoring3.computeScore(query3, reference3);
        assertEquals(75.0, reference3.getScore(), 0.1);

        Scoring scoring4 = new Scoring(2, 2, false);
        scoring4.computeScore(query3, reference3);
        assertEquals(75.0, reference3.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmQueryTest4() {
        Scoring scoring1 = new Scoring(2, 1, true);
        scoring1.computeScore(query4, reference4);
        assertEquals(64.7, reference4.getScore(), 0.1);

        Scoring scoring2 = new Scoring(2, 2, true);
        scoring2.computeScore(query4, reference4);
        assertEquals(64.7, reference4.getScore(), 0.1);

        Scoring scoring3 = new Scoring(2, 1, false);
        scoring3.computeScore(query4, reference4);
        assertEquals(62.5, reference4.getScore(), 0.1);

        Scoring scoring4 = new Scoring(2, 2, false);
        scoring4.computeScore(query4, reference4);
        assertEquals(62.5, reference4.getScore(), 0.1);
    }
    
    @Test
    public void mastersAlgorithmQueryTest5() {
        Haplotype copy = new Haplotype(query1);

        Scoring scoring1 = new Scoring(2, 1, true);
        scoring1.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring2 = new Scoring(2, 2, true);
        scoring2.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring3 = new Scoring(2, 1, false);
        scoring3.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring4 = new Scoring(2, 2, false);
        scoring4.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmReferenceTest1() {
        Scoring scoring1 = new Scoring(3, 1, true);
        scoring1.computeScore(query1, reference1);
        assertEquals(87.5, reference1.getScore(), 0.1);

        Scoring scoring2 = new Scoring(3, 2, true);
        scoring2.computeScore(query1, reference1);
        assertEquals(87.5, reference1.getScore(), 0.1);

        Scoring scoring3 = new Scoring(3, 1, false);
        scoring3.computeScore(query1, reference1);
        assertEquals(86.7, reference1.getScore(), 0.1);

        Scoring scoring4 = new Scoring(3, 2, false);
        scoring4.computeScore(query1, reference1);
        assertEquals(86.7, reference1.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmReferenceTest2() {
        Scoring scoring1 = new Scoring(3, 1, true);
        scoring1.computeScore(query2, reference2);
        assertEquals(88.9, reference2.getScore(), 0.1);

        Scoring scoring2 = new Scoring(3, 2, true);
        scoring2.computeScore(query2, reference2);
        assertEquals(88.9, reference2.getScore(), 0.1);

        Scoring scoring3 = new Scoring(3, 1, false);
        scoring3.computeScore(query2, reference2);
        assertEquals(87.5, reference2.getScore(), 0.1);

        Scoring scoring4 = new Scoring(3, 2, false);
        scoring4.computeScore(query2, reference2);
        assertEquals(87.5, reference2.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmReferenceTest3() {
        Scoring scoring1 = new Scoring(3, 1, true);
        scoring1.computeScore(query3, reference3);
        assertEquals(81.8, reference3.getScore(), 0.1);

        Scoring scoring2 = new Scoring(3, 2, true);
        scoring2.computeScore(query3, reference3);
        assertEquals(81.8, reference3.getScore(), 0.1);

        Scoring scoring3 = new Scoring(3, 1, false);
        scoring3.computeScore(query3, reference3);
        assertEquals(81.8, reference3.getScore(), 0.1);

        Scoring scoring4 = new Scoring(3, 2, false);
        scoring4.computeScore(query3, reference3);
        assertEquals(81.8, reference3.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmReferenceTest4() {
        Scoring scoring1 = new Scoring(3, 1, true);
        scoring1.computeScore(query4, reference4);
        assertEquals(64.7, reference4.getScore(), 0.1);

        Scoring scoring2 = new Scoring(3, 2, true);
        scoring2.computeScore(query4, reference4);
        assertEquals(64.7, reference4.getScore(), 0.1);

        Scoring scoring3 = new Scoring(3, 1, false);
        scoring3.computeScore(query4, reference4);
        assertEquals(66.7, reference4.getScore(), 0.1);

        Scoring scoring4 = new Scoring(3, 2, false);
        scoring4.computeScore(query4, reference4);
        assertEquals(66.7, reference4.getScore(), 0.1);
    }

    @Test
    public void mastersAlgorithmReferenceTest5() {
        Haplotype copy = new Haplotype(query1);

        Scoring scoring1 = new Scoring(3, 1, true);
        scoring1.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring2 = new Scoring(3, 2, true);
        scoring2.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring3 = new Scoring(3, 1, false);
        scoring3.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);

        Scoring scoring4 = new Scoring(3, 2, false);
        scoring4.computeScore(query1, copy);
        assertEquals(100.0, copy.getScore(), 0.1);
    }
}
