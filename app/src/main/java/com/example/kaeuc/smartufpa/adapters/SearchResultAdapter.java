package com.example.kaeuc.smartufpa.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.Constants;

import java.util.List;

/**
 * Created by kaeuc on 11/14/2016.
 * ArrayAdapter used for listing the results on searches with multiple POIs
 */

public class SearchResultAdapter extends RecyclerView.Adapter{

    private List<Place> places;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SearchResultAdapter(List<Place> places, Context context) {
        this.places = places;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.search_result_item,parent,false);

        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SearchResultViewHolder viewHolder = (SearchResultViewHolder) holder;

        Place place = places.get(position);
        if(!place.getShortName().equals(Constants.NO_SHORT_NAME))
            viewHolder.txtPlaceName.setText(place.getName() + " (" + place.getShortName() + ")");
        else
            viewHolder.txtPlaceName.setText(place.getName());

        if(!place.getLocName().equals(Constants.NO_LOCAL_NAME))
            viewHolder.txtLocName.setText(place.getLocName());
        else
            viewHolder.txtLocName.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return places.size();
    }


   private class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtPlaceName, txtLocName;


        private SearchResultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtPlaceName = itemView.findViewById(R.id.list_txt_place_name);
            txtLocName = itemView.findViewById(R.id.list_txt_place_loc_name);

        }

       @Override
       public void onClick(View v) {
           if(onItemClickListener != null)
               onItemClickListener.onItemClick(v, getAdapterPosition());
       }
   }
}
