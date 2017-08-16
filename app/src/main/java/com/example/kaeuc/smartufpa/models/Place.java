package com.example.kaeuc.smartufpa.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.osmdroid.util.GeoPoint;

/**
 * Created by kaeuc on 9/29/2016.
 */

public class Place implements Parcelable {
    private String amenity;
    private String description;
    private long id;
    private double latitude;
    private String locName;
    private double longitude;
    private String name;
    private String shop;
    private String shortName;

    public Place(@Nullable  final Long id, double latitude, double longitude, String name, String shortName,
                 String locName, String shop, String amenity, String description) {
        if(id != null)  this.id = id;
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

    protected Place(Parcel in) {
        amenity = in.readString();
        description = in.readString();
        id = in.readLong();
        latitude = in.readDouble();
        locName = in.readString();
        longitude = in.readDouble();
        name = in.readString();
        shop = in.readString();
        shortName = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

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
                id,latitude,longitude,name,shortName,shop,amenity,locName,description);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amenity);
        dest.writeString(description);
        dest.writeLong(id);
        dest.writeDouble(latitude);
        dest.writeString(locName);
        dest.writeDouble(longitude);
        dest.writeString(name);
        dest.writeString(shop);
        dest.writeString(shortName);
    }
}
