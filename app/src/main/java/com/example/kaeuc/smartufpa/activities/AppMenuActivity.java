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
//        implements MapDownloadFragment.OnMapDownloadFragmentListener{
    public static final String ACTION_APP_MENU = "smartufpa.ACTION_APP_MENU";
    public static final String CATEGORY_APP_MENU = "smartufpa.CATEGORY_APP_MENU";
    public static final String TAG = "AppMenu";

    private final int MAP_DOWNLOAD_NOT_EXECUTED = 0;
    private final int MAP_DOWNLOAD_EXECUTED = 1;



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

                        //Restaura as preferencias gravadas para executar ou não o download
//                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//                        final int tutorialStatus = sharedPref.getInt(getString(R.string.map_download_executed), MAP_DOWNLOAD_NOT_EXECUTED);
//                        if(tutorialStatus == MAP_DOWNLOAD_NOT_EXECUTED) downloadMap();
//                        else{

//                        }
                        Intent intent = new Intent(MainActivity.ACTION_MAIN);
                        intent.addCategory(MainActivity.CATEGORY_MAIN);
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
        Log.i(TAG,"onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
    }

//    private void downloadMap(){
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment prev = getSupportFragmentManager()
//                .findFragmentByTag(MapDownloadFragment.FRAGMENT_TAG);
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        final MapDownloadFragment mapDownloadFragment = MapDownloadFragment.newInstance();
//        mapDownloadFragment.show(ft,MapDownloadFragment.FRAGMENT_TAG);
//    }
//
//    @Override
//    public void onDownloadFinished(ServerResponse taskStatus) {
//        if (taskStatus.equals(ServerResponse.SUCCESS)){
//            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putInt(getString(R.string.map_download_executed),MAP_DOWNLOAD_EXECUTED);
//            editor.commit();
//            // INICIAR A MAIN ACTIVITY
//        }else if(taskStatus.equals(ServerResponse.SERVER_FORBIDDEN)){
//            Toast.makeText(this, "Permissão para download negada.", Toast.LENGTH_SHORT).show();
//        }
//    }
}