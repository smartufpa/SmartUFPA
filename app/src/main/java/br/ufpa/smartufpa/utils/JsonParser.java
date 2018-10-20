package br.ufpa.smartufpa.utils;

import br.ufpa.smartufpa.models.PlaceTranslator;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.overpass.OverpassModel;
import br.ufpa.smartufpa.models.smartufpa.POI;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Stable Commit (20/09)
 * Class responsible for processing the JSON response from Overpass servers and return the places
 * to the activity in list.
 *
 * @author kaeuchoa
 */

public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    public static List<POI> parseLocalResponse(String jsonResponse) {
        return null;
    }

    public static ArrayList<POI> parseOverpassResponse(String jsonInput) throws EmptyResponseException {
        Gson gson = new Gson();
        OverpassModel overpassModel;
        // Tries to convert the string input to an instance of OverpassModel
        try {
            overpassModel = gson.fromJson(jsonInput, OverpassModel.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new IrregularQueryException("Your query presents an syntax error.");
        }
        // If the query is right but there are no results
        if (overpassModel.isEmpty()) {
            throw new EmptyResponseException("The response for your query is empty");
        }

        // Translates the overpassmodel to place model
        final PlaceTranslator translator = PlaceTranslator.getInstance();

        ArrayList<POI> listOfPOIS = new ArrayList<>();

        for (Element element : overpassModel.getElements()) {
            final POI POI = translator.elementToPlace(element);
            listOfPOIS.add(POI);
        }

        return listOfPOIS;
    }

    public static class EmptyResponseException extends RuntimeException {
        private EmptyResponseException(String message) {
            super(message);
        }
    }

    public static class IrregularQueryException extends RuntimeException {
        private IrregularQueryException(String message) {
            super(message);
        }
    }


}
