package com.example.kaeuc.osmapp;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final String ACTION_PICKLANG = "osmapp.ACTION_MAIN";
    public static final String CATEGORY_PICKLANG = "osmapp.CATEGORY_MAIN";
    private IMapController mMapController;
    private MyLocationNewOverlay mLocationOverlay;

    private MapView mMap;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabBusRoutes;

    private Location myCurrentLocation;
    private LocationManager locationManager;

    private Local defaultLocation;
    private BoundingBoxE6 mapRegion;

    private boolean busRouteActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Requisita permissões para Android Marshmallow e devices superiores
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }


        /* Configura o caminho do armazenamento em cache do mapa, se o device não possui cartão SD,
         * ele deve ser configurado para o caminho de arquivos do device
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setCachePath(this.getFilesDir().getAbsolutePath());

        /* Importante! Configure o user agent para previnir ser banido dos servidores do OSM
         * O user agent deve ser uma identificação única do seu aplicativo
         * Um exemplo mostra a utilização de "BuildConfig.APPLICATION_ID"
         */
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        // Restrição de coordenadas do mapa
        mapRegion = new BoundingBoxE6(-1.457886,-48.437957,-1.479967,-48.459779);

        // Views
        mMap =(MapView) findViewById(R.id.map);
        fabMyLocation = (FloatingActionButton) findViewById(R.id.fab_my_location);
        fabBusRoutes = (FloatingActionButton) findViewById(R.id.fab_bus_route);
        fabBusRoutes.setBackgroundTintList(
                ColorStateList.valueOf(getResources().getColor(R.color.disabledButton)));


        // Ações para os butões flutuantes
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myCurrentLocation == null) {
                    Toast.makeText(MainActivity.this, "Carregando sua posição atual.", Toast.LENGTH_SHORT).show();
                }else if(!mapRegion.contains(
                        new GeoPoint(myCurrentLocation.getLatitude(),myCurrentLocation.getLongitude())))
                    Toast.makeText(MainActivity.this, "Você está fora da região coberta pelo nosso mapa!", Toast.LENGTH_SHORT).show();
                else
                    mMapController.animateTo(new GeoPoint(myCurrentLocation.getLatitude(),myCurrentLocation.getLongitude()));
            }
        });

       /* Cria-se um provedor de tiles que será setado para ser a camada de transportes
       e então adiciona-se essa camada sobrepondo a existe no mapa.
       A variável "busRouteActive" mantém o registro se essa camada está ativa ou não
       o que muda a cor do botão para indicar ao usuário
        */
        fabBusRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapTileProviderBasic provider = new MapTileProviderBasic(getApplicationContext());
                provider.setTileSource(TileSourceFactory.PUBLIC_TRANSPORT);
                TilesOverlay tilesOverlay = new TilesOverlay(provider,MainActivity.this);
                if(!busRouteActive){
                    mMap.getOverlays().add(tilesOverlay);
                    mMap.postInvalidate();
                    busRouteActive = true;
                    fabBusRoutes.setBackgroundTintList(
                            ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }else{
                    // 1 é a posição da overlay de transporte na lista de overlays aplicadas na MapView
                    mMap.getOverlays().remove(1);
                    mMap.postInvalidate();

                    busRouteActive = false;

                    fabBusRoutes.setBackgroundTintList(
                            ColorStateList.valueOf(getResources().getColor(R.color.disabledButton)));

                }

            }
        });



        mLocationOverlay = new MyLocationNewOverlay(this,new GpsMyLocationProvider(this),mMap);

        // Configuração do MapController: Posição inicial e zoom
        defaultLocation = new Local(-1.47485, -48.45651,"UFPA");
        GeoPoint startPoint = new GeoPoint(defaultLocation.getLatitude(),defaultLocation.getLongitude());
        mMapController = this.mMap.getController();
        mMapController.setZoom(16);
        mMapController.animateTo(startPoint);

        // Configuração do Mapa
        mMap.setTilesScaledToDpi(true);
        //mMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMap.setTileSource(new XYTileSource("map.mbtiles", 0, 18, 256, ".jpg", new String[] {})); //Atribui o mapa offline em mMap
        mMap.setUseDataConnection(false); //Desabilita o uso da internet (optional, but a good way to prevent loading from the network and test your zip loading.)
        mMap.setBuiltInZoomControls(true);
        mMap.setMinZoomLevel(15);
        mMap.setMaxZoomLevel(18);
        mMap.setMultiTouchControls(true);
        //mMap.setUseDataConnection(true);
        mMap.getOverlays().add(this.mLocationOverlay);
        // Restringe a área do mapa à região escolhida
        mMap.setScrollableAreaLimit(mapRegion);




        // Configuração para mostrar o boneco da posição do usuário
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.disableFollowLocation();
        mLocationOverlay.setOptionsMenuEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

        mLocationOverlay.disableMyLocation();
    }
    @Override
    public void onResume(){
        super.onResume();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0l,0f,this);
        }


        mLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager = null;
        myCurrentLocation=null;
        mLocationOverlay=null;
    }

    @Override
    public void onLocationChanged(Location location) {
        myCurrentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // Inicio da checagem de permissões - Métodos retirados do exemplo dado no projeto do OSMdroid
    // OpenStreetMapView > Samples > ExtraSamples > SampleFollowMe
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        String message = "osmdroid permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nLocation to show user location.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nStorage access to store map tiles.";
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } // else: We already have permissions, so handle as normal
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (location && storage) {
                    // All Permissions Granted
                    Toast.makeText(MainActivity.this, "Todas as permissões necessárias foram cedidas.",
                            Toast.LENGTH_SHORT).show();
                } else if (location) {
                    Toast.makeText(this, "Permissão de armzenamento é necessária para armazenar " +
                                    "porções do mapa para reduzir consumo de dados e para uso offline.",
                            Toast.LENGTH_LONG).show();
                } else if (storage) {
                    Toast.makeText(this, "Permissão de localização é necessária para mostrar a sua " +
                            "localização no mapa.", Toast.LENGTH_LONG).show();
                } else { // !location && !storage case
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permissão de armzenamento é necessária para armazenar " +
                            "porções do mapa para reduzir consumo de dados e para uso offline." +
                            "\nPermissão de localização é necessária para mostrar a sua " +
                            "localização no mapa.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // FIM DA CHECAGEM DE PERMISSÕES






}
