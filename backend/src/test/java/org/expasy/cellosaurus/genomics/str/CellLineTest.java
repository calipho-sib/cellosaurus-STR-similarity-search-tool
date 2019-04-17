package org.expasy.cellosaurus.genomics.str;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CellLineTest {

    @Test
    public void reduceProfilesTest1() {
        CellLine cellLine = new CellLine();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();

        profile1.setScore(22.3);
        profile2.setScore(98.5);

        cellLine.getProfiles().add(profile1);
        cellLine.getProfiles().add(profile2);

        cellLine.reduceProfiles();

        assertEquals(2, cellLine.getProfiles().size());
        assertEquals(profile2, cellLine.getProfiles().get(0));
        assertEquals(profile1, cellLine.getProfiles().get(1));
    }

    @Test
    public void reduceProfilesTest2() {
        CellLine cellLine = new CellLine();

        Profile profile1 = new Profile();

        profile1.setScore(33.5);

        cellLine.getProfiles().add(profile1);

        cellLine.reduceProfiles();

        assertEquals(1, cellLine.getProfiles().size());
        assertEquals(profile1, cellLine.getProfiles().get(0));
    }

    @Test
    public void reduceProfilesTest3() {
        CellLine cellLine = new CellLine();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();
        Profile profile4 = new Profile();

        profile1.setScore(23.4);
        profile2.setScore(11.8);
        profile3.setScore(99.6);
        profile4.setScore(54.8);

        cellLine.getProfiles().add(profile1);
        cellLine.getProfiles().add(profile2);
        cellLine.getProfiles().add(profile3);
        cellLine.getProfiles().add(profile4);

        cellLine.reduceProfiles();

        assertEquals(2, cellLine.getProfiles().size());
        assertEquals(profile3, cellLine.getProfiles().get(0));
        assertEquals(profile2, cellLine.getProfiles().get(1));
    }

    @Test
    public void reduceProfilesTest4() {
        CellLine cellLine = new CellLine();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();

        profile1.setScore(55.8);
        profile2.setScore(67.8);
        profile3.setScore(10.5);

        cellLine.getProfiles().add(profile1);
        cellLine.getProfiles().add(profile2);
        cellLine.getProfiles().add(profile3);

        cellLine.reduceProfiles();

        assertEquals(2, cellLine.getProfiles().size());
        assertEquals(profile2, cellLine.getProfiles().get(0));
        assertEquals(profile3, cellLine.getProfiles().get(1));
    }

    @Test
    public void reduceProfilesTest5() {
        CellLine cellLine = new CellLine();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();
        Profile profile4 = new Profile();
        Profile profile5 = new Profile();

        profile1.setScore(74.9);
        profile2.setScore(41.0);
        profile3.setScore(25.6);
        profile4.setScore(55.8);
        profile5.setScore(11.8);

        cellLine.getProfiles().add(profile1);
        cellLine.getProfiles().add(profile2);
        cellLine.getProfiles().add(profile3);
        cellLine.getProfiles().add(profile4);
        cellLine.getProfiles().add(profile5);

        cellLine.reduceProfiles();

        assertEquals(2, cellLine.getProfiles().size());
        assertEquals(profile1, cellLine.getProfiles().get(0));
        assertEquals(profile5, cellLine.getProfiles().get(1));
    }

    @Test
    public void compareToTest() {
        List<CellLine> cellLines = new ArrayList<>();

        CellLine cellLine1 = new CellLine();
        CellLine cellLine2 = new CellLine();
        CellLine cellLine3 = new CellLine();
        CellLine cellLine4 = new CellLine();
        CellLine cellLine5 = new CellLine();
        CellLine cellLine6 = new CellLine();
        CellLine cellLine7 = new CellLine();
        CellLine cellLine8 = new CellLine();
        CellLine cellLine9 = new CellLine();

        cellLine1.setBestScore(99.8);
        cellLine2.setBestScore(77.8);
        cellLine3.setBestScore(60.1);
        cellLine4.setBestScore(60.0);
        cellLine5.setBestScore(55.8);
        cellLine6.setBestScore(37.9);
        cellLine7.setBestScore(22.4);
        cellLine8.setBestScore(12.2);
        cellLine9.setBestScore(7.5);

        cellLines.add(cellLine1);
        cellLines.add(cellLine2);
        cellLines.add(cellLine3);
        cellLines.add(cellLine4);
        cellLines.add(cellLine5);
        cellLines.add(cellLine6);
        cellLines.add(cellLine7);
        cellLines.add(cellLine8);
        cellLines.add(cellLine9);

        List<CellLine> shuffled = new ArrayList<>(cellLines);
        Collections.shuffle(shuffled, new Random(84681484621L));
        assertNotEquals(cellLines, shuffled);
        Collections.sort(shuffled);
        assertEquals(cellLines, shuffled);
    }
}