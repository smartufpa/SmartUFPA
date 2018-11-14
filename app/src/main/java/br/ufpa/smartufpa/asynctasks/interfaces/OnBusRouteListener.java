package br.ufpa.smartufpa.asynctasks.interfaces;

import br.ufpa.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.views.overlay.Overlay;

/**
 * Stable Commit (20/09)
 * Interface to onRequestTokenResponse for Bus Route query.
 * Task is launched by the item "Rota do Circular" (pt-br) on the left drawer on Main Activity.
 * @author kaeuchoa
 */

public interface OnBusRouteListener {
    /**
     * Called when a Bus Route query is done on an AsyncTask.
     * @see br.ufpa.smartufpa.asynctasks.BusRouteTask
     * @param overlay Layer to add to the MapView
     * @param taskStatus The current status for the task executed
     */
    void onBusRouteResponse(Overlay overlay, ServerResponse taskStatus);
}
