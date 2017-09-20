package br.ufpa.smartufpa.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.MarkerStatuses;

/**
 * Stable Commit (20/09)
 * Auxiliary functions for map
 * @author kaeuchoa
 */



public class MapUtils {
    
    private Context parentContext;

    public MapUtils(Context parentContext) {
        this.parentContext = parentContext;
    }

    public HashMap<MarkerStatuses,Drawable> getMarkerDrawable(final MarkerTypes markerType){

        final HashMap<MarkerStatuses, Drawable> markerIcons = new HashMap<>(2);


        // TODO (VISUAL ADJUSTMENTS): CHANGE ICONS FOR CLICKED
        if (markerType.equals(MarkerTypes.XEROX)) {

            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_xerox));
            markerIcons.put(MarkerStatuses.CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.FOOD)){

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

        } else if(markerType.equals(MarkerTypes.BUS_STOP)){
            markerIcons.put(MarkerStatuses.NOT_CLICKED, ContextCompat
                    .getDrawable(parentContext, R.drawable.ic_bus_stop));
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


    private class NullMarkerDrawableException extends RuntimeException{
        private NullMarkerDrawableException(String message) { super(message); }
    }




}
