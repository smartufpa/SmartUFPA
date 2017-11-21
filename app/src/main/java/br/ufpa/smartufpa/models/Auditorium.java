package br.ufpa.smartufpa.models;

import android.os.Parcel;

import br.ufpa.smartufpa.models.smartufpa.Library;
import br.ufpa.smartufpa.models.smartufpa.Place;

/**
 * Created by kaeuc on 21/11/2017.
 */

public class Auditorium extends Place {
    public Auditorium(Long id, double latitude, double longitude, String name, String shortName, String locName, String description) {
        super(id, latitude, longitude, name, shortName, locName, description);
    }

    public Auditorium(double latitude, double longitude, String name) {
        super(latitude, longitude, name);
    }

    protected Auditorium(Parcel in) {
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
