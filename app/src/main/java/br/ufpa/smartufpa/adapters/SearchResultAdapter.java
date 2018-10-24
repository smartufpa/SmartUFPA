package br.ufpa.smartufpa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.models.smartufpa.POI;

import java.util.List;

/**
 * * ArrayAdapter used for listing the results on searches with multiple POIs
 * @author kaeuchoa
 *
 */

public class SearchResultAdapter extends RecyclerView.Adapter{

    private List<POI> POIS;
    private Context parentContext;
    private OnItemClickListener onItemClickListener;

    public SearchResultAdapter(List<POI> POIS, Context parentContext) {
        this.POIS = POIS;
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
                .inflate(R.layout.item_search_result,parent,false);

        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SearchResultViewHolder viewHolder = (SearchResultViewHolder) holder;

        POI POI = POIS.get(position);
//        if(!POI.getShortName().equals(Constants.NO_SHORT_NAME))
//            viewHolder.txtPlaceName.setText(POI.getName() + " (" + POI.getShortName() + ")");
//        else
//            viewHolder.txtPlaceName.setText(POI.getName());
//
//        if(!POI.getLocalName().equals(Constants.NO_LOCAL_NAME))
//            viewHolder.txtLocName.setText(POI.getLocalName());
//        else
//            viewHolder.txtLocName.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return POIS.size();
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
