package br.ufpa.smartufpa.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import br.ufpa.smartufpa.utils.enums.MarkerStatus;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;

import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.HashMap;

/**
 * Helper class to define styles for kml layers. On this case is used to style the bus route
 * layer style.
 * @author kaeuchoa
 */

public class BusRouteKmlStyler implements KmlFeature.Styler {

    private MapUtils mapUtils;


    public BusRouteKmlStyler(Context context)  {
        this.mapUtils = new MapUtils(context);
    }

    @Override
    public void onFeature(Overlay overlay, KmlFeature kmlFeature) {

    }

    @Override
    public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {
        final HashMap<MarkerStatus, Drawable> markerDrawable = mapUtils.getMarkerDrawable(MarkerTypes.BUS_STOP);
        marker.setIcon(markerDrawable.get(MarkerStatus.NOT_CLICKED));
        marker.setInfoWindow(null);
    }

    @Override
    public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {
        polyline.setWidth(10.0f);
        polyline.setColor(Color.argb(150,50,50,255));
        polyline.setInfoWindow(null);
    }

    @Override
    public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {

    }

    @Override
    public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {

    }
}
