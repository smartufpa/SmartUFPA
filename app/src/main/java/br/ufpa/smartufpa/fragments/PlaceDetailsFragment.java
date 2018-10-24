package br.ufpa.smartufpa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.smartufpa.POI;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.SystemServicesManager;

/**
 * Fragment to show details about an specific place selected by the user.
 * Must follow the POI model.
 * @author kaeuchoa
 */
public class PlaceDetailsFragment extends Fragment {


    public static final String FRAGMENT_TAG = PlaceDetailsFragment.class.getName();

    private static final String ARG_POINT_OF_INTEREST = "point_of_interest";
    private static final String TAG = PlaceDetailsFragment.class.getSimpleName();

    // VIEWS
    private TextView txtDetPlaceName;

    private TextView txtDetPlaceDesc;
    private TextView txtDetLocName;
    private Button btnDetFootRoute;


    // TODO (POSTPONED): LOAD IMAGE OF PLACE AND IMPLEMENT RATING FUNCTIONS

    private POI pointOfInterest;

    public PlaceDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectedPOI POI which the user has chosen to see details.
     * @return A new instance of fragment PlaceDetailsFragment.
     */
    public static PlaceDetailsFragment newInstance(POI selectedPOI) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POINT_OF_INTEREST, selectedPOI);
        fragment.setArguments(args);
        return fragment;
    }

    public static PlaceDetailsFragment newInstance(Element element) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POINT_OF_INTEREST, element);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pointOfInterest = getArguments().getParcelable(ARG_POINT_OF_INTEREST);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_place_details, container, false);

//        txtDetPlaceName = view.findViewById(R.id.txt_det_placename);
//        txtDetPlaceDesc = view.findViewById(R.id.txt_det_place_desc);
//        txtDetLocName = view.findViewById(R.id.txt_det_place_loc_name);
//        btnDetFootRoute = view.findViewById(R.id.btn_det_foot_route);
//
//        if(pointOfInterest.getShortName().equals(Constants.NO_SHORT_NAME))
//            txtDetPlaceName.setText(pointOfInterest.getName());
//        else
//            txtDetPlaceName.setText(pointOfInterest.getName() + " (" + pointOfInterest.getShortName()+ ")");
//
//        txtDetPlaceDesc.setText("");
//        txtDetLocName.setText(String.format("%s %s", getString(R.string.lbl_local_name), pointOfInterest.getLocalName()));
//
//        btnDetFootRoute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(SystemServicesManager.isNetworkEnabled(getContext())) {
//                    final MapFragment mapFragment = (MapFragment) getActivity().getSupportFragmentManager()
//                            .findFragmentByTag(MapFragment.FRAGMENT_TAG);
//                    mapFragment.findRouteToPlace(pointOfInterest);
//                }else{
//                    Toast.makeText(getContext(), R.string.error_no_internet_connection, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        return view;
    }



}
