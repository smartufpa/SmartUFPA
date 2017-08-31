package com.example.kaeuc.smartufpa.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.BuildConfig;
import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.SystemServicesManager;
import com.example.kaeuc.smartufpa.utils.enums.OverlayTags;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceDetailsFragment extends Fragment {


    public static final String FRAGMENT_TAG = PlaceDetailsFragment.class.getName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CURRENT_PLACE = "current_place";
    private static final String ARG_USER_LOCATION = "user_location";
    private static final String TAG = PlaceDetailsFragment.class.getSimpleName();

    // VIEWS
    private TextView txtDetPlaceName;

    private TextView txtDetPlaceDesc;
    private TextView txtDetLocName;
    private Button btnDetFootRoute;


    // TODO (POSTPONED): LOAD IMAGE OF PLACE AND IMPLEMENT RATING FUNCTIONS

    private Place currentPlace;



    private Place userLocation;

    public PlaceDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentPlace Place which the user has chosen to see details.
     * @return A new instance of fragment PlaceDetailsFragment.
     */
    public static PlaceDetailsFragment newInstance(Place currentPlace, Place userLocation) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_PLACE, currentPlace);
        args.putParcelable(ARG_USER_LOCATION,userLocation);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPlace = getArguments().getParcelable(ARG_CURRENT_PLACE);
            userLocation = getArguments().getParcelable(ARG_USER_LOCATION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        txtDetPlaceName = view.findViewById(R.id.txt_det_placename);
        txtDetPlaceDesc = view.findViewById(R.id.txt_det_place_desc);
        txtDetLocName = view.findViewById(R.id.txt_det_place_loc_name);
        btnDetFootRoute = view.findViewById(R.id.btn_det_foot_route);

        if(currentPlace.getShortName().equals(Constants.NO_SHORT_NAME))
            txtDetPlaceName.setText(currentPlace.getName());
        else
            txtDetPlaceName.setText(currentPlace.getName() + " (" + currentPlace.getShortName()+ ")");
        txtDetPlaceDesc.setText(currentPlace.getDescription());
        txtDetLocName.setText(String.format("%s %s", getString(R.string.lbl_local_name), currentPlace.getLocName()));

        btnDetFootRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemServicesManager.isNetworkEnabled(getContext())) {
                    final MapFragment mapFragment = (MapFragment) getActivity().getSupportFragmentManager()
                            .findFragmentByTag(MapFragment.FRAGMENT_TAG);
                    mapFragment.showRouteToPlace(currentPlace);
                }
            }
        });

        return view;
    }



}
