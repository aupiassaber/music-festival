import model.Band;
import model.RecordLabel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.SortedSet;

/**
 * This application gets the data provided by a web based api and prints it in the required format
 *
 * @author Aupi As-Saber
 */
public class FestivalInfoPrinter {
    private static final Logger debug = LogManager.getLogger("debug");
    private static FestivalInfoGetter festivalInfoGetter = new FestivalInfoGetter();

    /**
     * The main method acts as the entry point and initiates both the the getting and printing of the
     * music festival info
     */
    public static void main(String[] args) throws Exception {
        festivalInfoGetter.getResponseFromApi();
        printInfo(festivalInfoGetter.getObjectSets().getAllRecordLabels());
        writeInfo(festivalInfoGetter.getObjectSets().getAllRecordLabels());
    }

    /**
     * Prints info in required format to console
     */
    public static void printInfo(SortedSet<RecordLabel> allRecordLabels) {
        for (RecordLabel record : allRecordLabels) {
            System.out.println(record.getRecordLabelName());
            for (Band band : record.getBands()) {
                System.out.println("\t" + band.getBandName());
                for (String festival : band.getFestivalSet()) {
                    if (!festival.isEmpty())
                        System.out.println("\t\t" + festival);
                }
            }
        }
    }

    /**
     * Prints info in required format to file
     */
    public static void writeInfo(SortedSet<RecordLabel> allRecordLabels) {
        Path path = Paths.get("record-label-info.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            for (RecordLabel record : allRecordLabels) {
                writer.write(record.getRecordLabelName() + "\n");
                for (Band band : record.getBands()) {
                    writer.write("\t" + band.getBandName() + "\n");
                    for (String festival : band.getFestivalSet()) {
                        if (!festival.isEmpty())
                            writer.write("\t\t" + festival + "\n");
                    }
                }
            }
        } catch (IOException io) {
            System.out.println(io);
            debug.error(io);
        }
    }
}
