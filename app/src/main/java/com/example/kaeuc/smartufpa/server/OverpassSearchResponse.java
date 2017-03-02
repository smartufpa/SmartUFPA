package com.example.kaeuc.smartufpa.server;

import com.example.kaeuc.smartufpa.models.Place;

import java.util.List;

/**
 * Created by kaeuc on 02/03/2017.
 */

public interface OverpassSearchResponse {
    public void onOverpassTaskResponse(List<Place> places, int taskStatus);
}
