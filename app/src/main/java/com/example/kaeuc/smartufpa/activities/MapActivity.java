package com.example.kaeuc.smartufpa.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.BuildConfig;
import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.customviews.AddPlaceInfoWindow;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.server.BusLocationRequest;
import com.example.kaeuc.smartufpa.server.OsmDataRequest;
import com.example.kaeuc.smartufpa.server.OverpassSearchRequest;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.SystemServicesManager;
import com.example.kaeuc.smartufpa.utils.showcaseutils.AppTutorial;
import com.example.kaeuc.smartufpa.utils.showcaseutils.ShowcaseHolder;
import com.example.kaeuc.smartufpa.utils.showcaseutils.ToolbarActionItemTarget;
import com.example.kaeuc.smartufpa.utils.showcaseutils.ViewTargets;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import static com.example.kaeuc.smartufpa.R.id.map;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener,
        OsmDataRequest.OnOsmDataListener,BusLocationRequest.OnBusLocationListener,
        OverpassSearchRequest.OnOverpassListener,
        SearchView.OnQueryTextListener {

    public static final String ACTION_MAP = "smartufpa.ACTION_MAP";
    public static final String CATEGORY_MAP = "smartufpa.CATEGORY_MAP";
    private static final String TAG = MapActivity.class.getSimpleName();
    private final int TUTORIAL_EXECUTED = 1;
    private final int TUTORIAL_NOT_EXECUTED = 0;

    private final XYTileSource MAPA_UFPA = new XYTileSource("ufpa_mapa", 15, 18, 256, ".png", new String[] {});

    private final XYTileSource MAPA_UFPA_TRANSPORTE = new XYTileSource("ufpa_transporte", 15, 18, 256, ".png", new String[] {});


    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;

    //views
    private MapView mapView;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private BottomSheetBehavior searchResultSheetBehavior;
    private FloatingActionButton fabBusLocation;
    private FloatingActionButton fabMyLocation;
    private Button btnClearMap;
    private SearchView searchView;


    private Location myCurrentLocation;
    private LocationManager locationManager;

    private Place defaultLocation;
    private GeoPoint startPoint;
    private BoundingBoxE6 mapRegion;


    //Booleans para camadas marcadores
    private boolean isXeroxEnabled = false;
    private boolean isBusRouteEnabled = false;
    private boolean isRestaurantEnabled = false;
    private boolean isSearchEnabled = false;
    private boolean isGoToRouteEnabled = false;
    private boolean isRestroomEnabled= false;
    private boolean isBusLocationEnabled = false;
    private boolean isAuditoriumEnabled= false;
    private boolean isLibrariesEnabled = false;

    private List<String> mapLayers = new ArrayList<>();




    /*
     * Cria um provedor de tiles que será setado para ser a camada de transportes
     *  e então adiciona essa camada sobrepondo a existente no mapa.
     */


    private void addlayerToMap(Overlay poiMarkers, String layerName){
        mapView.getOverlays().add(poiMarkers);
        mapLayers.add(layerName);
//        if(!layerName.equals(Constants.LAYER_MY_LOCATION))
//            btnClearMap.setVisibility(View.VISIBLE);
        mapView.invalidate();
    }


    private boolean addLocationToMap() {
        Toast.makeText(MapActivity.this, R.string.msg_drag_marker, Toast.LENGTH_LONG).show();
        Marker customMarker = createCustomMarker(null, (GeoPoint) mapView.getMapCenter(), null);
        customMarker.setInfoWindow(new AddPlaceInfoWindow(R.layout.custom_info_window,mapView,customMarker,MapActivity.this));
        customMarker.setDraggable(true);

        customMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                if(!marker.isInfoWindowShown())
                    marker.showInfoWindow();
                return false;
            }
        });

        customMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.showInfoWindow();
                mapController.animateTo(marker.getPosition());
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                if(marker.isInfoWindowShown())
                    marker.getInfoWindow().close();
            }
        });
        final FolderOverlay folderOverlay = new FolderOverlay();
        folderOverlay.add(customMarker);
