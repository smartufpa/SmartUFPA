package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.enums.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.enums.OverlayTags;
import com.example.kaeuc.smartufpa.utils.enums.OverpassFilters;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;
import com.example.kaeuc.smartufpa.utils.HttpRequest;
import com.example.kaeuc.smartufpa.utils.JsonParser;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;



/**
 * Created by kaeuc on 10/22/2016.
 * Classe responsável por executar busca de Pontos de Interesse Específicos baseado em filtros
 * utilizando a Overpass query language de maneira assíncrona
 * Referência: http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL
 * Recebe um {@link OverpassFilters} contendo qual searchFilter deseja ser buscado e retorna uma lista com os locais encontrados
 */

public class FilterSearchRequest extends AsyncTask<OverpassFilters,Void,String> {

    private static final String TAG = FilterSearchRequest.class.getSimpleName() ;
    // interface resposável por devolver o resultado da task pra atividade principal
    private OnFilterSearchListener callBack;

    private OverpassFilters searchFilter;
    private ServerResponse taskStatus;
    private Context parentContext;

    private OverlayTags overlayTag;
    private MarkerTypes markersType;

    public FilterSearchRequest(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnFilterSearchListener) parentContext;
        this.taskStatus = ServerResponse.SUCCESS;
    }

    public interface OnFilterSearchListener {
        void onFilterSearchResponse(final ArrayList<Place> places, MarkerTypes markersType, OverlayTags overlayTag, ServerResponse taskStatus);
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
            taskStatus = ServerResponse.TIMEOUT;
            Log.e(TAG, "Request response took too long.", e);
        }

        return jsonResponse;
    }

    // executa após a operação ser finalizada
    @Override
    protected void onPostExecute(String jsonResponse) {

        super.onPostExecute(jsonResponse);
        if(taskStatus.equals(ServerResponse.TIMEOUT)){
            callBack.onFilterSearchResponse(null, null, null, taskStatus);

        }else if(taskStatus.equals(ServerResponse.SUCCESS)){
            //Recebe a resposta em json e processa os lugares em uma lista
            final ArrayList<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
            // Retorna os valores para a activity que chamou a ASyncTask
            callBack.onFilterSearchResponse(places, markersType, overlayTag, taskStatus);
        }


    }

}