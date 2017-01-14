package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kaeuc.smartufpa.extras.Constants;

/**
 * Created by kaeuc on 1/11/2017.
 */

public class LocalHostRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = "LocalHostRequest";

    private LocalHostRequestResponse callback = null;
    private Context parent;


    public LocalHostRequest(Context parent) {
        this.callback = (LocalHostRequestResponse) parent;
    }


    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        String response = "";
        try {
            response = HttpRequest.makeRequest(method, Constants.LOCAL_HOST_URL,null);
        } catch (HttpRequest.EmptyMethodException e) {
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
        callback.LocalHostTaskResponse(s);


    }
}
