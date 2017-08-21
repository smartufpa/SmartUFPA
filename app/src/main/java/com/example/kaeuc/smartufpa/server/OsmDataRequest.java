package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;

import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.Constants.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.Constants.OverlayTags;
import com.example.kaeuc.smartufpa.utils.Constants.OverpassFilters;
import com.example.kaeuc.smartufpa.utils.JsonParser;
import com.example.kaeuc.smartufpa.models.Place;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe responsável por executar busca de Pontos de Interesse Específicos baseado na busca do usuário
 * utilizando a Overpass query language de maneira assíncrona
 * Referência: http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL
 * Recebe um {@link OverpassFilters} contendo qual informação/searchFilter deseja ser buscado e retorna uma lista com os locais encontrados
 */

public class OsmDataRequest extends AsyncTask<OverpassFilters,Void,String> {

    private static final String TAG = OsmDataRequest.class.getSimpleName() ;
    // interface resposável por devolver o resultado da task pra atividade principal
    private OnOsmDataListener callBack;

    private OverpassFilters searchFilter;
    private int taskStatus;
    private Context parentContext;

    private OverlayTags overlayTag;
    private MarkerTypes markersType;

    public OsmDataRequest(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnOsmDataListener) parentContext;
        this.taskStatus = Constants.SERVER_RESPONSE_SUCCESS;
    }

    public interface OnOsmDataListener {
        void onOsmDataResponse(final List<Place> places, MarkerTypes markersType, OverlayTags overlayTag, int taskStatus);
    }


    @Override
    protected String doInBackground(OverpassFilters... params) {
        searchFilter = params[0];
        String query = null;
        String jsonResponse = null;

        if(searchFilter.equals(OverpassFilters.RESTAURANT)) {

            query = Constants.QUERY_OVERPASS_RESTAURANT;
            overlayTag = OverlayTags.FILTER_RESTAURANT;
            markersType = MarkerTypes.RESTAURANT;

        }else if(searchFilter.equals(OverpassFilters.RESTROOM)) {

            query = Constants.QUERY_OVERPASS_TOILETS;
            overlayTag = OverlayTags.FILTER_RESTROOM;
            markersType = MarkerTypes.RESTROOM;

        }else if(searchFilter.equals(OverpassFilters.XEROX)) {

            query = Constants.QUERY_OVERPASS_XEROX;
            overlayTag = OverlayTags.FILTER_XEROX;
            markersType = MarkerTypes.XEROX;

        }else if(searchFilter.equals(OverpassFilters.AUDITORIUMS)) {

            query = Constants.QUERY_OVERPASS_AUDITORIUMS;
            overlayTag = OverlayTags.FILTER_AUDITORIUMS;
            markersType = MarkerTypes.AUDITORIUM;

        }else if(searchFilter.equals(OverpassFilters.LIBRARIES)) {

            query = Constants.QUERY_OVERPASS_LIBRARIES;
            overlayTag = OverlayTags.FILTER_LIBRARIES;
            markersType = MarkerTypes.LIBRARY;
        }

        try {
            jsonResponse = HttpRequest.makeGetRequest(Constants.URL_OVERPASS_SERVER,query);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            taskStatus = Constants.SERVER_RESPONSE_TIMEOUT;
        }

        return jsonResponse;
    }

    // executa após a operação ser finalizada
    @Override
    protected void onPostExecute(String jsonResponse) {
        // TODO TREAT TIMEOUT
        super.onPostExecute(jsonResponse);
        //Recebe a resposta em json e processa os lugares em uma lista
        final List<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
        // Retorna os valores para a activity que chamou a ASyncTask
        callBack.onOsmDataResponse(places, markersType, overlayTag, taskStatus);

    }

}