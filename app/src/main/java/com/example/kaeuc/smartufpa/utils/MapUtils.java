package com.example.kaeuc.smartufpa.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.example.kaeuc.smartufpa.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by kaeuc on 09/08/2017.
 */

public class MapUtils {
    
    private Context parentContext;

    public MapUtils(Context parentContext) {
        this.parentContext = parentContext;
    }

    public Drawable getIconDrawable(final String iconTag){
        Drawable poiIcon = null;
        if (iconTag.equals(Constants.FILTER_XEROX))
            poiIcon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_xerox);
        else if (iconTag.equals(Constants.FILTER_RESTAURANT))
            poiIcon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_restaurant);
        else if (iconTag.equals(Constants.FILTER_RESTROOM))
            poiIcon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_restroom);
        else if (iconTag.equals(Constants.FILTER_LIBRARIES))
            poiIcon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_library);
        else if (iconTag.equals(Constants.FILTER_AUDITORIUMS))
            poiIcon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_auditorium);
        else if(iconTag.equals(Constants.DEFAULT_MARKER)){
            poiIcon = ContextCompat.getDrawable(parentContext,R.drawable.ic_marker);
        }
        
        return poiIcon;
    }

    public Marker createCustomMarker(MapView mapView, @Nullable Drawable poiIcon, GeoPoint location, @Nullable Marker.OnMarkerClickListener clickListener){

        Marker poiMarker = new Marker(mapView);
        poiMarker.setAnchor(0.5f,1);
        poiMarker.setPosition(location);
        if(poiIcon == null)
            poiIcon = this.getIconDrawable(Constants.DEFAULT_MARKER);
        poiMarker.setIcon(poiIcon);
        if(clickListener != null){
            poiMarker.setOnMarkerClickListener(clickListener);
        }
        return poiMarker;

    }


    public Marker createCustomMarker(MapView mapView,@Nullable Drawable poiIcon, GeoPoint location,
                                      String markerTitle, String markerDescription,
                                      @Nullable Marker.OnMarkerClickListener clickListener){

        Marker poiMarker = new Marker(mapView);
        poiMarker.setAnchor(0.5f,1);
        poiMarker.setPosition(location);
        if(poiIcon == null)
            poiIcon = this.getIconDrawable(Constants.DEFAULT_MARKER);
        poiMarker.setIcon(poiIcon);
        poiMarker.setSnippet(markerDescription);
        if(clickListener != null){
            poiMarker.setOnMarkerClickListener(clickListener);
        }

        return poiMarker;

    }

}
