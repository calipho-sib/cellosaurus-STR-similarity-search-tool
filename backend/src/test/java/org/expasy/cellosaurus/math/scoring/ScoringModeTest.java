package org.expasy.cellosaurus.math.scoring;

import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoringModeTest {
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

    @Test
    public void nonEmptyTest1a() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query1, reference1, true);
        assertEquals(14, hits);
        assertEquals(16, query1.getSize());
        assertEquals(16, reference1.getSize());
        assertEquals(9, reference1.getMarkerNumber());
    }

    @Test
    public void nonEmptyTest1b() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query1, reference1, false);
        assertEquals(13, hits);
        assertEquals(15, query1.getSize());
        assertEquals(15, reference1.getSize());
        assertEquals(8, reference1.getMarkerNumber());
    }

    @Test
    public void queryTest1a() {
        int hits = ScoringMode.QUERY.computeHits(query1, reference1, true);
        assertEquals(14, hits);
        assertEquals(16, query1.getSize());
        assertEquals(16, reference1.getSize());
        assertEquals(9, reference1.getMarkerNumber());
    }

    @Test
    public void queryTest1b() {
        int hits = ScoringMode.QUERY.computeHits(query1, reference1, false);
        assertEquals(13, hits);
        assertEquals(15, query1.getSize());
        assertEquals(15, reference1.getSize());
        assertEquals(8, reference1.getMarkerNumber());
    }

    @Test
    public void referenceTest1a() {
        int hits = ScoringMode.REFERENCE.computeHits(query1, reference1, true);
        assertEquals(14, hits);
        assertEquals(16, query1.getSize());
        assertEquals(16, reference1.getSize());
        assertEquals(9, reference1.getMarkerNumber());
    }

    @Test
    public void referenceTest1b() {
        int hits = ScoringMode.REFERENCE.computeHits(query1, reference1, false);
        assertEquals(13, hits);
        assertEquals(15, query1.getSize());
        assertEquals(15, reference1.getSize());
        assertEquals(8, reference1.getMarkerNumber());
    }

    private Profile reference2 = new Profile(
            new Marker("D5S818", "9"),
            new Marker("D13S317", "8", "11"),
            new Marker("D7S820", "8", "10", "11"),
            new Marker("D16S539", "11"),
            new Marker("VWA", "18", "19", "17"),
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

    @Test
    public void nonEmptyTest2a() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query2, reference2, true);
        assertEquals(10, hits);
        assertEquals(13, query2.getSize());
        assertEquals(16, reference2.getSize());
        assertEquals(8, reference2.getMarkerNumber());
    }

    @Test
    public void nonEmptyTest2b() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query2, reference2, false);
        assertEquals(9, hits);
        assertEquals(12, query2.getSize());
        assertEquals(14, reference2.getSize());
        assertEquals(7, reference2.getMarkerNumber());
    }

    @Test
    public void queryTest2a() {
        int hits = ScoringMode.QUERY.computeHits(query2, reference2, true);
        assertEquals(10, hits);
        assertEquals(15, query2.getSize());
        assertEquals(16, reference2.getSize());
        assertEquals(9, reference2.getMarkerNumber());
    }

    @Test
    public void queryTest2b() {
        int hits = ScoringMode.QUERY.computeHits(query2, reference2, false);
        assertEquals(9, hits);
        assertEquals(14, query2.getSize());
        assertEquals(14, reference2.getSize());
        assertEquals(8, reference2.getMarkerNumber());
    }

    @Test
    public void referenceTest2a() {
        int hits = ScoringMode.REFERENCE.computeHits(query2, reference2, true);
        assertEquals(10, hits);
        assertEquals(13, query2.getSize());
        assertEquals(16, reference2.getSize());
        assertEquals(8, reference2.getMarkerNumber());
    }

    @Test
    public void referenceTest2b() {
        int hits = ScoringMode.REFERENCE.computeHits(query2, reference2, false);
        assertEquals(9, hits);
        assertEquals(12, query2.getSize());
        assertEquals(14, reference2.getSize());
        assertEquals(7, reference2.getMarkerNumber());
    }

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
            new Marker("TPOX", "8"),
            new Marker("CSF1PO", "12")
    );

    @Test
    public void nonEmptyTest3a() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query3, reference3, true);
        assertEquals(9, hits);
        assertEquals(12, query3.getSize());
        assertEquals(11, reference3.getSize());
        assertEquals(8, reference3.getMarkerNumber());
    }

    @Test
    public void nonEmptyTest3b() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query3, reference3, false);
        assertEquals(9, hits);
        assertEquals(12, query3.getSize());
        assertEquals(11, reference3.getSize());
        assertEquals(8, reference3.getMarkerNumber());
    }

    @Test
    public void queryTest3a() {
        int hits = ScoringMode.QUERY.computeHits(query3, reference3, true);
        assertEquals(9, hits);
        assertEquals(12, query3.getSize());
        assertEquals(11, reference3.getSize());
        assertEquals(8, reference3.getMarkerNumber());
    }

    @Test
    public void queryTest3b() {
        int hits = ScoringMode.QUERY.computeHits(query3, reference3, false);
        assertEquals(9, hits);
        assertEquals(12, query3.getSize());
        assertEquals(11, reference3.getSize());
        assertEquals(8, reference3.getMarkerNumber());
    }

    @Test
    public void referenceTest3a() {
        int hits = ScoringMode.REFERENCE.computeHits(query3, reference3, true);
        assertEquals(9, hits);
        assertEquals(12, query3.getSize());
        assertEquals(12, reference3.getSize());
        assertEquals(9, reference3.getMarkerNumber());
    }

    @Test
    public void referenceTest3b() {
        int hits = ScoringMode.REFERENCE.computeHits(query3, reference3, false);
        assertEquals(9, hits);
        assertEquals(12, query3.getSize());
        assertEquals(11, reference3.getSize());
        assertEquals(8, reference3.getMarkerNumber());
    }

    private Profile reference4 = new Profile(
            new Marker("D3S1358", "11", "13"),
            new Marker("D5S818", "11"),
            new Marker("D13S317", "ND"),
            new Marker("D7S820", "11", "12"),
            new Marker("D16S539", "12", "12"),
            new Marker("VWA", "16", "18"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("CSF1PO", "10")
    );
    private Profile query4 = new Profile(
            new Marker("D5S818", "11"),
            new Marker("D13S317", "ND"),
            new Marker("D7S820", "11", "11.3"),
            new Marker("D16S539", "12"),
            new Marker("VWA", "16", "18", "19"),
            new Marker("TH01", "9", "9"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "8", "9"),
            new Marker("CSF1PO", "ND")
    );

    @Test
    public void nonEmptyTest4a() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query4, reference4, true);
        assertEquals(7, hits);
        assertEquals(9, query4.getSize());
        assertEquals(9, reference4.getSize());
        assertEquals(8, reference4.getMarkerNumber());
    }

    @Test
    public void nonEmptyTest4b() {
        int hits = ScoringMode.NON_EMPTY.computeHits(query4, reference4, false);
        assertEquals(6, hits);
        assertEquals(8, query4.getSize());
        assertEquals(8, reference4.getSize());
        assertEquals(7, reference4.getMarkerNumber());
    }

    @Test
    public void queryTest4a() {
        int hits = ScoringMode.QUERY.computeHits(query4, reference4, true);
        assertEquals(7, hits);
        assertEquals(11, query4.getSize());
        assertEquals(9, reference4.getSize());
        assertEquals(9, reference4.getMarkerNumber());
    }

    @Test
    public void queryTest4b() {
        int hits = ScoringMode.QUERY.computeHits(query4, reference4, false);
        assertEquals(6, hits);
        assertEquals(10, query4.getSize());
        assertEquals(8, reference4.getSize());
        assertEquals(8, reference4.getMarkerNumber());
    }

    @Test
    public void referenceTest4a() {
        int hits = ScoringMode.REFERENCE.computeHits(query4, reference4, true);
        assertEquals(7, hits);
        assertEquals(9, query4.getSize());
        assertEquals(11, reference4.getSize());
        assertEquals(9, reference4.getMarkerNumber());
    }

    @Test
    public void referenceTest4b() {
        int hits = ScoringMode.REFERENCE.computeHits(query4, reference4, false);
        assertEquals(6, hits);
        assertEquals(8, query4.getSize());
        assertEquals(10, reference4.getSize());
        assertEquals(8, reference4.getMarkerNumber());
    }
}
