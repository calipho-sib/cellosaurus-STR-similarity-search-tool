package org.expasy.cellosaurus.formats.xml;

import org.expasy.cellosaurus.genomics.str.Marker;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    public void getDatabaseTest1() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("cellosaurus_min.xml").getFile());
        
        assertEquals("28.0", parser.getDatabase().getVersion());
        assertEquals("2018-11-13", parser.getDatabase().getUpdated());
        assertEquals(109135, parser.getDatabase().getCellLineCount());
        assertEquals(16132, parser.getDatabase().getPublicationCount());
    }

    @Test
    public void getCellLinesTest1() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("cellosaurus_min.xml").getFile());

        assertEquals(4, parser.getCellLines().size());
    }

    @Test
    public void getCellLinesTest2() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("cellosaurus_min.xml").getFile());

        List<Marker> markers = new ArrayList<>();
        markers.add(new Marker("F13A01", "6", "14"));
        markers.add(new Marker("F13B", "8"));
        markers.add(new Marker("FESFPS", "10"));
        markers.add(new Marker("LPL", "10", "11"));
        markers.add(new Marker("Penta C", "11", "13"));
        markers.add(new Marker("Penta D", "10", "13"));
        markers.add(new Marker("Penta E", "8", "15"));

        assertEquals("CVCL_VE81", parser.getCellLines().get(0).getAccession());
        assertEquals("PSMi002-A", parser.getCellLines().get(0).getName());
        assertEquals("Homo sapiens", parser.getCellLines().get(0).getSpecies());
        assertEquals(0, parser.getCellLines().get(0).getBestScore());
        assertEquals(markers, parser.getCellLines().get(0).getProfiles().get(0).getMarkers());
    }

    @Test
    public void getCellLinesTest3() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("cellosaurus_min.xml").getFile());

        List<Marker> markers = new ArrayList<>();
        markers.add(new Marker("Dog FHC2010", "231", "235"));
        markers.add(new Marker("Dog FHC2054", "156", "172"));
        markers.add(new Marker("Dog FHC2079", "275", "279"));
        markers.add(new Marker("Dog PEZ1", "115"));
        markers.add(new Marker("Dog PEZ12", "270", "274"));
        markers.add(new Marker("Dog PEZ20", "180"));
        markers.add(new Marker("Dog PEZ3", "124", "127"));
        markers.add(new Marker("Dog PEZ5", "111"));
        markers.add(new Marker("Dog PEZ6", "179", "183"));
        markers.add(new Marker("Dog PEZ8", "240"));

        assertEquals("CVCL_L353", parser.getCellLines().get(1).getAccession());
        assertEquals("Gracie", parser.getCellLines().get(1).getName());
        assertEquals("Canis lupus familiaris", parser.getCellLines().get(1).getSpecies());
        assertEquals(0, parser.getCellLines().get(1).getBestScore());
        assertEquals(markers, parser.getCellLines().get(1).getProfiles().get(0).getMarkers());
    }

    @Test
    public void getCellLinesTest4() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("cellosaurus_min.xml").getFile());

        List<Marker> markers = new ArrayList<>();
        markers.add(new Marker("Amelogenin", "X", "Y"));
        markers.add(new Marker("D3S1358", "17"));
        markers.add(new Marker("D5S818", "10", "14"));
        markers.add(new Marker("D7S820", "12", "13"));
        markers.add(new Marker("D8S1179", "13", "14"));
        markers.add(new Marker("D13S317", "9"));
        markers.add(new Marker("D18S51", "12", "15"));
        markers.add(new Marker("D21S11", "28", "30"));
        markers.add(new Marker("FGA", "22"));
        markers.add(new Marker("vWA", "16"));

        assertEquals("CVCL_6287", parser.getCellLines().get(2).getAccession());
        assertEquals("FRO", parser.getCellLines().get(2).getName());
        assertEquals("Homo sapiens", parser.getCellLines().get(2).getSpecies());
        assertEquals(0, parser.getCellLines().get(2).getBestScore());
        assertEquals(markers, parser.getCellLines().get(2).getProfiles().get(0).getMarkers());
    }

    @Test
    public void getCellLinesTest5() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("cellosaurus_min.xml").getFile());

        List<Marker> markers = new ArrayList<>();
        markers.add(new Marker("Dog FHC2010", "231"));
        markers.add(new Marker("Dog FHC2054", "147", "151"));
        markers.add(new Marker("Dog FHC2079", "271"));
        markers.add(new Marker("Dog PEZ1", "119", "127"));
        markers.add(new Marker("Dog PEZ12", "274", "277"));
        markers.add(new Marker("Dog PEZ20", "180", "192"));
        markers.add(new Marker("Dog PEZ3", "139"));
        markers.add(new Marker("Dog PEZ5", "103"));
        markers.add(new Marker("Dog PEZ6", "183", "191"));
        markers.add(new Marker("Dog PEZ8", "236", "249"));

        assertEquals("CVCL_0B18", parser.getCellLines().get(3).getAccession());
        assertEquals("17-71", parser.getCellLines().get(3).getName());
        assertEquals("Canis lupus familiaris", parser.getCellLines().get(3).getSpecies());
        assertEquals(0, parser.getCellLines().get(3).getBestScore());
        assertEquals(markers, parser.getCellLines().get(3).getProfiles().get(0).getMarkers());
    }
}
