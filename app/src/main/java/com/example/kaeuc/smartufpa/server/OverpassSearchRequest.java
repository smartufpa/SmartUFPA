package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.HttpRequest;
import com.example.kaeuc.smartufpa.utils.JsonParser;
import com.example.kaeuc.smartufpa.utils.JsonParser.EmptyResponseException;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kaeuc on 02/03/2017.
 */

public class OverpassSearchRequest extends AsyncTask<String,Void,String> {
    public final String TAG = OverpassSearchRequest.class.getSimpleName();
    private OnOverpassListener callBack;
    private Context parentContext;
    private ServerResponse taskStatus;


    // TODO (STABLE VERSION): INCLUDE POIS ONLY WITHIN GIVEN COORDINATES
    public OverpassSearchRequest(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnOverpassListener) parentContext;
        this.taskStatus = ServerResponse.SUCCESS ;
    }

    public interface OnOverpassListener {
        void onOverpassResponse(final ArrayList<Place> places, ServerResponse taskStatus);
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

    @Override
    protected String doInBackground(String... params) {
        String jsonResponse = null;
        try {
            jsonResponse = HttpRequest.makeGetRequest(Constants.URL_OVERPASS_SERVER, buildSearchQuery(params[0]));
        } catch (SocketTimeoutException e) {
            taskStatus = ServerResponse.TIMEOUT;
            Log.e(TAG, "Request response took too long.", e);
        }
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute(jsonResponse);

      if(taskStatus.equals(ServerResponse.TIMEOUT)){
            taskStatus = ServerResponse.TIMEOUT;
            callBack.onOverpassResponse(null,taskStatus);
        }else if(taskStatus.equals(ServerResponse.SUCCESS)){
          try{
              ArrayList<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
              callBack.onOverpassResponse(places,taskStatus);
          }catch (EmptyResponseException e){
              Log.e(TAG,"", e);
              taskStatus = ServerResponse.EMPTY_BODY;
              callBack.onOverpassResponse(null,taskStatus);
          }

      }
    }



}