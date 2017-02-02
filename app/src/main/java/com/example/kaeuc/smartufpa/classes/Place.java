package com.example.kaeuc.smartufpa.classes;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kaeuc on 9/29/2016.
 */

public class Place implements Serializable {
    private double latitude;
    private double longitude;
    private String name;
    private String shortName;
    private long ID;
    private String description;
    private GeoPoint position;

    public Place(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    public Place( final long id, double latitude, double longitude, String name, String shortName, String description) {
        this.ID= id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.shortName = shortName;
    }
    public Place(double latitude, double longitude, String name,String shortName, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.position = new GeoPoint(latitude,longitude);
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GeoPoint getPosition(){return position;}

    @Override
    public String toString() {
        return "Nome: " + this.name + "\nDescrição: " + this.description;
    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",this.ID);
            jsonObject.put("name",this.name);
            jsonObject.put("short_name",this.shortName);
            jsonObject.put("description",this.description);
            jsonObject.put("latitude",this.latitude);
            jsonObject.put("longitude",this.longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    // Converte a ArrayList de POIs retornadas pela busca e transforma para uma da classe Place
    public static ArrayList<Place> convertPOIsToPlaces(final ArrayList<POI> pois){
        ArrayList<Place> places = new ArrayList<>();
        for (POI poi : pois) {
            String poiName = poi.mDescription.substring(0, poi.mDescription.indexOf(","));
            places.add(new Place(poi.mLocation.getLatitude(), poi.mLocation.getLongitude(),
                    poiName,"sem nome curto", poi.mDescription));
        }
        return places;
    }

}
