package com.example.kaeuc.smartufpa;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoGpsActivity extends AppCompatActivity {
    public final static String CATEGORY_NO_GPS = "osmapp.CATEGORY_NO_GPS";
    public final static String ACTION_NO_GPS = "osmapp.ACTION_NO_GPS";

    private Button turnOnGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_gps_activity);
        turnOnGps = (Button) findViewById(R.id.btn_turn_on_gps);
        turnOnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(MapActivity.ACTION_MAP);
            intent.addCategory(MapActivity.CATEGORY_MAP);
            startActivity(intent);
            finish();
        }
    }
}
