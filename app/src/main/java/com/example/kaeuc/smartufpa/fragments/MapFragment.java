package com.example.kaeuc.smartufpa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.models.Place;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private static final String LOG_TAG = MapFragment.class.getSimpleName() ;
    public static String FRAGMENT_TAG = MapFragment.class.getName();

    // MAP
    private MyLocationNewOverlay myLocationOverlay;
    private IMapController mapCameraController;
    private static final int DEFAULT_ZOOM = 16;

    // VIEWS
    private MapView mapView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: BUSCAR ESSES VALORES A PARTIR DA CONFIGURAÇÃO
    private Place defaultLocation = new Place(-1.47485, -48.45651, "UFPA");


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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initializeMap(){
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()),mapView);

        // ANALISAR SE PRECISA SER GLOBAL
        // Restrição da região mostrada do mapa usando coordenadas
        // TODO: BUSCAR ESSES VALORES A PARTIR DA CONFIGURAÇÃO
        BoundingBox mapRegion = new BoundingBox(-1.457886, -48.437957, -1.479967, -48.459779);

        GeoPoint startCameraPoint = new GeoPoint
                (defaultLocation.getLatitude(),defaultLocation.getLongitude());
        mapCameraController = mapView.getController();
        mapCameraController.setZoom(DEFAULT_ZOOM);
        mapCameraController.animateTo(startCameraPoint);

        // Configuração do Mapa
        mapView.setTilesScaledToDpi(true);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        //  Atribui o mapa offline em mapView
//        mapView.setTileSource(MAPA_UFPA);

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
        mapView.setScrollableAreaLimitDouble(mapRegion);

        mapView.getOverlays().add(myLocationOverlay);
        mapView.postInvalidate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapview);
        initializeMap();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
        myLocationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause()");
        myLocationOverlay.disableMyLocation();
    }
}
