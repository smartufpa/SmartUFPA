package br.ufpa.smartufpa.utils;

import br.ufpa.smartufpa.models.PlaceFactory;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.overpass.OverpassJsonModel;
import br.ufpa.smartufpa.models.Place;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for processing the JSON response from Overpass servers and return the places
 * to the activity in list.
 * @author kaeuchoa
 */

public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    public static List<Place> parseLocalResponse(String jsonResponse){
        return null;
    }

    public static ArrayList<Place> parseOverpassResponse(String jsonInput) throws EmptyResponseException{
        Gson gson = new Gson();
        OverpassJsonModel overpassJsonModel;
        try{
            overpassJsonModel = gson.fromJson(jsonInput, OverpassJsonModel.class);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
            throw new IrregularQueryException("Your query presents an syntax error.");
        }

        if(overpassJsonModel.getElements().isEmpty()){
            throw new EmptyResponseException("The response for your query is empty");
        }

        PlaceFactory factory = PlaceFactory.getInstance();
        ArrayList<Place> places = new ArrayList<>();
        try {
            for (Element element :
                overpassJsonModel.getElements()) {
                if (element.isCenterEmpty())
                    places.add(factory.createPlace(element.getId(),
                            element.getLat(), element.getLon(), element.getTags()));
                else
                    places.add(factory.createPlace(element.getId(),
                            element.getCenter().getLat(), element.getCenter().getLon(), element.getTags()));

            }
        }catch (NullPointerException e){
            e.printStackTrace();

        }
        return places;
    }

    public static class EmptyResponseException extends RuntimeException{
        private EmptyResponseException(String message) { super(message); }
    }

    public static class IrregularQueryException extends RuntimeException{
        private IrregularQueryException(String message) { super(message); }
    }


}
