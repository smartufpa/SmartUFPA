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
import br.ufpa.smartufpa.models.ElementCategoryItem;
import br.ufpa.smartufpa.utils.enums.ElementCategories;


/**
 * Created by kaeuchoa on 29/11/2017.
 */

public class ElementCategoryAdapter extends RecyclerView.Adapter {

    private Context parentContext;

    public static final String TAG = ElementCategoryAdapter.class.getSimpleName();

    private ArrayList<ElementCategoryItem> categoriesList;
    private ElementCategoryAdapter.OnItemClickListener onItemClickListener;

    public ElementCategoryAdapter(Context context) {
        this.parentContext = context;
        categoriesList = new ArrayList<>();

        for (ElementCategories category :  ElementCategories.values()) {
            categoriesList.add(new ElementCategoryItem(category));
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parentContext)
                .inflate(R.layout.item_select_category,parent,false);

        return new ElementCategoryAdapter.AddPlaceOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AddPlaceOptionViewHolder viewHolder = (AddPlaceOptionViewHolder) holder;
        final ElementCategoryItem category = categoriesList.get(position);
        final String optionName = category.getName();
        final Drawable iconDrawable = ContextCompat.getDrawable(parentContext,category.getDrawable());
        // Sets the title and icon for the card
        viewHolder.txtAddPlaceCategory.setText(optionName);
        viewHolder.imgAddPlaceCategory.setImageDrawable(iconDrawable);

    }


    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    /**
     * Interface to mimic the OnItemClick from list view
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ElementCategoryAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public ArrayList<ElementCategoryItem> getCategoriesList() {
        return categoriesList;
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
