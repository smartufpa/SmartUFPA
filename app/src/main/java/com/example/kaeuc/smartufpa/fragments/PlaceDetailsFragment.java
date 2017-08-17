package com.example.kaeuc.smartufpa.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceDetailsFragment extends Fragment {


    public static final String FRAGMENT_TAG = PlaceDetailsFragment.class.getName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CURRENT_PLACE = "current_place";

    // VIEWS
    private TextView txtDetPlaceName;
    private TextView txtDetPlaceDesc;
    private TextView txtDetLocName;
    private Button btnDetFootRoute;

    // TODO: CARREGAR IMAGEM


    private Place currentPlace;

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
    public static PlaceDetailsFragment newInstance(Parcelable currentPlace) {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_PLACE, currentPlace);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPlace = getArguments().getParcelable(ARG_CURRENT_PLACE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_place_details, container, false);

        txtDetPlaceName = (TextView) view.findViewById(R.id.txt_det_placename);
        txtDetPlaceDesc = (TextView) view.findViewById(R.id.txt_det_place_desc);
        txtDetLocName = (TextView) view.findViewById(R.id.txt_det_place_loc_name);
        btnDetFootRoute = (Button) view.findViewById(R.id.btn_det_foot_route);

        if(currentPlace.getShortName().equals(Constants.NO_SHORT_NAME))
            txtDetPlaceName.setText(currentPlace.getName());
        else
            txtDetPlaceName.setText(currentPlace.getName() + " (" + currentPlace.getShortName()+ ")");
        txtDetPlaceDesc.setText(currentPlace.getDescription());
        txtDetLocName.setText("Nome local: " + currentPlace.getLocName());

        // TODO: COLOCAR UM MARCADOR NA POSIÇÃO DO LOCAL ESCOLHIDO
        btnDetFootRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Função de calcular rota", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
