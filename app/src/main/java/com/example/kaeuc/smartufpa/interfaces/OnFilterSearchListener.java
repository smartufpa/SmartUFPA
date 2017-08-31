package com.example.kaeuc.smartufpa.interfaces;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.utils.enums.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.enums.OverlayTags;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;

import java.util.ArrayList;

/**
 * Created by kaeuc on 29/08/2017.
 */

public interface OnFilterSearchListener {
    void onFilterSearchResponse(final ArrayList<Place> places, MarkerTypes markersType, OverlayTags overlayTag, ServerResponse taskStatus);
}