//        addlayerToMap(folderOverlay,Constants.LAYER_ADD_NEW_PLACE);
        return false;
    }


    private void clearMapView(){
        Log.i(TAG+".clearMap()","Previous map overlays: "+ mapView.getOverlayManager().toString());
        int size = mapView.getOverlays().size() -1;
        for (int i= size ; i> 0 ;i--){
            if(mapView.getOverlays().get(i) instanceof FolderOverlay
                    || mapView.getOverlays().get(i) instanceof Polyline  || mapView.getOverlays().get(i) instanceof TilesOverlay){
                mapView.getOverlays().remove(i);
                mapLayers.remove(i);
            }
        }
        if(isSearchEnabled){
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.setIconifiedByDefault(false);
        }
        if(searchResultSheetBehavior != null && searchResultSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            searchResultSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        if(isBusRouteEnabled){
            mapView.setTileSource(MAPA_UFPA);
        }
        isXeroxEnabled = false;
        isRestaurantEnabled = false;
        isSearchEnabled = false;
        isGoToRouteEnabled = false;
        isRestroomEnabled= false;
        isBusRouteEnabled = false;
        isAuditoriumEnabled= false;
        isLibrariesEnabled = false;
        btnClearMap.setVisibility(View.GONE);

        mapView.invalidate();
        Log.i(TAG+".clearMap()","Cleared map: "+ mapView.getOverlayManager().toString());
    }

    private Marker createCustomMarker(@Nullable Drawable poiIcon, GeoPoint location, @Nullable Marker.OnMarkerClickListener clickListener){
        Marker poiMarker = new Marker(mapView);
        poiMarker.setAnchor(0.5f,1);
        poiMarker.setPosition(location);
        if(poiIcon == null)
            poiIcon = ContextCompat.getDrawable(MapActivity.this,R.drawable.ic_marker);
        poiMarker.setIcon(poiIcon);
        if(clickListener != null){
            poiMarker.setOnMarkerClickListener(clickListener);
        }
        return poiMarker;

    }


    private Marker createCustomMarker(@Nullable Drawable poiIcon, GeoPoint location, String markerTitle,
                                      String markerDescription, @Nullable Marker.OnMarkerClickListener clickListener){
        Marker poiMarker = new Marker(mapView);
        poiMarker.setAnchor(0.5f,1);
        poiMarker.setPosition(location);
        if(poiIcon == null)
            poiIcon = ContextCompat.getDrawable(MapActivity.this,R.drawable.ic_marker);
        poiMarker.setIcon(poiIcon);
        poiMarker.setSnippet(markerDescription);
        if(clickListener != null){
            poiMarker.setOnMarkerClickListener(clickListener);
        }

        return poiMarker;

    }

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
    public void onBusLocationResponse(GeoPoint busLocation, final int taskStatus) {
        if(taskStatus == Constants.SERVER_RESPONSE_SUCCESS || taskStatus == Constants.SERVER_INTERNAL_ERROR) {
            final FolderOverlay busLocationLayer = new FolderOverlay();
            busLocationLayer.add(createCustomMarker(
                    ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_bus_location_marker),
                    busLocation, null));
//            addlayerToMap(busLocationLayer,Constants.LAYER_BUS_MARKER);
            if (taskStatus == Constants.SERVER_INTERNAL_ERROR) { // TODO tratar erro de timeout devidamente
                Toast.makeText(this, getString(R.string.msg_bus_last_known_location), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.msg_bus_updated_location), Toast.LENGTH_SHORT).show();
            }
            Log.println(Log.INFO, TAG, "Layer added: Bus Location - " + mapView.getOverlayManager().toString());
        }else if(taskStatus == Constants.SERVER_RESPONSE_TIMEOUT){
            Toast.makeText(this, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate()");
        setContentView(R.layout.activity_map);

        // Views
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mapView = (MapView) findViewById(map);
        fabMyLocation = (FloatingActionButton) findViewById(R.id.fab_my_location);
        fabBusLocation = (FloatingActionButton) findViewById(R.id.fab_bus_location);
        fabBusLocation.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this, R.color.disabledButton)));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnClearMap = (Button) findViewById(R.id.btn_clear_map);


        // Configuração da action bar e do drawer lateral
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

        setupMap();

    }
    // Método responsável por criar as opções de menu na barra superior do aplicativo
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  Infla o menu; Isso adiciona os itens à barra de ações se existir
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Acha o item de busca e seta as ações que serão executadas ao clicar no ícone
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            // Quando o botão de voltar for pressionado
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                /* Somente se a busca já foi realizada, remover a camada com os marcadores.
                 * Somente uma camada de busca é permitida por vez
                 */
                if (isSearchEnabled) {
                    isSearchEnabled = false;
//                    removeLayerFromMap(Constants.LAYER_SEARCH);
                    // Contrai a bottomsheet de resultados
                    if(searchResultSheetBehavior != null) {
                        if ((searchResultSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                                || (searchResultSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED))
                            searchResultSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    if(isGoToRouteEnabled){
                        isGoToRouteEnabled = false;
//                        removeLayerFromMap(Constants.LAYER_ROUTE);
                    }
//                    if(btnClearMap.getVisibility() == View.VISIBLE){
//                        btnClearMap.setVisibility(View.GONE);
//                    }
                    mapView.invalidate();
                }
                return true;
            }
        });
        // Configura a barra de busca (SearchWidget)
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        //Restaura as preferencias gravadas para executar ou não o tutorial
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final int tutorialStatus = sharedPref.getInt(getString(R.string.tutorial_map_executed), TUTORIAL_NOT_EXECUTED);
        if(tutorialStatus == TUTORIAL_NOT_EXECUTED)
            runMapTutorial();

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
        locationManager = null;
        myCurrentLocation = null;
        myLocationOverlay = null;
        this.mapView = null;
        this.mapController = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        myCurrentLocation = location;
    }

    // Configuração dos items da lista lateral
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Guarda a ID do botão clicado
        int id = item.getItemId();
        if (id == R.id.nav_copy_service) {
            // Caso a camada de filtro não esteja ativa, executar a busca e adicionar marcadores
            if (!isXeroxEnabled) {
//                new OsmDataRequest(this, progressBar).execute(Constants.FILTER_XEROX);
                isXeroxEnabled = true;
            }
        } else if (id == R.id.nav_restaurant) {
            if (!isRestaurantEnabled) {
//                new OsmDataRequest(this, progressBar).execute(Constants.FILTER_RESTAURANT);
                isRestaurantEnabled = true;
            }
        } else if (id == R.id.nav_restroom) {
            if (!isRestroomEnabled) {
//                new OsmDataRequest(this, progressBar).execute(Constants.FILTER_RESTROOM);
                isRestroomEnabled = true;
            }
        }else if(id == R.id.nav_bus_route){
            activeBusRouteLayer();
        }else if(id == R.id.nav_auditorium){
            if(!isAuditoriumEnabled){
//                new OsmDataRequest(this, progressBar).execute(Constants.FILTER_AUDITORIUMS);
                isAuditoriumEnabled = true;
            }
        }else if(id == R.id.nav_library){
            if (!isLibrariesEnabled){
//                new OsmDataRequest(this, progressBar).execute(Constants.FILTER_LIBRARIES);
                isLibrariesEnabled = true;
            }
        }else if(id == R.id.nav_about){
            final Intent intent = new Intent(AboutActivity.ACTION_ABOUT);
            intent.addCategory(AboutActivity.CATEGORY_ABOUT);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_location:
                addLocationToMap();

            default:
                return super.onOptionsItemSelected(item);
        }

    }



    @Override
    public void onOverpassResponse(final ArrayList<Place> places, final int taskStatus) {

        if (taskStatus == Constants.SERVER_RESPONSE_SUCCESS) {
            // Se mais de um resultado for retornado, utiliza uma bottomsheet para apresentar os resultados
            if (places.size() > 1) {
//                setupSearchResultBottomSheet(places);
            }else{
                mapController.animateTo(places.get(0).getGeoPoint());
            }

            final FolderOverlay poiMarkers = new FolderOverlay();

            for (final Place place : places) {
                Marker.OnMarkerClickListener markerClick = new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        marker.setIcon(ContextCompat.getDrawable(MapActivity.this,R.drawable.ic_marker_details));
                        marker.setAnchor(0.5f,1);
                        MapActivity.this.mapView.invalidate();
//                        PlaceDetailsBottomSheet sheet = PlaceDetailsBottomSheet.newInstance(place);
//                        sheet.show(getSupportFragmentManager(),"bottom sheet");
                        return true;
                    }
                };

                Marker marker = createCustomMarker(null,
                        place.getGeoPoint(),
                        place.getName(),
                        place.getDescription(),
                        markerClick);
                poiMarkers.add(marker);
            }

