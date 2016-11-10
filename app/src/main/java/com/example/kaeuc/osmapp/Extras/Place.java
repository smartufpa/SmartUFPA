package com.example.kaeuc.osmapp.Extras;

import org.osmdroid.bonuspack.location.POI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaeuc on 9/29/2016.
 */

public class Place {
    private double latitude;
    private double longitude;
    private String name;
    private String description;

    public Place(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    public Place(double latitude, double longitude, String name, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "Nome: " + this.name + "\nDescrição: " + this.description;
    }

    public static List<Place> convertPOIsToPlaces(final ArrayList<POI> pois){
        List<Place> places = new ArrayList<>();
        for (POI poi : pois) {
            String poiName = poi.mDescription.substring(0, poi.mDescription.indexOf(","));
            places.add(new Place(poi.mLocation.getLatitude(), poi.mLocation.getLongitude(),
                    poiName, poi.mDescription));
        }

        return places;
    }

}
