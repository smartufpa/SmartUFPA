package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

/**
 * Created by kaeuc on 2/11/2017.
 */

public class BusLocationRequest extends AsyncTask<String, Void, String> {
    private final String TAG = "BusLocationRequest";
    private BusLocationRequestResponse callback = null;
    private Context parentContext;
    private ProgressBar progressBar;

    public BusLocationRequest(Context parentContext, ProgressBar progressBar) {
        this.parentContext = parentContext;
        this.callback = (BusLocationRequestResponse) parentContext;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        final String response = HttpRequest.makeGetRequest(url, null);
        try {
            Log.i(TAG,response);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        // TODO status deve vir do servidor
        super.onPostExecute(s);
        JSONObject jsonObject;
        double latitude;
        double longitude;
        int status;
        try {
            jsonObject = new JSONObject(s);
            JSONObject currentLocation = jsonObject.getJSONObject("currentLocation");
            if(currentLocation.get("latitude").equals(null)){
                JSONObject lastLocation = jsonObject.getJSONObject("lastLocation");
                latitude = lastLocation.getDouble("latitude");
                longitude = lastLocation.getDouble("longitude");
                status = 503;
            }else {
                status = 200;
                latitude = jsonObject.getJSONObject("currentLocation").getDouble("latitude");
                longitude = jsonObject.getJSONObject("currentLocation").getDouble("longitude");
            }
            GeoPoint busLocation = new GeoPoint(latitude,longitude);
            progressBar.setVisibility(View.GONE);
            callback.onBusLocationTaskResponse(busLocation,status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
