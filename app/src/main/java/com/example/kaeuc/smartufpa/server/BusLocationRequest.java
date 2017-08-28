package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.net.SocketTimeoutException;

/**
 * Created by kaeuc on 2/11/2017.
 */

public class BusLocationRequest extends AsyncTask<String, Void, String> {
    private final String TAG = "BusLocationRequest";
    private OnBusLocationListener callback;
    private Context parentContext;
    private ProgressBar progressBar;
    private int taskStatus;

    public BusLocationRequest(Context parentContext, ProgressBar progressBar) {
        this.parentContext = parentContext;
        this.callback = (OnBusLocationListener) parentContext;
        this.progressBar = progressBar;
//        this.taskStatus = Constants.SERVER_RESPONSE_SUCCESS;
    }

    public interface OnBusLocationListener {
        void onBusLocationResponse(GeoPoint busLocation, int taskStatus);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String response = null;
        try {
            response = HttpRequest.makeGetRequest(url, null);
            Log.i(TAG,response);
        }catch (NullPointerException | SocketTimeoutException e){
            e.printStackTrace();
//            this.taskStatus = Constants.SERVER_RESPONSE_TIMEOUT;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        if(this.taskStatus == Constants.SERVER_RESPONSE_TIMEOUT) {
//            callback.onBusLocationResponse(null,taskStatus);
//            progressBar.setVisibility(View.GONE);
//            return;
//        }
//        JSONObject jsonObject;
//        double latitude;
//        double longitude;
//
//        try {
//            jsonObject = new JSONObject(s);
//            if (jsonObject.getInt("status") == Constants.SERVER_RESPONSE_SUCCESS){
//                latitude = jsonObject.getJSONObject("currentLocation").getDouble("latitude");
//                longitude = jsonObject.getJSONObject("currentLocation").getDouble("longitude");
//                this.taskStatus = jsonObject.getInt("status");
//            }else{
//                JSONObject lastLocation = jsonObject.getJSONObject("lastLocation");
//                latitude = lastLocation.getDouble("latitude");
//                longitude = lastLocation.getDouble("longitude");
//                this.taskStatus = jsonObject.getInt("status");
//            }
//            GeoPoint busLocation = new GeoPoint(latitude,longitude);
//            progressBar.setVisibility(View.GONE);
//            callback.onBusLocationResponse(busLocation,taskStatus);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}
