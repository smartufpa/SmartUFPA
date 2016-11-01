package com.example.kaeuc.osmapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kaeuc.osmapp.Extras.Constants;
import com.example.kaeuc.osmapp.Extras.Local;
import com.example.kaeuc.osmapp.Server.OsmDataRequest;
import com.example.kaeuc.osmapp.Server.ServerTaskResponse;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import static com.example.kaeuc.osmapp.R.id.map;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener,ServerTaskResponse {

    public static final String ACTION_MAP = "osmapp.ACTION_MAP";
    public static final String CATEGORY_MAP = "osmapp.CATEGORY_MAP";
    private static final String TAG = "MapActivity";

    private IMapController mMapController;
    private MyLocationNewOverlay mLocationOverlay;

    private MapView mMap;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabBusRoutes;
    private ProgressBar progressBar;


    private Location myCurrentLocation;
    private LocationManager locationManager;

    private Local defaultLocation;
    private BoundingBoxE6 mapRegion;


    //Booleans para camadas informação
    private boolean xeroxActive = false;
    private boolean busRouteActive = false;
    private boolean restaurantActive = false;

    private List<String>mapFilters = new ArrayList<>();

    /* Inicio dos métodos do ciclo da activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Views
                progressBar = (ProgressBar) findViewById(R.id.progressbar);
                drawer =(DrawerLayout) findViewById(R.id.drawer_layout);
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                mMap =(MapView) findViewById(map);
                fabMyLocation = (FloatingActionButton) findViewById(R.id.fab_my_location);
                fabBusRoutes = (FloatingActionButton) findViewById(R.id.fab_bus_route);
                fabBusRoutes.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this,R.color.disabledButton)));

            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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


        // Ações para os butões flutuantes
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myCurrentLocation == null) {
                    // TODO 1. Checar se o gps está ligado
                    Toast.makeText(MapActivity.this, "Carregando sua posição atual.", Toast.LENGTH_SHORT).show();
                }else if(!mapRegion.contains(
                        new GeoPoint(myCurrentLocation.getLatitude(),myCurrentLocation.getLongitude())))
                    Toast.makeText(MapActivity.this, "Você está fora da região coberta pelo nosso mapa!", Toast.LENGTH_SHORT).show();
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
                TilesOverlay tilesOverlay = new TilesOverlay(provider,MapActivity.this);
                if(!busRouteActive){
                    mMap.getOverlays().add(0,tilesOverlay);
                    mMap.invalidate();
                    busRouteActive = true;
                    fabBusRoutes.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this,R.color.colorAccent)));
                }else{
                    // 0 é a posição da overlay de transporte na lista de overlays aplicadas na MapView
                    mMap.getOverlayManager().remove(0);
                    mMap.invalidate();
                    busRouteActive = false;
                    fabBusRoutes.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this,R.color.disabledButton)));
                }
            }
        });

        setupMap();
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

    /* Fim dos métodos do ciclo da activity*/

    public void setupMap(){
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),mMap);


        // Configuração do MapController: Posição inicial e zoom
        defaultLocation = new Local(-1.47485, -48.45651,"UFPA");
        final GeoPoint startPoint = new GeoPoint(defaultLocation.getLatitude(),defaultLocation.getLongitude());
        mMapController = mMap.getController();
        mMapController.setZoom(16);
        mMapController.animateTo(startPoint);

        // Configuração do Mapa
        mMap.setTilesScaledToDpi(true);
        mMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
//        mMap.setTileSource(new XYTileSource("map.mbtiles", 0, 18, 256, ".jpg", new String[] {})); //Atribui o mapa offline em mMap
//        mMap.setUseDataConnection(false); //Desabilita o uso da internet (optional, but a good way to prevent loading from the network and test your zip loading.)
        mMap.setBuiltInZoomControls(true);
        mMap.setMinZoomLevel(15);
        mMap.setMaxZoomLevel(18);
        mMap.setMultiTouchControls(true);
        mMap.setUseDataConnection(true);
        mMap.getOverlays().add(mLocationOverlay);


        // Restringe a área do mapa à região escolhida
        mMap.setScrollableAreaLimit(mapRegion);
        // Configuração para mostrar o boneco da posição do usuário
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.disableFollowLocation();
        mLocationOverlay.setOptionsMenuEnabled(true);
        mMap.invalidate();
    }

    /* Início dos métodos de utilização do Drawer lateral*/

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_xerox) {
            if(!xeroxActive) {
                new OsmDataRequest(this,progressBar).execute(Constants.XEROX_FILTER);
                xeroxActive = true;
            }else {
                mMap.getOverlays().remove(mapFilters.indexOf(Constants.XEROX_FILTER)+1);
                mapFilters.remove(Constants.XEROX_FILTER);
                xeroxActive = false;
            }
        } else if (id == R.id.nav_restaurantes) {
            if(!restaurantActive) {
                new OsmDataRequest(this,progressBar).execute(Constants.RESTAURANT_FILTER);
                restaurantActive = true;
            }else {
                mMap.getOverlays().remove(mapFilters.indexOf(Constants.RESTAURANT_FILTER)+1);
                mapFilters.remove(Constants.RESTAURANT_FILTER);
                restaurantActive = false;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        mMap.invalidate();
        return true;
    }

    /* Fim dos métodos de utilização do Drawer lateral*/

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

    @Override
    public void onTaskCompleted(final List<Local> locais, final String filtro) {
        new Thread(new Runnable()
        {
            public void run() {
                FolderOverlay poiMarkers = new FolderOverlay(MapActivity.this);
                mMap.getOverlays().add(poiMarkers);
                Drawable poiIcon = null;

                if(filtro.equals(Constants.XEROX_FILTER))
                    poiIcon = ContextCompat.getDrawable(MapActivity.this,R.drawable.ic_marker_xerox);
                else if(filtro.equals(Constants.RESTAURANT_FILTER))
                    poiIcon = ContextCompat.getDrawable(MapActivity.this,R.drawable.ic_marker_restaurant);
                for (Local local:locais){
                    Marker poiMarker = new Marker(mMap);
                    poiMarker.setTitle(local.getNome());
                    // Descrição do marcador
                    // poiMarker.setSnippet(local.getNome());
                    poiMarker.setPosition(new GeoPoint(local.getLatitude(),local.getLongitude()));
                    poiMarker.setIcon(poiIcon);
                    poiMarkers.add(poiMarker);
                }
                // Mapeia em que posição da arraylist a camada está sendo aplicada
                mapFilters.add(filtro);
            }
        }).start();

    }

}
