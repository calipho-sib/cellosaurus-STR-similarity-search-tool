package org.expasy.cellosaurus.genomics.str;

import org.expasy.cellosaurus.math.scoring.ScoringMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlleleTest {

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
    public void getMatchedTest1() {
        ScoringMode.NON_EMPTY.computeHits(query1, reference1, true);

        for (Marker marker : reference1.getMarkers()) {
            for (Allele allele : marker.getAlleles()) {
                switch (marker.getName()) {
                    case "D5S818":
                    case "D13S317":
                    case "D7S820":
                    case "D16S539":
                    case "VWA":
                    case "TH01":
                    case "Amelogenin":
                        assertTrue(allele.getMatched());
                        break;
                    case "TPOX":
                        if (allele.getValue().equals("7")) {
                            assertFalse(allele.getMatched());
                        } else {
                            assertTrue(allele.getMatched());
                        }
                        break;
                    case "CSF1PO":
                        if (allele.getValue().equals("13")) {
                            assertFalse(allele.getMatched());
                        } else {
                            assertTrue(allele.getMatched());
                        }
                        break;
                }
            }
        }
        for (Marker marker : query1.getMarkers()) {
            for (Allele allele : marker.getAlleles()) {
                assertFalse(allele.getMatched());
            }
        }
    }

    private Profile reference2 = new Profile(
            new Marker("D5S818", "12", "13"),
            new Marker("D13S317", "11"),
            new Marker("D7S820", "ND"),
            new Marker("D16S539", "10", "13"),
            new Marker("VWA", "17", "19"),
            new Marker("TH01", "6", "7"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "7", "8"),
            new Marker("CSF1PO", "10", "13")
    );

    private Profile query2 = new Profile(
            new Marker("D5S818", "12", "13"),
            new Marker("D13S317", "11", "12"),
            new Marker("D7S820", "9", "13"),
            new Marker("D16S539", "10", "13"),
            new Marker("VWA", "17", "19"),
            new Marker("TH01", "6", "7"),
            new Marker("Amelogenin", "X"),
            new Marker("TPOX", "ND"),
            new Marker("CSF1PO", "10", "11")
    );

    @Test
    public void getMatchedTest2() {
        ScoringMode.QUERY.computeHits(query2, reference2, true);

        for (Marker marker : reference2.getMarkers()) {
            for (Allele allele : marker.getAlleles()) {
                switch (marker.getName()) {
                    case "D5S818":
                    case "D13S317":
                    case "D16S539":
                    case "VWA":
                    case "TH01":
                    case "Amelogenin":
                        assertTrue(allele.getMatched());
                        break;
                    case "D7S820":
                    case "TPOX":
                        assertFalse(allele.getMatched());
                        break;
                    case "CSF1PO":
                        if (allele.getValue().equals("13")) {
                            assertFalse(allele.getMatched());
                        } else {
                            assertTrue(allele.getMatched());
                        }
                        break;
                }
            }
        }
        for (Marker marker : query2.getMarkers()) {
            for (Allele allele : marker.getAlleles()) {
                assertFalse(allele.getMatched());
            }
        }
    }

    private Profile reference3 = new Profile(
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

    private Profile query3 = new Profile(
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
    public void getMatchedTest3() {
        ScoringMode.REFERENCE.computeHits(query3, reference3, false);

        for (Marker marker : reference3.getMarkers()) {
            for (Allele allele : marker.getAlleles()) {
                switch (marker.getName()) {
                    case "D13S317":
                    case "D7S820":
                    case "D16S539":
                    case "VWA":
                    case "TH01":
                    case "Amelogenin":
                        assertTrue(allele.getMatched());
                        break;
                    case "D5S818":
                        assertFalse(allele.getMatched());
                        break;
                    case "TPOX":
                        if (allele.getValue().equals("7")) {
                            assertFalse(allele.getMatched());
                        } else {
                            assertTrue(allele.getMatched());
                        }
                        break;
                    case "CSF1PO":
                        if (allele.getValue().equals("13")) {
                            assertFalse(allele.getMatched());
                        } else {
                            assertTrue(allele.getMatched());
                        }
                        break;
                }
            }
        }
        for (Marker marker : query3.getMarkers()) {
            for (Allele allele : marker.getAlleles()) {
                assertFalse(allele.getMatched());
            }
        }
    }
}
