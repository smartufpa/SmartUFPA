package com.example.kaeuc.smartufpa.asynctasks.interfaces;

import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.views.overlay.Overlay;

/**
 * Created by kaeuc on 31/08/2017.
 */

public interface OnBusRouteListener {
    void onBusRouteResponse(Overlay overlay, ServerResponse taskStatus);
}
