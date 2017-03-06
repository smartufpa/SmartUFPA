package com.example.kaeuc.smartufpa.utils;

import com.example.kaeuc.smartufpa.models.overpass.Element;
import com.example.kaeuc.smartufpa.models.overpass.OverpassJsonModel;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.models.PlaceFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaeuch on 09/01/2017.
 * Classe responsável por processar a resposta JSON do servidor Overpass e retornar os locais
 * á atividade principal em forma de ArrayList<Place>
 */

public class JsonParser {
    // TAG para logs
    private static final String TAG = "JsonParser";

    public static List<Place> parseLocalResponse(String jsonResponse){
        return null;
    }

    public static ArrayList<Place> parseOverpassResponse(String jsonInput){
        Gson gson = new Gson();
        OverpassJsonModel overpassJsonModel = gson.fromJson(jsonInput, OverpassJsonModel.class);
        PlaceFactory factory = new PlaceFactory();
        ArrayList<Place> places = new ArrayList<>();
        for (Element element :
                overpassJsonModel.getElements()) {
            try {
                if (element.isCenterEmpty())
                    places.add(factory.getPlace(element.getId(),
                            element.getLat(), element.getLon(), element.getTags()));
                else
                    places.add(factory.getPlace(element.getId(),
                            element.getCenter().getLat(), element.getCenter().getLon(), element.getTags()));
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return places;
    }
}
