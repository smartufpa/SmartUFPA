package com.example.kaeuc.smartufpa.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by kaeuc on 9/29/2016.
 */

public class Place implements Serializable {
    private String amenity;
    private String description;
    private long ID;
    private double latitude;
    private String locName;
    private double longitude;
    private String name;
    private String shop;
    private String shortName;

    public Place(final long id, double latitude, double longitude, String name, String shortName,
                 String locName, String shop, String amenity, String description) {
        this.ID = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shortName = shortName;
        this.locName = locName;
        this.shop = shop;
        this.amenity = amenity;
        this.description = description;
        this.name = name;
    }
    public Place(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public String getLocName() {
        return locName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GeoPoint getGeoPoint(){return  new GeoPoint(latitude,longitude);}

    @Override
    public String toString() {
        return String.format("[id=%s,lat=%f,lon=%f,name=%s,short_name=%s,shop=%s,amenity=%s,loc_name=%s,description=%s]",
                ID,latitude,longitude,name,shortName,shop,amenity,locName,description);
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


}
