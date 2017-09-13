package com.example.kaeuc.smartufpa.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.utils.SystemServicesManager;

public class NoGpsActivity extends AppCompatActivity {
    public final static String CATEGORY_NO_GPS = "smartufpa.CATEGORY_NO_GPS";
    public final static String ACTION_NO_GPS = "smartufpa.ACTION_NO_GPS";
    public final static String TAG = NoGpsActivity.class.getSimpleName();

    private Button btnGoToGpsSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_gps_activity);
        btnGoToGpsSettings = findViewById(R.id.btn_turn_on_gps);
        btnGoToGpsSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemServicesManager.isGPSEnabled(this)) {
            Intent intent = new Intent(MainActivity.ACTION_MAIN);
            intent.addCategory(MainActivity.CATEGORY_MAIN);
            startActivity(intent);
            finish();
        }
    }
}
