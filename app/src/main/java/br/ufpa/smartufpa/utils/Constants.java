package br.ufpa.smartufpa.utils;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe para manter constantes que serão usado diretamente no código.
 * Reference: http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL
 *
 */

public final class Constants {

    public static final String TAG_COPYSHOP = "copyshop";
    public static final String TAG_TOILETS = "toilets";
    public static final String TAG_EXHIBITION_CENTRE = "exhibition_centre";
    public static final String TAG_LIBRARY = "library";
    public static final String TAG_FOOD = "food";
    public static final String TAG_FOOD_COURT = "food_court";

    //URLS
    public static final String URL_LOCAL_HOST = "http://177.194.168.159:80/";
    public static final String URL_BUS_LOCATION = "http://104.41.62.111:8080/bus-location";
    public static final String URL_OVERPASS_SERVER = "http://overpass-api.de/api/interpreter?data=";

    // Overpass API url
    
    // Queries compactadas para url
    public static final String QUERY_OVERPASS_RESTROOM = "[out:json][timeout:30];" +
            "(way[\"toilets\"=\"yes\"](%f,%f,%f,%f);" +
            "way[\"amenity\"=\"toilets\"](%f,%f,%f,%f);" +
            "node[\"amenity\"=\"toilets\"](%f,%f,%f,%f););" +
            "out body center;";


    public static final String QUERY_OVERPASS_FOOD =  "[out:json][timeout:30];" +
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

    // Usado na factory para places, apresentado nos detalhes sobre o local
    // caso um nome não tenha sido definido
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
    public static final String DEFAULT_PLACE_NAME = "default_place_name" ;
    public static final String MAP_REGION_BOUNDS = "map_region_bounds";
}
