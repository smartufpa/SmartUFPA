package com.example.kaeuc.smartufpa.interfaces;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import java.util.ArrayList;

/**
 * Created by kaeuc on 29/08/2017.
 */

public interface OnSearchQueryListener {
    void onSearchQueryResponse(final ArrayList<Place> PLACES, final ServerResponse TASK_STATUS);
}
