package com.example.kaeuc.smartufpa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaeuc.smartufpa.R;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PLACES = "PLACES_LIST";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Place> places;
    private String mParam2;


    private RecyclerView rvSearchResult;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param places Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(ArrayList<Place> places, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PLACES, places);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            places = getArguments().getParcelableArrayList(ARG_PLACES);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        rvSearchResult = (RecyclerView) view.findViewById(R.id.list_search_result);
        rvSearchResult.setAdapter(new SearchResultAdapter(places,getContext()));
        LinearLayoutManager llm =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearchResult.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvSearchResult.getContext(),
                ((LinearLayoutManager) rvSearchResult.getLayoutManager()).getOrientation());
        rvSearchResult.addItemDecoration(dividerItemDecoration);
        return view;
    }

}
