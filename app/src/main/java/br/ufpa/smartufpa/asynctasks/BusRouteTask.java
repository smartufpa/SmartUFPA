package br.ufpa.smartufpa.asynctasks;

import android.os.AsyncTask;

import br.ufpa.smartufpa.asynctasks.interfaces.OnBusRouteListener;
import br.ufpa.smartufpa.fragments.MapFragment;
import br.ufpa.smartufpa.utils.BusRouteKmlStyler;
import br.ufpa.smartufpa.utils.OverpassHelper;
import br.ufpa.smartufpa.utils.enums.ServerResponse;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

/**
 * AsyncTask responsible for querying the route of internal buses.
 * Uses KML and OverpassAPI.
 * The query construction is made by OverpassHelper class.
 * @see OverpassHelper
 * reference: https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4
 * @author kaeuchoa
 *
 */

public class BusRouteTask extends AsyncTask<Void,Void, Overlay> {

    private final MapView mapView;
    private MapFragment mapFragment;
    private ServerResponse taskStatus;
    private OverpassHelper overpassHelper;
    // Parent Context that implements OnBusRouteListener which will receive the results
    private OnBusRouteListener callback;

    public BusRouteTask(MapFragment mapFragment, MapView mapView) {
        this.mapFragment = mapFragment;
        this.callback = mapFragment;
        this.mapView = mapView;
        this.taskStatus = ServerResponse.SUCCESS;
        this.overpassHelper = OverpassHelper.getInstance(mapFragment.getContext().getApplicationContext());
    }


    @Override
    protected Overlay doInBackground(Void... voids) {
        OverpassAPIProvider overpassProvider = new OverpassAPIProvider();

        final String busRouteURL = overpassHelper.getBusRouteByNameURL().toString();
//        final String busRouteURL = overpassProvider.urlForTagSearchKml("route=bus,name='Ônibus Universitário'", mapView.getBoundingBox(), 500, 30);;

        KmlDocument kmlDocument = new KmlDocument();
        // true if ok, false if technical error.
        if(overpassProvider.addInKmlFolder(kmlDocument.mKmlRoot, busRouteURL)){
            KmlFeature.Styler busRouteStyler = new BusRouteKmlStyler(mapFragment.getContext());
            return kmlDocument.mKmlRoot.buildOverlay(mapView,null, busRouteStyler, kmlDocument);
        }
        taskStatus = ServerResponse.CONNECTION_FAILED;
        return null;
    }


    @Override
    protected void onPostExecute(Overlay overlay) {
        super.onPostExecute(overlay);
        if(overlay == null){
            callback.onBusRouteResponse(null,taskStatus);
        }else{
            callback.onBusRouteResponse(overlay,taskStatus);
        }
    }
}
