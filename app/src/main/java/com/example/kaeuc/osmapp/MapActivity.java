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
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaeuc.osmapp.Extras.Constants;
import com.example.kaeuc.osmapp.Extras.Place;
import com.example.kaeuc.osmapp.Extras.PlaceDetailsBottomSheet;
import com.example.kaeuc.osmapp.Extras.SearchListAdapter;
import com.example.kaeuc.osmapp.Server.NominatimDataRequest;
import com.example.kaeuc.osmapp.Server.NominatimDataRequestResponse;
import com.example.kaeuc.osmapp.Server.OsmDataRequest;
import com.example.kaeuc.osmapp.Server.OsmDataRequestResponse;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
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
    private BottomSheetBehavior searchResultSheetBehavior;
    private BottomSheetBehavior placeDetailsSheetBehavior;


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

    private List<String> mapMarkers = new ArrayList<>();


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
                        Toast.makeText(MapActivity.this, "Por favor, ligar o GPS novamente.", Toast.LENGTH_SHORT).show();
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

    // Método responsável por configurar o mapa na sua inicialização
    private void setupMap() {
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mMap);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Configuração do MapController: Posição inicial e zoom
                defaultLocation = new Place(-1.47485, -48.45651, "UFPA");
                startPoint = new GeoPoint(defaultLocation.getLatitude(), defaultLocation.getLongitude());
                mMapController = mMap.getController();
                mMapController.setZoom(16);
                mMapController.animateTo(startPoint);

                // Configuração do Mapa
                mMap.setTilesScaledToDpi(true);
                mMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

                //  Atribui o mapa offline em mMap
                //mMap.setTileSource(new XYTileSource("actions_bar_items.xml.mbtiles", 0, 18, 256, ".jpg", new String[] {}));

                /* Desabilita o uso da internet (opcional, mas uma boa forma de previnir que o mapa
                 * seja carregado via rede e de testar se o zip está carregando
                 */
                //mMap.setUseDataConnection(false);

                mMap.setBuiltInZoomControls(false);
                mMap.setMinZoomLevel(15);
                mMap.setMaxZoomLevel(19);
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


    // Método responsável por criar as opções de menu na barra superior do aplicativo
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  Infla o menu; Isso adiciona os itens à barra de ações se existir
        getMenuInflater().inflate(R.menu.actions_bar_items, menu);

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


                    mMap.getOverlays().remove(mapMarkers.indexOf(Constants.SEARCH_LAYER) + 1);
                    mapMarkers.remove(Constants.SEARCH_LAYER);

                    Log.i(TAG,"Map Overlays: " + mMap.getOverlays().size());
                    Log.i(TAG,"Map Markers: " + mapMarkers.size());
                    Log.i(TAG,"Map Overlays: " + mMap.getOverlays().toString());
                    if(searchResultSheetBehavior != null) {
                        if ((searchResultSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                                || (searchResultSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED))
                            searchResultSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    if(placeDetailsSheetBehavior!=null) {
                        if ((placeDetailsSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                                || (placeDetailsSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED))
                            placeDetailsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    if(isGoToRouteEnabled){
                        mMap.getOverlays().remove(mapMarkers.indexOf(Constants.ROUTE_LAYER) + 1);
                        mapMarkers.remove(Constants.ROUTE_LAYER);
                    }
                    mMap.invalidate();
                }
                return true;
            }
        });

        // Configura a barra de busca (SearchWidget)
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);


        return true;
    }

    /* Início dos métodos de utilização da lista lateral (Drawer)*/

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // Configuração dos items da lista lateral
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_xerox) {
            // Caso a camada de filtro não esteja ativa, executar a busca e adicionar marcadores
            if (!isXeroxEnabled) {
                new OsmDataRequest(this, progressBar).execute(Constants.XEROX_FILTER);
                isXeroxEnabled = true;
            } else {
                /* Senão, remover a camada do mapa. Utiliza-se a arraylist mapMarkers para resgatar
                * o indice em que a camada foi inserida no mapa, dado que o método remove apresenta
                * problemas para remover diretamente a camada.
                * Ex.: mMap.getOverlays.remove(tilesOverlay)
                * Então é necessário remover a camada pelo indice que será sempre adicionado de um
                * por conta da camada base adicionada para a localização do usuário
                */
                mMap.getOverlays().remove(mapMarkers.indexOf(Constants.XEROX_FILTER)+1);
                mapMarkers.remove(Constants.XEROX_FILTER);
                isXeroxEnabled = false;

            }
        } else if (id == R.id.nav_restaurantes) {
            if (!isRestaurantEnabled) {
                new OsmDataRequest(this, progressBar).execute(Constants.RESTAURANT_FILTER);
                isRestaurantEnabled = true;
            } else {
                mMap.getOverlays().remove(mapMarkers.indexOf(Constants.RESTAURANT_FILTER)+1);
                mapMarkers.remove(Constants.RESTAURANT_FILTER);
                isRestaurantEnabled = false;

            }
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* Fim dos métodos de utilização do Drawer lateral*/


    /* Início de  métodos envolvendo localização*/

    public void traceRoute(final Place place){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoadManager roadManager = new MapQuestRoadManager(Constants.MAPQUEST_KEY);
                roadManager.addRequestOption("routeType=pedestrian");
                ArrayList<GeoPoint> wayPoints = new ArrayList<>();
                if(myCurrentLocation != null) {
                    final GeoPoint startPoint = new GeoPoint(myCurrentLocation);
                    final GeoPoint endPoint = new GeoPoint(place.getLatitude(),place.getLongitude());
                    wayPoints.add(startPoint);
                    wayPoints.add(endPoint);
                    Road road = roadManager.getRoad(wayPoints);
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                    mMap.getOverlays().add(roadOverlay);
                    mapMarkers.add(Constants.ROUTE_LAYER);
                    isGoToRouteEnabled = true;
                }
            }
        }).start();

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

    /* Fim dos  métodos envolvendo localização*/


    /* Início dos métodos que executam em uma AsyncTask.
    *  Esses são responsáveis por receber o resultado dessas tarefas
    */

    // Esse método retorna dados do servidor do OSM (Referência http://wiki.openstreetmap.org/wiki/Xapi )
    @Override
    public void osmTaskCompleted(final List<Place> places, final String filter) {
        new Thread(new Runnable() {
            public void run() {
                // Cria e adiciona a camada de marcadores ao mapa
                FolderOverlay poiMarkers = new FolderOverlay(MapActivity.this);
                Drawable poiIcon = null;
                // Configura o ícone de acordo com o filtro que será adicionado
                if (filter.equals(Constants.XEROX_FILTER))
                    poiIcon = ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_marker_xerox);
                else if (filter.equals(Constants.RESTAURANT_FILTER))
                    poiIcon = ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_marker_restaurant);
                else if(filter.equals(Constants.RESTROOM_FILTER)) // Definir essa string
                    poiIcon = ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_marker_restroom);

                // Cria um marcador para cada local encontrado
                for (Place place : places) {
                    Marker poiMarker = new Marker(mMap);
                    poiMarker.setTitle(place.getName());
                    // Descrição do marcador
                    // poiMarker.setSnippet(place.getName());
                    poiMarker.setPosition(new GeoPoint(place.getLatitude(), place.getLongitude()));
                    poiMarker.setIcon(poiIcon);
                    poiMarkers.add(poiMarker);
                }
                // Mapeia em que posição da arraylist a camada está sendo aplicada
                mMap.getOverlays().add(poiMarkers);
                mapMarkers.add(filter);
                Log.e(TAG,"Map Overlays: " + mMap.getOverlays().size());
                Log.e(TAG,"Map Markers: " + mapMarkers.size());
                Log.e(TAG,"Map Overlays: " + mMap.getOverlays().toString());
            }
        }).start();

    }

    // Esse método retorna dados do servidor do Nominatim (Referência http://wiki.openstreetmap.org/wiki/Nominatim )
    @Override
    public void nominatimTaskResponse(final ArrayList<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (places != null) {
                    // Se mais de um resultado for retornado, utiliza uma bottomsheet para apresentar os resultados
                    if (places.size() > 1) {
                        setupSearchResultBottomSheet(places);
                    }

                    // Cria e adiciona a camada de marcadores ao mapa
                    FolderOverlay poiMarkers = new FolderOverlay(MapActivity.this);

                    // Configura o ícone de marcador para cada local encontrado
                    Drawable poiIcon = getResources().getDrawable(R.drawable.ic_marker);
                    for (final Place place : places) {
                        Marker poiMarker = new Marker(mMap);
                        poiMarker.setAnchor(0.5f,1);
                        poiMarker.setTitle(place.getName());
                        poiMarker.setSnippet(place.getDescription());
                        poiMarker.setPosition(place.getPosition());
                        poiMarker.setIcon(poiIcon);
                        poiMarkers.add(poiMarker);
                        poiMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                marker.setIcon(ContextCompat.getDrawable(MapActivity.this,R.drawable.ic_marker_details));
                                marker.setAnchor(0.5f,1);
                                mMap.invalidate();
                                PlaceDetailsBottomSheet sheet = PlaceDetailsBottomSheet.newInstance(place);
                                sheet.show(getSupportFragmentManager(),"bottom sheet");
                                return true;
                            }
                        });

                    }
                    mapMarkers.add(Constants.SEARCH_LAYER);
                    mMap.getOverlays().add(poiMarkers);

                    Log.e(TAG,"Map Overlays: " + mMap.getOverlays().size());
                    Log.e(TAG,"Map Markers: " + mapMarkers.size());
                    Log.e(TAG,"Map Overlays: " + mMap.getOverlays().toString());

                    isSearchEnabled = true;
                } else {
                    Toast.makeText(MapActivity.this, "Houve um problema na sua conexão. Tente novamente.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mMap.invalidate();
    }



    // Configura a bottomsheet de múltiplos resultados da busca
    private void setupSearchResultBottomSheet(final ArrayList<Place> pois){
        // Configurando a listview
        SearchListAdapter searchListAdapter = new SearchListAdapter(MapActivity.this,pois);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(searchListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Configura a bottomsheet de detalhes sobre o local
                searchResultSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                View bottomSheet = findViewById(R.id.bottom_sheet_place_details);
                bottomSheet.setVisibility(View.VISIBLE);
                placeDetailsSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                placeDetailsSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                placeDetailsSheetBehavior.setPeekHeight(300);
                placeDetailsSheetBehavior.setHideable(true);
                ((TextView)findViewById(R.id.txt_place_description)).setText(pois.get(position).getDescription());
                ((TextView)findViewById(R.id.txt_place_name)).setText(pois.get(position).getName());
                mMapController.animateTo(new GeoPoint(pois.get(position).getLatitude(),pois.get(position).getLongitude()));
                mMapController.setZoom(18);

            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet_results);
        bottomSheet.setVisibility(View.VISIBLE);
        searchResultSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        searchResultSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        searchResultSheetBehavior.setHideable(true);
        searchResultSheetBehavior.setPeekHeight(800);
        searchResultSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mMapController.animateTo(new GeoPoint(defaultLocation.getLatitude(),defaultLocation.getLongitude()));
                }
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                }
                else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                }
            }


            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }



    @Override
    public boolean onQueryTextSubmit(final String query) {
        final String latitude = String.valueOf(defaultLocation.getLatitude());
        final String longitude = String.valueOf(defaultLocation.getLongitude());
        findViewById(R.id.action_search).clearFocus();
        if (isSearchEnabled) {
            mMap.getOverlays().remove(mapMarkers.indexOf(Constants.SEARCH_LAYER) + 1);
            mapMarkers.remove(Constants.SEARCH_LAYER);
            isSearchEnabled = false;
        }
        new NominatimDataRequest(this, progressBar).execute(query, latitude, longitude);


        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // // TODO: Salvar informações relevantes para restaurar o estado da aplicação
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }






}

