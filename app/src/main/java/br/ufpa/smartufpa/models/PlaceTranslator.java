package br.ufpa.smartufpa.models;

import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.overpass.Tags;
import br.ufpa.smartufpa.models.smartufpa.Place;
import br.ufpa.smartufpa.utils.Constants;

/**
 * Stable Commit (20/09)
 * @author kaeuchoa
 */

public class PlaceTranslator {
    public static final String TAG = PlaceTranslator.class.getSimpleName();

    private static PlaceTranslator instance;

    private PlaceTranslator(){}

    public static synchronized PlaceTranslator getInstance(){
        if(instance == null)
            return new PlaceTranslator();
        return instance;
    }

    public Place elementToPlace(Element element){
        // Initial values in case there is none coming from JSON parsing
        String name = Constants.NO_NAME;
        String shortName = Constants.NO_SHORT_NAME;
        String locName = Constants.NO_LOCAL_NAME;
        String description = Constants.NO_DESCRIPTION;

        final Tags tags = element.getTags();

        // Sets the name
        if (tags.getName() != null)
            name = tags.getName();
        // If local_name is set but name is not, sets the local name as the name
        else if ((tags.getLocName() != null) && (tags.getName() == null)){
            name = tags.getLocName();
            locName = tags.getLocName();
        }

        if(tags.getShortName() != null)
            shortName = tags.getShortName();

        if(tags.getDescription() != null)
            description = tags.getDescription();
        Place place = null;
        if(element.getType().equals(Constants.JSON_ELEMENT_NODE)){
            place = new Place(element.getId(), element.getLat(),element.getLon(), name, shortName,
                    locName,tags.getShop(),tags.getAmenity(),description);
        }else if(element.getType().equals(Constants.JSON_ELEMENT_WAY)){
            place = new Place(element.getId(), element.getCenter().getLat(),element.getCenter().getLon(),
                    name, shortName,locName,tags.getShop(),tags.getAmenity(),description);
        }

        return place;
    }

    public Element placeToElement(Place place){

        return null;
    }

}
