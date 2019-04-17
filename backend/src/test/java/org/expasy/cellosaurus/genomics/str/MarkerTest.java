package org.expasy.cellosaurus.genomics.str;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MarkerTest {

    @Test
    public void countAllelesTest1() {
        Marker marker = new Marker("Test", "ND");
        assertEquals(0, marker.countAlleles());
    }

    @Test
    public void countAllelesTest2() {
        Marker marker = new Marker("Test", "10", "11", "13");
        assertEquals(3, marker.countAlleles());
    }

    @Test
    public void countAllelesTest3() {
        Marker marker = new Marker("Test", "12");
        assertEquals(1, marker.countAlleles());
    }

    @Test
    public void countAllelesTest4() {
        Marker marker = new Marker("Test", "12", "12");
        assertEquals(1, marker.countAlleles());
    }

    @Test
    public void countAllelesTest5() {
        Marker marker = new Marker("Test", "10", "11", "ND");
        assertEquals(2, marker.countAlleles());
    }

    @Test
    public void matchAgainstTest1() {
        Marker marker1 = new Marker("Test", "10", "11");
        Marker marker2 = new Marker("Test", "10", "11");
        assertEquals(2, marker1.matchAgainst(marker2));
    }

    @Test
    public void matchAgainstTest2() {
        Marker marker1 = new Marker("Test", "12");
        Marker marker2 = new Marker("Test", "12", "12");
        assertEquals(1, marker1.matchAgainst(marker2));
    }

    @Test
    public void matchAgainstTest3() {
        Marker marker1 = new Marker("Test", "7", "8", "9");
        Marker marker2 = new Marker("Test", "7", "9");
        assertEquals(2, marker1.matchAgainst(marker2));
    }

    @Test
    public void matchAgainstTest4() {
        Marker marker1 = new Marker("Test", "15", "13", "14", "12");
        Marker marker2 = new Marker("Test", "14", "13", "15");
        assertEquals(3, marker1.matchAgainst(marker2));
    }

    @Test
    public void matchAgainstTest5() {
        Marker marker1 = new Marker("Test", "ND");
        Marker marker2 = new Marker("Test", "ND");
        assertEquals(0, marker1.matchAgainst(marker2));
    }

    @Test
    public void compareToTest() {
        List<Marker> markers = new ArrayList<>();
        markers.add(new Marker("Amelogenin"));
        markers.add(new Marker("CSF1PO"));
        markers.add(new Marker("D2S1338"));
        markers.add(new Marker("D3S1358"));
        markers.add(new Marker("D5S818"));
        markers.add(new Marker("D7S820"));
        markers.add(new Marker("D8S1179"));
        markers.add(new Marker("D13S317"));
        markers.add(new Marker("D16S539"));
        markers.add(new Marker("D18S51"));
        markers.add(new Marker("D19S433"));
        markers.add(new Marker("D21S11"));
        markers.add(new Marker("FGA"));
        markers.add(new Marker("Penta D"));
        markers.add(new Marker("Penta E"));
        markers.add(new Marker("TH01"));
        markers.add(new Marker("TPOX"));
        markers.add(new Marker("vWA"));

        List<Marker> shuffled = new ArrayList<>(markers);
        Collections.shuffle(shuffled, new Random(84681484621L));
        assertNotEquals(markers, shuffled);
        Collections.sort(shuffled);
        assertEquals(markers, shuffled);
    }
}
