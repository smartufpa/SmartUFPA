package com.example.kaeuc.smartufpa;

import org.osmdroid.views.overlay.Overlay;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kaeuc on 29/08/2017.
 */

public class MapBundle implements Serializable {

    private List<Overlay> overlaysList;

    public List<Overlay> getOverlaysList() {
        return overlaysList;
    }

    public MapBundle(){

    }

    public boolean putOverlay(){
        return false;
    }


}
