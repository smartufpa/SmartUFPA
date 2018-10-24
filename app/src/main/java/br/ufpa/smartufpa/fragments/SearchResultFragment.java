package br.ufpa.smartufpa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.SearchResultAdapter;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.smartufpa.POI;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment that can show a list of pointsOfInterest that is return for a search query.
 * @author kaeuchoa
 */
public class SearchResultFragment extends Fragment {

    // TAG TO IDENTIFY THE FRAGMENT ON FINDFRAGMENTBYTAG CALLS
    public final static String FRAGMENT_TAG = SearchResultFragment.class.getName();

    // TAG FOR LOGGING PURPOSES
    private final static String TAG = SearchResultFragment.class.getSimpleName();

    // KEY TO IDENTIFY THE ARGUMENT CONTAINING A LIST OF PLACES
    private static final String ARG_POINTS_OF_INTEREST = "points_of_interest";

    private ArrayList<POI> pointsOfInterest;
    private RecyclerView rvSearchResult;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param POIS List of pointsOfInterest to be shown on the recycler view.
     * @return A new instance of fragment SearchResultFragment.
     */
    public static SearchResultFragment newInstance(ArrayList<Element> POIS) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_POINTS_OF_INTEREST, POIS);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pointsOfInterest = getArguments().getParcelableArrayList(ARG_POINTS_OF_INTEREST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

//        // Find RecyclerView
        rvSearchResult = view.findViewById(R.id.list_search_result);
//
//        // Create the adapter to the RecyclerView
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(pointsOfInterest, getContext());
//        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) { // On click, create a PlaceDetailFragment
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                final POI currentPOI = pointsOfInterest.get(position);
//
//                // Instantiate the fragment with the current place
//                PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(currentPOI);
//                // Start Transaction
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                // TODO (VISUAL ADJUSTMENTS): CREATE A TRANSITION
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .replace(R.id.bottom_sheet_container, placeDetailsFragment, PlaceDetailsFragment.FRAGMENT_TAG)
//                    .addToBackStack(SearchResultFragment.FRAGMENT_TAG)
//                    .commit();
//            }
//        });
//
//        // Attach the adapter to the RecyclerView
        rvSearchResult.setAdapter(searchResultAdapter);
//
//        // Create and attach a LayoutManager to the RecyclerView
        LinearLayoutManager llm =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearchResult.setLayoutManager(llm);

        return view;
    }


}
