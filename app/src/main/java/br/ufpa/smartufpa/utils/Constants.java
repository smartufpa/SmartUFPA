package br.ufpa.smartufpa.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Class to keep constants that will be directly used on the code.
 *
 * @author kaeuchoa
 */

public final class Constants {

    public static final String TAG_COPYSHOP = "copyshop";
    public static final String TAG_TOILETS = "toilets";
    public static final String TAG_EXHIBITION_CENTRE = "exhibition_centre";
    public static final String TAG_LIBRARY = "library";
    public static final String TAG_RESTAURANT = "restaurant";
    public static final String TAG_FOOD_COURT = "food_court";
    public static final String TAG_BUILDING = "university";

    //URLS
    public static final String URL_LOCAL_HOST = "http://177.194.168.159:80/";
    public static final String URL_BUS_LOCATION = "http://104.41.62.111:8080/bus-location";
    public static final String URL_OVERPASS_SERVER = "http://overpass-api.de/api/";

    /* Overpass API urls
     * test on https://overpass-turbo.eu/
     * reference: http://wiki.openstreetmap.org/wiki/Overpass_API
     */
    public static final String QUERY_OVERPASS_RESTROOM = "[out:json][timeout:30];" +
            "(way[\"toilets\"=\"yes\"](%f,%f,%f,%f);" +
            "way[\"amenity\"=\"toilets\"](%f,%f,%f,%f);" +
            "node[\"amenity\"=\"toilets\"](%f,%f,%f,%f););" +
            "out body center;";


    public static final String QUERY_OVERPASS_FOOD = "[out:json][timeout:30];" +
            "(way[\"amenity\"=\"restaurant\"](%f,%f,%f,%f);" +
            "way[\"amenity\"=\"food_court\"](%f,%f,%f,%f);" +
            "node[\"amenity\"=\"restaurant\"](%f,%f,%f,%f););" +
            "out body center;";

    public static final String QUERY_OVERPASS_XEROX = "[out:json][timeout:30];" +
            "(way[\"shop\"=\"copyshop\"](%f,%f,%f,%f);" +
            "way[\"shop\"=\"copyshop\"](%f,%f,%f,%f);" +
            "node[\"shop\"=\"copyshop\"](%f,%f,%f,%f););" +
            "out body center;";

    public static final String QUERY_OVERPASS_AUDITORIUMS = "[out:json][timeout:30];" +
            "(way[\"amenity\"=\"exhibition_centre\"](%f,%f,%f,%f);" +
            "way[\"amenity\"=\"exhibition_centre\"](%f,%f,%f,%f);" +
            "node[\"amenity\"=\"exhibition_centre\"](%f,%f,%f,%f););" +
            "out body center;";


    public static final String QUERY_OVERPASS_LIBRARIES = "[out:json][timeout:30];" +
            "(way[\"amenity\"=\"library\"](%f,%f,%f,%f);" +
            "way[\"amenity\"=\"library\"](%f,%f,%f,%f);" +
            "node[\"amenity\"=\"library\"](%f,%f,%f,%f););" +
            "out body center;";

    // TODO: change the name for the name on config file
    public static final String QUERY_OVERPASS_SEARCH = "[out:json][timeout:30];" +
            "area[\"name\" = \"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"name\"~\"%s\",i];" +
            "node(area.a)[\"name\"~\"%s\",i];" +
            "way(area.a)[\"short_name\"~\"%s\",i];" +
            "node(area.a)[\"short_name\"~\"%s\",i];" +
            "way(area.a)[\"loc_name\"~\"%s\",i];" +
            "node(area.a)[\"loc_name\"~\"%s\",i];" +
            ");" +
            "out center;";

    public static final String QUERY_OVERPASS_BUS_ROUTE = "[out:json][timeout:30];" +
            "(relation[name=\"circular\"][highway=bus_stop]);" + "out qt body 500;" +
            "(node[route=bus][name=\"circular\"](%f,%f,%f,%f);" +
            "way[route=bus][name=\"circular\"](%f,%f,%f,%f););" +
            "out qt geom tags 500;relation[route=bus][name=\"circular\"]" +
            "(%f,%f,%f,%f);out qt geom body 500;";

