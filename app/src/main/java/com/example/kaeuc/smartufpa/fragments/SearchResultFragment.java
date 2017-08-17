package com.example.kaeuc.smartufpa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.activities.MainActivity;
import com.example.kaeuc.smartufpa.adapters.SearchResultAdapter;
import com.example.kaeuc.smartufpa.models.Place;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {


    public final static String FRAGMENT_TAG = SearchResultFragment.class.getName();
    private final static String TAG = SearchResultFragment.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PLACES = "PLACES_LIST";

    private ArrayList<Place> places;

    private RecyclerView rvSearchResult;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param places List of places to be shown on the recycler view.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(ArrayList<Place> places) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PLACES, places);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            places = getArguments().getParcelableArrayList(ARG_PLACES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        // Find RecyclerView
        rvSearchResult = (RecyclerView) view.findViewById(R.id.list_search_result);

        // Create the adapter to the RecyclerView
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(places, getContext());
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Place currentPlace = places.get(position);
                // On click, create a PlaceDetailFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // Instantiate the fragment with the current place
                PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(currentPlace);
                // Start Transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();
                // TODO: CREATE A TRANSTION
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.bottom_sheet_container, placeDetailsFragment, PlaceDetailsFragment.FRAGMENT_TAG);
                ft.addToBackStack(SearchResultFragment.FRAGMENT_TAG);
                ft.commit();
            }
        });

        // Attach the adapter to the RecyclerView
        rvSearchResult.setAdapter(searchResultAdapter);

        // Create and attach a LayoutManager to the RecyclerView
        LinearLayoutManager llm =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearchResult.setLayoutManager(llm);



        return view;
    }
}
