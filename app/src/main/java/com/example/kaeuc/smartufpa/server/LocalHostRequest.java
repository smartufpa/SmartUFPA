package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;

import java.net.SocketTimeoutException;

/**
 * Created by kaeuc on 1/11/2017.
 */

public class LocalHostRequest extends AsyncTask<Place, Void, String> {
    private static final String TAG = "LocalHostRequest";

    private LocalHostRequestResponse callback = null;
    private Context parent;


    public LocalHostRequest(Context parent) {
        this.callback = (LocalHostRequestResponse) parent;
    }


    @Override
    protected String doInBackground(Place... params) {

        String response = null;
        Place place = params[0];
        try {
            response = HttpRequest.makePostRequest(Constants.URL_LOCAL_HOST,null,place.toJsonObject());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            Log.println(Log.INFO,TAG,s);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        callback.onLocalHostTaskResponse(s);


    }
}
