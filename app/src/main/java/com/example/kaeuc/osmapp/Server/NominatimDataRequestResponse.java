package com.example.kaeuc.osmapp.Server;

import com.example.kaeuc.osmapp.Extras.Place;

import org.osmdroid.bonuspack.location.POI;

import java.util.ArrayList;

/**
 * Created by MESTRADO on 08/11/2016.
 */

public interface NominatimDataRequestResponse {
    void nominatimTaskResponse(ArrayList<Place> places);
}
