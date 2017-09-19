package br.ufpa.smartufpa.models;

import android.support.annotation.Nullable;

import br.ufpa.smartufpa.models.overpass.Tags;
import br.ufpa.smartufpa.utils.Constants;

/**
 * Created by kaeuc on 3/6/2017.
 */

public class PlaceFactory {
    public static final String TAG = PlaceFactory.class.getSimpleName();

    private static PlaceFactory instance;

    private PlaceFactory(){}

    public static synchronized PlaceFactory getInstance(){
        if(instance == null)
            return new PlaceFactory();
        return instance;
    }

    public Place createPlace(@Nullable final Long id, double latitude, double longitude, Tags tags){
        String name = Constants.NO_NAME;
        String shortName = Constants.NO_SHORT_NAME;
        String locName = Constants.NO_LOCAL_NAME;
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
