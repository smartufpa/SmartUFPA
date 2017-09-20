package br.ufpa.smartufpa.asynctasks.interfaces;

import br.ufpa.smartufpa.asynctasks.SearchQueryTask;
import br.ufpa.smartufpa.utils.enums.ServerResponse;
import br.ufpa.smartufpa.models.Place;

import java.util.ArrayList;

/**
 * Stable Commit (20/09)
 * Interface to listen for query searching.
 * Task is launched by the search widget on the Main Activity
 * @author kaeuchoa
 */
public interface OnSearchQueryListener {

    /**
     * Called when a Query Search is done on an AsyncTask.
     * @see SearchQueryTask

     * @param places List of places found
     * @param taskStatus The current status for the task executed
     *
     */
    void onSearchQueryResponse(ArrayList<Place> places, ServerResponse taskStatus);
}
