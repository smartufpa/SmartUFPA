package com.example.kaeuc.smartufpa.asynctasks.interfaces;

import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.views.overlay.Overlay;

/**
 * Created by kaeuc on 29/08/2017.
 */

public interface OnSearchRouteListener{
    void onSearchRouteResponse(Overlay routeOverlay, ServerResponse taskStatus);
}
