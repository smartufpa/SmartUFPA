package br.ufpa.smartufpa.customviews;

import android.content.Context;
import android.util.Log;

import br.ufpa.smartufpa.utils.enums.OverlayTags;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Stable Commit (20/09)
 * Custom MapView to adapt methods to the app necessity.
 * @author kaeuchoa
 */

public class CustomMapView extends MapView {

    // Contains tags for all layers added to the map.
    private List<OverlayTags> layersTags;
    private final static String TAG = CustomMapView.class.getSimpleName();


    public CustomMapView(Context context) {
        super(context);
        layersTags = new ArrayList<>();
    }

    /**
     * Adds a new Overlay to the MapView and stores its tag for easy remove operations.
     * @param newOverlay Overlay to be added
     * @param layerTag Layer tag that serves as an id
     */
    public void addOverlay(final Overlay newOverlay, final OverlayTags layerTag){
        layersTags.add(layerTag);
        final int overlayIndex = layersTags.indexOf(layerTag);
        this.getOverlays().add(overlayIndex, newOverlay);
        this.postInvalidate();
        Log.i(TAG, "Overlay added: " + layerTag);
        Log.i(TAG, "Current Overlays: " + this.getLayersTagsNames());
    }

    /**
     * Removes an specif layer from the MapView.
     * @param layerTag Tag of the layer to be removed
     */
    public void removeOverlay(final OverlayTags layerTag){
        final int overlayIndex = layersTags.indexOf(layerTag);
        layersTags.remove(overlayIndex);
        this.getOverlays().remove(overlayIndex);
        this.postInvalidate();
        Log.i(TAG,"Overlay removed: " + layerTag);
    }

    /**
     * Removes all overlays from the MapView except MyLocationOverlay instances.
     */
    public void removeAllOverlays(){
        int size = layersTags.size()- 1;
        for (int i = size; i > 0 ; i--) {
            Overlay currentOverlay = this.getOverlays().get(i);
            if(!(currentOverlay instanceof MyLocationNewOverlay))
                this.removeOverlay(layersTags.get(i));
        }
        Log.i(TAG, "Map Cleared.");
        Log.i(TAG, "Current Overlays: " + this.getLayersTagsNames());

    }

    /**
     *
     * @return a String containg all the layers currently on the MapView.
     */
    public String getLayersTagsNames() {
        return layersTags.toString();
    }

    /**
     * Checks if the MapView contains or not an specific layer.
     * @param layerTag Tag of the layer to Query
     * @return true if contain
     *         false if don't contain
     */
    public boolean containsOverlay(final OverlayTags layerTag){
        return layersTags.contains(layerTag);
    }


}
