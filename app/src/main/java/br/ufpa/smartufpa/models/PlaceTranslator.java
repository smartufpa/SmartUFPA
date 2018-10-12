package br.ufpa.smartufpa.models;

import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.overpass.Tags;
import br.ufpa.smartufpa.models.smartufpa.Auditorium;
import br.ufpa.smartufpa.models.smartufpa.Building;
import br.ufpa.smartufpa.models.smartufpa.Copyshop;
import br.ufpa.smartufpa.models.smartufpa.FoodPlace;
import br.ufpa.smartufpa.models.smartufpa.Library;
import br.ufpa.smartufpa.models.smartufpa.Place;
import br.ufpa.smartufpa.models.smartufpa.Restroom;
import br.ufpa.smartufpa.utils.Constants;

/**
 *
 * @author kaeuchoa
 */

public class PlaceTranslator {
    public static final String TAG = PlaceTranslator.class.getSimpleName();

    private static PlaceTranslator instance;

    private PlaceTranslator() {
    }

    public static synchronized PlaceTranslator getInstance() {
        if (instance == null)
            return new PlaceTranslator();
        return instance;
    }

    // TODO: Refatorar as classes de locais para utilizar nos ifs
    public Place elementToPlace(Element element) {
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
        else if ((tags.getLocName() != null) && (tags.getName() == null)) {
            name = tags.getLocName();
            locName = tags.getLocName();
        }

        if (tags.getShortName() != null)
            shortName = tags.getShortName();

        if (tags.getDescription() != null)
            description = tags.getDescription();

        Place place = null;
        // Identifies which type of element is on json
        final String amenity = (element.getTags().getAmenity() == null) ? "" : element.getTags().getAmenity();
        // Special case for copyshops, need to check shop tag
        final String shopTag = (element.getTags().getShop() == null) ? "" : element.getTags().getShop();
        // Special case for toilets, need to check toilet tag
        final String toiletTag = (element.getTags().getToilets() == null) ? "" : element.getTags().getToilets();
        // Special case for buildings, need to check building tag
        final String buildingTag = (element.getTags().getBuilding() == null) ? "" : element.getTags().getBuilding();

            // For Building (OSM tag -> building = yes or building = university)
        if (buildingTag.equals("yes") || buildingTag.equals(Constants.TAG_BUILDING)) {
            // Always check if it's a node or way element
            if (element.getType().equals(Constants.JSON_ELEMENT_NODE)) {
                place = new Building(element.getLat(), element.getLon(), name, shortName,
                        locName, "","" ,"", Building.AdministrativeRole.NONE,"");
            } else if (element.getType().equals(Constants.JSON_ELEMENT_WAY)) {
                place = new Building(element.getId(), element.getCenter().getLat(), name, shortName,
                        locName, "","" ,"",Building.AdministrativeRole.NONE,"");
            }
            // For auditoriums (OSM tag -> amenity = exhibition_centre)
        } else if (amenity.equals(Constants.TAG_EXHIBITION_CENTRE)) {

            // Always check if it's a node or way element
            if (element.getType().equals(Constants.JSON_ELEMENT_NODE)) {
//                place = new Auditorium(element.getId(), element.getLat(), element.getLon(), name, shortName,
//                        locName, description);
            } else if (element.getType().equals(Constants.JSON_ELEMENT_WAY)) {
//                place = new Auditorium(element.getId(), element.getCenter().getLat(), element.getCenter().getLon(),
//                        name, shortName, locName, description);
            }

            // For copyshop (OSM tag -> shop = copyshop)
        } else if (shopTag.equals(Constants.TAG_COPYSHOP)) {
            // Always check if it's a node or way element
            if (element.getType().equals(Constants.JSON_ELEMENT_NODE)) {
//                place = new Copyshop(element.getId(), element.getLat(), element.getLon(), name);
            } else if (element.getType().equals(Constants.JSON_ELEMENT_WAY)) {
                double latitude = element.getCenter().getLat();
                double longitude = element.getCenter().getLon();
//                place = new Copyshop(element.getId(), latitude, longitude, name);
            }

            // For Library (OSM tag -> amenity = library)
        } else if (amenity.equals(Constants.TAG_LIBRARY)) {
            // Always check if it's a node or way element
            if (element.getType().equals(Constants.JSON_ELEMENT_NODE)) {
//                place = new Library(element.getId(), element.getLat(), element.getLon(), name, shortName,
//                        locName, description);
            } else if (element.getType().equals(Constants.JSON_ELEMENT_WAY)) {
//                place = new Library(element.getId(), element.getCenter().getLat(), element.getCenter().getLon(),
//                        name, shortName, locName, description);
            }

            // For FoodPlaces (OSM tag -> amenity = food_court or amenity = restaurant)
        } else if (amenity.equals(Constants.TAG_FOOD_COURT) || amenity.equals(Constants.TAG_RESTAURANT)) {
            // Always check if it's a node or way element
            if (element.getType().equals(Constants.JSON_ELEMENT_NODE)) {
//                place = new FoodPlace(element.getId(), element.getLat(), element.getLon(), name, shortName,
//                        locName, description);
            } else if (element.getType().equals(Constants.JSON_ELEMENT_WAY)) {
//                place = new FoodPlace(element.getId(), element.getCenter().getLat(), element.getCenter().getLon(),
//                        name, shortName, locName, description);
            }
            // For Toilet (OSM tag -> amenity = toilet or toilets = yes)
        } else if (toiletTag.equals("yes") || amenity.equals(Constants.TAG_TOILETS)) {
            // Always check if it's a node or way element
            if (element.getType().equals(Constants.JSON_ELEMENT_NODE)) {
//                place = new Restroom(element.getId(), element.getLat(), element.getLon(), name);
            } else if (element.getType().equals(Constants.JSON_ELEMENT_WAY)) {
//                place = new Restroom(element.getId(), element.getCenter().getLat(), element.getCenter().getLon(),
//                        name);
            }

        } else {
            // Always check if it's a node or way element
            if (element.getType().equals(Constants.JSON_ELEMENT_NODE)) {
                place = new Place(element.getLat(), element.getLon(), name, shortName,locName, "");
            } else if (element.getType().equals(Constants.JSON_ELEMENT_WAY)) {
                place = new Place(element.getCenter().getLat(), element.getCenter().getLon(),
                        name, shortName, locName,"");
            }
        }


        return place;
    }
}
