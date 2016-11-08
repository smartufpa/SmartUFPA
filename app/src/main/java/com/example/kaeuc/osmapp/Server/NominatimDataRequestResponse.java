package com.example.kaeuc.osmapp.Server;

import org.osmdroid.bonuspack.location.POI;

import java.util.ArrayList;

/**
 * Created by MESTRADO on 08/11/2016.
 */

public interface NominatimDataRequestResponse {
    void nominatimTaskResponse(ArrayList<POI> pois);
}
