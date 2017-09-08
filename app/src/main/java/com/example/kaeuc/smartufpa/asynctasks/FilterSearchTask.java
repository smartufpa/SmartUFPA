package com.example.kaeuc.smartufpa.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnFilterSearchListener;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.ConfigHelper;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.enums.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.enums.OverlayTags;
import com.example.kaeuc.smartufpa.utils.enums.OverpassFilters;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;
import com.example.kaeuc.smartufpa.utils.HttpRequest;
import com.example.kaeuc.smartufpa.utils.JsonParser;

import java.net.SocketTimeoutException;
import java.util.ArrayList;


/**
 * Created by kaeuc on 10/22/2016.
 * Classe responsável por executar busca de Pontos de Interesse Específicos baseado em filtros
 * utilizando a Overpass query language de maneira assíncrona
 * Referência: http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL
 * Recebe um {@link OverpassFilters} contendo qual searchFilter deseja ser buscado e retorna uma lista com os locais encontrados
 */

public class FilterSearchTask extends AsyncTask<OverpassFilters,Void,String> {

    private static final String TAG = FilterSearchTask.class.getSimpleName() ;
    // interface resposável por devolver o resultado da task pra atividade principal
    private OnFilterSearchListener callBack;
    private Context parentContext;

    private OverpassFilters searchFilter;
    private ServerResponse taskStatus;
    private OverlayTags overlayTag;
    private MarkerTypes markersType;

    public FilterSearchTask(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnFilterSearchListener) parentContext;
        this.taskStatus = ServerResponse.SUCCESS;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected String doInBackground(OverpassFilters... params) {
        searchFilter = params[0];
        String query = null;
        String jsonResponse = null;

        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String[] mapRegionBounds = ConfigHelper.getConfigValue(parentContext, Constants.MAP_REGION_BOUNDS).split(",");
        double north = Double.valueOf(mapRegionBounds[0]);
        double east = Double.valueOf(mapRegionBounds[1]);
        double south = Double.valueOf(mapRegionBounds[2]);
        double west = Double.valueOf(mapRegionBounds[3]);


        if(searchFilter.equals(OverpassFilters.FOOD)) {

            query = String.format(Constants.QUERY_OVERPASS_FOOD,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_FOOD;
            markersType = MarkerTypes.FOOD;

        }else if(searchFilter.equals(OverpassFilters.RESTROOM)) {

            query = String.format(Constants.QUERY_OVERPASS_RESTROOM,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_RESTROOM;
            markersType = MarkerTypes.RESTROOM;

        }else if(searchFilter.equals(OverpassFilters.XEROX)) {

            query = String.format(Constants.QUERY_OVERPASS_XEROX,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_XEROX;
            markersType = MarkerTypes.XEROX;

        }else if(searchFilter.equals(OverpassFilters.AUDITORIUMS)) {

            query = String.format(Constants.QUERY_OVERPASS_AUDITORIUMS,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_AUDITORIUMS;
            markersType = MarkerTypes.AUDITORIUM;

        }else if(searchFilter.equals(OverpassFilters.LIBRARIES)) {

            query = String.format(Constants.QUERY_OVERPASS_LIBRARIES,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
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
        if (jsonResponse == null){
            taskStatus = ServerResponse.CONNECTION_FAILED;
            callBack.onFilterSearchResponse(null,null,null,taskStatus);
        }else if(taskStatus.equals(ServerResponse.TIMEOUT)){
            callBack.onFilterSearchResponse(null, null, null, taskStatus);

        }else if(taskStatus.equals(ServerResponse.SUCCESS)){
            //Recebe a resposta em json e processa os lugares em uma lista
            try{
                final ArrayList<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
                callBack.onFilterSearchResponse(places, markersType, overlayTag, taskStatus);
            }catch (JsonParser.EmptyResponseException e){
                taskStatus = ServerResponse.EMPTY_RESPONSE;
                callBack.onFilterSearchResponse(null, null, null, taskStatus);
            }
            // Retorna os valores para a activity que chamou a ASyncTask
        }


    }

}