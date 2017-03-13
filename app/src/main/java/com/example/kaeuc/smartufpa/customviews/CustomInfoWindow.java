package com.example.kaeuc.smartufpa.customviews;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

/**
 * Created by kaeuc on 07/03/2017.
 */

public class CustomInfoWindow extends InfoWindow{

    private final FragmentManager fragmentManager;
    private ImageButton btnAddInfo;
    private ImageButton btnCloseInfo;
    private Marker locationMarker;

    public CustomInfoWindow(int layoutResId, MapView mapView, Marker marker, FragmentManager fragmentManager) {
        super(layoutResId, mapView);
        this.locationMarker = marker;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onOpen(Object o) {
        btnAddInfo = (ImageButton) getView().findViewById(R.id.img_add_info);
        btnCloseInfo = (ImageButton) getView().findViewById(R.id.img_close);
        btnCloseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        btnAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlaceFragment.newInstance(locationMarker.getPosition().getLatitude(),
                        locationMarker.getPosition().getLongitude())
                        .show(fragmentManager,"Fragment");

                close();
            }
        });
    }

    @Override
    public void onClose() {

    }


}
