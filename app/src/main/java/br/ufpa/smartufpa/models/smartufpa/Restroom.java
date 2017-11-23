package br.ufpa.smartufpa.models.smartufpa;

import android.os.Parcel;

/**
 * Created by kaeuc on 21/11/2017.
 */

public class Restroom extends Place {

    public Restroom(Long id,double latitude, double longitude, String name) {
        super(id,latitude, longitude, name);
    }

    public Restroom(Parcel in) {
        super(in);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Restroom(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Restroom[size];
        }
    };

    @Override
    public void rate() {

    }
}
