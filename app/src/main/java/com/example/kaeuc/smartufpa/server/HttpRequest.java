package com.example.kaeuc.smartufpa.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by kaeuc on 1/14/2017.
 * Classe para reutilização de código e tratamento de requisições http.
 */

public class HttpRequest {
    private static final String TAG = "HttpRequest";

    public static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";

    protected static String makeRequest(final String method,final String url,final String query) throws EmptyMethodException {


        if(method == null || method.isEmpty()){
            throw new EmptyMethodException("You need to specify an http method (GET or POST).");
        }
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String response = "Resposta em branco";
        /*Server URL*/
        URL finalUrl;
        try{

            if(query == null){
                finalUrl = new URL(url);
            }else{
                finalUrl = new URL(url+ URLEncoder.encode(query,"UTF-8"));
            }

            Log.i(TAG,"Request sent to: "+ finalUrl.toString());
            connection = (HttpURLConnection) finalUrl.openConnection();
            connection.setReadTimeout( 25000 /*milliseconds*/ );
            connection.setConnectTimeout( 25000 /* milliseconds */ );

            if(method.equals(GET_REQUEST)) {
                // false para GET requests
                connection.setDoOutput(false);
            }else if(method.equals(POST_REQUEST)){
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


    public static class EmptyMethodException extends Exception{
        public EmptyMethodException() {
        }

        public EmptyMethodException(String message) {
            super(message);
        }

        public EmptyMethodException(String message, Throwable cause) {
            super(message, cause);
        }

        public EmptyMethodException(Throwable cause) {
            super(cause);
        }
    }

}
