package com.example.kaeuc.smartufpa.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnSearchQueryListener;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.HttpRequest;
import com.example.kaeuc.smartufpa.utils.JsonParser;
import com.example.kaeuc.smartufpa.utils.JsonParser.EmptyResponseException;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by kaeuc on 02/03/2017.
 */

public class SearchQueryTask extends AsyncTask<String,Void,String> {

    public final String TAG = SearchQueryTask.class.getSimpleName();

    private OnSearchQueryListener callBack;
    private Context parentContext;

    private ServerResponse taskStatus;


    // TODO (STABLE VERSION): INCLUDE POIs ONLY WITHIN GIVEN COORDINATES
    public SearchQueryTask(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnSearchQueryListener) parentContext;
        this.taskStatus = ServerResponse.SUCCESS ;
    }


    private String buildSearchQuery(String userQuery){
        // Cleans all the white spaces on the query
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

        if(jsonResponse == null) {
            taskStatus = ServerResponse.CONNECTION_FAILED;
            callBack.onSearchQueryResponse(null,taskStatus);
        }else if(taskStatus.equals(ServerResponse.TIMEOUT)){
            taskStatus = ServerResponse.TIMEOUT;
            callBack.onSearchQueryResponse(null,taskStatus);
        }else if(taskStatus.equals(ServerResponse.SUCCESS)){
          try{
              ArrayList<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
              callBack.onSearchQueryResponse(places,taskStatus);
          }catch (EmptyResponseException e){
              Log.e(TAG,"", e);
              taskStatus = ServerResponse.EMPTY_RESPONSE;
              callBack.onSearchQueryResponse(null,taskStatus);
          }

        }
    }



}