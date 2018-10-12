package br.ufpa.smartufpa.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.utils.PrefManager;
import br.ufpa.smartufpa.utils.SystemServicesManager;

public class SplashScreenActivity extends AppCompatActivity {

    private PrefManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        setContentView(R.layout.activity_splash_screen);
        manager = new PrefManager(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Checks if the user has a proper network and GPS connection
                if (SystemServicesManager.isNetworkEnabled(getApplicationContext())){
                    if (!SystemServicesManager.isGPSEnabled(getApplicationContext())){
                        // Doesn't start the main activity, instead asks user to turn on the GPS
                        Intent intent = new Intent(NoGpsActivity.ACTION_NO_GPS);
                        intent.addCategory(NoGpsActivity.CATEGORY_NO_GPS);
                        startActivity(intent);
                    }else{
                        // Proper network connection, goes to MainActivity
                        if(manager.isFirstTimeLaunch() || !isPermissionGranted()){
                            startActivity(new Intent(SplashScreenActivity.this, PermissionCheckActivity.class));
                            finish();
                        }else{
//                            Intent intent = new Intent(MainActivity.ACTION_MAIN);
//                            intent.addCategory(MainActivity.CATEGORY_MAIN);
//                            startActivity(intent);
                            startActivity(new Intent(SplashScreenActivity.this,EnterScreenActivity.class));
                        }

                    }
                }else{
                    // TODO: Adicionar atividade para sem conexão
                }
                finish();
            }
        },2000);

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
