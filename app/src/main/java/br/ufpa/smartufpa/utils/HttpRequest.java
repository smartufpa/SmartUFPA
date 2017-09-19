package br.ufpa.smartufpa.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by kaeuc on 1/14/2017.
 * Classe para reutilização de código e tratamento de requisições http.
 */

public class HttpRequest {
    private static final String TAG = "HttpRequest";

    public static String makePostRequest(final String url,final String query, String jsonBody) throws SocketTimeoutException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        OutputStream os = null;
        String response = "empty_response";
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
            connection.setReadTimeout( 15000 /*milliseconds*/ );
            connection.setConnectTimeout( 15000 /* milliseconds */ );
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Send POST output.
            Log.println(Log.INFO,TAG+"/POST",jsonBody);
            os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(jsonBody);
            writer.flush();
            writer.close();
            os.close();


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
            if (e instanceof SocketTimeoutException){
                throw new SocketTimeoutException();
            }else{
                e.printStackTrace();
            }
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



    public static String makeGetRequest(final String url, final String query) throws SocketTimeoutException{
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
            connection.setReadTimeout( 15000 /*milliseconds*/ );
            connection.setConnectTimeout( 15000 /* milliseconds */ );
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

            response = buffer.toString();

            return response;

        } catch (IOException e){
            if (e instanceof SocketTimeoutException){
                throw new SocketTimeoutException();
            }else{
                e.printStackTrace();
            }
        } finally {
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


}
