package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.utils.SystemServicesManager;


/**
 * Activity launched when the user tries to initialize the
 * Main Activity without GPS signal
 */

public class NoGpsActivity extends AppCompatActivity {
    public final static String CATEGORY_NO_GPS = "smartufpa.CATEGORY_NO_GPS";
    public final static String ACTION_NO_GPS = "smartufpa.ACTION_NO_GPS";
    public final static String TAG = NoGpsActivity.class.getSimpleName();

    // Views
    private Button btnSettings;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_gps);
        btnSettings = findViewById(R.id.btn_settings);
        btnExit = findViewById(R.id.btn_exit);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemServicesManager.isGPSEnabled(getApplicationContext())) {
            Intent intent = new Intent(MainActivity.ACTION_MAIN);
            intent.addCategory(MainActivity.CATEGORY_MAIN);
            startActivity(intent);
            finish();
        }
    }
}
