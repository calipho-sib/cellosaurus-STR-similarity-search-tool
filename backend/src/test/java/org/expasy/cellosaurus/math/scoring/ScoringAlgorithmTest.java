package org.expasy.cellosaurus.math.scoring;

import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoringAlgorithmTest {

    private Profile reference1 = new Profile(
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

    private Profile query1 = new Profile(
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
    Profile copy = new Profile(query1);
    private Profile reference2 = new Profile(
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
    private Profile query2 = new Profile(
            new Marker("D5S818", "9"),
            new Marker("D13S317", "8", "12"),
            new Marker("D7S820", "8", "12"),
            new Marker("D16S539", "11"),
            new Marker("VWA", "18", "19"),
            new Marker("TH01", "6", "9.3"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8", "10"),
            new Marker("CSF1PO", "11", "12")
    );
    private Profile reference3 = new Profile(
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
    private Profile query3 = new Profile(
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
    private Profile reference4 = new Profile(
            new Marker("D5S818", "11"),
            new Marker("D13S317", "9"),
            new Marker("D7S820", "11", "12"),
            new Marker("D16S539", "10", "12"),
            new Marker("VWA", "16", "18"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "9"),
            new Marker("CSF1PO", "10")
    );
    private Profile query4 = new Profile(
            new Marker("D5S818", "11"),
            new Marker("D13S317", "9"),
            new Marker("D7S820", "11", "11.3"),
            new Marker("D16S539", "10", "12"),
            new Marker("VWA", "16", "18", "19"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8", "9"),
            new Marker("CSF1PO", "10")
    );
    private Profile reference5 = new Profile(
            new Marker("D5S818", "12", "13"),
            new Marker("D13S317", "12", "13"),
            new Marker("D7S820", "8", "10", "7"),
            new Marker("D16S539", "11", "14"),
            new Marker("VWA", "17", "18"),
            new Marker("TH01", "6", "8"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8"),
            new Marker("CSF1PO", "11", "12")
    );
    private Profile query5 = new Profile(
            new Marker("D5S818", "11", "12", "13"),
            new Marker("D13S317", "12", "13"),
            new Marker("D7S820", "7", "8", "10"),
            new Marker("D16S539", "11", "12", "13"),
            new Marker("VWA", "17", "18", "19"),
            new Marker("TH01", "6", "8"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8"),
            new Marker("CSF1PO", "11", "12")
    );
    private Profile reference6 = new Profile(
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
    private Profile query6 = new Profile(
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
    private Profile reference7 = new Profile(
            new Marker("D5S818", "11", "12", "10"),
            new Marker("D13S317", "9", "12", "8"),
            new Marker("D7S820", "8", "10", "9"),
            new Marker("D16S539", "10", "11", "9"),
            new Marker("VWA", "15", "16"),
            new Marker("TH01", "8", "9"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8", "10"),
            new Marker("CSF1PO", "12")
    );
    private Profile query7 = new Profile(
            new Marker("D5S818", "11", "12"),
            new Marker("D13S317", "9", "13"),
            new Marker("D7S820", "8", "11"),
            new Marker("D16S539", "10", "11"),
            new Marker("VWA", "15", "16"),
            new Marker("TH01", "8", "9"),
            new Marker("Amelogenin", "X", "Y"),
            new Marker("TPOX", "8", "10"),
            new Marker("CSF1PO", "13")
    );
    private Profile reference8 = new Profile(
            new Marker("D5S818", "6", "12"),
            new Marker("D13S317", "9", "12"),
            new Marker("D7S820", "9", "13"),
            new Marker("D16S539", "12", "13"),
            new Marker("VWA", "15", "16", "17"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "11"),
            new Marker("CSF1PO", "11", "14")
    );
    private Profile query8 = new Profile(
            new Marker("D5S818", "6", "12", "13"),
            new Marker("D13S317", "8", "9", "11"),
            new Marker("D7S820", "8", "9", "12"),
            new Marker("D16S539", "12", "13", "14"),
            new Marker("VWA", "15", "16", "17"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "11"),
            new Marker("CSF1PO", "11", "13")
    );
    private Profile reference9 = new Profile(
            new Marker("D5S818", "12"),
            new Marker("D13S317", "10", "12"),
            new Marker("D7S820", "9", "11"),
            new Marker("D16S539", "12"),
            new Marker("VWA", "14", "15"),
            new Marker("TH01", "6", "10"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8", "11"),
            new Marker("CSF1PO", "12")
    );
    private Profile query9 = new Profile(
            new Marker("D5S818", "12"),
            new Marker("D13S317", "10", "12"),
            new Marker("D7S820", "9", "11"),
            new Marker("D16S539", "12"),
            new Marker("VWA", "15"),
            new Marker("TH01", "9.3"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8", "11"),
            new Marker("CSF1PO", "12")
    );

    @Test
    public void tanabeTest1() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query1, reference1, true);
        assertEquals(87.5, score, 0.1);
    }

    @Test
    public void mastersQueryTest1() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query1, reference1, true);
        assertEquals(87.5, score, 0.1);
    }

    @Test
    public void mastersReferenceTest1() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query1, reference1, true);
        assertEquals(87.5, score, 0.1);
    }

    @Test
    public void tanabeTest2() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query2, reference2, true);
        assertEquals(72.7, score, 0.1);
    }

    @Test
    public void mastersQueryTest2() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query2, reference2, true);
        assertEquals(80.0, score, 0.1);
    }

    @Test
    public void mastersReferenceTest2() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query2, reference2, true);
        assertEquals(66.7, score, 0.1);
    }

    @Test
    public void tanabeTest3() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query3, reference3, true);
        assertEquals(80.0, score, 0.1);
    }

    @Test
    public void mastersQueryTest3() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query3, reference3, true);
        assertEquals(76.9, score, 0.1);
    }

    @Test
    public void mastersReferenceTest3() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query3, reference3, true);
        assertEquals(83.3, score, 0.1);
    }

    @Test
    public void tanabeTest4() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query4, reference4, true);
        assertEquals(84.6, score, 0.1);
    }

    @Test
    public void mastersQueryTest4() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query4, reference4, true);
        assertEquals(78.6, score, 0.1);
    }

    @Test
    public void mastersReferenceTest4() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query4, reference4, true);
        assertEquals(91.7, score, 0.1);
    }

    @Test
    public void tanabeTest5() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query5, reference5, true);
        assertEquals(87.2, score, 0.1);
    }

    @Test
    public void mastersQueryTest5() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query5, reference5, true);
        assertEquals(81.0, score, 0.1);
    }

    @Test
    public void mastersReferenceTest5() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query5, reference5, true);
        assertEquals(94.4, score, 0.1);
    }

    @Test
    public void tanabeTest6() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query6, reference6, true);
        assertEquals(62.5, score, 0.1);
    }

    @Test
    public void mastersQueryTest6() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query6, reference6, true);
        assertEquals(62.5, score, 0.1);
    }

    @Test
    public void mastersReferenceTest6() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query6, reference6, true);
        assertEquals(62.5, score, 0.1);
    }

    @Test
    public void tanabeTest7() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query7, reference7, true);
        assertEquals(73.7, score, 0.1);
    }

    @Test
    public void mastersQueryTest7() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query7, reference7, true);
        assertEquals(82.3, score, 0.1);
    }

    @Test
    public void mastersReferenceTest7() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query7, reference7, true);
        assertEquals(66.7, score, 0.1);
    }

    @Test
    public void tanabeTest8() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query8, reference8, true);
        assertEquals(72.2, score, 0.1);
    }

    @Test
    public void mastersQueryTest8() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query8, reference8, true);
        assertEquals(65.0, score, 0.1);
    }

    @Test
    public void mastersReferenceTest8() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query8, reference8, true);
        assertEquals(81.3, score, 0.1);
    }

    @Test
    public void tanabeTest9() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query9, reference9, true);
        assertEquals(84.6, score, 0.1);
    }

    @Test
    public void mastersQueryTest9() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query9, reference9, true);
        assertEquals(91.7, score, 0.1);
    }

    @Test
    public void mastersReferenceTest9() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query9, reference9, true);
        assertEquals(78.6, score, 0.1);
    }

    @Test
    public void tanabeTest10() {
        double score = ScoringAlgorithm.TANABE.computeScore(ScoringMode.NON_EMPTY, query1, copy, true);
        assertEquals(100.0, score, 0.1);
    }

    @Test
    public void mastersQueryTest10() {
        double score = ScoringAlgorithm.MASTERS_QUERY.computeScore(ScoringMode.NON_EMPTY, query1, copy, true);
        assertEquals(100.0, score, 0.1);
    }

    @Test
    public void mastersReferenceTest10() {
        double score = ScoringAlgorithm.MASTERS_REFERENCE.computeScore(ScoringMode.NON_EMPTY, query1, copy, true);
        assertEquals(100.0, score, 0.1);
    }
}
