package br.ufpa.smartufpa.models.smartufpa;

import android.os.Parcel;

/**
 * Created by kaeuc on 21/11/2017.
 */

public class Copyshop extends Place {

    private double blackAndWhitePrice;
    private double coloredPrice;
    // TODO: ADD catalog
    private boolean hasServices;


    public Copyshop(Long id, double latitude, double longitude, String name, String shortName, String locName, String description) {
        super(id, latitude, longitude, name, shortName, locName, description);
    }

    public Copyshop(double latitude, double longitude, String name) {
        super(latitude, longitude, name);
    }

    public Copyshop(Parcel in) {
        super(in);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Copyshop(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Copyshop[size];
        }
    };

    @Override
    public void rate() {

    }
}
