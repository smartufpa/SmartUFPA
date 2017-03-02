package com.example.kaeuc.smartufpa.utils;

import android.util.Log;

import com.example.kaeuc.smartufpa.models.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static ArrayList<Place> parseOsmResponse(String jsonResponse){
        // Lista de locais que será retornado ao fim da execução
        ArrayList<Place> places = new ArrayList<>();

        if(jsonResponse != null){
            try {
                // A raiz do documento é um JSONObject e a partir dela buscamos a chave "elements"
                JSONObject response = new JSONObject(jsonResponse);
                JSONArray elements = response.getJSONArray("elements");

                if(elements.length() != 0){
                    for (int i = 0; i < elements.length(); i++) {
                        // a partir da chave "elements" buscar os objetos relevantes
                        JSONObject element = elements.getJSONObject(i);
                        /* Primeiro indetificar qual o tipo de objeto. Tratamentos diferentes entre
                         * nodes e ways.
                         */
                        String type = element.getString("type");
                        // CASO NODE
                        if(type.equalsIgnoreCase("node")){
                            long id = Long.parseLong(element.getString("id"));
                            double lat = Double.parseDouble(element.getString("lat"));
                            double lon = Double.parseDouble(element.getString("lon"));
                            JSONObject tags = element.getJSONObject("tags");
                            String name = null;
                            String locName = null;
                            for (int j = 0; j < tags.length(); j++) {
                                // Uso do try catch porque essas tags podem ou não existir lançando uma execção
                                try{
                                    name = tags.getString("name");
                                }catch (JSONException e){
                                    name = Constants.NAMEPLACE_UNKNOWN;
                                    try{
                                        locName = tags.getString("loc_name");

                                    }catch (JSONException er){
                                        locName = Constants.NAMEPLACE_UNKNOWN;
                                    }
                                }
                            }
                            if(locName == null)
                                // TODO grab short name e description
                                places.add(new Place(id,lat,lon,name,"",""));
                            else
                                places.add(new Place(id,lat,lon,locName,"",""));

                        // CASO WAY
                        }else if(type.equalsIgnoreCase("way")){
                            long id = Long.parseLong(element.getString("id"));
                            // Ways guardam a lat e lon dentro do elemento "center"
                            JSONObject center = element.getJSONObject("center");
                            double lat = Double.parseDouble(center.getString("lat"));
                            double lon = Double.parseDouble(center.getString("lon"));
                            JSONObject tags = element.getJSONObject("tags");
                            String name = null;
                            String locName = null;
                            for (int j = 0; j < tags.length(); j++) {
                                try{
                                    name = tags.getString("name");

                                }catch (JSONException e){
                                    name = Constants.NAMEPLACE_UNKNOWN;
                                    try{
                                        locName = tags.getString("loc_name");

                                    }catch (JSONException er){
                                        locName = Constants.NAMEPLACE_UNKNOWN;
                                    }
                                }
                            }
                            if(locName == null)
                                places.add(new Place(id,lat,lon,name,"",""));
                            else
                                places.add(new Place(id,lat,lon,locName,"",""));
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG,places.toString());
        return places;
    }

    public static List<Place> parseLocalResponse(String jsonResponse){



        return null;
    }
}
