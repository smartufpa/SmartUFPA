package com.example.kaeuc.smartufpa.customviews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.fragments.AddPlaceFragment;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

/**
 * Created by kaeuc on 07/03/2017.
 */

public class AddPlaceInfoWindow extends InfoWindow{


    private ImageButton btnAddPlaceFrag;
    private ImageButton btnCloseWindow;
    private final Marker locationMarker;
    private final AppCompatActivity parentActivity;

    public AddPlaceInfoWindow(int layoutResId, MapView mapView, Marker marker, Context parentContext) {
        super(layoutResId, mapView);
        this.locationMarker = marker;
        this.parentActivity = (AppCompatActivity) parentContext;

    }

    @Override
    public void onOpen(Object o) {
        btnAddPlaceFrag = getView().findViewById(R.id.img_add_info);
        btnCloseWindow = getView().findViewById(R.id.img_close);
        btnCloseWindow.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        btnAddPlaceFrag.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                close();
            }
        });
    }

    @Override
    public void onClose() {

    }


    private void showDialog(){
        FragmentTransaction ft = parentActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = parentActivity.getSupportFragmentManager()
                .findFragmentByTag(AddPlaceFragment.FRAGMENT_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        final AddPlaceFragment addPlaceFragment = AddPlaceFragment.newInstance(
                locationMarker.getPosition().getLatitude(),
                locationMarker.getPosition().getLongitude());
        addPlaceFragment.show(ft,AddPlaceFragment.FRAGMENT_TAG);
    }

}
