package br.ufpa.smartufpa.customviews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.AddPlaceActivity;

/**
 * Created by kaeuc on 07/03/2017.
 */

public class AddPlaceInfoWindow extends InfoWindow {


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
                startAddPlaceActivity();
                close();
            }
        });
    }

    @Override
    public void onClose() {
    }

    private void startAddPlaceActivity() {
        double latitude = locationMarker.getPosition().getLatitude();
        double longitude = locationMarker.getPosition().getLongitude();
        Intent intent = new Intent(parentActivity, AddPlaceActivity.class);
        intent.putExtra(AddPlaceActivity.LABEL_LATITUDE,latitude);
        intent.putExtra(AddPlaceActivity.LABEL_LONGITUDE,longitude);
        parentActivity.startActivity(intent);
    }
}
