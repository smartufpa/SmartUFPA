package com.example.kaeuc.smartufpa.server;

import android.support.annotation.Nullable;

import com.example.kaeuc.smartufpa.models.Place;

import java.util.ArrayList;

/**
 * Created by MESTRADO on 08/11/2016.
 */

public interface NominatimDataRequestResponse {
    void onNominatimTaskResponse(@Nullable ArrayList<Place> places, int taskStatus);

}