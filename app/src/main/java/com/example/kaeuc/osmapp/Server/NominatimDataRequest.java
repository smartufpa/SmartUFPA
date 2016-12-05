package com.example.kaeuc.osmapp.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.kaeuc.osmapp.Extras.Place;

import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by kaeuc on 08/11/2016.
 * Classe responsável por executar busca de Pontos de Interesse múltiplos no servidor do Nominatim
 * de maneira assíncrona.
 * Recebe uma String contendo o termo da busca e retorna uma ArrayList de POIs
 */

public class NominatimDataRequest extends AsyncTask<String,Void,ArrayList<POI>> {

    private Context parentContext;
    private ProgressBar progressBar;
    // interface resposável por devolver o resultado da task pra atividade principal
    private NominatimDataRequestResponse callBack = null;
    private static final String TAG = "NominatatimDataRequest";

    public NominatimDataRequest(Context parentContext, ProgressBar progressBar) {
        this.parentContext = parentContext;
        this.callBack = (NominatimDataRequestResponse) parentContext;
        this.progressBar = progressBar;
    }
    // Mostra a barra de progresso durante a execução da task
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }
    /* Faz a consulta ao servidor do nominatim
     * Referência: https://github.com/MKergall/osmbonuspack/wiki/Tutorial_2
     */
    @Override
    protected ArrayList<POI> doInBackground(String... params) {
        String query = params[0] + ",Belém";
        GeoPoint viewBox = new GeoPoint(Double.valueOf(params[1]),Double.valueOf(params[2]));

        NominatimPOIProvider poiProvider = new NominatimPOIProvider("OsmNavigator/1.0");
        return poiProvider.getPOICloseTo(viewBox, query, 50, 0.1);

    }

    // recebe a lista dos pontos de interesse (POIs) e devolve para a atividade principal
    @Override
    protected void onPostExecute(ArrayList<POI> pois) {
        super.onPostExecute(pois);
        callBack.nominatimTaskResponse(Place.convertPOIsToPlaces(pois));
        progressBar.setVisibility(View.GONE);


    }


}
