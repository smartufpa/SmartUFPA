package br.ufpa.smartufpa;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddPlaceParent extends AppCompatActivity {

    protected static final String ARG_LATITUDE = "latitude";
    protected static final String ARG_LONGITUDE = "longitude";

    protected double latitude;
    protected double longitude;

    protected JSONObject json;

    public AddPlaceParent() {
        this.json = new JSONObject();
    }


    public JSONObject getJson() {
        return json;
    }
}
