package br.ufpa.smartufpa.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.AddBuildingActivity;
import br.ufpa.smartufpa.activities.AddCopyshopActivity;
import br.ufpa.smartufpa.activities.AddFacultyActivity;
import br.ufpa.smartufpa.activities.AddFoodPlaceActivity;
import br.ufpa.smartufpa.activities.AddLaboratoryActivity;
import br.ufpa.smartufpa.activities.AddLibraryActivity;
import br.ufpa.smartufpa.activities.AddMiscActivity;
import br.ufpa.smartufpa.adapters.AddPlaceOptionAdapter;
import br.ufpa.smartufpa.models.PlaceCategory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectCategoryFragment.OnAddPlaceListener} interface
 * to handle interaction events.
 * Use the {@link SelectCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class SelectCategoryFragment extends Fragment {
    //Tags de identificação do fragmento
    public static final String FRAGMENT_TAG = SelectCategoryFragment.class.getName();
    private static final String TAG = SelectCategoryFragment.class.getSimpleName();
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";


    private RecyclerView rvOptions;
    private Toolbar tbAddPlace;

    private OnAddPlaceListener mListener;
    private double latitude;
    private double longitude;

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * @param latitude  Latitude do ponto sinalizado pelo marcador.
     * @param longitude Longitude do ponto sinalizado pelo marcador.
     * @return Uma nova instância de SelectCategoryFragment.
     */

    public static SelectCategoryFragment newInstance(double latitude, double longitude) {
        final SelectCategoryFragment selectCategoryFragment = new SelectCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(ARG_LATITUDE, latitude);
        bundle.putDouble(ARG_LONGITUDE, longitude);
        selectCategoryFragment.setArguments(bundle);
        return selectCategoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Infla a o layout
        final View view = inflater.inflate(R.layout.fragment_select_category, container, false);

        // Encontra todas as Views do layout

        rvOptions = view.findViewById(R.id.list_add_place_options);
        tbAddPlace = getActivity().findViewById(R.id.tb_add_place);

        // Create the adapter to the RecyclerView
        final AddPlaceOptionAdapter addPlaceOptionAdapter = new AddPlaceOptionAdapter(getActivity().getApplicationContext());


        addPlaceOptionAdapter.setOnItemClickListener(new AddPlaceOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ArrayList<PlaceCategory> placeCategories = addPlaceOptionAdapter.getPlaceCategories();
                final PlaceCategory category = placeCategories.get(position);
                Intent intent = null;
                Context context = view.getContext();
                switch (position){
                    case 0: // "Prédio"
                        intent  = new Intent(context, AddBuildingActivity.class);
                        break;
                    case 1: //  "Banheiro"
                        break;
                    case 2: //"Biblioteca"
                        intent = new Intent(context, AddLibraryActivity.class);
                        break;
                    case 3: // "Refeições"
                        intent = new Intent(context, AddFoodPlaceActivity.class);
                        break;
                    case 4: // "Xerox"
                        intent = new Intent(context, AddCopyshopActivity.class);
                        break;
                    case 5: // "Faculdade"
                        intent = new Intent(context, AddFacultyActivity.class);
                        break;
                    case 6: // "Laboratório"
                        intent = new Intent(context, AddLaboratoryActivity.class);
                        break;
                    default: // "Outro"
                        intent = new Intent(context, AddMiscActivity.class);
                        break;
                }
                if(intent != null){
                    intent.putExtra(ARG_LATITUDE,latitude);
                    intent.putExtra(ARG_LONGITUDE,longitude);
                    startActivity(intent);
                }

            }
        });

        // Attach the adapter to the RecyclerView
        rvOptions.setAdapter(addPlaceOptionAdapter);

        // Create and attach a LayoutManager to the RecyclerView
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvOptions.setLayoutManager(llm);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tbAddPlace != null)
            tbAddPlace.setSubtitle(R.string.add_place_subtitle_categories);
    }

    //Implementar caso necessite de comunicação com a activity
    public interface OnAddPlaceListener {
        void onAddPlaceResponse(int taskStatus);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(FRAGMENT_TAG, "onDestroyView()");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(FRAGMENT_TAG, "onDetach()");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(FRAGMENT_TAG, "onSaveInstanceState()");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(FRAGMENT_TAG, "onStart()");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(FRAGMENT_TAG, "onStop()");
    }
}
