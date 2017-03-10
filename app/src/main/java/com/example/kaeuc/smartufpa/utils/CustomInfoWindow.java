package com.example.kaeuc.smartufpa.utils;

import android.view.View;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

/**
 * Created by kaeuc on 07/03/2017.
 */

public class CustomInfoWindow extends InfoWindow implements View.OnClickListener{


    public CustomInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }

    @Override
    public void onOpen(Object o) {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Teste", Toast.LENGTH_SHORT).show();
    }
}
