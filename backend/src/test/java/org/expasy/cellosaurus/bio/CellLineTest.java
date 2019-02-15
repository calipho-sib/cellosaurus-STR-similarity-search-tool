package org.expasy.cellosaurus.bio;

import org.expasy.cellosaurus.bio.str.Haplotype;
import org.expasy.cellosaurus.format.xml.Parser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CellLineTest {

    @Test
    public void resolve1() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("CVCL/CVCL_0238.xml").getFile());
        List<Haplotype> haplotypes = parser.getCellLines().get(0).getHaplotypes();
        assertEquals(16, haplotypes.size());
    }

    @Test
    public void resolve2() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("CVCL/CVCL_0624.xml").getFile());
        List<Haplotype> haplotypes = parser.getCellLines().get(0).getHaplotypes();
        assertEquals(150, haplotypes.size());
    }

    @Test
    public void resolve3() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("CVCL/CVCL_0633.xml").getFile());
        List<Haplotype> haplotypes = parser.getCellLines().get(0).getHaplotypes();
        assertEquals(16, haplotypes.size());
    }

    @Test
    public void resolve4() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("CVCL/CVCL_1793.xml").getFile());
        List<Haplotype> haplotypes = parser.getCellLines().get(0).getHaplotypes();
        assertEquals(3, haplotypes.size());
    }

    @Test
    public void resolve5() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("CVCL/CVCL_1893.xml").getFile());
        List<Haplotype> haplotypes = parser.getCellLines().get(0).getHaplotypes();
        assertEquals(4, haplotypes.size());
    }

    @Test
    public void resolve6() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("CVCL/CVCL_D605.xml").getFile());
        List<Haplotype> haplotypes = parser.getCellLines().get(0).getHaplotypes();
        assertEquals(1, haplotypes.size());
    }

    @Test
    public void resolve7() throws IOException {
        Parser parser = new Parser(getClass().getClassLoader().getResource("CVCL/CVCL_0013.xml").getFile());
        List<Haplotype> haplotypes = parser.getCellLines().get(0).getHaplotypes();
        assertEquals(150, haplotypes.size());
    }
}