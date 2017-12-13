package br.ufpa.smartufpa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.AddPlaceOptionAdapter;
import br.ufpa.smartufpa.models.PlaceCategory;

/**
 * Stable Commit (20/09)
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectCategoryFragment.OnAddPlaceListener} interface
 * to handle interaction events.
 * Use the {@link SelectCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */




public class SelectCategoryFragment extends Fragment {

    // Rótulos para os argumentos passados na instanciação do fragmento
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";

    //Tags de identificação do fragmento
    public static final String FRAGMENT_TAG = SelectCategoryFragment.class.getName();
    private static final String TAG = SelectCategoryFragment.class.getSimpleName();

    private double latitude;
    private double longitude;

    private RecyclerView rvOptions;

    private OnAddPlaceListener mListener;

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latitude Latitude do ponto sinalizado pelo marcador.
     * @param longitude Longitude do ponto sinalizado pelo marcador.
     * @return Uma nova instância de SelectCategoryFragment.
     */

    public static SelectCategoryFragment newInstance(double latitude, double longitude) {
        SelectCategoryFragment fragment = new SelectCategoryFragment();
        Bundle args = new Bundle();

        //Armazena os dados passados na instanciação em um bundle e guarda no fragmento
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recupera os dados do bundle
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


        // Create the adapter to the RecyclerView
        final AddPlaceOptionAdapter addPlaceOptionAdapter = new AddPlaceOptionAdapter(getContext());
        addPlaceOptionAdapter.setOnItemClickListener(new AddPlaceOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ArrayList<PlaceCategory> placeCategories = addPlaceOptionAdapter.getPlaceCategories();
                Toast.makeText(getContext(), placeCategories.get(position).getName()+ "\n " + latitude + "\n " + longitude, Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().findFragmentById()
            }
        });

        // Attach the adapter to the RecyclerView
        rvOptions.setAdapter(addPlaceOptionAdapter);

        // Create and attach a LayoutManager to the RecyclerView
        RecyclerView.LayoutManager llm = new GridLayoutManager(getContext(), 2);
        rvOptions.setLayoutManager(llm);

        //

        // Inflate the layout for this fragment
        return view;
    }


    //Implementar caso necessite de comunicação com a activity
    public interface OnAddPlaceListener {
        void onAddPlaceResponse(int taskStatus);
    }




    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.i(TAG, "onDestroyView()");
    }


    @Override
    public void onDetach(){
        super.onDetach();
        Log.i(TAG, "onDetach()");
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "onStart()");
    }


    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG, "onStop()");
    }
}
