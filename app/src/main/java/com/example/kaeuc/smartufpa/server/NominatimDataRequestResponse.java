package com.example.kaeuc.smartufpa.server;

import com.example.kaeuc.smartufpa.classes.Place;

import java.util.ArrayList;

/**
 * Created by MESTRADO on 08/11/2016.
 */

public interface NominatimDataRequestResponse {
    void nominatimTaskResponse(ArrayList<Place> places);
}
