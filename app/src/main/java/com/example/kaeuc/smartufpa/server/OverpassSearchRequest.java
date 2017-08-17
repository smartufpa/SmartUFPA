package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;


import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.JsonParser;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaeuc on 02/03/2017.
 */

public class OverpassSearchRequest extends AsyncTask<String,Void,String> {
    public final String TAG = OverpassSearchRequest.class.getSimpleName();
    private OnOverpassListener callBack;
    private Context parentContext;
    private int taskStatus;

    public OverpassSearchRequest(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnOverpassListener) parentContext;
        this.taskStatus = Constants.SERVER_RESPONSE_SUCCESS;
    }

    public interface OnOverpassListener {
        void onOverpassResponse(ArrayList<Place> places, int taskStatus);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        ArrayList<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
        if(places.isEmpty()){
            taskStatus = Constants.SERVER_RESPONSE_NO_CONTENT;
            callBack.onOverpassResponse(places,taskStatus);
        }else{
            callBack.onOverpassResponse(places,taskStatus);
        }
    }

    private String buildSearchQuery(String userQuery){
        // Tratamento da query para eliminar espaços extras e espaço no final da string
        userQuery = userQuery.replaceAll("\\s+", " ");
        if (Character.isWhitespace(userQuery.charAt(userQuery.length()-1))){
            userQuery = userQuery.substring(0,userQuery.length()-1);
        }
        return String.format(Constants.QUERY_OVERPASS_SEARCH,userQuery,userQuery,
                userQuery,userQuery,userQuery,userQuery);
    }
}