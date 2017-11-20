package br.ufpa.smartufpa.utils;

import br.ufpa.smartufpa.models.PlaceTranslator;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.overpass.OverpassModel;
import br.ufpa.smartufpa.models.Place;

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

    public static List<Place> parseLocalResponse(String jsonResponse) {
        return null;
    }

    public static ArrayList<Place> parseOverpassResponse(String jsonInput) throws EmptyResponseException {
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

        ArrayList<Place> listOfPlaces = new ArrayList<>();

        for (Element element : overpassModel.getElements()) {
            final Place place = translator.elementToPlace(element);
            listOfPlaces.add(place);
        }

        return listOfPlaces;
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
