import model.Band;
import model.ObjectSets;
import model.RecordLabel;
import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * This class gets the data provided by a web based api and stores it into corresponding object models
 *
 * @author Aupi As-Saber
 */
public class FestivalInfoGetter {
    private static final Logger debug = LogManager.getLogger("debug");
    private ObjectSets objectSets = new ObjectSets();
    private int queryCounter = 0;

    /**
     * Getter for stored object sets
     */
    public ObjectSets getObjectSets() {
        return objectSets;
    }

    /**
     * This is the main entry point of this class. It sets up the rest call and stores the response into
     * the relevant object models.
     */
    //TODO: Create properties file for storing the url endpoint
    public void getResponseFromApi() throws Exception {
        String url = "http://eacodingtest.digital.energyaustralia.com.au/api/v1/festivals";
        Client client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);

        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();

        while (response.getStatus() != Response.Status.OK.getStatusCode()) {
            TimeUnit.SECONDS.sleep(10);
            response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();
        }

        passJsonObjectsFromResponse(response);
    }

    /**
     * This method extracts the Json array from the response object before passing it to
     * the extractInfoFromJsonObject() method
     *
     * @param response
     */
    public void passJsonObjectsFromResponse(Response response) {
        try {
            if ((response.getLength() < 500) && queryCounter < 10) {
                getResponseFromApi();
                queryCounter++;
            }

            JsonArray responseObjectArray = response.readEntity(JsonArray.class);
            debug.info(responseObjectArray);
            response.close();

            for (JsonObject jsonObject : responseObjectArray.getValuesAs(JsonObject.class)) {
                extractInfoFromJsonObject(jsonObject);
            }
        } catch (Exception e) {
            System.out.println(e);
            debug.error(e);
        }
    }

    /**
     * Extracts relevant info from Json object
     *
     * @param jsonObject
     */
    public void extractInfoFromJsonObject(JsonObject jsonObject) {
        String festivalName = "";
        if (jsonObject.containsKey("name"))
            festivalName = jsonObject.getString("name");

        JsonArray jsonArray = jsonObject.getJsonArray("bands");
        for (JsonObject bandObject : jsonArray.getValuesAs(JsonObject.class)) {
            String recordLabel = "RECORD LABEL NOT RECEIVED";
            String bandName = "BAND NAME NOT RECEIVED";

            if (bandObject.containsKey("name"))
                if (!bandObject.getString("name").isEmpty())
                    bandName = bandObject.getString("name");

            if (bandObject.containsKey("recordLabel"))
                if (!bandObject.getString("recordLabel").isEmpty())
                    recordLabel = bandObject.getString("recordLabel");

            storeInfo(festivalName, bandName, recordLabel);
        }
    }

    /**
     * Stores info into relevant objects
     *
     * @param festivalName
     * @param bandName
     * @param recordLabel
     */
    public void storeInfo(String festivalName, String bandName, String recordLabel) {
        Band currentBand = getBandInfo(festivalName, bandName);
        boolean recordFound = false;
        boolean bandFound = false;

        for (RecordLabel record : objectSets.getAllRecordLabels()) {
            if (record.getRecordLabelName().equals(recordLabel)) {
                recordFound = true;
                for (Band band : record.getBands()) {
                    if (band.getBandName().equals(bandName))
                        bandFound = true;
                }
                if (!bandFound)
                    record.addBand(currentBand);
            }
        }

        if (!recordFound) {
            RecordLabel currentLabel = new RecordLabel(recordLabel);
            currentLabel.addBand(currentBand);
            objectSets.addRecordLabel(currentLabel);
        }
    }

    /**
     * Checks to see if band was previously stored and appends festival if required
     *
     * @param festivalName
     * @param bandName
     * @return currentBand
     */
    public Band getBandInfo(String festivalName, String bandName) {
        Band currentBand = new Band(bandName);
        currentBand.addFestival(festivalName);

        boolean bandExists = false;
        for (Band band : objectSets.getAllBands()) {
            if (band.getBandName().equals(bandName)) {
                band.addFestival(festivalName);
                currentBand = band;
                bandExists = true;
            }
        }

        if (!bandExists)
            objectSets.addBand(currentBand);

        return currentBand;
    }

}