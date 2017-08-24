package com.example.kaeuc.smartufpa.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.example.kaeuc.smartufpa.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by kaeuc on 09/08/2017.
 */

import com.example.kaeuc.smartufpa.utils.Constants.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.Constants.MarkerStatuses;

// TODO: MAKE THIS CLASS A SINGLETON
public class MapUtils {
    
    private Context parentContext;

    public MapUtils(Context parentContext) {
        this.parentContext = parentContext;
    }

    public HashMap<MarkerStatuses,Drawable> getMarkerDrawable(final MarkerTypes markerType){

        final HashMap<MarkerStatuses, Drawable> markerIcons = new HashMap<>(2);


        // TODO: CHANGE ICONS FOR CLICKED
        if (markerType.equals(MarkerTypes.XEROX)) {

            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_xerox));
            markerIcons.put(MarkerStatuses.CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.RESTAURANT)){

            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_restaurant));
            markerIcons.put(MarkerStatuses.CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.RESTROOM)) {

            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_restroom));
            markerIcons.put(MarkerStatuses.CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.AUDITORIUM)) {

            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_auditorium));
            markerIcons.put(MarkerStatuses.CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.LIBRARY)){

            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_library));
            markerIcons.put(MarkerStatuses.CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_details));

        }else if(markerType.equals(MarkerTypes.DEFAULT)){

            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker));
            markerIcons.put(MarkerStatuses.CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_details));

        }
        
        return markerIcons;
    }

    public Marker createCustomMarker(MapView mapView, Drawable markerIcon, GeoPoint location, @Nullable Marker.OnMarkerClickListener clickListener){

        Marker poiMarker = new Marker(mapView);
        poiMarker.setAnchor(0.5f,1);
        poiMarker.setPosition(location);
        if(markerIcon == null){
            throw new NullMarkerDrawableException("You must choose a drawable asset for your Marker");
        }

        poiMarker.setIcon(markerIcon);

        if(clickListener != null){
            poiMarker.setOnMarkerClickListener(clickListener);
        }
        return poiMarker;

    }

    public String getBusRouteURL(){
        StringBuilder s = new StringBuilder();
        s.append("http://overpass-api.de/api/interpreter?data=");

        String data ="[out:json][timeout:30];(node[route=bus][name=\"circular\"](-1.479967,-48.459779,-1.457886,-48.437957);"
                + "way[route=bus][name=\"circular\"](-1.479967,-48.459779,-1.457886,-48.437957););" +
                "out qt geom tags 500;relation[route=bus][name=\"circular\"](-1.479967,-48.459779,-1.457886,-48.437957);" +
                "out qt geom body 500;";

        try {
            s.append(URLEncoder.encode(data,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s.toString();
    }


    private class NullMarkerDrawableException extends RuntimeException{
        public NullMarkerDrawableException(String message) { super(message); }
    }




}
