package br.ufpa.smartufpa.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.smartufpa.POI;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.MarkerStatus;

/**
 * Auxiliary functions for map
 * @author kaeuchoa
 */



public class MapUtils {
    
    private Context context;
    public static final String TAG = MapUtils.class.getSimpleName();

    public MapUtils(Context parentContext) {
        this.context = parentContext;
    }

    public HashMap<MarkerStatus,Drawable> getMarkerDrawable(final MarkerTypes markerType){

        final HashMap<MarkerStatus, Drawable> markerIcons = new HashMap<>(2);


        // TODO (VISUAL ADJUSTMENTS): CHANGE ICONS FOR CLICKED
        if (markerType.equals(MarkerTypes.XEROX)) {

            markerIcons.put(MarkerStatus.NOT_CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_copyshop));
            markerIcons.put(MarkerStatus.CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.FOOD)){

            markerIcons.put(MarkerStatus.NOT_CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_food_place));
            markerIcons.put(MarkerStatus.CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.RESTROOM)) {

            markerIcons.put(MarkerStatus.NOT_CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_restroom));
            markerIcons.put(MarkerStatus.CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.AUDITORIUM)) {

            markerIcons.put(MarkerStatus.NOT_CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_auditorium));
            markerIcons.put(MarkerStatus.CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_details));

        }else if (markerType.equals(MarkerTypes.LIBRARY)){

            markerIcons.put(MarkerStatus.NOT_CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_library));
            markerIcons.put(MarkerStatus.CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_details));

        }else if(markerType.equals(MarkerTypes.DEFAULT)){

            markerIcons.put(MarkerStatus.NOT_CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker));
            markerIcons.put(MarkerStatus.CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_details));

        } else if(markerType.equals(MarkerTypes.BUS_STOP)){
            markerIcons.put(MarkerStatus.NOT_CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_bus_stop));
            markerIcons.put(MarkerStatus.CLICKED, ContextCompat
                    .getDrawable(context, R.drawable.ic_marker_details));
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

    public BoundingBox getMapBoundingBox(){
        // Get map configuration from location_config.properties
        final String configWestLimit = ConfigHelper.getConfigValue(context, Constants.CONFIG_WEST_LIMIT);
        final String configSouthLimit = ConfigHelper.getConfigValue(context, Constants.CONFIG_SOUTH_LIMIT);
        final String configEastLimit = ConfigHelper.getConfigValue(context, Constants.CONFIG_EAST_LIMIT);
        final String configNorthLimit = ConfigHelper.getConfigValue(context, Constants.CONFIG_NORTH_LIMIT);

        // Parse information about map bounds
        double westBound = Double.valueOf(configWestLimit);
        double southBound = Double.valueOf(configSouthLimit);
        double eastBound = Double.valueOf(configEastLimit);
        double northBound = Double.valueOf(configNorthLimit);

        return  new BoundingBox(northBound,eastBound,southBound,westBound);
    }

    public GeoPoint getMapStartPoint(){
        final String[] startCameraCoordinates = ConfigHelper.getConfigValue(
                context, Constants.CONFIG_START_CAMERA_COORDINATES).split(",");
        double latitude = Double.valueOf(startCameraCoordinates[0]);
        double longitude = Double.valueOf(startCameraCoordinates[1]);
        return new GeoPoint(latitude,longitude);
    }

    public boolean isUserWithinLimits(POI userLocation, BoundingBox mapLimits) throws NullPointerException{
      double latitude = userLocation.getLatitude();
      double longitude = userLocation.getLongitude();
        return mapLimits.contains(latitude,longitude);
    }


}
