package com.example.kaeuc.smartufpa.database;

import android.net.Uri;
import android.util.Log;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.server.HttpRequest;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by kaeuc on 4/3/2017.
 */

public class PlaceDAO {

    private static final int GET_PLACE_BY_NAME = 0;

    private final String API_PATH = "phpAPI";
    private final String FILE_NAME = "insertPlace.php";


    private final String TAG = PlaceDAO.class.getSimpleName();

    private static PlaceDAO ourInstance;

    public static synchronized PlaceDAO getInstance() {
        if(ourInstance == null){
            return new PlaceDAO();
        }
        return ourInstance;
    }

    private PlaceDAO() {}

    public boolean insertPlace(final String jsonInput){
        final Uri builtUri = Uri.parse(Constants.URL_LOCAL_HOST).buildUpon()
                .appendPath(API_PATH)
                .appendPath(FILE_NAME)
                .build();
        new Thread(){
            @Override
            public void run() {
                try {
                    final String s = HttpRequest.makePostRequest(builtUri.toString(), null, jsonInput);
                    Log.i(TAG, "Server Response: " + s);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return false;
    }

    //TODO
    public String getPlaceByName(String placeName) {


        final String QUERY_PLACE_NAME = "name";
        final ArrayList<Place> places = new ArrayList<>();

        final Uri builtUri = Uri.parse(Constants.URL_LOCAL_HOST).buildUpon()
                .appendPath(API_PATH)
                .appendPath(FILE_NAME)
                .appendQueryParameter(QUERY_PLACE_NAME,placeName)
                .build();

        new Thread(){
            @Override
            public void run() {
                try {
                    final String response = HttpRequest.makeGetRequest(builtUri.toString(), null);
                    Gson gson = new Gson();
                    final Place place = gson.fromJson(response, Place.class);
                    Log.i(TAG, "run: " + place.toString());
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        return null;
    }
}
