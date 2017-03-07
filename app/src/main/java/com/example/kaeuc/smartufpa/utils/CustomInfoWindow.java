package com.example.kaeuc.smartufpa.utils;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

/**
 * Created by kaeuc on 07/03/2017.
 */

public class CustomInfoWindow extends InfoWindow {


    public CustomInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }

    @Override
    public void onOpen(Object o) {

    }

    @Override
    public void onClose() {

    }
}
