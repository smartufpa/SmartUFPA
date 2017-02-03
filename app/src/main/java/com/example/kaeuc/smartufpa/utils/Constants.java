package com.example.kaeuc.smartufpa.utils;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe para manter constantes que serão usado diretamente no código, para evitar a utilização
 * de resources.
 */

public final class Constants {
    // Usados na requisição do nominatim e controle de camadas plotadas no mapa
    public static final String XEROX_FILTER = "[filter=copyshop]";
    public static final String RESTAURANT_FILTER = "[filter=restaurant]";
    public static final String TOILETS_FILTER = "[filter=toilets]" ;

    //Local host URL
    public static final String LOCAL_HOST_URL = "http://192.168.0.25:80/smart-ufpa/testmysql.php?";


    // Overpass API url
    public static final String OVERPASS_SERVER_URL = "http://overpass-api.de/api/interpreter?data=";
    // Queries compactadas para url
    public static final String OVERPASS_TOILETS_QUERY= "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"toilets\"=\"yes\"];way(area.a)[\"amenity\"=\"toilets\"];" +
            "node(area.a)[\"amenity\"=\"toilets\"];);" +
            "out body center;";
    public static final String OVERPASS_RESTAURANT_QUERY = "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"amenity\"=\"restaurant\"];" +
            "way(area.a)[\"amenity\"=\"food_court\"];" +
            "node(area.a)[\"amenity\"=\"restaurant\"];);" +
            "out body center;";
    public static final String OVERPASS_XEROX_QUERY = "[out:json][timeout:30];" +
            "area[\"name\"=\"Universidade Federal do Pará\"]->.a;" +
            "(way(area.a)[\"shop\"=\"copyshop\"];" +
            "node(area.a)[\"shop\"=\"copyshop\"];);" +
            "out body center;\n";

    // Usados no controle de camadas plotadas no mapa
    public static final String ROUTE_LAYER = "Camada de rota";
    public static final String SEARCH_LAYER = "Camada de busca";
    public static final String BUS_ROUTE_LAYER = "Camada de onibus";
    public static final String MY_LOCATION_LAYER = "Camada de localização atual";


    // Usado no parser do xml retornado na requisição ao XAPI, apresentado nos detalhes sobre o local
    // caso um nome não tenha sido definido
    public static final String NAMEPLACE_UNKNOWN = "Nome ainda não identificado";


    // Chave para a utilização do Graphhopper como provedor de busca de rotas
    public static final String GRAPHHOPPER_KEY = "9931dfa3-e158-4c74-b907-e808313ef176";


}
