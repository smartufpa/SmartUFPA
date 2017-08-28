package com.example.kaeuc.smartufpa.utils;

import android.util.Log;

import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.util.BoundingBox;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe para manter constantes que serão usado diretamente no código.
 *
 */

public final class Constants {

    public static final String TAG_COPYSHOP = "copyshop";
    public static final String TAG_TOILETS = "toilets";
    public static final String TAG_EXHIBITION_CENTRE = "exhibition_centre";
    public static final String TAG_LIBRARY = "library";
    public static final String TAG_RESTAURANT = "restaurant";
    public static final String TAG_FOOD_COURT = "food_court";











    //URLS
    public static final String URL_LOCAL_HOST = "http://177.194.168.159:80/";
    public static final String URL_BUS_LOCATION = "http://104.41.62.111:8080/bus-location";
    public static final String URL_OVERPASS_SERVER = "http://overpass-api.de/api/interpreter?data=";

    // Overpass API url
    
    // Queries compactadas para url
    public static final String QUERY_OVERPASS_TOILETS = "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"toilets\"=\"yes\"];way(area.a)[\"amenity\"=\"toilets\"];" +
            "node(area.a)[\"amenity\"=\"toilets\"];);" +
            "out body center;";
    public static final String QUERY_OVERPASS_RESTAURANT = "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"amenity\"=\"restaurant\"];" +
            "way(area.a)[\"amenity\"=\"food_court\"];" +
            "node(area.a)[\"amenity\"=\"restaurant\"];);" +
            "out body center;";
    public static final String QUERY_OVERPASS_XEROX = "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"shop\"=\"copyshop\"];" +
            "node(area.a)[\"shop\"=\"copyshop\"];);" +
            "out body center;\n";
    public static final String QUERY_OVERPASS_AUDITORIUMS = "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"amenity\"=\"exhibition_centre\"];" +
            "way(area.a)[\"amenity\"=\"exhibition_centre\"];" +
            "node(area.a)[\"amenity\"=\"exhibition_centre\"];);" +
            "out body center;";
    public static final String QUERY_OVERPASS_LIBRARIES = "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"amenity\"=\"library\"];" +
            "way(area.a)[\"amenity\"=\"library\"];" +
            "node(area.a)[\"amenity\"=\"library\"];);" +
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

    // Usado na factory para places, apresentado nos detalhes sobre o local
    // caso um nome não tenha sido definido
    public static final String NO_NAME = "Sem nome identificado";
    public static final String NO_LOCAL_NAME = "Sem nome local identificado";
    public static final String NO_SHORT_NAME = "Sem abreviação";
    public static final String NO_DESCRIPTION = "Sem descrição";
    
    public static final int TUTORIAL_EXECUTED = 1;
    public static final int TUTORIAL_NOT_EXECUTED = 0;


    public static final int TUTORIAL_BTN_LEFT = 1;



    ;
}
