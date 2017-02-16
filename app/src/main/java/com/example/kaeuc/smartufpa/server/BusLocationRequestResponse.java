package com.example.kaeuc.smartufpa.server;

import android.location.Location;

import org.osmdroid.util.GeoPoint;

/**
 * Created by kaeuc on 2/11/2017.
 */

public interface BusLocationRequestResponse {
    void onBusLocationTaskResponse(GeoPoint busLocation, int taskStatus);
}
