package br.ufpa.smartufpa.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import br.ufpa.smartufpa.utils.HttpRequest;
import br.ufpa.smartufpa.asynctasks.interfaces.OnSearchQueryListener;
import br.ufpa.smartufpa.models.smartufpa.POI;
import br.ufpa.smartufpa.utils.JsonParser;
import br.ufpa.smartufpa.utils.osm.OverpassHelper;
import br.ufpa.smartufpa.utils.enums.ServerResponse;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * AsyncTask responsible for executing searches for specific Points of Interest based on
 * user's queries. Utilizes Overpass Query Language.
 * The query construction is made by OverpassHelper class.
 * @see OverpassHelper
 * @author kaeuchoa
 */

public class SearchQueryTask extends AsyncTask<String,Void,String> {

    public final String TAG = SearchQueryTask.class.getSimpleName();

    // Parent Context that implements OnSearchQueryListener which will receive the results
    private OnSearchQueryListener callBack;

    private Context parentContext;

    private ServerResponse taskStatus;
    private OverpassHelper overpassHelper;

    public SearchQueryTask(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnSearchQueryListener) parentContext;
        this.taskStatus = ServerResponse.SUCCESS ;
        this.overpassHelper = OverpassHelper.getInstance(parentContext.getApplicationContext());
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonResponse = null;
        try {
            jsonResponse = HttpRequest.makeGetRequest(overpassHelper.getSearchURL(params[0]));
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
              ArrayList<POI> POIS = JsonParser.parseOverpassResponse(jsonResponse);
              callBack.onSearchQueryResponse(POIS,taskStatus);
          }catch (JsonParser.EmptyResponseException e){
              Log.e(TAG,"", e);
              taskStatus = ServerResponse.EMPTY_RESPONSE;
              callBack.onSearchQueryResponse(null,taskStatus);
          }

        }
    }



}