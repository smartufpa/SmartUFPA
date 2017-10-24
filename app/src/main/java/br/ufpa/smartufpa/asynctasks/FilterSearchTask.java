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
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.OverlayTags;
import br.ufpa.smartufpa.utils.enums.OverpassFilters;
import br.ufpa.smartufpa.utils.enums.ServerResponse;
import br.ufpa.smartufpa.utils.JsonParser;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Stable Commit (20/09)
 * AsyncTask responsible for executing searches for specific Points of Interest based on
 * filters. Utilizes Overpass Query Language.
 * The base query is pre-stored on Constants helper class then appended with the filter chosen.
 * @see Constants
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

            query = String.format(Locale.US,Constants.QUERY_OVERPASS_FOOD,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_FOOD;
            markersType = MarkerTypes.FOOD;

        }else if(searchFilter.equals(OverpassFilters.RESTROOM)) {

            query = String.format(Locale.US,Constants.QUERY_OVERPASS_RESTROOM,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_RESTROOM;
            markersType = MarkerTypes.RESTROOM;

        }else if(searchFilter.equals(OverpassFilters.XEROX)) {

            query = String.format(Locale.US,Constants.QUERY_OVERPASS_XEROX,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_XEROX;
            markersType = MarkerTypes.XEROX;

        }else if(searchFilter.equals(OverpassFilters.AUDITORIUMS)) {

            query = String.format(Locale.US,Constants.QUERY_OVERPASS_AUDITORIUMS,
                    south,west,north,east,
                    south,west,north,east,
                    south,west,north,east);
            overlayTag = OverlayTags.FILTER_AUDITORIUMS;
            markersType = MarkerTypes.AUDITORIUM;

        }else if(searchFilter.equals(OverpassFilters.LIBRARIES)) {

            query = String.format(Locale.US,Constants.QUERY_OVERPASS_LIBRARIES,
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