package com.example.kaeuc.smartufpa.utils;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe para manter constantes que serão usado diretamente no código, para evitar a utilização
 * de resources.
 */

public final class Constants {
    // Usados na requisição do nominatim e controle de camadas plotadas no mapa
    public static final String FILTER_XEROX = "filter=copyshop";
    public static final String FILTER_RESTAURANT = "filter=restaurant";
    public static final String FILTER_TOILETS = "filter=toilets" ;

    //URLS
    public static final String URL_LOCAL_HOST = "http://192.168.0.25:80/smart-ufpa/testmysql.php?";
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

    // Usados no controle de camadas plotadas no mapa
    public static final String LAYER_ROUTE = "camada-de-rota";
    public static final String LAYER_SEARCH = "camada-de-busca";
    public static final String LAYER_BUS_ROUTE = "camada-de-onibus";
    public static final String LAYER_MY_LOCATION = "camada-de-localização-atual";
    public static final String LAYER_BUS_MARKER = "marcador-do-circular";


    // Usado no parser do xml retornado na requisição ao XAPI, apresentado nos detalhes sobre o local
    // caso um nome não tenha sido definido
    public static final String NAMEPLACE_UNKNOWN = "Nome ainda não identificado";


    // Chave para a utilização do Graphhopper como provedor de busca de rotas
    public static final String GRAPHHOPPER_KEY = "9931dfa3-e158-4c74-b907-e808313ef176";


    // Constantes de Erro
    public static final int SERVER_RESPONSE_TIMEOUT = 408;
    public static final int SERVER_RESPONSE_NO_CONTENT = 204;
    public static final int SERVER_RESPONSE_SUCESS = 200;
    public static final int SERVER_INTERNAL_ERROR = 500;



}
