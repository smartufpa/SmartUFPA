package br.ufpa.smartufpa.models.smartufpa;

import android.os.Parcel;

/**
 * Created by kaeuc on 21/11/2017.
 */

public class Library extends Place {
    public Library(Long id, double latitude, double longitude, String name, String shortName, String locName, String description) {
        super(id, latitude, longitude, name, shortName, locName, description);
    }

    public Library(Long id,double latitude, double longitude, String name) {
        super(id,latitude, longitude, name);
    }

    protected Library(Parcel in) {
        super(in);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Library(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Library[size];
        }
    };

    @Override
    public void rate() {

    }

}
