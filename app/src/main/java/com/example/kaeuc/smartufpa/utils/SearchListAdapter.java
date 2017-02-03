package com.example.kaeuc.smartufpa.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.models.Place;

import java.util.ArrayList;

/**
 * Created by kaeuc on 11/14/2016.
 * ArrayAdapter para ser utilizado na lista de resultados da Busca
 * TODO 1. ViewHolder (Melhora a performance); 2. Setar a imagem para cada local
 */

public class SearchListAdapter extends ArrayAdapter<Place>{

    private ArrayList<Place> places;
    private Context context;


    public SearchListAdapter(Context context,ArrayList<Place> places) {
        super(context, 0, places);
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){
             view = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
        }

        TextView placeName = (TextView) view.findViewById(R.id.list_txt_place_name);
        TextView placeDetails = (TextView) view.findViewById(R.id.list_txt_place_description);

        Place place = places.get(position);

        placeName.setText(place.getName());
        placeDetails.setText(place.getDescription());


        return view;

    }
}
