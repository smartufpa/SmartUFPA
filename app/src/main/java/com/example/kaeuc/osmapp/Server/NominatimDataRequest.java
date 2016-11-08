package com.example.kaeuc.osmapp.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by MESTRADO on 08/11/2016.
 */

public class NominatimDataRequest extends AsyncTask<String,Void,ArrayList<POI>> {

    private Context parentContext;
    private ProgressBar progressBar;
    private NominatimDataRequestResponse callBack = null;
    private static final String TAG = "NominatatimDataRequest";

    public NominatimDataRequest(Context parentContext, ProgressBar progressBar) {
        this.parentContext = parentContext;
        this.callBack = (NominatimDataRequestResponse) parentContext;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<POI> doInBackground(String... params) {
        String query = params[0] + ",Belém";
        GeoPoint viewBox = new GeoPoint(Double.valueOf(params[1]),Double.valueOf(params[2]));
        
        NominatimPOIProvider poiProvider = new NominatimPOIProvider("OsmNavigator/1.0");
        return poiProvider.getPOICloseTo(viewBox, query, 50, 0.1);

    }

    @Override
    protected void onPostExecute(ArrayList<POI> pois) {
        super.onPostExecute(pois);
        callBack.nominatimTaskResponse(pois);
        progressBar.setVisibility(View.GONE);


    }


}