//            addlayerToMap(poiMarkers,Constants.LAYER_SEARCH);
            isSearchEnabled = true;
            Log.println(Log.INFO, TAG, "Layer added: Search - " + mapView.getOverlayManager().toString());
            Toast.makeText(MapActivity.this, getString(R.string.msg_click_marker), Toast.LENGTH_SHORT).show();
            mapView.invalidate();
        } else if(taskStatus == Constants.SERVER_INTERNAL_ERROR) {
            Toast.makeText(MapActivity.this, getString(R.string.error_on_connection),
                    Toast.LENGTH_SHORT).show();
            btnClearMap.setVisibility(View.GONE);
        }else if(taskStatus == Constants.SERVER_RESPONSE_NO_CONTENT){
            Toast.makeText(MapActivity.this, getString(R.string.msg_no_results ),
                    Toast.LENGTH_SHORT).show();
            btnClearMap.setVisibility(View.GONE);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

        myLocationOverlay.disableMyLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // Método executado quando a busca é a submetida na Searchview
    @Override
    public boolean onQueryTextSubmit(final String query) {
        // É necessário passar como argumentos para a requisição a lat e long para restringir a busca
        // à região da universidade
        if (SystemServicesManager.isNetworkEnabled(this)) {
            final String latitude = String.valueOf(defaultLocation.getLatitude());
            final String longitude = String.valueOf(defaultLocation.getLongitude());
            findViewById(R.id.action_search).clearFocus();
            // Se uma busca ainda está ativa, limpar o mapa antes de fazer uma nova
            if (isSearchEnabled) {
//                removeLayerFromMap(Constants.LAYER_SEARCH);
                isSearchEnabled = false;
            }
//            new OverpassSearchRequest(this,progressBar).execute(query);
        }else{
            Toast.makeText(this, getString(R.string.error_on_connection), Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"onRestart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume()");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        }

//        addlayerToMap(myLocationOverlay,Constants.LAYER_MY_LOCATION);
        Log.i(TAG,"Layer added: My Location - " + mapView.getOverlayManager().toString());
        // Configuração para mostrar o boneco da posição do usuário
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.disableFollowLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);


        fabBusLocation.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(MapActivity.this, R.color.colorAccent)));
        fabBusLocation.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemServicesManager.isNetworkEnabled(MapActivity.this)) {
                    new BusLocationRequest(MapActivity.this, progressBar).execute(Constants.URL_BUS_LOCATION);
                }else{
                    Toast.makeText(MapActivity.this, getString(R.string.error_on_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Ações para os butões flutuantes
        fabMyLocation.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCurrentLocation == null) {
                    //Ações do GPS, é verificado se o gps está ativo quando o usuário aperta o botão flutuante da localização
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //GPSEnabled(Variável booleana) recebe o status do gps
                    //Verifica se o gps está ligado, se sim abre o menu de configurações para ativá-lo
                    if (!GPSEnabled) {
                        Toast.makeText(MapActivity.this, R.string.msg_turn_on_gps, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MapActivity.this, R.string.msg_loading_current_position, Toast.LENGTH_SHORT).show();
                    // se o usuário se encontra fora da região do mapa
                } else if (!mapRegion.contains(new GeoPoint(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude())))
                    Toast.makeText(MapActivity.this, R.string.msg_out_of_covered_region, Toast.LENGTH_SHORT).show();
                    // senão move a camera para a localização atual do usuário
                else
                    mapController.animateTo(new GeoPoint(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude()));
            }
        });

        btnClearMap.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMapView();
            }


    });





    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart()");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()");
    }

    private boolean removeLayerFromMap(String layerName){
        try{
            mapView.getOverlays().remove(mapLayers.indexOf(layerName));
            mapLayers.remove(layerName);
            if(mapLayers.size() < 2){
                btnClearMap.setVisibility(View.GONE);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }



       /* Cria um provedor de tiles que será setado para ser a camada de transportes
       e então adiciona essa camada sobrepondo a existente no mapa.

        */

    private void activeBusRouteLayer(){
        if(!isBusRouteEnabled) {

            mapView.setTileSource(MAPA_UFPA_TRANSPORTE);
            mapView.invalidate();

            Log.i(TAG, "Layer activated: Bus route -" + mapView.getOverlayManager().toString());
            isBusRouteEnabled = true;
            btnClearMap.setVisibility(View.VISIBLE);
        }
    }


    private void runMapTutorial(){
        ArrayList<ShowcaseHolder> holders = new ArrayList<>();

        try {
            holders.add(new ShowcaseHolder(new ToolbarActionItemTarget(toolbar,R.id.action_add_location),
                    getString(R.string.tutorial_msg_add_location)));
            holders.add( new ShowcaseHolder(new ToolbarActionItemTarget(toolbar,R.id.action_search),
                    getString(R.string.tutorial_msg_search)));
            holders.add(new ShowcaseHolder(ViewTargets.navigationButtonViewTarget(toolbar),
                    getString(R.string.tutorial_msg_menu)));
            holders.add(new ShowcaseHolder(new ViewTarget(fabMyLocation),
                    getString(R.string.tutorial_msg_current_location),
                    Constants.TUTORIAL_BTN_LEFT));
            holders.add(new ShowcaseHolder(new ViewTarget(fabBusLocation),
                    getString(R.string.tutorial_msg_bus_location),
                    Constants.TUTORIAL_BTN_LEFT));
            new AppTutorial(holders,MapActivity.this);
        } catch (ViewTargets.MissingViewException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.tutorial_map_executed),TUTORIAL_EXECUTED);
        final boolean commit = editor.commit();
        Log.i(TAG + ".tutorial()", String.valueOf(commit));

    }

    /* Fim dos métodos do ciclo da activity*/

    // Método responsável por configurar o mapa na sua inicialização
    private void setupMap() {
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);

        // Restrição da região mostrada do mapa usando coordenadas
        mapRegion = new BoundingBoxE6(-1.457886, -48.437957, -1.479967, -48.459779);

        // Configuração do MapController: Posição inicial e zoom
        defaultLocation = new Place(-1.47485, -48.45651, "UFPA");
        startPoint = new GeoPoint(defaultLocation.getLatitude(), defaultLocation.getLongitude());
        mapController = mapView.getController();
        mapController.setZoom(16);
        mapController.animateTo(startPoint);

         // Configuração do Mapa
                mapView.setTilesScaledToDpi(true);
//                mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

                //  Atribui o mapa offline em mapView
                mapView.setTileSource(MAPA_UFPA);

                /* Desabilita o uso da internet (opcional, mas uma boa forma de previnir que o mapa
                 * seja carregado via rede e de testar se o zip está carregando
                 */
                mapView.setUseDataConnection(false);

                mapView.setBuiltInZoomControls(false);
                mapView.setMinZoomLevel(15);
                mapView.setMaxZoomLevel(18);
                mapView.setMultiTouchControls(true);
                mapView.setUseDataConnection(true);


        // Restringe a área do mapa à região escolhida
        mapView.setScrollableAreaLimit(mapRegion);

//       addlayerToMap(myLocationOverlay,Constants.LAYER_MY_LOCATION);
        Log.i(TAG,"Layer added: My Location - " + mapView.getOverlayManager().toString());
        // Configuração para mostrar o boneco da posição do usuário
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.disableFollowLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);

    }



    // Configura a bottomsheet de múltiplos resultados da busca
    // // TODO: 2/24/2017 reestruturar a view para multiplos resultados
