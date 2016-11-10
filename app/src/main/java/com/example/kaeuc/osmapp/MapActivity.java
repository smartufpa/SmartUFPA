package com.example.kaeuc.osmapp;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.design.widget.BottomSheetBehavior;

import com.example.kaeuc.osmapp.Extras.Constants;
import com.example.kaeuc.osmapp.Extras.Place;
import com.example.kaeuc.osmapp.Server.NominatimDataRequest;
import com.example.kaeuc.osmapp.Server.NominatimDataRequestResponse;
import com.example.kaeuc.osmapp.Server.OsmDataRequest;
import com.example.kaeuc.osmapp.Server.OsmDataRequestResponse;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
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
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener,
        OsmDataRequestResponse, NominatimDataRequestResponse,
        SearchView.OnQueryTextListener {

    public static final String ACTION_MAP = "osmapp.ACTION_MAP";
    public static final String CATEGORY_MAP = "osmapp.CATEGORY_MAP";
    private static final String TAG = "MapActivity";

    private IMapController mMapController;
    private MyLocationNewOverlay mLocationOverlay;

    //views
    private MapView mMap;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabBusRoutes;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private BottomSheetBehavior sheetBehavior;


    private Location myCurrentLocation;
    private LocationManager locationManager;

    private Place defaultLocation;
    private BoundingBoxE6 mapRegion;


    //Booleans para camadas informação
    private boolean isXeroxEnabled = false;
    private boolean isBusRouteEnabled = false;
    private boolean isRestaurantEnabled = false;
    private boolean isSearchEnabled = false;

    private List<String> mapMarkers = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /* Inicio dos métodos do ciclo da activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Views
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                mMap = (MapView) findViewById(map);
                fabMyLocation = (FloatingActionButton) findViewById(R.id.fab_my_location);
                fabBusRoutes = (FloatingActionButton) findViewById(R.id.fab_bus_route);
                fabBusRoutes.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this, R.color.disabledButton)));
                progressBar = (ProgressBar) findViewById(R.id.progress_bar);

                final View view = findViewById(R.id.bottom_sheet);
                sheetBehavior = BottomSheetBehavior.from(view);
                sheetBehavior.setHideable(true);
                sheetBehavior.setPeekHeight(300);
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            }
        });

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        /* Configura o caminho do armazenamento em cache do mapa, se o device não possui cartão SD,
         * ele deve ser configurado para o caminho de arquivos do device
         */
        OpenStreetMapTileProviderConstants.setCachePath(this.getFilesDir().getAbsolutePath());

        /* Importante! Configure o user agent para previnir ser banido dos servidores do OSM
         * O user agent deve ser uma identificação única do seu aplicativo
         * Um exemplo mostra a utilização de "BuildConfig.APPLICATION_ID"
         */
        OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        // Restrição de coordenadas do mapa
        mapRegion = new BoundingBoxE6(-1.457886, -48.437957, -1.479967, -48.459779);


        // Ações para os butões flutuantes
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCurrentLocation == null) {
                    //Ações do GPS, é verificado se o gps está ativo quando o usuário aperta o botão flutuante da localização
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //GPSEnabled(Variável booleana) recebe o status do gps
                    //Verifica se o gps está ligado, se sim abre o menu de configurações para ativá-lo
                    if (!GPSEnabled) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                    Toast.makeText(MapActivity.this, "Carregando sua posição atual.", Toast.LENGTH_SHORT).show();
                } else if (!mapRegion.contains(
                        new GeoPoint(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude())))
                    Toast.makeText(MapActivity.this, "Você está fora da região coberta pelo nosso mapa!", Toast.LENGTH_SHORT).show();
                else
                    mMapController.animateTo(new GeoPoint(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude()));
            }
        });

       /* Cria-se um provedor de tiles que será setado para ser a camada de transportes
       e então adiciona-se essa camada sobrepondo a existe no mapa.
       A variável "isBusRouteEnabled" mantém o registro se essa camada está ativa ou não
       o que muda a cor do botão para indicar ao usuário
        */
        fabBusRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapTileProviderBasic provider = new MapTileProviderBasic(getApplicationContext());
                provider.setTileSource(TileSourceFactory.PUBLIC_TRANSPORT);
                TilesOverlay tilesOverlay = new TilesOverlay(provider, MapActivity.this);
                if (!isBusRouteEnabled) {
                    mMap.getOverlays().add(0, tilesOverlay);
                    mMap.invalidate();
                    isBusRouteEnabled = true;
                    fabBusRoutes.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this, R.color.colorAccent)));

                } else {
                    // 0 é a posição da overlay de transporte na lista de overlays aplicadas na MapView
                    mMap.getOverlayManager().remove(0);
                    mMap.invalidate();
                    isBusRouteEnabled = false;
                    fabBusRoutes.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this, R.color.disabledButton)));
                }
            }
        });
        setupMap();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    public void onResume() {
        super.onResume();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        }


        mLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager = null;
        myCurrentLocation = null;
        mLocationOverlay = null;
    }

    /* Fim dos métodos do ciclo da activity*/

    public void setupMap() {
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mMap);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Configuração do MapController: Posição inicial e zoom
                defaultLocation = new Place(-1.47485, -48.45651, "UFPA");
                final GeoPoint startPoint = new GeoPoint(defaultLocation.getLatitude(), defaultLocation.getLongitude());
                mMapController = mMap.getController();
                mMapController.setZoom(16);
                mMapController.animateTo(startPoint);

                // Configuração do Mapa
                mMap.setTilesScaledToDpi(true);
                mMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
