package model;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This is object model is used to store groups of record labels and bands
 *
 * @author Aupi As-Saber
 */
public class ObjectSets {
    private SortedSet<RecordLabel> allRecordLabels;
    private SortedSet<Band> allBands;

    public ObjectSets() {
        this.allRecordLabels = new TreeSet<>(Comparator.comparing(RecordLabel::getRecordLabelName, String.CASE_INSENSITIVE_ORDER));
        this.allBands = new TreeSet<>(Comparator.comparing(Band::getBandName, String.CASE_INSENSITIVE_ORDER));
    }

    public SortedSet<RecordLabel> getAllRecordLabels() {
        return allRecordLabels;
    }

    public SortedSet<Band> getAllBands() {
        return allBands;
    }

    public void addRecordLabel(RecordLabel recordLabel) {
        this.allRecordLabels.add(recordLabel);
    }

    public void addBand(Band band) {
        this.allBands.add(band);
    }
}