//    private void setupSearchResultBottomSheet(final ArrayList<Place> places){
//        // Configuração da listview
////        final SearchResultAdapter searchResultAdapter = new SearchResultAdapter(MapActivity.this,places);
////        ListView listView = (ListView) findViewById(R.id.listview);
////        listView.setAdapter(searchResultAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Configura a bottomsheet de detalhes sobre o local
//                searchResultSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                PlaceDetailsBottomSheet sheet = PlaceDetailsBottomSheet.newInstance(places.get(position));
//                sheet.show(getSupportFragmentManager(),"bottom sheet");
//            }
//        });
//
//        View bottomSheet = findViewById(R.id.bottom_sheet_results);
//        bottomSheet.setVisibility(View.VISIBLE);
//        searchResultSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        searchResultSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        searchResultSheetBehavior.setHideable(true);
//        searchResultSheetBehavior.setPeekHeight(120);
//        searchResultSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mapController.animateTo(new GeoPoint(defaultLocation.getLatitude(),defaultLocation.getLongitude()));
//                }
//                else if (newState == BottomSheetBehavior.STATE_COLLAPSED) ;
//
//                else if (newState == BottomSheetBehavior.STATE_HIDDEN) ;
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
//
//    }


    /* Localiza e traça as rotas entre dois pontos no mapa
    * Referências: https://github.com/MKergall/osmbonuspack/wiki/Tutorial_1
    *              https://graphhopper.com/api/1/docs/routing/
    *              https://github.com/MKergall/osmbonuspack/wiki/WhichRoutingService
    *              RoadManager Class
    */
    public void traceRoute(final Place place){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoadManager roadManager = new GraphHopperRoadManager(Constants.GRAPHHOPPER_KEY,true);
                // RoadManager roadManager = new GoogleRoadManager(); // bom para carros
                roadManager.addRequestOption("vehicle=foot");
                ArrayList<GeoPoint> wayPoints = new ArrayList<>();
                if(myCurrentLocation != null) {
                    // final GeoPoint startPoint = new GeoPoint(-1.47465, -48.45605); // Local teste: icen
                    wayPoints.add(new GeoPoint(myCurrentLocation)); // ponto de inicio
                    wayPoints.add(new GeoPoint(place.getLatitude(),place.getLongitude())); // ponto final
                    final Polyline roadOverlay = RoadManager.buildRoadOverlay(roadManager.getRoad(wayPoints));
                    roadOverlay.setWidth(15);
                    mapView.getOverlays().add(roadOverlay);
//                    mapLayers.add(Constants.LAYER_ROUTE);
                    btnClearMap.setVisibility(View.VISIBLE);
                    Log.i(TAG,"Layer added: foot path - " + mapView.getOverlayManager().toString());
                    isGoToRouteEnabled = true;

                }
            }
        }).start();
        mapView.invalidate();

    }

    @Override
    public void onOsmDataResponse(List<Place> places, Constants.MarkerTypes markersType, Constants.OverlayTags overlayTag, int taskStatus) {

    }
}



