package br.ufpa.smartufpa.asynctasks;

import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Collections;

import br.ufpa.smartufpa.BuildConfig;
import br.ufpa.smartufpa.asynctasks.interfaces.OnSearchRouteListener;
import br.ufpa.smartufpa.fragments.MapFragment;
import br.ufpa.smartufpa.utils.enums.ServerResponse;

/**
 * Stable Commit (20/09)
 * AsyncTask responsible for executing searches for routes between two specific points on map.
 * At this version (1.0) only works with the current user location and his/her final destination.
 * Utilizes GraphHopperRoadManager.
 * Reference: https://graphhopper.com/api/1/docs/routing/
 * @author kaeuchoa
 */

public class SearchRouteTask extends AsyncTask<GeoPoint,Void,Polyline> {

    private static final String TAG = SearchRouteTask.class.getName();
    // Parent Context that implements OnSearchRouteListener which will receive the results
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
