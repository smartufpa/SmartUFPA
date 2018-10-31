package br.ufpa.smartufpa.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.SearchResultAdapter;
import br.ufpa.smartufpa.models.overpass.Element;

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

    private List<Element> pointsOfInterest;
    private RecyclerView rvSearchResult;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pointsOfInterest List of pointsOfInterest to be shown on the recycler view.
     * @return A new instance of fragment SearchResultFragment.
     */
    public static SearchResultFragment newInstance(List<Element> pointsOfInterest) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_POINTS_OF_INTEREST, new ArrayList<Parcelable>(pointsOfInterest));
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
        // Find RecyclerView
        rvSearchResult = view.findViewById(R.id.list_search_result);
        // Create the adapter to the RecyclerView
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(pointsOfInterest, getContext(), getResources());
        // Attach the adapter to the RecyclerView
        rvSearchResult.setAdapter(searchResultAdapter);
        // Create and attach a LayoutManager to the RecyclerView
        LinearLayoutManager llm =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearchResult.setLayoutManager(llm);

        return view;
    }


}
