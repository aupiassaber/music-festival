package model;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This is object model is used to store a Record Label's name and band's that they signed
 *
 * @author Aupi As-Saber
 */
public class RecordLabel {
    private String recordLabelName;
    private SortedSet<Band> bands;

    public RecordLabel(String recordLabelName) {
        this.recordLabelName = recordLabelName;
        this.bands = new TreeSet<>(Comparator.comparing(Band::getBandName));
    }

    public String getRecordLabelName() {
        return recordLabelName;
    }

    public void setRecordLabelName(String recordLabelName) {
        this.recordLabelName = recordLabelName;
    }

    public SortedSet<Band> getBands() {
        return bands;
    }

    public void addBand(Band band) {
        this.bands.add(band);
    }

}
