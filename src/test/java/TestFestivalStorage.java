import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The purpose of these tests are to check whether a Json object is stored into the relevant object models correctly
 *
 * @author Aupi As-Saber
 */
public class TestFestivalStorage {
    private FestivalInfoGetter festivalInfoGetter = new FestivalInfoGetter();

    @Test
    public void testObjectStorage() {
        JsonObject responseObjectArray = getObject();
        festivalInfoGetter.extractInfoFromJsonObject(responseObjectArray);

        String firstRecordLabelBand = festivalInfoGetter.getObjectSets().getAllRecordLabels().first().getBands().first().getBandName();
        assertEquals("Coolest Band Name", firstRecordLabelBand, "First record label band test");

        String secondRecordLabelBand = festivalInfoGetter.getObjectSets().getAllRecordLabels().last().getBands().first().getBandName();
        assertEquals("Second Coolest BandName", secondRecordLabelBand, "Second record label band test");
    }

    public JsonObject getObject() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder
                .add("name", "BEST FESTIVAL")
                .add("bands",
                        Json.createArrayBuilder()
                                .add(
                                        Json.createObjectBuilder()
                                                .add("recordLabel", "First Record Label")
                                                .add("name", "Coolest Band Name")
                                )
                                .add(
                                        Json.createObjectBuilder()
                                                .add("recordLabel", "Second Record Label")
                                                .add("name", "Second Coolest BandName")
                                )
                );

        return builder.build();
    }

}

