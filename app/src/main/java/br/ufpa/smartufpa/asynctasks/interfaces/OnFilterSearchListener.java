package br.ufpa.smartufpa.asynctasks.interfaces;

import br.ufpa.smartufpa.models.smartufpa.POI;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.OverlayTags;
import br.ufpa.smartufpa.utils.enums.ServerResponse;

import java.util.ArrayList;

/**
 * Stable Commit (20/09)
 * Interface to listen for filter searching.
 * Task is launched by the items on the left drawer on Main Activity
 * @author kaeuchoa
 */

public interface OnFilterSearchListener {
    /**
     * Called when a Filter Search is done on an AsyncTask.
     * @see br.ufpa.smartufpa.asynctasks.FilterSearchTask
     * @param POIS List of POIS found
     * @param markersType Type of Markers to include on Map (defines the drawable to be used)
     * @param overlayTag Tag to identify the layer on the MapView
     * @param taskStatus The current status for the task executed
     */
    void onFilterSearchResponse(ArrayList<POI> POIS, MarkerTypes markersType, OverlayTags overlayTag, ServerResponse taskStatus);
}