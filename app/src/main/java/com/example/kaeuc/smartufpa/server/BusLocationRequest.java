package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

    public BusLocationRequest(Context parentContext) {
        this.parentContext = parentContext;
        this.callback = (BusLocationRequestResponse) parentContext;
    }

    @Override
    protected void onPreExecute() {
        // TODO Adicionar barra de progresso
        super.onPreExecute();
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
        super.onPostExecute(s);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            final double latitude =  jsonObject.getDouble("latitude");
            final double longitude = jsonObject.getDouble("longitude");
            GeoPoint busLocation = new GeoPoint(latitude,longitude);
            callback.onBusLocationTaskResponse(busLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
