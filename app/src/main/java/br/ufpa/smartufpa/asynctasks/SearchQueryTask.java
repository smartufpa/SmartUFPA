package br.ufpa.smartufpa.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import br.ufpa.smartufpa.utils.HttpRequest;
import br.ufpa.smartufpa.asynctasks.interfaces.OnSearchQueryListener;
import br.ufpa.smartufpa.models.Place;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.JsonParser;
import br.ufpa.smartufpa.utils.enums.ServerResponse;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Stable Commit (20/09)
 * AsyncTask responsible for executing searches for specific Points of Interest based on
 * user's queries. Utilizes Overpass Query Language.
 * Search query is pre-stored on Constants helper class and the appended with the user query.
 * @see Constants
 * @author kaeuchoa
 */

public class SearchQueryTask extends AsyncTask<String,Void,String> {

    public final String TAG = SearchQueryTask.class.getSimpleName();

    // Parent Context that implements OnSearchQueryListener which will receive the results
    private OnSearchQueryListener callBack;

    private Context parentContext;

    private ServerResponse taskStatus;

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
        return String.format(Locale.US,Constants.QUERY_OVERPASS_SEARCH,userQuery,userQuery,
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
          }catch (JsonParser.EmptyResponseException e){
              Log.e(TAG,"", e);
              taskStatus = ServerResponse.EMPTY_RESPONSE;
              callBack.onSearchQueryResponse(null,taskStatus);
          }

        }
    }



}