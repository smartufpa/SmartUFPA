package br.ufpa.smartufpa.utils;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * code based on: https://wildanmsyah.wordpress.com/2017/05/11/mqtt-android-client-tutorial/
 */

public class MqttBusHelper {

    private MqttAndroidClient mqttAndroidClient;

    public static final String TAG = MqttBusHelper.class.getSimpleName();


    // TODO: ADD LASSE server credentials
    //final String serverUri = "tcp://iot.eclipse.org:1883";

    // TODO: transfer strings to Constants
    // Test server
    private final String serverUri = "tcp://m13.cloudmqtt.com:13687";
    private final String clientId = "ExampleAndroid";
    private final String subscriptionTopic = "/ufpa/circular/loc/+";

     final String username = "rrlampnt";
     final String password = "2ZBKsPgOJxdp";

    public MqttBusHelper(Context context){
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                    Log.w(TAG, "MqttClient Connected to : " + serverUri);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "Failed to connect to: " + serverUri + exception.toString());
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "failed to disconnect from server. ", e);
        }finally {
            Log.e(TAG, "MqttClient Disconnected from server. ");
        }
    }

    public boolean isConnected(){
        return mqttAndroidClient.isConnected();
    }

    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG,"Mqtt Client Subscribed to " + subscriptionTopic + " Successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exceptionst subscribing");
            ex.printStackTrace();
        }
    }

}
