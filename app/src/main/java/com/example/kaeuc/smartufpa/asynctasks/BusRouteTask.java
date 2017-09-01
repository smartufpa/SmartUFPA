package com.example.kaeuc.smartufpa.asynctasks;

import android.os.AsyncTask;

import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnBusRouteListener;
import com.example.kaeuc.smartufpa.utils.BusRouteKmlStyler;
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
 * https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4
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

        // Build the query
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.URL_OVERPASS_SERVER);
        try {
            builder.append(URLEncoder.encode(Constants.QUERY_OVERPASS_BUS_ROUTE,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String finalUrl = builder.toString();

        KmlDocument kmlDocument = new KmlDocument();
        // true if ok, false if technical error.
        if(overpassProvider.addInKmlFolder(kmlDocument.mKmlRoot, finalUrl)){
            KmlFeature.Styler busRouteStyler = new BusRouteKmlStyler(mapFragment.getContext());
            FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView,null, busRouteStyler, kmlDocument);
            return kmlOverlay;
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
