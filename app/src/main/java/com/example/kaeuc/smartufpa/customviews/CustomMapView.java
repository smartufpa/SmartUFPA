package com.example.kaeuc.smartufpa.customviews;

import android.content.Context;
import android.util.Log;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaeuc on 07/08/2017.
 */

public class CustomMapView extends MapView {

    private List<String> layersTags;
    private final static String LOG_TAG = CustomMapView.class.getSimpleName();


    public CustomMapView(Context context) {
        super(context);
        layersTags = new ArrayList<>();
    }


    public void addTileOverlay(Overlay newOverlay, final String layerTag){
        layersTags.add(layerTag);
        final int overlayIndex = layersTags.indexOf(layerTag);
        this.getOverlays().add(overlayIndex, newOverlay);
        Log.i(LOG_TAG,"Overlay added: " + layerTag);
        this.postInvalidate();
    }

    public void removeTileOverlay(final String layerTag){
        final int overlayIndex = layersTags.indexOf(layerTag);
        layersTags.remove(overlayIndex);
        this.getOverlays().remove(overlayIndex);
        Log.i(LOG_TAG,"Overlay removed: " + layerTag);
        this.postInvalidate();
    }

    public void clearMap(){
        int size = layersTags.size()- 1;
        for (int i = size; i > 0 ; i--) {
            Overlay currentOverlay = this.getOverlays().get(i);
            if(!(currentOverlay instanceof MyLocationNewOverlay))
                this.removeTileOverlay(layersTags.get(i));
        }
        Log.i(LOG_TAG, "Map Cleared.");
        Log.i(LOG_TAG, "Current Overlays: " + this.getLayersTagsNames());

    }

    public String getLayersTagsNames() {
        return layersTags.toString();
    }


    public boolean containsOverlay(final String layerTag){
        return layersTags.contains(layerTag);
    }


}
