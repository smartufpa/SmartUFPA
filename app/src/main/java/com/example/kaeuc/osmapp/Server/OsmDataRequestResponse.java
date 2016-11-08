package com.example.kaeuc.osmapp.Server;

import com.example.kaeuc.osmapp.Extras.Local;

import java.util.List;

/**
 * Created by kaeuc on 10/22/2016.
 */

public interface OsmDataRequestResponse {
    void osmTaskCompleted(List<Local> locais, String filtro);
}
