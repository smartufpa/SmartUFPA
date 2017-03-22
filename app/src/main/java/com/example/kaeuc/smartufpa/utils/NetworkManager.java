package com.example.kaeuc.smartufpa.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by kaeuc on 26/01/2017.
 */

public class NetworkManager {


    public static boolean checkWifiConnection(Context parentContext){
        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)parentContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!= null){
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            if(!isWiFi){
                isConnected = activeNetwork.isConnectedOrConnecting();
            }
        }

        return isConnected;
    }

    public static boolean checkMobileConnection(Context parentContext){
        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)parentContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!= null){
            boolean isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
            if(!isMobile){
                isConnected = activeNetwork.isConnectedOrConnecting();
            }
        }

        return isConnected;
    }

    public static boolean checkNetworkConnection(Context parentContext){
        return checkMobileConnection(parentContext) || checkWifiConnection(parentContext);
    }
}