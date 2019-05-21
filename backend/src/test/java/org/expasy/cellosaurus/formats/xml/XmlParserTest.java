package org.expasy.cellosaurus.formats.xml;

import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.genomics.str.Allele;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {
    private final Parser parser;

    public XmlParserTest() throws IOException {
        this.parser = new XmlParser();
        parser.parse(getClass().getClassLoader().getResource("cellosaurus_head_1000.xml").getFile());
    }

    private List<Allele> getAlleles(List<Marker> markers) {
        List<Allele> alleles = new ArrayList<>();
        markers.stream().map(Marker::getAlleles).forEach(alleles::addAll);

        return alleles;
    }

    @Test
    public void getDatabaseTest1() {
        assertEquals("29.0", parser.getDatabase().getVersion());
        assertEquals("2019-02-25", parser.getDatabase().getUpdated());
        assertEquals(110948, parser.getDatabase().getCellLineCount());
        assertEquals(16699, parser.getDatabase().getPublicationCount());
    }

    @Test
    public void getSpeciesTest1() {
        assertEquals(86, parser.getSpecies("Homo sapiens").getCellLines().size());
    }

    @Test
    public void getSpeciesTest2() {
        assertEquals(2, parser.getSpecies("Canis lupus familiaris").getCellLines().size());
    }

    @Test
    public void getCellLinesTestHuman1() {
        CellLine cellLine = this.parser.getSpecies("Homo sapiens").getCellLines().get(1);

        List<Marker> markers = Arrays.asList(
                new Marker("Amelogenin", "X"),
                new Marker("CSF1PO", "9", "10"),
                new Marker("D3S1358", "15", "18"),
                new Marker("D5S818", "11", "12"),
                new Marker("D7S820", "8", "12"),
                new Marker("D13S317", "12", "13.3"),
                new Marker("D16S539", "9", "10"),
                new Marker("FGA", "18", "21"),
                new Marker("TH01", "7"),
                new Marker("TPOX", "8", "12"),
                new Marker("vWA", "16", "18")
        );

        assertEquals("CVCL_2260", cellLine.getAccession());
        assertEquals("1-5c-4", cellLine.getName());
        assertEquals("Homo sapiens", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertTrue(cellLine.isProblematic());
        assertEquals("Contaminated. Shown to be a HeLa derivative (PubMed=566722; PubMed=20143388).", cellLine.getProblem());
        assertNull(cellLine.getStability());
        assertEquals(1, cellLine.getProfiles().size());
        assertEquals(markers, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
    }

    @Test
    public void getCellLinesTestHuman2() {
        CellLine cellLine = this.parser.getSpecies("Homo sapiens").getCellLines().get(31);

        List<Marker> markers = Arrays.asList(
                new Marker("Amelogenin", "X", "Y"),
                new Marker("CSF1PO", "12"),
                new Marker("D5S818", "11"),
                new Marker("D7S820", "11", "12"),
                new Marker("D13S317", "8"),
                new Marker("D16S539", "10"),
                new Marker("TH01", "7", "9"),
                new Marker("TPOX", "8"),
                new Marker("vWA", "14", "18")
        );

        assertEquals("CVCL_F949", cellLine.getAccession());
        assertEquals("1616-EB", cellLine.getName());
        assertEquals("Homo sapiens", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertFalse(cellLine.isProblematic());
        assertNull(cellLine.getProblem());
        assertNull(cellLine.getStability());
        assertEquals(1, cellLine.getProfiles().size());
        assertEquals(markers, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
    }

    @Test
    public void getCellLinesTestHuman3() {
        CellLine cellLine = this.parser.getSpecies("Homo sapiens").getCellLines().get(40);

        List<Marker> markers = Arrays.asList(
                new Marker("Amelogenin", "X"),
                new Marker("CSF1PO", "10", "11"),
                new Marker("D5S818", "11", "13"),
                new Marker("D7S820", "9", "11"),
                new Marker("D13S317", "11"),
                new Marker("D16S539", "11", "12"),
                new Marker("D21S11", "29", "30"),
                new Marker("TH01", "9.3"),
                new Marker("TPOX", "11"),
                new Marker("vWA", "18", "19")
        );

        assertEquals("CVCL_3040", cellLine.getAccession());
        assertEquals("184A1", cellLine.getName());
        assertEquals("Homo sapiens", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertFalse(cellLine.isProblematic());
        assertNull(cellLine.getProblem());
        assertEquals("Stable (MSS) (PubMed=12661003).", cellLine.getStability());
        assertEquals(1, cellLine.getProfiles().size());
        assertEquals(markers, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
    }

    @Test
    public void getCellLinesTestHuman4() {
        CellLine cellLine = this.parser.getSpecies("Homo sapiens").getCellLines().get(50);

        List<Marker> markers = Arrays.asList(
                new Marker("Amelogenin", "X", "Y"),
                new Marker("CSF1PO", "10", "11", "12"),
                new Marker("D5S818", "8", "9", "12"),
                new Marker("D7S820", "8", "11", "12"),
                new Marker("D13S317", "12", "14"),
                new Marker("D16S539", "9"),
                new Marker("TH01", "7", "9", "9.3"),
                new Marker("TPOX", "11"),
                new Marker("vWA", "15", "19")
        );

        assertEquals("CVCL_6345", cellLine.getAccession());
        assertEquals("1G2", cellLine.getName());
        assertEquals("Homo sapiens", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertFalse(cellLine.isProblematic());
        assertNull(cellLine.getProblem());
        assertNull(cellLine.getStability());
        assertEquals(1, cellLine.getProfiles().size());
        assertEquals(markers, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
    }

    @Test
    public void getCellLinesTestHuman5() {
        CellLine cellLine = this.parser.getSpecies("Homo sapiens").getCellLines().get(80);

        List<Marker> markers = Arrays.asList(
                new Marker("Amelogenin", "X"),
                new Marker("CSF1PO", "10", "12"),
                new Marker("D5S818", "10", "12"),
                new Marker("D7S820", "9", "11"),
                new Marker("D13S317", "10", "12"),
                new Marker("D16S539", "12"),
                new Marker("TH01", "6", "9.3"),
                new Marker("TPOX", "8", "11"),
                new Marker("vWA", "15")
        );

        assertEquals("CVCL_EP74", cellLine.getAccession());
        assertEquals("28SC-ES", cellLine.getName());
        assertEquals("Homo sapiens", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertTrue(cellLine.isProblematic());
        assertEquals("Contaminated. From its STR profile the parent cell line (SC) seems to be a U-937 derivative.", cellLine.getProblem());
        assertNull(cellLine.getStability());
        assertEquals(1, cellLine.getProfiles().size());
        assertEquals(markers, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
    }

    @Test
    public void getCellLinesTestHuman6() {
        CellLine cellLine = this.parser.getSpecies("Homo sapiens").getCellLines().get(24);

        List<Marker> markers1 = Arrays.asList(
                new Marker("Amelogenin", "X"),
                new Marker("CSF1PO", "12"),
                new Marker("D2S1338", "24", "25"),
                new Marker("D3S1358", "15"),
                new Marker("D5S818", "13"),
                new Marker("D7S820", "11", "12"),
                new Marker("D8S1179", "11", "14"),
                new Marker("D13S317", "12"),
                new Marker("D16S539", "10", "13"),
                new Marker("D18S51", "17"),
                new Marker("D19S433", "13"),
                new Marker("D21S11", "31.2", "32.2"),
                new Marker("FGA", "24"),
                new Marker("Penta_D", "9", "10"),
                new Marker("Penta_E", "7", "12"),
                new Marker("SE33", "21"),
                new Marker("TH01", "6"),
                new Marker("TPOX", "11"),
                new Marker("vWA", "18")
        );

        List<Marker> markers2 = Arrays.asList(
                new Marker("Amelogenin", "X"),
                new Marker("CSF1PO", "12"),
                new Marker("D2S1338", "24", "25"),
                new Marker("D3S1358", "15"),
                new Marker("D5S818", "13", "14"),
                new Marker("D7S820", "11", "12"),
                new Marker("D8S1179", "11", "14"),
                new Marker("D13S317", "12"),
                new Marker("D16S539", "10", "13"),
                new Marker("D18S51", "17"),
                new Marker("D19S433", "13"),
                new Marker("D21S11", "31.2", "32.2"),
                new Marker("FGA", "24"),
                new Marker("Penta_D", "9", "10"),
                new Marker("Penta_E", "7", "12"),
                new Marker("SE33", "21"),
                new Marker("TH01", "6"),
                new Marker("TPOX", "11"),
                new Marker("vWA", "18", "19")
        );

        assertEquals("CVCL_2270", cellLine.getAccession());
        assertEquals("143B", cellLine.getName());
        assertEquals("Homo sapiens", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertFalse(cellLine.isProblematic());
        assertNull(cellLine.getProblem());
        assertNull(cellLine.getStability());
        assertEquals(2, cellLine.getProfiles().size());
        assertEquals(markers1, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers1), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
        assertEquals(markers2, cellLine.getProfiles().get(1).getMarkers());
        assertEquals(getAlleles(markers2), getAlleles(cellLine.getProfiles().get(1).getMarkers()));
    }

    @Test
    public void getCellLinesTestDog1() {
        CellLine cellLine = parser.getSpecies("Canis lupus familiaris").getCellLines().get(0);

        List<Marker> markers = Arrays.asList(
                new Marker("Dog_FHC2010", "231"),
                new Marker("Dog_FHC2054", "147", "151"),
                new Marker("Dog_FHC2079", "271"),
                new Marker("Dog_PEZ1", "119", "127"),
                new Marker("Dog_PEZ12", "274", "277"),
                new Marker("Dog_PEZ20", "180", "192"),
                new Marker("Dog_PEZ3", "139"),
                new Marker("Dog_PEZ5", "103"),
                new Marker("Dog_PEZ6", "183", "191"),
                new Marker("Dog_PEZ8", "236", "249")
        );

        assertEquals("CVCL_0B18", cellLine.getAccession());
        assertEquals("17-71", cellLine.getName());
        assertEquals("Canis lupus familiaris", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertFalse(cellLine.isProblematic());
        assertNull(cellLine.getProblem());
        assertNull(cellLine.getStability());
        assertEquals(markers, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
    }

    @Test
    public void getCellLinesTestDog2() {
        CellLine cellLine = parser.getSpecies("Canis lupus familiaris").getCellLines().get(1);

        List<Marker> markers = Arrays.asList(
                new Marker("Dog_FHC2010", "231"),
                new Marker("Dog_FHC2054", "156"),
                new Marker("Dog_FHC2079", "271"),
                new Marker("Dog_PEZ1", "127"),
                new Marker("Dog_PEZ12", "270", "274"),
                new Marker("Dog_PEZ20", "176"),
                new Marker("Dog_PEZ3", "121", "131"),
                new Marker("Dog_PEZ5", "107"),
                new Marker("Dog_PEZ6", "176"),
                new Marker("Dog_PEZ8", "236", "240")
        );

        assertEquals("CVCL_0C82", cellLine.getAccession());
        assertEquals("17CM98", cellLine.getName());
        assertEquals("Canis lupus familiaris", cellLine.getSpecies());
        assertEquals(.0, cellLine.getBestScore());
        assertFalse(cellLine.isProblematic());
        assertNull(cellLine.getProblem());
        assertNull(cellLine.getStability());
        assertEquals(markers, cellLine.getProfiles().get(0).getMarkers());
        assertEquals(getAlleles(markers), getAlleles(cellLine.getProfiles().get(0).getMarkers()));
    }
}
