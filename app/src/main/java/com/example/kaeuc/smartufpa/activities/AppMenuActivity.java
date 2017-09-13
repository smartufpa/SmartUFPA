package com.example.kaeuc.smartufpa.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.utils.SystemServicesManager;

public class AppMenuActivity extends AppCompatActivity{

    public static final String ACTION_APP_MENU = "smartufpa.ACTION_APP_MENU";
    public static final String CATEGORY_APP_MENU = "smartufpa.CATEGORY_APP_MENU";
    public static final String TAG = AppMenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_menu);

        Button btnMobility = findViewById(R.id.btn_mobilidade);

        btnMobility.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (SystemServicesManager.isNetworkEnabled(AppMenuActivity.this)){
                    if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        Intent intent = new Intent(NoGpsActivity.ACTION_NO_GPS);
                        intent.addCategory(NoGpsActivity.CATEGORY_NO_GPS);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainActivity.ACTION_MAIN);
                        intent.addCategory(MainActivity.CATEGORY_MAIN);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(AppMenuActivity.this, R.string.error_no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}