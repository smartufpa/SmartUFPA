package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.JsonParser;
import com.example.kaeuc.smartufpa.models.Place;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe responsável por executar busca de Pontos de Interesse Específicos baseado na buca do usuário
 * no servidor do OSM (XAPI - API direta de busca) de maneira assíncrona
 * Referência: http://wiki.openstreetmap.org/wiki/Xapi#Overpass_API
 * Recebe uma String contendo qual informação/filtro deseja ser buscado e retorna uma objeto XML
 * em formato String que deve ser analisado para retirar as localizações relevantes
 */

public class OsmDataRequest extends AsyncTask<String,Void,String> {

    private static final String TAG = "OsmDataRequest" ;
    // interface resposável por devolver o resultado da task pra atividade principal
    private OsmDataRequestResponse callBack;
    private ProgressBar progressBar;
    private String filtro;
    private int taskStatus;

    private Context parentContext;

    public OsmDataRequest(Context parentContext, ProgressBar progressBar) {
        this.parentContext = parentContext;
        this.callBack = (OsmDataRequestResponse) parentContext;
        this.progressBar = progressBar;
        this.taskStatus = Constants.SERVER_RESPONSE_SUCCESS;
    }

    @Override
    protected String doInBackground(String... params) {
        filtro = params[0];
        String query = null;
        String jsonResponse = null;

        if(filtro.equalsIgnoreCase(Constants.FILTER_RESTAURANT))
            query = Constants.QUERY_OVERPASS_RESTAURANT;
        else if(filtro.equalsIgnoreCase(Constants.FILTER_TOILETS))
            query = Constants.QUERY_OVERPASS_TOILETS;
        else if(filtro.equalsIgnoreCase(Constants.FILTER_XEROX))
            query = Constants.QUERY_OVERPASS_XEROX;
        else if(filtro.equalsIgnoreCase(Constants.FILTER_AUDITORIUMS))
            query = Constants.QUERY_OVERPASS_AUDITORIUMS;
        else if(filtro.equalsIgnoreCase(Constants.FILTER_LIBRARIES))
            query = Constants.QUERY_OVERPASS_LIBRARIES;
        try {
            jsonResponse = HttpRequest.makeGetRequest(Constants.URL_OVERPASS_SERVER,query);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            taskStatus = Constants.SERVER_RESPONSE_TIMEOUT;
        }


        return jsonResponse;
    }
    // Mostra a barra de progresso
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    // executa após a operação ser finalizada
    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute(jsonResponse);
        // Recebe o xml em forma de uma String e e analisa as informções relevantes
        final List<Place> locais = JsonParser.parseOsmResponse(jsonResponse);
        // Retorna os valores para a activity que chamou a ASyncTask
        callBack.onOsmTaskResponse(locais,filtro,taskStatus);
        // esconde a barra de progresso
        progressBar.setVisibility(View.GONE);


    }

}