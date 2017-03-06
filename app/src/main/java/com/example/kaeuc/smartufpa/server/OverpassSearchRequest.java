package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.JsonParser;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kaeuc on 02/03/2017.
 */

public class OverpassSearchRequest extends AsyncTask<String,Void,String> {
    public final String TAG = "OverpassSearch";
    private OverpassSearchResponse callBack;
    private Context parentContext;
    private ProgressBar progressBar;
    private int taskStatus;

    public OverpassSearchRequest(Context parentContext, ProgressBar progressBar) {
        this.parentContext = parentContext;
        this.callBack = (OverpassSearchResponse) parentContext;
        this.progressBar = progressBar;
        this.taskStatus = Constants.SERVER_RESPONSE_SUCCESS;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonResponse = null;
        try {
            jsonResponse = HttpRequest.makeGetRequest(Constants.URL_OVERPASS_SERVER, buildSearchQuery(params[0]));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            taskStatus = Constants.SERVER_RESPONSE_TIMEOUT;
        }

        return jsonResponse;

    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute(jsonResponse);
        progressBar.setVisibility(View.GONE);
        ArrayList<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
        callBack.onOverpassTaskResponse(places,taskStatus);

    }

    private String buildSearchQuery(String userQuery){
        // Tratamento da query para eliminar espaços extras e espaço no final da string
        userQuery = userQuery.replaceAll("\\s+", " ");
        if (Character.isWhitespace(userQuery.charAt(userQuery.length()-1))){
            userQuery = userQuery.substring(0,userQuery.length()-1);
        }
        return String.format(Constants.QUERY_OVERPASS_SEARCH,userQuery,userQuery,userQuery,userQuery);
    }
}


