package com.example.kaeuc.smartufpa.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.kaeuc.smartufpa.BuildConfig;
import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnSearchRouteListener;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kaeuc on 29/08/2017.
 */

public class SearchRouteTask extends AsyncTask<GeoPoint,Void,Polyline> {

    private static final String TAG = SearchRouteTask.class.getName();


    private OnSearchRouteListener callback;
    private ServerResponse taskStatus;

    public SearchRouteTask(MapFragment parentFragment) {
        this.callback = parentFragment;
        this.taskStatus = ServerResponse.SUCCESS;
    }

    @Override
    protected Polyline doInBackground(GeoPoint... places) {
        // Starts a RoadManager using the key acquired with a Graphhopper account
        RoadManager roadManager = new GraphHopperRoadManager(BuildConfig.GRAPHHOPPER_KEY,true);
        roadManager.addRequestOption("vehicle=foot");
        ArrayList<GeoPoint> wayPoints = new ArrayList<>();
        Collections.addAll(wayPoints, places);
        // Calculates the route
        Road road = roadManager.getRoad(wayPoints);
        if( road.mStatus == 0) // succeed
            return RoadManager.buildRoadOverlay(road);

        taskStatus = ServerResponse.CONNECTION_FAILED;
        return null;
    }

    @Override
    protected void onPostExecute(Polyline routeOverlay) {
        super.onPostExecute(routeOverlay);
        if(routeOverlay != null){
            callback.onSearchRouteResponse(routeOverlay,taskStatus);
        }else if(taskStatus.equals(ServerResponse.CONNECTION_FAILED)){
            callback.onSearchRouteResponse(null,taskStatus);

        }
    }
}
