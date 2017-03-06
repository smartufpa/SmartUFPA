package com.example.kaeuc.smartufpa.models;

import android.util.Log;

import com.example.kaeuc.smartufpa.models.overpass.Tags;
import com.example.kaeuc.smartufpa.utils.Constants;

/**
 * Created by kaeuc on 3/6/2017.
 */

public class PlaceFactory {
    public static final String TAG = "PlaceFactory";
    public Place getPlace(final long id, double latitude, double longitude, Tags tags){
        String name = Constants.NO_NAME;
        String shortName = Constants.NO_SHORT_NAME;
        String locName = Constants.NO_NAME;
        String description = Constants.NO_DESCRIPTION;

        if (tags.getName() != null) name = tags.getName();

        else if (tags.getLocName() != null && tags.getName() == null){
            name = tags.getLocName();
            locName = tags.getLocName();
        }

        if(tags.getLocName() != null) locName = tags.getLocName();


        if(tags.getShortName() != null) shortName = tags.getShortName();

        if(tags.getDescription() != null) description = tags.getDescription();
        final Place place = new Place(id, latitude, longitude, name, shortName,
                locName,tags.getShop(),tags.getAmenity(),description);


        return place;
    }

}
