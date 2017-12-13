package br.ufpa.smartufpa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.PlaceCategory;


/**
 * Created by kaeuchoa on 29/11/2017.
 */

public class AddPlaceOptionAdapter extends RecyclerView.Adapter {

    private Context parentContext;

    public static final String TAG = AddPlaceOptionAdapter.class.getSimpleName();

    private ArrayList<PlaceCategory> placeCategories;
    private AddPlaceOptionAdapter.OnItemClickListener onItemClickListener;

    public AddPlaceOptionAdapter(Context parentContext) {
        this.parentContext = parentContext;
        // List of categories defined on values>array.xml
        placeCategories = new ArrayList<>();
        final int [] categoriesIDs = parentContext.getResources().getIntArray(R.array.default_categories_ids);
        for (int categoryID : categoriesIDs) {
            placeCategories.add(new PlaceCategory(categoryID,parentContext));
        }
    }

    /**
     * Interface to mimic the OnItemClick from list view
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(AddPlaceOptionAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parentContext)
                .inflate(R.layout.add_place_option_item,parent,false);

        return new AddPlaceOptionAdapter.AddPlaceOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AddPlaceOptionViewHolder viewHolder = (AddPlaceOptionViewHolder) holder;

        // Sets the title for the card
        viewHolder.txtAddPlaceCategory.setText(placeCategories.get(position).getName());
        viewHolder.imgAddPlaceCategory.setImageDrawable(placeCategories.get(position).getIcon());


    }


    @Override
    public int getItemCount() {
        return placeCategories.size();
    }

    public ArrayList<PlaceCategory> getPlaceCategories() {
        return placeCategories;
    }



    private class AddPlaceOptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtAddPlaceCategory;
        private ImageView imgAddPlaceCategory;


        private AddPlaceOptionViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtAddPlaceCategory = itemView.findViewById(R.id.txt_add_place_category);
            imgAddPlaceCategory = itemView.findViewById(R.id.img_add_place_category);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null)
                onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
