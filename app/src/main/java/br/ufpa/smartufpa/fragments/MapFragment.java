package br.ufpa.smartufpa.fragments;


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

import br.ufpa.smartufpa.BuildConfig;
import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.asynctasks.SearchRouteTask;
import br.ufpa.smartufpa.asynctasks.interfaces.OnBusRouteListener;

import br.ufpa.smartufpa.utils.MapUtils;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.OverlayTags;
import br.ufpa.smartufpa.utils.enums.ServerResponse;
import br.ufpa.smartufpa.asynctasks.BusRouteTask;
import br.ufpa.smartufpa.customviews.CustomMapView;
import br.ufpa.smartufpa.asynctasks.interfaces.OnSearchRouteListener;
import br.ufpa.smartufpa.models.Place;
import br.ufpa.smartufpa.utils.SystemServicesManager;
import br.ufpa.smartufpa.utils.enums.MarkerStatuses;

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

/**
 * Stable Commit (20/09)
 * Fragment that holds the map view and all the tasks related to the map.
 * @author kaeuchoa
 */
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

    // Maximum number of routes that can be shown simultaneously
    private static final int MAX_ROUTES = 3;
    // Keeps the colors that will be used on the lines for routes
    private final ArrayList<Integer> ROUTE_LINE_COLORS = new ArrayList<>(MAX_ROUTES);
    // Number of routes already plotted on the map
    private static int ROUTES_COUNTER = 0;

    // VIEWS
    private CustomMapView mapView;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabBusLocation;
    private Button btnClearMap;

    private Context context;

    // Default region of interest for the map. Must be set based on location_config file
    private static Place defaultPlace;
    // Map boundaries that must be set based on location_config file
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
            Toast.makeText(context, "Adicionar lógica da função de localizar o circular", Toast.LENGTH_SHORT).show();
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

    /**
     * Initialize a MapFragment based on an specifc location
     * @param chosenPlace Place containing name and coordinates
     * @param mapBoundaries Coordinates of the box to limit the map
     * @return MapFragment Instance
     */
    public static MapFragment newInstance(final Place chosenPlace, final BoundingBox mapBoundaries) {
        MapFragment fragment = new MapFragment();
        defaultPlace = chosenPlace;
        mapRegion = mapBoundaries;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

        /* Important! Configure the user agent so you don't get banned from OSM servers.
         * The user agent must be a unique ID of you app.
         * For example, BuildConfig.APPLICATION_ID
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

        // Must add the CustomMapView to the layout on index 0 because of the axis Z used on CoordinatorLayouts
        mapView = new CustomMapView(context);
        final CoordinatorLayout cl = view.findViewById(R.id.coordinator_layout);
        cl.addView(mapView,0);

        fabMyLocation = view.findViewById(R.id.fab_my_location);
        fabBusLocation = view.findViewById(R.id.fab_bus_location);
        btnClearMap = view.findViewById(R.id.btn_clear_map);

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
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        }
        enableMyLocationOverlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
        myLocationOverlay.disableMyLocation();

    }

    @Override
    public void onStop() {
        super.onStop();
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

    /**
     * Initialize the map configurations such as MapCamera, zoom values
     * and interface aspects.
     */
    private void initializeMap(){
        // Map camera configuration
        GeoPoint startCameraPoint = new GeoPoint
                (defaultPlace.getLatitude(), defaultPlace.getLongitude());
        mapCamera = mapView.getController();
        mapCamera.setZoom(DEFAULT_ZOOM);
        mapCamera.animateTo(startCameraPoint);

        // Map configuration
        mapView.setTilesScaledToDpi(true);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(false);
        mapView.setMinZoomLevel(MIN_ZOOM);
        mapView.setMaxZoomLevel(MAX_ZOOM);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        // Limits the map area to the region set
        mapView.setScrollableAreaLimitDouble(mapRegion);
    }

    /**
     * Initialize the map layer and feature to show user's location
     * on the map.
     */
    private void enableMyLocationOverlay(){
        if(userLocation == null){
            myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),mapView);
        }
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.disableFollowLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);
        if(!mapView.containsOverlay(OverlayTags.MY_LOCATION))
            mapView.addOverlay(myLocationOverlay, OverlayTags.MY_LOCATION);
    }

    /**
     * Enables the layer to show the internal bus route with its bus stops
     */
    public void enableBusOverlay(){
        new BusRouteTask(this,mapView).execute();
    }

    // TODO (OFFLINE FUNCTIONS): USE KMLFOLDER TO SAVE IT
    // https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4
    private boolean areMapsInStorage(){

        return false;
    }

    private void clearMap(){
        mapView.removeAllOverlays();
        if(btnClearMap.getVisibility() == View.VISIBLE) btnClearMap.setVisibility(View.GONE);
        if(ROUTES_COUNTER == MAX_ROUTES)
            ROUTES_COUNTER = 0;
    }


    private void moveCameraToMyLocation(){
        try {
            double latitude = userLocation.getLatitude(),
                    longitude = userLocation.getLongitude();

            // Check if the user is inside the map region and just moves the camera if he/she is
            if(!mapRegion.contains(latitude,longitude))
                Toast.makeText(context, R.string.msg_out_of_covered_region, Toast.LENGTH_SHORT).show();
            else
                mapCamera.animateTo(new GeoPoint(latitude,longitude));

        }catch (NullPointerException e){
            Log.e(TAG,"Could not get current location.",e);
            if(!SystemServicesManager.isGPSEnabled(context)) {
                Toast.makeText(context, R.string.msg_turn_on_gps, Toast.LENGTH_SHORT).show();
                return;
            }
            // Can be still loading
            Toast.makeText(context, R.string.msg_loading_current_position, Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isLayerEnabled(final OverlayTags layerTag){
        return mapView.containsOverlay(layerTag);
    }

    /**
     * Creates and add an overlay to the map.
     * @param places List of places to plot on the map.
     * @param markersType Identifier for what icon should be used for markers.
     * @param overlayTag  Identifier for the overlay to be added on the OverlayManager.
     */
    public void createOverlay(List<Place> places, MarkerTypes markersType, OverlayTags overlayTag){
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
                    PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(rightPlace);
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

        mapView.addOverlay(poiMarkers,overlayTag);
        btnClearMap.setVisibility(View.VISIBLE);

    }

    /**
     * Moves and zoom in or out the camera to an specific geopoint.
     * @param geoPoint Point to move the camera
     * @param zoomLevel Value to zoom in or out - default zoom is 16
     */
    public void zoomToGeoPoint(final GeoPoint geoPoint, final int zoomLevel){
        mapView.getController().setZoom(zoomLevel);
        mapView.getController().animateTo(geoPoint);
        mapView.getController().setCenter(geoPoint);
    }

    /**
     * Calls the task that will calculate the route between user's current location and
     * a destination and limits the number of routes that can be shown simultaneously.
     * @param destination the final place to calculate the route
     */
    public void findRouteToPlace(final Place destination) {
        if (userLocation == null){
            Toast.makeText(context, getString(R.string.msg_loading_current_position), Toast.LENGTH_SHORT).show();
        }else if(ROUTES_COUNTER == MAX_ROUTES) {
            Toast.makeText(context, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
        } else {
            getActivity().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            new SearchRouteTask(this)
                    .execute(userLocation.getGeoPoint(), destination.getGeoPoint());
        }
    }

    /**
     * Calls the task that will calculate the route between two different locations
     * and limits the number of routes that can be shown simultaneously.
     * @param destination the final place to calculate the route
     */
    public void findRouteToPlace(final Place origin, final Place destination){
        if (userLocation == null){
            Toast.makeText(context, getString(R.string.msg_loading_current_position), Toast.LENGTH_SHORT).show();
        }else if(ROUTES_COUNTER == MAX_ROUTES) {
            Toast.makeText(context, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
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
    public void onSearchRouteResponse(final Overlay overlay, final ServerResponse taskStatus) {
        // actually plots the route on the map
        if(taskStatus == ServerResponse.SUCCESS){
            Polyline roadOverlay = (Polyline) overlay;
            roadOverlay.setColor(ROUTE_LINE_COLORS.get(ROUTES_COUNTER));
            roadOverlay.setWidth(10);
            mapView.addOverlay(roadOverlay, OverlayTags.ROUTE);
            ROUTES_COUNTER++;
        }else if(taskStatus == ServerResponse.TIMEOUT){
            Toast.makeText(context, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        }else if (taskStatus == ServerResponse.CONNECTION_FAILED){
            Toast.makeText(context, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        }else if(ROUTES_COUNTER == MAX_ROUTES){
            Toast.makeText(context, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
        }
        (getActivity().findViewById(R.id.progress_bar)).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBusRouteResponse(final Overlay overlay,final ServerResponse taskStatus) {
        // actually plots the bus route on the map
        if(taskStatus == ServerResponse.SUCCESS){
            mapView.addOverlay(overlay, OverlayTags.BUS_ROUTE);
            btnClearMap.setVisibility(View.VISIBLE);
        }else if(taskStatus == ServerResponse.TIMEOUT){
            Toast.makeText(context, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        }else if (taskStatus == ServerResponse.CONNECTION_FAILED){
            Toast.makeText(context, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        }
        (getActivity().findViewById(R.id.progress_bar)).setVisibility(View.INVISIBLE);

    }
}
