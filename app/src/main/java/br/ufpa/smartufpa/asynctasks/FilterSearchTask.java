package br.ufpa.smartufpa.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import br.ufpa.smartufpa.utils.HttpRequest;
import br.ufpa.smartufpa.asynctasks.interfaces.OnFilterSearchListener;
import br.ufpa.smartufpa.models.Place;
import br.ufpa.smartufpa.utils.ConfigHelper;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.OverpassHelper;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.OverlayTags;
import br.ufpa.smartufpa.utils.enums.OverpassFilters;
import br.ufpa.smartufpa.utils.enums.ServerResponse;
import br.ufpa.smartufpa.utils.JsonParser;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


/**
 * AsyncTask responsible for executing searches for specific Points of Interest based on
 * filters. Utilizes Overpass Query Language.
 * The query construction is made by OverpassHelper class.
 * @see OverpassHelper
 * @author kaeuchoa
 */

public class FilterSearchTask extends AsyncTask<OverpassFilters,Void,String> {

    private static final String TAG = FilterSearchTask.class.getSimpleName() ;

    // Parent Context that implements OnFilterSearchListener which will receive the results
    private OnFilterSearchListener callBack;
    private Context parentContext;

    private OverpassFilters searchFilter;
    private ServerResponse taskStatus;
    private OverlayTags overlayTag;
    private MarkerTypes markersType;

    private OverpassHelper overpassHelper;

    public FilterSearchTask(Context parentContext) {
        this.parentContext = parentContext;
        this.callBack = (OnFilterSearchListener) parentContext;
        this.taskStatus = ServerResponse.SUCCESS;
        this.overpassHelper = OverpassHelper.getInstance(parentContext);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected String doInBackground(OverpassFilters... params) {
        searchFilter = params[0];
        URL url = null;
        String jsonResponse = null;


        if(searchFilter.equals(OverpassFilters.FOOD)) {
            url = overpassHelper.getFoodURL();
            overlayTag = OverlayTags.FILTER_FOOD;
            markersType = MarkerTypes.FOOD;
        }else if(searchFilter.equals(OverpassFilters.RESTROOM)) {
            url = overpassHelper.getRestroomURL();
            overlayTag = OverlayTags.FILTER_RESTROOM;
            markersType = MarkerTypes.RESTROOM;
        }else if(searchFilter.equals(OverpassFilters.XEROX)) {
            url = overpassHelper.getXeroxURL();
            overlayTag = OverlayTags.FILTER_XEROX;
            markersType = MarkerTypes.XEROX;
        }else if(searchFilter.equals(OverpassFilters.AUDITORIUMS)) {
            url = overpassHelper.getAuditoriumsURL();
            overlayTag = OverlayTags.FILTER_AUDITORIUMS;
            markersType = MarkerTypes.AUDITORIUM;
        }else if(searchFilter.equals(OverpassFilters.LIBRARIES)) {
            url = overpassHelper.getLibrariesURL();
            overlayTag = OverlayTags.FILTER_LIBRARIES;
            markersType = MarkerTypes.LIBRARY;
        }

        try {
            jsonResponse = HttpRequest.makeGetRequest(url);
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
            callBack.onFilterSearchResponse(null, null, null, taskStatus);

        }else if(taskStatus.equals(ServerResponse.SUCCESS)){
            try{
                final ArrayList<Place> places = JsonParser.parseOverpassResponse(jsonResponse);
                callBack.onFilterSearchResponse(places, markersType, overlayTag, taskStatus);
            }catch (JsonParser.EmptyResponseException e){
                taskStatus = ServerResponse.EMPTY_RESPONSE;
                callBack.onFilterSearchResponse(null, null, null, taskStatus);
            } catch (JsonParser.IrregularQueryException e ){
                taskStatus = ServerResponse.INTERNAL_ERROR;
                callBack.onFilterSearchResponse(null,null,null,taskStatus);
            }

        }


    }

}