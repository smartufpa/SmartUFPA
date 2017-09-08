package com.example.kaeuc.smartufpa.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.BuildConfig;
import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.asynctasks.BusRouteTask;
import com.example.kaeuc.smartufpa.asynctasks.SearchRouteTask;
import com.example.kaeuc.smartufpa.customviews.CustomMapView;
import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnBusRouteListener;
import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnSearchRouteListener;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.MapUtils;
import com.example.kaeuc.smartufpa.utils.SystemServicesManager;
import com.example.kaeuc.smartufpa.utils.enums.MarkerStatuses;
import com.example.kaeuc.smartufpa.utils.enums.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.enums.OverlayTags;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapFragment extends Fragment implements LocationListener, OnSearchRouteListener,
        OnBusRouteListener {

    private static final String TAG = MapFragment.class.getSimpleName() ;
    public static String FRAGMENT_TAG = MapFragment.class.getName();

    // MAP
    private MyLocationNewOverlay myLocationOverlay;
    private IMapController mapCamera;
    private static final int DEFAULT_ZOOM = 16;
    private static final int MIN_ZOOM = 15;
    private static final int MAX_ZOOM = 18;

    private final ArrayList<Integer> ROUTE_LINE_COLORS = new ArrayList<>(3);
    private static int ROUTES_COUNTER = 0;
    private static final int MAX_ROUTES = 3;

    private final XYTileSource MAPA_UFPA = new XYTileSource("ufpa_mapa", 15, 18, 256, ".png", new String[] {});
    private final XYTileSource MAPA_UFPA_TRANSPORTE = new XYTileSource("ufpa_transporte", 15, 18, 256, ".png", new String[] {});

    // VIEWS
    private CustomMapView mapView;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabBusLocation;
    private Button btnClearMap;


    private Context parentContext;

    private static Place defaultPlace;
    // Restrição da região mostrada do mapa usando coordenadas
    private static BoundingBox mapRegion;


    private Place userLocation;


    private LocationManager locationManager;

      /* CLICK LISTENERS */

    private Button.OnClickListener myLocationListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            moveCameraToMyLocation();
        }
    };

    private Button.OnClickListener busLocationListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Toast.makeText(parentContext, "Adicionar lógica da função de localizar o circular", Toast.LENGTH_SHORT).show();
        }
    };

    private Button.OnClickListener clearMapListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            clearMap();
        }
    };



    // Required empty public constructor
    public MapFragment() {}


    public static MapFragment newInstance(final Place chosenPlace, final BoundingBox mapBoundaries) {
        MapFragment fragment = new MapFragment();
        defaultPlace = chosenPlace;
        mapRegion = mapBoundaries;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentContext = getContext();

        /* Importante! Configure o user agent para previnir ser banido dos servidores do OSM
         * O user agent deve ser uma identificação única do seu aplicativo
         * Um exemplo mostra a utilização de "BuildConfig.APPLICATION_ID"
         */
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        ROUTE_LINE_COLORS.add(Color.rgb(100, 100, 255)); // blue
        ROUTE_LINE_COLORS.add(Color.rgb(255, 100, 100)); // red
        ROUTE_LINE_COLORS.add(Color.rgb(62, 153, 62)); // green

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        // Adiciona a CustomMapView ao layout na posição 0 por conta do eixo Z do CoordinatorLayout
        mapView = new CustomMapView(parentContext);
        final CoordinatorLayout cl = view.findViewById(R.id.coordinator_layout);
        cl.addView(mapView,0);

        // Encontra as views
        fabMyLocation = view.findViewById(R.id.fab_my_location);
        fabBusLocation = view.findViewById(R.id.fab_bus_location);
        btnClearMap = view.findViewById(R.id.btn_clear_map);


        // Atrela os listerners aos botões
        fabMyLocation.setOnClickListener(myLocationListener);
        fabBusLocation.setOnClickListener(busLocationListener);

        // TODO: MAKE IT VISIBLE WHEN THE BUS LOCATION FUNCTION IS IMPLEMENTED
        fabBusLocation.setVisibility(View.GONE);
        btnClearMap.setOnClickListener(clearMapListener);

        initializeMap();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager = (LocationManager) parentContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(parentContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(parentContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        }
        // Adiciona a camada de localização do usuário
        enableMyLocationOverlay();

        Log.i(TAG, "onResume: " + mapView.getLayersTagsNames());

    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(parentContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(parentContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
        myLocationOverlay.disableMyLocation();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: " + mapView.getLayersTagsNames());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        userLocation = null;
        myLocationOverlay = null;
        mapView = null;
        mapCamera = null;
    }

    // TODO (POSTPONED): IMPLEMENT THIS METHOD
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    private void initializeMap(){
        // Configuração da câmera do mapa
        GeoPoint startCameraPoint = new GeoPoint
                (defaultPlace.getLatitude(), defaultPlace.getLongitude());
        mapCamera = mapView.getController();
        mapCamera.setZoom(DEFAULT_ZOOM);
        mapCamera.animateTo(startCameraPoint);
        // Configuração do Mapa
        mapView.setTilesScaledToDpi(true);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        //  Atribui o mapa offline em mapView
//        if (checar configurações para saber se o download do mapa foi realizado){
//        mapView.setTileSource(MAPA_UFPA);
//        }

        /* Desabilita o uso da internet (opcional, mas uma boa forma de previnir que o mapa
         * seja carregado via rede e de testar se o zip está carregando
         */
//        mapView.setUseDataConnection(false);
        mapView.setBuiltInZoomControls(false);
        mapView.setMinZoomLevel(MIN_ZOOM);
        mapView.setMaxZoomLevel(MAX_ZOOM);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        mapView.setScrollableAreaLimitDouble(mapRegion); // Restringe a área do mapa à região escolhida
    }

    private void enableMyLocationOverlay(){
        // Camada de posição do usuário
        if(userLocation == null){
            myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(parentContext),mapView);
        }
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.disableFollowLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);
        if(!mapView.containsOverlay(OverlayTags.MY_LOCATION))
            mapView.addTileOverlay(myLocationOverlay, OverlayTags.MY_LOCATION);
    }

    public void enableBusOverlay(){
        new BusRouteTask(this,mapView).execute();
    }

    // TODO (OFFLINE FUNCTIONS): USE KMLFOLDER TO SAVE IT
    // https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4
    private boolean areMapsInStorage(){

        return false;
    }

    private void clearMap(){
        mapView.clearMap();
        if(btnClearMap.getVisibility() == View.VISIBLE) btnClearMap.setVisibility(View.GONE);
        if(ROUTES_COUNTER == MAX_ROUTES)
            ROUTES_COUNTER = 0;
    }




    private void moveCameraToMyLocation(){
        try {
            double latitude = userLocation.getLatitude(),
                    longitude = userLocation.getLongitude();

            // Checa se o usuário se encontra dentro da area do mapa
            if(!mapRegion.contains(latitude,longitude)) Toast.makeText(parentContext,
                    R.string.msg_out_of_covered_region, Toast.LENGTH_SHORT).show();
            else{
                Log.i(TAG,"Current Location: (" + latitude + "," + longitude + ")" );
                mapCamera.animateTo(new GeoPoint(latitude,longitude));
            }
        // Se capturar a exception, o sistema não pôde recuperar as informações do GPS
        }catch (NullPointerException e){
            Log.e(TAG,"Could not get current location.",e);
            // Checa se o GPS está ligado
            if(!SystemServicesManager.isGPSEnabled(parentContext)) {
                Toast.makeText(parentContext, R.string.msg_turn_on_gps, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(parentContext, R.string.msg_loading_current_position, Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isLayerEnabled(final OverlayTags layerTag){
        return mapView.containsOverlay(layerTag);
    }

    /**
     * Creates and add an overlay to the map.
     * @param places - List of places to plot on the map.
     * @param markersType - Identify what icon should be used for markers.
     * @param overlayTag - Identify the overlay to be added on the OverlayManager.
     */
    public void createLayerToMap(List<Place> places, MarkerTypes markersType, OverlayTags overlayTag){
        final FolderOverlay poiMarkers = new FolderOverlay();

        MapUtils mapUtils = new MapUtils(getContext());
        final HashMap<MarkerStatuses, Drawable> markerDrawables = mapUtils.getMarkerDrawable(markersType);


        // Creates a marker for each place found
        for (int i = 0; i < places.size(); i++) {
            Place checkPlace = places.get(i);
            if (!mapRegion.contains(checkPlace.getLatitude(),checkPlace.getLongitude()))
                places.remove(checkPlace);

            final Place rightPlace =  places.get(i);
            Marker.OnMarkerClickListener onMarkerClickListener = new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    // Finds the container in which the fragment will be inflated
                    FrameLayout bottomSheetContainer = getActivity().findViewById(R.id.bottom_sheet_container);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetContainer);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    // Sets up a PlaceDetailsFragment to show specific information about the selected Place
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(rightPlace,userLocation);
                    fragmentManager.beginTransaction()
                            .replace(R.id.bottom_sheet_container,placeDetailsFragment,PlaceDetailsFragment.FRAGMENT_TAG)
                            .commit();

                    // Will change the marker to its clicked icon
                    marker.setIcon(markerDrawables.get(MarkerStatuses.CLICKED));
                    mapView.postInvalidate();
                    return true;
                }
            };

            final Marker customMarker = mapUtils
                    .createCustomMarker(mapView, markerDrawables.get(MarkerStatuses.NOT_CLICKED),
                    new GeoPoint(rightPlace.getLatitude(), rightPlace.getLongitude()), onMarkerClickListener);

            poiMarkers.add(customMarker);

        }

        mapView.addTileOverlay(poiMarkers,overlayTag);
        btnClearMap.setVisibility(View.VISIBLE);

    }

    public void zoomToGeoPoint(GeoPoint geoPoint, int zoomLevel){
        mapView.getController().setZoom(zoomLevel);
        mapView.getController().animateTo(geoPoint);
        mapView.getController().setCenter(geoPoint);
    }

    public void showRouteToPlace(final Place destination) {

        if (userLocation == null){
            Toast.makeText(parentContext, getString(R.string.msg_loading_current_position), Toast.LENGTH_SHORT).show();
        }else if(ROUTES_COUNTER == MAX_ROUTES) {
            Toast.makeText(parentContext, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
        } else {
            getActivity().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            new SearchRouteTask(this)
                    .execute(userLocation.getGeoPoint(), destination.getGeoPoint());
        }
    }

    public void showRouteToPlace(final Place origin, final Place destination){
        getActivity().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        if (userLocation == null){
            Toast.makeText(parentContext, getString(R.string.msg_loading_current_position), Toast.LENGTH_SHORT).show();
        }else if(ROUTES_COUNTER == MAX_ROUTES) {
            Toast.makeText(parentContext, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
        } else {
            new SearchRouteTask(this)
                    .execute(origin.getGeoPoint(), destination.getGeoPoint());
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        if(userLocation == null)
            userLocation = new Place(location.getLatitude(),location.getLongitude(),"user_location");
        else {
            userLocation.setLongitude(location.getLongitude());
            userLocation.setLatitude(location.getLatitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    public Place getUserLocation() {
        return userLocation;
    }

    @Override
    public void onSearchRouteResponse(final Overlay ROUTE_OVERLAY, ServerResponse taskStatus) {
        if(taskStatus == ServerResponse.SUCCESS){
            Polyline roadOverlay = (Polyline) ROUTE_OVERLAY;
            roadOverlay.setColor(ROUTE_LINE_COLORS.get(ROUTES_COUNTER));
            roadOverlay.setWidth(10);
            mapView.addTileOverlay(roadOverlay, OverlayTags.ROUTE);
            ROUTES_COUNTER++;
        }else if(taskStatus == ServerResponse.TIMEOUT){
            Toast.makeText(parentContext, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        }else if (taskStatus == ServerResponse.CONNECTION_FAILED){
            Toast.makeText(parentContext, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        }else if(ROUTES_COUNTER == MAX_ROUTES){
            Toast.makeText(parentContext, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
        }
        getActivity().findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBusRouteResponse(Overlay overlay, ServerResponse taskStatus) {
        if(taskStatus == ServerResponse.SUCCESS){
            mapView.addTileOverlay(overlay, OverlayTags.BUS_ROUTE);
            btnClearMap.setVisibility(View.VISIBLE);
        }else if(taskStatus == ServerResponse.TIMEOUT){
            Toast.makeText(parentContext, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        }else if (taskStatus == ServerResponse.CONNECTION_FAILED){
            Toast.makeText(parentContext, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        }
        getActivity().findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);

    }
}
