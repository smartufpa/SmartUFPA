package br.ufpa.smartufpa.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.PlaceCategory;


/**
 * Created by kaeuchoa on 29/11/2017.
 */

public class SelectCategoryAdapter extends RecyclerView.Adapter {

    private Context parentContext;

    public static final String TAG = SelectCategoryAdapter.class.getSimpleName();

    private ArrayList<PlaceCategory> placeCategories;
    private SelectCategoryAdapter.OnItemClickListener onItemClickListener;

    public SelectCategoryAdapter(Context parentContext) {
        this.parentContext = parentContext;
        placeCategories = new ArrayList<>();

        PlaceCategory.Categories[] categories = PlaceCategory.Categories.values();

        for (PlaceCategory.Categories category : categories) {
            placeCategories.add(new PlaceCategory(category));
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parentContext)
                .inflate(R.layout.add_place_option_item,parent,false);

        return new SelectCategoryAdapter.AddPlaceOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AddPlaceOptionViewHolder viewHolder = (AddPlaceOptionViewHolder) holder;
        final PlaceCategory category = placeCategories.get(position);
        final String optionName = category.getName();
        final Drawable iconDrawable = ContextCompat.getDrawable(parentContext,category.getIconID());
        // Sets the title and icon for the card
        viewHolder.txtAddPlaceCategory.setText(optionName);
        viewHolder.imgAddPlaceCategory.setImageDrawable(iconDrawable);

    }


    @Override
    public int getItemCount() {
        return placeCategories.size();
    }

    /**
     * Interface to mimic the OnItemClick from list view
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(SelectCategoryAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
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
