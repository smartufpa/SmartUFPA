package com.example.kaeuc.smartufpa.database;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.server.HttpRequest;
import com.example.kaeuc.smartufpa.utils.Constants;

import java.net.SocketTimeoutException;

/**
 * Created by kaeuc on 4/3/2017.
 */

public class PlaceDAO extends AsyncTask<Integer,Void,Place> {

    public static final int GET_ALL_PLACES = 0;

    private final String TAG = PlaceDAO.class.getSimpleName();


    private Context parentContext;
    public PlaceDAO(Context parentContext) {


    }


    @Override
    protected Place doInBackground(Integer... method) {

        switch (method[0]){
            case GET_ALL_PLACES:
                try {
                    final String allPlaces = getAllPlaces();
                    Log.i(TAG, allPlaces);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
                break;
        }


        return null;
    }


    private String getAllPlaces() throws SocketTimeoutException {

        Uri builtUri = Uri.parse(Constants.URL_LOCAL_HOST).buildUpon()
                .appendPath("phpAPI")
                .appendQueryParameter("method", String.valueOf(GET_ALL_PLACES))
                .build();


        return HttpRequest.makeGetRequest(builtUri.toString(),null);
    }
}
