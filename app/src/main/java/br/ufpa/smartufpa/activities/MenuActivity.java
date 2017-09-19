package br.ufpa.smartufpa.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.utils.SystemServicesManager;


/**
 * Simple activity to show the options menu to users
 * @author kaeuchoa
 */


public class MenuActivity extends AppCompatActivity{

    public static final String ACTION_APP_MENU = "smartufpa.ACTION_APP_MENU";
    public static final String CATEGORY_APP_MENU = "smartufpa.CATEGORY_APP_MENU";
    public static final String TAG = MenuActivity.class.getSimpleName();

    // Views
    private Button btnMobility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_menu);

        btnMobility = findViewById(R.id.btn_mobilidade);

        btnMobility.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Checks if the user has a proper network and GPS connection
                if (SystemServicesManager.isNetworkEnabled(MenuActivity.this)){
                    if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        // Doesn't start the main activity, instead asks user to turn on the GPS
                        Intent intent = new Intent(NoGpsActivity.ACTION_NO_GPS);
                        intent.addCategory(NoGpsActivity.CATEGORY_NO_GPS);
                        startActivity(intent);
                    }else{
                        // Proper network connection, goes to MainActivity
                        Intent intent = new Intent(MainActivity.ACTION_MAIN);
                        intent.addCategory(MainActivity.CATEGORY_MAIN);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(MenuActivity.this, R.string.error_no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}