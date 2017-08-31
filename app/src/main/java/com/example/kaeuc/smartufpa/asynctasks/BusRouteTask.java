package com.example.kaeuc.smartufpa.asynctasks;

import android.os.AsyncTask;

import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.interfaces.OnBusRouteListener;
import com.example.kaeuc.smartufpa.utils.BusRouteKmlStyler;
import com.example.kaeuc.smartufpa.utils.Constants;

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

    private OnBusRouteListener callback;

    public BusRouteTask(MapFragment mapFragment, MapView mapView) {
        this.mapFragment = mapFragment;
        this.callback = mapFragment;
        this.mapView = mapView;
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
        // TODO: TRATAR ERRO DE PERDA DE CONEXÃO DURANTE O REQUEST
        if(overpassProvider.addInKmlFolder(kmlDocument.mKmlRoot, finalUrl)){
            KmlFeature.Styler busRouteStyler = new BusRouteKmlStyler(mapFragment.getContext());
            FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView,null, busRouteStyler, kmlDocument);
            return kmlOverlay;

        }
        return null;
    }


    @Override
    protected void onPostExecute(Overlay overlay) {
        super.onPostExecute(overlay);
        callback.onBusRouteResponse(overlay);
    }
}
