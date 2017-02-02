package com.example.kaeuc.smartufpa.server;

import com.example.kaeuc.smartufpa.classes.Place;

import java.util.List;

/**
 * Created by kaeuc on 10/22/2016.
 */

public interface OsmDataRequestResponse {
    void osmTaskCompleted(List<Place> locais, String filtro);
}
