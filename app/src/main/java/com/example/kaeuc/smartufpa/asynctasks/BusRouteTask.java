package com.example.kaeuc.smartufpa.asynctasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnBusRouteListener;
import com.example.kaeuc.smartufpa.utils.BusRouteKmlStyler;
import com.example.kaeuc.smartufpa.utils.ConfigHelper;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Overlay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by kaeuc on 31/08/2017.
 * reference: https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4
 */

public class BusRouteTask extends AsyncTask<Void,Void, Overlay> {

    private final MapView mapView;
    private MapFragment mapFragment;
    private ServerResponse taskStatus;

    private OnBusRouteListener callback;

    public BusRouteTask(MapFragment mapFragment, MapView mapView) {
        this.mapFragment = mapFragment;
        this.callback = mapFragment;
        this.mapView = mapView;
        this.taskStatus = ServerResponse.SUCCESS;
    }


    @Override
    protected Overlay doInBackground(Void... voids) {
        OverpassAPIProvider overpassProvider = new OverpassAPIProvider();
        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String[] mapRegionBounds = ConfigHelper.getConfigValue(mapFragment.getContext(), Constants.MAP_REGION_BOUNDS).split(",");
        double north = Double.valueOf(mapRegionBounds[0]);
        double east = Double.valueOf(mapRegionBounds[1]);
        double south = Double.valueOf(mapRegionBounds[2]);
        double west = Double.valueOf(mapRegionBounds[3]);

        // Build the query
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.URL_OVERPASS_SERVER);
        @SuppressLint("DefaultLocale") final String query = String.format(Constants.QUERY_OVERPASS_BUS_ROUTE,
                south,west,north,east,
                south,west,north,east,
                south,west,north,east);
        try {
            builder.append(URLEncoder.encode(query,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String finalUrl = builder.toString();

        KmlDocument kmlDocument = new KmlDocument();
        // true if ok, false if technical error.
        if(overpassProvider.addInKmlFolder(kmlDocument.mKmlRoot, finalUrl)){
            KmlFeature.Styler busRouteStyler = new BusRouteKmlStyler(mapFragment.getContext());
            return kmlDocument.mKmlRoot.buildOverlay(mapView,null, busRouteStyler, kmlDocument);
        }
        taskStatus = ServerResponse.CONNECTION_FAILED;
        return null;
    }


    @Override
    protected void onPostExecute(Overlay overlay) {
        super.onPostExecute(overlay);
        if(overlay == null){
            callback.onBusRouteResponse(null,taskStatus);
        }else{
            callback.onBusRouteResponse(overlay,taskStatus);
        }
    }
}
