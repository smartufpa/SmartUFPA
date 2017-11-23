package br.ufpa.smartufpa.models.smartufpa;

import android.os.Parcel;
import android.support.annotation.Nullable;

/**
 * Created by kaeuc on 20/11/2017.
 */

public class Department extends Place {

    public Department(@Nullable Long id, double latitude, double longitude, String name,
                      String shortName, String locName, String description) {
        super(id, latitude, longitude, name, shortName, locName, description);
    }

    public Department(Long id,double latitude, double longitude, String name) {
        super(id,latitude, longitude, name);
    }

    protected Department(Parcel in) {
        super(in);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Department(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Department[size];
        }
    };

    @Override
    public void rate() {

    }
}
