package com.example.kaeuc.osmapp.Extras;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe para manter constantes que serão usado diretamente no código, para evitar a utilização
 * de resources.
 */

public final class Constants {
    // Usados na requisição do nominatim e controle de camadas plotadas no mapa
    public static final String XEROX_URL_FILTER = "[shop=copyshop]";
    public static final String RESTAURANT_URL_FILTER = "[amenity=restaurant]";
    public static final String RESTROOM_URL_FILTER = "[amenity=toilets]" ;
    // Restrição da região da universidade no mapa para a busca
    public static final String BOUNDING_BOX = "[bbox=-48.46069,-1.47956,-48.45348,-1.47158]";
    public static final String XAPI_SERVER_URL = "http://www.overpass-api.de/api/xapi?node";

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