    // TODO: TRANSFER TO STRING XML
    public static final String NO_NAME = "Sem nome identificado";
    public static final String NO_LOCAL_NAME = "Sem nome local identificado";
    public static final String NO_SHORT_NAME = "Sem abreviação";
    public static final String NO_DESCRIPTION = "Sem descrição";

    // App tutorial constants
    public static final int TUTORIAL_EXECUTED = 1;
    public static final int TUTORIAL_NOT_EXECUTED = 0;
    public static final int TUTORIAL_BTN_LEFT = 1;


    // Config file constants
    public static final String DEFAULT_PLACE_COORDINATES = "default_place_coordinates";
    public static final String DEFAULT_PLACE_NAME = "default_place_name";
    public static final String MAP_REGION_BOUNDS = "map_region_bounds";

    // JSON Parse
    public static final String JSON_ELEMENT_NODE = "node";
    public static final String JSON_ELEMENT_WAY = "way";


    public static final String CONFIG_START_CAMERA_COORDINATES = "start_camera_coordinates";
    public static final String CONFIG_MAP_REGION_NAME = "map_region_name";
    public static final String CONFIG_MAP_BUS_ROUTE_NAME = "map_bus_route_name";
    public static final String CONFIG_MAP_BUS_OPERATOR = "map_bus_operator";
    public static final String CONFIG_WEST_LIMIT = "map_region_westlimit";
    public static final String CONFIG_SOUTH_LIMIT = "map_region_southlimit";
    public static final String CONFIG_EAST_LIMIT = "map_region_eastlimit";
    public static final String CONFIG_NORTH_LIMIT = "map_region_northlimit";

    public class OpeningHours {
        public static final String OPENING_DAY = "opening_day";
        public static final String CLOSING_DAY = "closing_day";
        public static final String OPENING_HOUR = "opening_hour";
        public static final String CLOSING_HOUR = "closing_hour";
    }

    public class RequestCode {
        public static final int EDIT_ELEMENT = 100;
    }

    public class SharedPrefs {
        public static final String PREFS_NAME = "app_preferences";
        public static final String KEY_ACCESS_TOKEN = "access_token";
        public static final String KEY_ACCESS_SECRET = "access_secret";
        public static final String KEY_ANDROID_ID = "android_id";

        public static final String KEY_USER_ID = "user_id";
        public static final String KEY_USER_DISPLAY_NAME = "user_display_name";
        public static final String KEY_USER_ACCOUNT_CREATED = "user_account_created";
    }


    public class OsmXmlTags {
        public static final String TAG_OSM = "osm";
        public static final String TAG_OSM_CHANGE = "osmChange";
        public static final String TAG_CHANGESET = "changeset";
        public static final String TAG_CREATE = "create";
        public static final String TAG_MODIFY = "modify";
        public static final String TAG_DELETE = "delete";
        public static final String TAG_TAG = "tag";
        public static final String TAG_NODE = "node";
    }

    public class OsmXmlAttr {
        public static final String ATTR_VERSION = "version";
        public static final String ATTR_GENERATOR = "generator";
        public static final String ATTR_CHANGESET_ID = "changeset";
        public static final String ATTR_ID = "id";
        public static final String ATTR_KEY = "k";
        public static final String ATTR_VALUE = "v";
        public static final String ATTR_LAT = "lat";
        public static final String ATTR_LON = "lon";

    }


    public class ElementTags {
        public static final String TAG_NAME = "name";
        public static final String TAG_SHORT_NAME = "short_name";
        public static final String TAG_LOC_NAME = "loc_name";
        public static final String TAG_SHOP = "shop";
        public static final String TAG_TOILETS = "toilets";
        public static final String TAG_AMENITY = "amenity";
        public static final String TAG_DESCRIPTION = "description";
        public static final String TAG_WEBSITE = "website";
        public static final String TAG_BUILDING = "building";
        public static final String TAG_INDOOR = "indoor";
    }

    public class ErrorCodes{
        public static final String ERROR_CREATE_CHANGESET = "-1";
    }


    public class MapConfig {
        public static final double DEFAULT_ZOOM = 16.0;
        public static final double MIN_ZOOM = 15.0;
        public static final double MAX_ZOOM = 18.0;
    }

}
