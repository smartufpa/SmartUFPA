package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kaeuc on 1/11/2017.
 */

public class LocalHostRequest extends AsyncTask<String, Void, String> {
    public static final String TAG = "LocalHostRequest";

    LocalHostRequestResponse callback = null;
    Context parent;
    public LocalHostRequest(Context parent) {
        this.callback = (LocalHostRequestResponse) parent;
    }


    private String makeRequest(final String requestType){

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String response = "Resposta em branco";
        /*Server URL*/
        URL url = null;
        try{
            url = new URL("http://192.168.0.25:80/smart-ufpa/testmysql.php");

            Log.i(TAG,"Request sent to: "+ url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout( 25000 /*milliseconds*/ );
            connection.setConnectTimeout( 25000 /* milliseconds */ );

            if(requestType.equals("GET")) {
                // false para GET requests
                connection.setDoOutput(false);
            }else{
                connection.setDoOutput(true);
            }
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

            response = buffer.toString();

            return response;
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


    @Override
    protected String doInBackground(String... params) {
        String requestType = params[0];
        final String response = makeRequest(requestType);
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            Log.println(Log.INFO,TAG,s);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        callback.LocalHostTaskResponse(s);


    }
}
