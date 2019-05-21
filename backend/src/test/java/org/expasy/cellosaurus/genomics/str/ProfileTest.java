package org.expasy.cellosaurus.genomics.str;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProfileTest {
    
    @Test
    public void compareToTest1() {
        List<Profile> profiles = new ArrayList<>();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();
        Profile profile4 = new Profile();
        Profile profile5 = new Profile();
        Profile profile6 = new Profile();
        Profile profile7 = new Profile();
        Profile profile8 = new Profile();
        Profile profile9 = new Profile();

        profile1.setScore(100.0);
        profile2.setScore(93.4);
        profile3.setScore(77.2);
        profile4.setScore(68.0);
        profile5.setScore(54.4);
        profile6.setScore(41.5);
        profile7.setScore(33.3);
        profile8.setScore(28.9);
        profile9.setScore(10.7);

        profiles.add(profile1);
        profiles.add(profile2);
        profiles.add(profile3);
        profiles.add(profile4);
        profiles.add(profile5);
        profiles.add(profile6);
        profiles.add(profile7);
        profiles.add(profile8);
        profiles.add(profile9);

        List<Profile> shuffled = new ArrayList<>(profiles);
        Collections.shuffle(shuffled, new Random(84681484621L));
        assertNotEquals(profiles, shuffled);
        Collections.sort(shuffled);
        assertEquals(profiles, shuffled);
    }
}