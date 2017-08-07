package com.example.kaeuc.smartufpa.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.BuildConfig;
import com.example.kaeuc.smartufpa.CustomOverlayManager;
import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.models.Place;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import static com.example.kaeuc.smartufpa.utils.SystemServicesManager.isGPSEnabled;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements LocationListener{

    private static final String LOG_TAG = MapFragment.class.getSimpleName() ;
    public static String FRAGMENT_TAG = MapFragment.class.getName();

    // MAP
    private MyLocationNewOverlay myLocationOverlay;
    private IMapController mapCamera;
    private static final int DEFAULT_ZOOM = 16;
    private static final int MIN_ZOOM = 15;
    private static final int MAX_ZOOM = 18;

    // VIEWS
    private MapView mapView;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabBusLocation;
    private Button btnClearMap;


    private Context parentContext;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: BUSCAR ESSES VALORES A PARTIR DA CONFIGURAÇÃO
    private final Place defaultLocation = new Place(-1.47485, -48.45651, "UFPA");
    // Restrição da região mostrada do mapa usando coordenadas
    private final BoundingBox mapBoundaries = new BoundingBox(-1.457886, -48.437957, -1.479967, -48.459779);


    private Location myCurrentLocation;
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
            Toast.makeText(parentContext, "Adicionar lógica da função de limpar o mapa", Toast.LENGTH_SHORT).show();
        }
    };



    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate()");
        parentContext = getContext();

        /* Configura o caminho do armazenamento em cache do mapa, se o device não possui cartão SD,
         * ele deve ser configurado para o caminho de arquivos do device
         */
        OpenStreetMapTileProviderConstants.setCachePath(getActivity().getFilesDir().getAbsolutePath());

        /* Importante! Configure o user agent para previnir ser banido dos servidores do OSM
         * O user agent deve ser uma identificação única do seu aplicativo
         * Um exemplo mostra a utilização de "BuildConfig.APPLICATION_ID"
         */
        OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initializeMap(){
        // Camada de posição do usuário
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(parentContext),mapView);
        myLocationOverlay.disableFollowLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);

        // Configuração da câmera do mapa
        GeoPoint startCameraPoint = new GeoPoint
                (defaultLocation.getLatitude(),defaultLocation.getLongitude());
        mapCamera = mapView.getController();
        mapCamera.setZoom(DEFAULT_ZOOM);
        mapCamera.animateTo(startCameraPoint);
        // Configuração do Mapa
        mapView.setTilesScaledToDpi(true);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        //  Atribui o mapa offline em mapView
//        mapView.setTileSource(MAPA_UFPA);

        /* Desabilita o uso da internet (opcional, mas uma boa forma de previnir que o mapa
         * seja carregado via rede e de testar se o zip está carregando
         */
//        mapView.setUseDataConnection(false);

        mapView.setBuiltInZoomControls(false);
        mapView.setMinZoomLevel(MIN_ZOOM);
        mapView.setMaxZoomLevel(MAX_ZOOM);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        mapView.setScrollableAreaLimitDouble(mapBoundaries); // Restringe a área do mapa à região escolhida

        mapView.getOverlays().add(myLocationOverlay);
        mapView.postInvalidate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView()");

        // Infla o layout para este fragmento
        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        // Encontra as views
        mapView = (MapView) view.findViewById(R.id.mapview);
        fabMyLocation = (FloatingActionButton) view.findViewById(R.id.fab_my_location);
        fabBusLocation = (FloatingActionButton) view.findViewById(R.id.fab_bus_location);
        btnClearMap = (Button) view.findViewById(R.id.btn_clear_map);

        // Atrela os listerners aos botões
        fabMyLocation.setOnClickListener(myLocationListener);
        fabBusLocation.setOnClickListener(busLocationListener);
        btnClearMap.setOnClickListener(clearMapListener);

        initializeMap();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
        locationManager = (LocationManager) parentContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(parentContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(parentContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        }
        myLocationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause()");
        myLocationOverlay.disableMyLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy()");
        myCurrentLocation = null;
        myLocationOverlay = null;
        mapView = null;
        mapCamera = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void moveCameraToMyLocation(){
        try {
            double latitude = myCurrentLocation.getLatitude(),
                    longitude = myCurrentLocation.getLongitude();

            // Checa se o usuário se encontra dentro da area do mapa
            if(!mapBoundaries.contains(latitude,longitude)) Toast.makeText(parentContext,
                    R.string.msg_out_of_covered_region, Toast.LENGTH_SHORT).show();
            else{
                Log.i(LOG_TAG,"Current Location: (" + latitude + "," + longitude + ")" );
                mapCamera.animateTo(new GeoPoint(latitude,longitude));
            }
        // Se capturar a exception, o sistema não pôde recuperar as informações do GPS
        }catch (NullPointerException e){
            Log.e(LOG_TAG,"Could not get current location.");
            e.printStackTrace();
            // Checa se o GPS está ligado
            if(!isGPSEnabled(parentContext)) {
                Toast.makeText(parentContext, R.string.msg_turn_on_gps, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(parentContext, R.string.msg_loading_current_position, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        myCurrentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
