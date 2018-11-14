package br.ufpa.smartufpa.asynctasks.interfaces;

import br.ufpa.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.views.overlay.Overlay;

/**
 * Stable Commit (20/09)
 * Interface to onRequestTokenResponse for Route queries.
 * Task is launched when user query the route between his/her current location until a place of chosen
 * on PlaceDetailsFragment.
 * @see br.ufpa.smartufpa.fragments.PlaceDetailsFragment
 * @author kaeuchoa
 */

public interface OnSearchRouteListener{
    /**
     * Called when a Route query is done on an AsyncTask.
     * @see br.ufpa.smartufpa.asynctasks.SearchRouteTask
     * @param overlay Layer to add to the MapView
     * @param taskStatus The current status for the task executed
     */
    void onSearchRouteResponse(Overlay overlay, ServerResponse taskStatus);
}
