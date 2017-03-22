package com.example.kaeuc.smartufpa;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.utils.NetworkManager;

public class AppMenuActivity extends AppCompatActivity {
    public static final String ACTION_APP_MENU = "osmapp.ACTION_APP_MENU";
    public static final String CATEGORY_APP_MENU = "osmapp.CATEGORY_APP_MENU";
    public static final String TAG = "AppMenu";


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_menu);

        Button mobilidade = (Button) findViewById(R.id.btn_mobilidade);


        mobilidade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (NetworkManager.checkNetworkConnection(AppMenuActivity.this)){
                    if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        Intent intent = new Intent(NoGpsActivity.ACTION_NO_GPS);
                        intent.addCategory(NoGpsActivity.CATEGORY_NO_GPS);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(AppMenuActivity.this, MapActivity.class);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(AppMenuActivity.this, "Cheque sua conexão com a internet e tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy called");
    }
}