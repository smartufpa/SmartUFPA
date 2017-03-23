package com.example.kaeuc.smartufpa.server;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMapDownloadFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MapDownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapDownloadFragment extends DialogFragment {

    private OnMapDownloadFragmentListener mListener;
    private ProgressBar progressBar;
    private TextView txtMapDownloadProgress;
    private TextView txtFileSize;
    private int fileSize;
    private int taskStatus = Constants.SERVER_RESPONSE_SUCCESS;

    private final String TAG = MapDownloadFragment.class.getSimpleName();
    public static final String FRAGMENT_TAG = "MapDownloadDialog";

    public MapDownloadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapDownloadFragment.
     */

    public static MapDownloadFragment newInstance() {
        MapDownloadFragment fragment = new MapDownloadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NO_INPUT,android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new DownloadMapTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_download_map, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.prog_map_download);
        txtMapDownloadProgress = (TextView) view.findViewById(R.id.txt_map_download_progress);
        txtFileSize = (TextView) view.findViewById(R.id.txt_map_download_filesize);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapDownloadFragmentListener) {
            mListener = (OnMapDownloadFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapDownloadFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDownloadFinished(taskStatus);
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMapDownloadFragmentListener {
        void onDownloadFinished(int taskStatus );
    }


    private class DownloadMapTask extends AsyncTask<Void,Integer,Integer>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(Void... voids) {

            HttpURLConnection connection = null;
            /*Server URL*/
            URL finalUrl;
            try{

                Uri builtUri = Uri.parse(getString(R.string.map_download_url)).buildUpon()
                        .build();


                URL url = new URL(builtUri.toString());
                // Create the request to the server, and open the connection
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout( 15000 /* milliseconds */ );
                connection.setReadTimeout( 15000 /*milliseconds*/ );
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.connect();
                Log.i(TAG,"Request sent to: "+ url.toString());

                // recebe a resposta da requisição
                InputStream inputStream;

                int status = connection.getResponseCode();
                Log.i(TAG,"Connection status: " + status);

                if (status != HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getErrorStream();
                    return status;
                }else
                    inputStream = connection.getInputStream();

                // se a resposta for vazia
                if(inputStream == null){
                    return null;
                }


                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
                File saveFilePath = new File(Environment.getExternalStorageDirectory(),"osmdroid");

                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File file = new File(saveFilePath,"tiles.zip");

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet

                //this is the total size of the file
                fileSize = connection.getContentLength();

                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    publishProgress(calculatePercentDownload(downloadedSize,fileSize));

                }
                //close the output stream when done
                fileOutput.close();

                return Constants.SERVER_RESPONSE_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }

            }
           return null;
        }
        private int calculatePercentDownload(int currentValue, int fileSize){
            return (currentValue*100)/fileSize;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            txtFileSize.setText(fileSize + " KB");
            progressBar.setProgress(values[0]);
            txtMapDownloadProgress.setText(values[0] + "%");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == Constants.SERVER_FORBIDDEN) taskStatus = Constants.SERVER_FORBIDDEN;

            else if(integer == Constants.SERVER_INTERNAL_ERROR) taskStatus = Constants.SERVER_INTERNAL_ERROR;

            else if(integer == Constants.SERVER_RESPONSE_TIMEOUT) taskStatus = Constants.SERVER_RESPONSE_TIMEOUT;

            else if(integer == Constants.SERVER_RESPONSE_SUCCESS) taskStatus = Constants.SERVER_RESPONSE_SUCCESS;

            dismiss();

        }
    }




}
