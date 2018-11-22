package br.ufpa.smartufpa.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufpa.smartufpa.BuildConfig;
import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.SelectCategoryActivity;
import br.ufpa.smartufpa.asynctasks.BusRouteTask;
import br.ufpa.smartufpa.asynctasks.SearchRouteTask;
import br.ufpa.smartufpa.asynctasks.interfaces.OnBusRouteListener;
import br.ufpa.smartufpa.asynctasks.interfaces.OnSearchRouteListener;
import br.ufpa.smartufpa.customviews.CustomMapView;
import br.ufpa.smartufpa.interfaces.PlaceDetailsDelegate;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.smartufpa.POI;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.MapUtils;
import br.ufpa.smartufpa.utils.PermissionHelper;
import br.ufpa.smartufpa.utils.SystemServicesManager;
import br.ufpa.smartufpa.utils.enums.MarkerStatus;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.OverlayTags;
import br.ufpa.smartufpa.utils.enums.ServerResponse;

/**
 * Fragment that holds the map view and all the tasks related to the map.
 *
 * @author kaeuchoa
 */
public class MapFragment extends Fragment implements LocationListener, OnSearchRouteListener,
        OnBusRouteListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    public static String FRAGMENT_TAG = MapFragment.class.getName();

    // MAP
    private MyLocationNewOverlay myLocationOverlay;
    private IMapController mapCamera;


    // Maximum number of routes that can be shown simultaneously
    private static final int MAX_ROUTES = 3;
    // Keeps the colors that will be used on the lines for routes
    private final ArrayList<Integer> ROUTE_LINE_COLORS = new ArrayList<>(MAX_ROUTES);
    // Number of routes already plotted on the map
    private static int ROUTES_COUNTER = 0;

    // VIEWS
    private CustomMapView mapView;
    private FloatingActionButton fabMyLocation;
    private Button btnClearMap;

    private Context context;

    // Default region of interest for the map. Must be set based on location_config file
    private static GeoPoint startCameraPoint;
    // Map boundaries that must be set based on location_config file
    private static BoundingBox mapRegion;
    private POI userLocation;
    private LocationManager locationManager;
    private PermissionHelper permissionHelper;
    private PlaceDetailsDelegate placeDetailsDelegate;

    private MapUtils mapUtils;

    /* CLICK LISTENERS */

    private Button.OnClickListener myLocationListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            moveCameraToUserLocation();
        }
    };

    private Button.OnClickListener clearMapListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearMap();
        }
    };


    // Required empty public constructor
    public MapFragment() {
    }

    /**
     * Initialize a MapFragment
     *
     * @return MapFragment Instance
     */


    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mapUtils = new MapUtils(context);
        permissionHelper = new PermissionHelper(context);
        placeDetailsDelegate = (PlaceDetailsDelegate) context;

        startCameraPoint = mapUtils.getMapStartPoint();
        mapRegion = mapUtils.getMapBoundingBox();

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

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Must add the CustomMapView to the layout on index 0 because of the axis Z used on CoordinatorLayouts
        mapView = new CustomMapView(context);
        final CoordinatorLayout cl = view.findViewById(R.id.coordinator_layout);
        cl.addView(mapView, 0);

        fabMyLocation = view.findViewById(R.id.fab_my_location);
        btnClearMap = view.findViewById(R.id.btn_clear_map);

        fabMyLocation.setOnClickListener(myLocationListener);

        btnClearMap.setOnClickListener(clearMapListener);

        initMapView();
        initMapCamera();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setupLocationManager();
        enableMyLocationOverlay();
    }

    @SuppressLint("MissingPermission") // Permission check done by static method
    private void setupLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (permissionHelper.isLocationPermissionGranted()) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            final String bestProvider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(bestProvider, 0L, 0f, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        myLocationOverlay.disableMyLocation();

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
    private void initMapView() {
        mapView.setTilesScaledToDpi(true);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(false);
        mapView.setMinZoomLevel(Constants.MapConfig.MIN_ZOOM);
        mapView.setMaxZoomLevel(Constants.MapConfig.MAX_ZOOM);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        // Limits the map area to the region set
        mapView.setScrollableAreaLimitDouble(mapRegion);
        CopyrightOverlay mCopyrightOverlay = new CopyrightOverlay(context);
        mapView.addOverlay(mCopyrightOverlay, OverlayTags.COPYRIGHT);
    }

    private void initMapCamera() {
        // Map camera configuration
        mapCamera = mapView.getController();
        mapCamera.animateTo(startCameraPoint, Constants.MapConfig.DEFAULT_ZOOM, (long) 0);
    }

    /**
     * Initialize the map layer and feature to show user's location
     * on the map.
     */
    private void enableMyLocationOverlay() {
        if (userLocation == null) {
            GpsMyLocationProvider provider = new GpsMyLocationProvider(context);
            provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
            myLocationOverlay = new MyLocationNewOverlay(provider, mapView);
        }
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.disableFollowLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);

        if (!mapView.isMyLocationActive())
            mapView.activeMyLocationOverlay(myLocationOverlay);
    }

    /**
     * Enables the layer to show the internal bus route with its bus stops
     */
    public void enableBusOverlay() {
        new BusRouteTask(this, mapView).execute();
    }

    // TODO (OFFLINE FUNCTIONS): USE KMLFOLDER TO SAVE IT
    // https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4
    private boolean areMapsInStorage() {
        return false;
    }

    private void clearMap() {
        mapView.removeAllOverlays();
        if (btnClearMap.getVisibility() == View.VISIBLE) btnClearMap.setVisibility(View.GONE);
        if (ROUTES_COUNTER == MAX_ROUTES)
            ROUTES_COUNTER = 0;
    }


    private void moveCameraToUserLocation() {
        try {
            // Check if the user is inside the map region and just moves the camera if he/she is
            if (mapUtils.isUserWithinLimits(userLocation, mapRegion)) {
                mapCamera.animateTo(userLocation.getGeoPoint());
            } else {
                Toast.makeText(context, R.string.msg_out_of_covered_region, Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            Log.e(TAG, "Could not get current location.", e);
            if (!SystemServicesManager.isGPSEnabled(context)) {
                Toast.makeText(context, R.string.msg_turn_on_gps, Toast.LENGTH_SHORT).show();
                return;
            }
            // Can be still loading
            Toast.makeText(context, R.string.msg_loading_current_position, Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isLayerEnabled(final OverlayTags layerTag) {
        return mapView.containsOverlay(layerTag);
    }

    public IMapController getMapCamera() {
        return mapCamera;
    }


    public void addMarkersToMap(List<Element> elements) {
        final FolderOverlay poiMarkers = new FolderOverlay();
        // Creates a marker for each place found
        for (final Element element : elements) {
            final Marker.OnMarkerClickListener onMarkerClickListener = createMarkerClickListener(element);
            final Drawable markerIcon = ContextCompat.getDrawable(context, element.getMarkerIcon());
            final GeoPoint location = new GeoPoint(element.getLat(), element.getLon());
            final Marker customMarker = mapUtils.createCustomMarker(mapView, markerIcon, location, onMarkerClickListener);
            poiMarkers.add(customMarker);
        }
        mapView.addOverlay(poiMarkers, OverlayTags.SEARCH);
        btnClearMap.setVisibility(View.VISIBLE);
    }

    @NonNull
    private Marker.OnMarkerClickListener createMarkerClickListener(final Element element) {
        final Drawable clickedMarkerDrawable = ContextCompat.getDrawable(context, R.drawable.ic_marker_details);
        return new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                // Will change the marker to its clicked icon
                marker.setIcon(clickedMarkerDrawable);
                mapView.postInvalidate();
                placeDetailsDelegate.showPlaceDetailsFragment(element);
                return true;
            }
        };
    }

    /**
     * Calls the task that will calculate the route between user's current location and
     * a destination and limits the number of routes that can be shown simultaneously.
     *
     * @param destination the final place to calculate the route
     */
    public void findRouteToPlace(final POI destination) {
        if (userLocation == null) {
            Toast.makeText(context, getString(R.string.msg_loading_current_position), Toast.LENGTH_SHORT).show();
        } else if (ROUTES_COUNTER == MAX_ROUTES) {
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
     *
     * @param destination the final place to calculate the route
     */
    public void findRouteToPlace(final POI origin, final POI destination) {
        if (userLocation == null) {
            Toast.makeText(context, getString(R.string.msg_loading_current_position), Toast.LENGTH_SHORT).show();
        } else if (ROUTES_COUNTER == MAX_ROUTES) {
            Toast.makeText(context, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
        } else {
            new SearchRouteTask(this)
                    .execute(origin.getGeoPoint(), destination.getGeoPoint());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (userLocation == null)
            userLocation = new POI(location.getLatitude(), location.getLongitude(), "user_location", "", "", "");
        else {
            userLocation.setLongitude(location.getLongitude());
            userLocation.setLatitude(location.getLatitude());
        }
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
    public void onSearchRouteResponse(final Overlay overlay, final ServerResponse taskStatus) {
        // actually plots the route on the map
        if (taskStatus == ServerResponse.SUCCESS) {
            Polyline roadOverlay = (Polyline) overlay;
            roadOverlay.setColor(ROUTE_LINE_COLORS.get(ROUTES_COUNTER));
            roadOverlay.setWidth(10);
            mapView.addOverlay(roadOverlay, OverlayTags.ROUTE);
            ROUTES_COUNTER++;
        } else if (taskStatus == ServerResponse.TIMEOUT) {
            Toast.makeText(context, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        } else if (taskStatus == ServerResponse.CONNECTION_FAILED) {
            Toast.makeText(context, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        } else if (ROUTES_COUNTER == MAX_ROUTES) {
            Toast.makeText(context, R.string.msg_routes_limit, Toast.LENGTH_SHORT).show();
        }
        (getActivity().findViewById(R.id.progress_bar)).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBusRouteResponse(final Overlay overlay, final ServerResponse taskStatus) {
        // actually plots the bus route on the map
        if (taskStatus == ServerResponse.SUCCESS) {
            mapView.addOverlay(overlay, OverlayTags.BUS_ROUTE);
            btnClearMap.setVisibility(View.VISIBLE);
        } else if (taskStatus == ServerResponse.TIMEOUT) {
            Toast.makeText(context, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        } else if (taskStatus == ServerResponse.CONNECTION_FAILED) {
            Toast.makeText(context, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        }
        (getActivity().findViewById(R.id.progress_bar)).setVisibility(View.INVISIBLE);

    }

    public void addLocationToMap() {
        Toast.makeText(context, R.string.msg_drag_marker, Toast.LENGTH_LONG).show();
        // TODO: Mudar esse ícone
        final Marker customMarker = mapUtils.createCustomMarker(
                mapView, ContextCompat.getDrawable(context, R.drawable.ic_marker),
                (GeoPoint) mapView.getMapCenter(), null);
        customMarker.setDraggable(true);
        customMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                double latitude = customMarker.getPosition().getLatitude();
                double longitude = customMarker.getPosition().getLongitude();
                goToSelectCategoryActivity(latitude, longitude);
                return false;
            }
        });

        customMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mapCamera.animateTo(marker.getPosition());
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
            }
        });
        final FolderOverlay folderOverlay = new FolderOverlay();
        folderOverlay.add(customMarker);
        mapView.addOverlay(folderOverlay, OverlayTags.NEW_LOCATION);
        btnClearMap.setVisibility(View.VISIBLE);
    }

    private void goToSelectCategoryActivity(double latitude, double longitude) {
        Intent intent = new Intent(getActivity(), SelectCategoryActivity.class);
        intent.putExtra(SelectCategoryActivity.Companion.getKEY_LATITUDE(), latitude);
        intent.putExtra(SelectCategoryActivity.Companion.getKEY_LONGITUDE(), longitude);
        startActivity(intent);
    }

}
