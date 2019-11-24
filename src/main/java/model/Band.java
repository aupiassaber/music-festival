package model;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This is object model is used to store a band's name and the festivals they performed at
 *
 * @author Aupi As-Saber
 */
public class Band {
    private String bandName;
    private SortedSet<String> festivalSet;

    public Band(String bandName) {
        this.bandName = bandName;
        this.festivalSet = new TreeSet<>();
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public SortedSet<String> getFestivalSet() {
        return festivalSet;
    }

    public void addFestival(String festival) {
        this.festivalSet.add(festival);
    }
}
