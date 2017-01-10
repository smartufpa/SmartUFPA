package com.example.kaeuc.osmapp.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.kaeuc.osmapp.Extras.Constants;
import com.example.kaeuc.osmapp.Extras.OsmJsonParser;
import com.example.kaeuc.osmapp.Extras.Place;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by kaeuc on 10/22/2016.
 * Classe responsável por executar busca de Pontos de Interesse Específicos baseado na buca do usuário
 * no servidor do OSM (XAPI - API direta de busca) de maneira assíncrona
 * Referência: http://wiki.openstreetmap.org/wiki/Xapi#Overpass_API
 * Recebe uma String contendo qual informação/filtro deseja ser buscado e retorna uma objeto XML
 * em formato String que deve ser analisado para retirar as localizações relevantes
 */

public class OsmDataRequest extends AsyncTask<String,Void,String> {

    private static final String TAG = "OsmDataRequest" ;
    // interface resposável por devolver o resultado da task pra atividade principal
    private OsmDataRequestResponse callBack = null;
    private ProgressBar progressBar;

    private Context parentContext;
    private String filtro = null;

    public OsmDataRequest(Context parentContext, ProgressBar progressBar) {
        this.parentContext = parentContext;
        this.callBack = (OsmDataRequestResponse) parentContext;
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(String... params) {
        filtro = params[0];
        String jsonResponse = null;
        String query = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        /*Server URL*/
        URL url = null;
        try{
            // Essa url deve se parecer algo como http://www.overpass-api.de/api/xapi?*[key=value][bbox=-48.46069,-1.47956,-48.45348,-1.47158]

            if(filtro.equalsIgnoreCase(Constants.RESTAURANT_FILTER))
                query = URLEncoder.encode(Constants.OVERPASS_RESTAURANT_QUERY,"UTF-8");
            else if(filtro.equalsIgnoreCase(Constants.TOILETS_FILTER))
                query = URLEncoder.encode(Constants.OVERPASS_TOILETS_QUERY,"UTF-8");
            else if(filtro.equalsIgnoreCase(Constants.XEROX_FILTER))
                query = URLEncoder.encode(Constants.OVERPASS_XEROX_QUERY,"UTF-8");

            url = new URL(Constants.OVERPASS_SERVER_URL + query);

            Log.i(TAG,"Request sent to: "+ url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout( 10000 /*milliseconds*/ );
            connection.setConnectTimeout( 10000 /* milliseconds */ );
            // false para GET requests
            connection.setDoOutput(false);
            connection.setRequestProperty("Content-Encoding", "gzip");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();


            // recebe a resposta da requisição
            InputStream inputStream;

            int status = connection.getResponseCode();
            Log.i(TAG,"Connection status: " + status);

            if (status != HttpURLConnection.HTTP_OK)
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();

            StringBuffer buffer = new StringBuffer();
            // se a resposta for vazia
            if(inputStream == null){
                return null;
            }
            reader =  new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                return null;
            }

            jsonResponse = buffer.toString();

            return jsonResponse;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
    // Mostra a barra de progresso
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    // executa após a operação ser finalizada
    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute(jsonResponse);
        // Recebe o xml em forma de uma String e e analisa as informções relevantes
        final List<Place> locais = OsmJsonParser.parseResponse(jsonResponse);
        // Retorna os valores para a activity que chamou a ASyncTask
        callBack.osmTaskCompleted(locais,filtro);
        // esconde a barra de progresso
        progressBar.setVisibility(View.GONE);


    }

}