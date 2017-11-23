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
        ADMINISTRATIVE, INSTITUTE
    }

    private List<Department> departmentList;
    private List<Library> libraryList;

    public Building(@Nullable Long id, double latitude, double longitude, String name,
                    String shortName, String locName,String description) {
        super(id, latitude, longitude, name, shortName, locName,description);
    }

    public Building(Long id,double latitude, double longitude, String name) {
        super(id,latitude, longitude, name);
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

    @Override
    public void rate() {

    }

}
