package com.example.kaeuc.osmapp;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {
    MapView mMap;
    IMapController mMapController;
    Local defaultLocation;
    MyLocationNewOverlay mLocationOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_layout);
        this.mMap = new MapView(this);
        rl.addView(mMap, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT));


        /* Configura o caminho do armazenamento em cache do mapa, se o device não possui cartão SD,
         * ele deve ser configurado para o caminho de arquivos do device
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setCachePath(this.getFilesDir().getAbsolutePath());

        /* Importante! Configure o user agent para previnir ser banido dos servidores do OSM
         * O user agent deve ser uma identificação única do seu aplicativo
         * Um exemplo mostra a utilização de "BuildConfig.APPLICATION_ID"
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);


        // Configuração do Mapa
        this.mMap.setTilesScaledToDpi(true);
        this.mMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        this.mMap.setBuiltInZoomControls(true);
        this.mMap.setMinZoomLevel(15);
        this.mMap.setMultiTouchControls(true);
        mMap.setUseDataConnection(true);

        // Configuração do MapController: Posição inicial e zoom
        defaultLocation = new Local(-1.47485, -48.45651,"UFPA");
        GeoPoint startPoint = new GeoPoint(defaultLocation.getLatitude(),defaultLocation.getLongitude());
        this.mMapController = this.mMap.getController();
        this.mMapController.setZoom(16);
        this.mMapController.animateTo(startPoint);

        // Configuração do MyLocationOverlay

        mLocationOverlay = new MyLocationNewOverlay(getBaseContext(),this.mMap);
        mLocationOverlay.enableMyLocation();
        this.mMap.getOverlays().add(mLocationOverlay);











    }
}
