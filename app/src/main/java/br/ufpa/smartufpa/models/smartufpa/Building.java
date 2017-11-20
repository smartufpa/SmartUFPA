package br.ufpa.smartufpa.models.smartufpa;

import android.os.Parcel;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by kaeuc on 20/11/2017.
 */

public class Building extends Place {
    // TODO: add more types
    public enum BuildingType {
        ADMINISTRATIVE,
    }

    private List<Department> departmentList;

    public Building(@Nullable Long id, double latitude, double longitude, String name,
                    String shortName, String locName, String shop, String amenity, String description) {

        super(id, latitude, longitude, name, shortName, locName, shop, amenity, description);
    }

    public Building(double latitude, double longitude, String name) {
        super(latitude, longitude, name);
    }

    public Building(Parcel in) {
        super(in);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Building(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Building[size];
        }
    };

}
