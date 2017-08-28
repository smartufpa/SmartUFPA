package com.example.kaeuc.smartufpa.customviews;

import android.content.Context;
import android.util.Log;

import com.example.kaeuc.smartufpa.utils.enums.OverlayTags;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaeuc on 07/08/2017.
 */

public class CustomMapView extends MapView {

    private List<OverlayTags> layersTags;
    private final static String TAG = CustomMapView.class.getSimpleName();


    public CustomMapView(Context context) {
        super(context);
        layersTags = new ArrayList<>();
    }


    public void addTileOverlay(Overlay newOverlay, final OverlayTags layerTag){
        layersTags.add(layerTag);
        final int overlayIndex = layersTags.indexOf(layerTag);
        this.getOverlays().add(overlayIndex, newOverlay);
        this.postInvalidate();
        Log.i(TAG, "Overlay added: " + layerTag);
        Log.i(TAG, "Current Overlays: " + this.getLayersTagsNames());
    }

    public void removeTileOverlay(final OverlayTags layerTag){
        final int overlayIndex = layersTags.indexOf(layerTag);
        layersTags.remove(overlayIndex);
        this.getOverlays().remove(overlayIndex);
        this.postInvalidate();
        Log.i(TAG,"Overlay removed: " + layerTag);
    }

    public void clearMap(){
        int size = layersTags.size()- 1;
        for (int i = size; i > 0 ; i--) {
            Overlay currentOverlay = this.getOverlays().get(i);
            if(!(currentOverlay instanceof MyLocationNewOverlay))
                this.removeTileOverlay(layersTags.get(i));
        }
        Log.i(TAG, "Map Cleared.");
        Log.i(TAG, "Current Overlays: " + this.getLayersTagsNames());

    }

    public String getLayersTagsNames() {
        return layersTags.toString();
    }


    public boolean containsOverlay(final OverlayTags layerTag){
        return layersTags.contains(layerTag);
    }


}
