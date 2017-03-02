package com.example.kaeuc.smartufpa.server;

import com.example.kaeuc.smartufpa.models.Place;

import java.util.ArrayList;

/**
 * Created by kaeuc on 02/03/2017.
 */

public interface OverpassSearchResponse {
    void onOverpassTaskResponse(ArrayList<Place> places, int taskStatus);
}
