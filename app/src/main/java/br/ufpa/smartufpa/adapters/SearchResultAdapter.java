package br.ufpa.smartufpa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.models.Place;

import java.util.List;

/**
 * ArrayAdapter used for listing the results on searches with multiple POIs
 * @author kaeuchoa
 *
 */

public class SearchResultAdapter extends RecyclerView.Adapter{

    private List<Place> places;
    private Context parentContext;
    private OnItemClickListener onItemClickListener;

    public SearchResultAdapter(List<Place> places, Context parentContext) {
        this.places = places;
        this.parentContext = parentContext;
    }

    /**
     * Interface to mimic the OnItemClick from list view
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parentContext)
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
