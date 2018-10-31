package br.ufpa.smartufpa.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.interfaces.PlaceDetailsDelegate;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.utils.ElementParser;

import java.util.List;

/**
 * ArrayAdapter used for listing the results on searches with multiple POIs
 *
 * @author kaeuchoa
 */

public class SearchResultAdapter extends RecyclerView.Adapter {

    private List<Element> pointsOfInterest;
    private Context parentContext;
    private ElementParser elementParser;
    private Resources resources;
    private PlaceDetailsDelegate placeDetailsDelegate;

    public SearchResultAdapter(List<Element> pointsOfInterest, Context parentContext, Resources resources) {
        this.pointsOfInterest = pointsOfInterest;
        this.parentContext = parentContext;
        this.placeDetailsDelegate = (PlaceDetailsDelegate) parentContext;
        this.resources = resources;
        this.elementParser = ElementParser.INSTANCE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parentContext)
                .inflate(R.layout.item_search_result, parent, false);

        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchResultViewHolder viewHolder = (SearchResultViewHolder) holder;
        final Element element = pointsOfInterest.get(position);
        final String name = elementParser.getName(element);
        final String localName = elementParser.getLocalName(element);
        initPlaceName(viewHolder, name);
        initLocalName(viewHolder, localName);
        initInfoBtn(viewHolder, element);
    }

    private void initInfoBtn(SearchResultViewHolder viewHolder, final Element element) {
        viewHolder.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeDetailsDelegate.showPlaceDetailsFragment(element);
            }
        });
    }

    private void initPlaceName(SearchResultViewHolder viewHolder, String name) {
        viewHolder.txtPlaceName.setTextColor(resources.getColor(android.R.color.black));
        if (name != null)
            viewHolder.txtPlaceName.setText(name);
        else
            viewHolder.txtPlaceName.setTextColor(resources.getColor(android.R.color.darker_gray));
    }

    private void initLocalName(SearchResultViewHolder viewHolder, String localName) {
        if (localName != null){
            viewHolder.txtLocName.setVisibility(View.VISIBLE);
            viewHolder.txtLocName.setText(localName);
        }
        else
            viewHolder.txtLocName.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return pointsOfInterest.size();
    }


    private class SearchResultViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPlaceName, txtLocName;
        private ImageView ivInfo;


        private SearchResultViewHolder(View itemView) {
            super(itemView);
            txtPlaceName = itemView.findViewById(R.id.list_txt_place_name);
            txtLocName = itemView.findViewById(R.id.list_txt_place_loc_name);
            ivInfo = itemView.findViewById(R.id.ivInfo);

        }


    }
}