//        mMap.setTileSource(new XYTileSource("actions_bar_items.xml.mbtiles", 0, 18, 256, ".jpg", new String[] {})); //Atribui o mapa offline em mMap
//        mMap.setUseDataConnection(false); //Desabilita o uso da internet (optional, but a good way to prevent loading from the network and test your zip loading.)
                mMap.setBuiltInZoomControls(true);
                mMap.setMinZoomLevel(15);
                mMap.setMaxZoomLevel(18);
                mMap.setMultiTouchControls(true);
                mMap.setUseDataConnection(true);
                mMap.getOverlays().add(mLocationOverlay);


                // Restringe a área do mapa à região escolhida
                mMap.setScrollableAreaLimit(mapRegion);
            }
        }).start();

        // Configuração para mostrar o boneco da posição do usuário
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.disableFollowLocation();
        mLocationOverlay.setOptionsMenuEnabled(true);
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
        getMenuInflater().inflate(R.menu.actions_bar_items, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (isSearchEnabled) {
                    isSearchEnabled = false;
                    mMap.getOverlays().remove(mapMarkers.indexOf(Constants.SEARCH_LAYER) + 1);
                    mMap.invalidate();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                return true;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);

        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_xerox) {
            if (!isXeroxEnabled) {
                new OsmDataRequest(this, progressBar).execute(Constants.XEROX_FILTER);
                isXeroxEnabled = true;
            } else {
                mMap.getOverlays().remove(mapMarkers.indexOf(Constants.XEROX_FILTER) + 1);
                mapMarkers.remove(Constants.XEROX_FILTER);
                isXeroxEnabled = false;

            }
        } else if (id == R.id.nav_restaurantes) {
            if (!isRestaurantEnabled) {
                new OsmDataRequest(this, progressBar).execute(Constants.RESTAURANT_FILTER);
                isRestaurantEnabled = true;
            } else {
                mMap.getOverlays().remove(mapMarkers.indexOf(Constants.RESTAURANT_FILTER) + 1);
                mapMarkers.remove(Constants.RESTAURANT_FILTER);
                isRestaurantEnabled = false;

            }
        }


        drawer.closeDrawer(GravityCompat.START);
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
    public void osmTaskCompleted(final List<Place> locais, final String filtro) {
        new Thread(new Runnable() {
            public void run() {
                FolderOverlay poiMarkers = new FolderOverlay(MapActivity.this);
                mMap.getOverlays().add(poiMarkers);
                Drawable poiIcon = null;

                if (filtro.equals(Constants.XEROX_FILTER))
                    poiIcon = ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_marker_xerox);
                else if (filtro.equals(Constants.RESTAURANT_FILTER))
                    poiIcon = ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_marker_restaurant);
                for (Place place : locais) {
                    Marker poiMarker = new Marker(mMap);
                    poiMarker.setTitle(place.getName());
                    // Descrição do marcador
                    // poiMarker.setSnippet(place.getName());
                    poiMarker.setPosition(new GeoPoint(place.getLatitude(), place.getLongitude()));
                    poiMarker.setIcon(poiIcon);
                    poiMarkers.add(poiMarker);
                }
                // Mapeia em que posição da arraylist a camada está sendo aplicada
                mapMarkers.add(filtro);
            }
        }).start();

    }

    @Override
    public void nominatimTaskResponse(final ArrayList<POI> pois) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pois != null) {
                    if (pois.size() > 1) {
                        Log.e(TAG,Place.convertPOIsToPlaces(pois).toString());
                        Log.e(TAG, String.valueOf(Place.convertPOIsToPlaces(pois).size()));
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        ListView listView = (ListView) findViewById(R.id.list_places);
                        ArrayAdapter<Place> placeArrayAdapter = new ArrayAdapter<>(MapActivity.this,
                                android.R.layout.simple_list_item_1,Place.convertPOIsToPlaces(pois));
                        listView.setAdapter(placeArrayAdapter);
                        listView.setVisibility(View.VISIBLE);

                    }
                    FolderOverlay poiMarkers = new FolderOverlay(MapActivity.this);
                    mMap.getOverlays().add(poiMarkers);

                    Drawable poiIcon = getResources().getDrawable(R.drawable.ic_marker);
                    for (POI poi : pois) {
                        Marker poiMarker = new Marker(mMap);
                        String poiName = poi.mDescription.substring(0, poi.mDescription.indexOf(","));
                        poiMarker.setTitle(poiName);
                        poiMarker.setSnippet(poi.mDescription);
                        poiMarker.setPosition(poi.mLocation);
                        poiMarker.setIcon(poiIcon);
                        poiMarkers.add(poiMarker);
                        poiMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                findViewById(R.id.layout_about_place).setVisibility(View.VISIBLE);

                                return true;
                            }
                        });
                    }
                    mapMarkers.add(Constants.SEARCH_LAYER);
                    isSearchEnabled = true;
                } else {
                    Toast.makeText(MapActivity.this, "Houve um problema na sua conexão. Tente novamente.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mMap.invalidate();
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        final String latitude = String.valueOf(defaultLocation.getLatitude());
        final String longitude = String.valueOf(defaultLocation.getLongitude());
        if (isSearchEnabled) {
            mMap.getOverlays().remove(mapMarkers.indexOf(Constants.SEARCH_LAYER) + 1);
            isSearchEnabled = false;
        }
        new NominatimDataRequest(this, progressBar).execute(query, latitude, longitude);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Map Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
