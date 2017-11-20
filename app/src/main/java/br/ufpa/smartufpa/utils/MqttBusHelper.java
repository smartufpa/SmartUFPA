package br.ufpa.smartufpa.utils;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.osmdroid.util.GeoPoint;

/**
 * code based on: https://wildanmsyah.wordpress.com/2017/05/11/mqtt-android-client-tutorial/
 */

public class MqttBusHelper {

    private MqttAndroidClient mqttAndroidClient;

    public static final String TAG = MqttBusHelper.class.getSimpleName();

    // Test server
//    private final String SERVER_URI = "tcp://m13.cloudmqtt.com:13687";
//     final String username = "rrlampnt";
//     final String password = "2ZBKsPgOJxdp";

    private final String SERVER_URI = Constants.MQTT_SERVER_URI;
    private final String SUBSCRIPTION_TOPIC = Constants.MQTT_SUBSCRIPTION_TOPIC ;
    private final String CLIENT_ID = MqttClient.generateClientId() + Constants.MQTT_CLIENT_ID;


    public MqttBusHelper(Context context){
        mqttAndroidClient = new MqttAndroidClient(context, SERVER_URI, CLIENT_ID);

    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

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
                    Log.w(TAG, "MqttClient Connected to : " + mqttAndroidClient.getServerURI());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "Failed to connect to: " + SERVER_URI + exception.toString());
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
            Log.e(TAG, "Failed to disconnect from server. ", e);
        }finally {
            Log.e(TAG, "MqttClient Disconnected from server. ");
        }
    }

    public boolean isConnected(){
        return mqttAndroidClient.isConnected();
    }

    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(SUBSCRIPTION_TOPIC, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG,"Mqtt Client Subscribed to " + SUBSCRIPTION_TOPIC + " Successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Subscribed fail!",exception);
                }
            });

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }
    //  Message structure:
    //  +CGNSINF: <GNSS run status>,<Fix status>, <UTC date & Time>,<Latitude>,<Longitude>,
    //  <MSL Altitude>,<Speed Over Ground>,<Course Over Ground>,<Fix Mode>,<Reserved1>,<HDOP>,
    //  <PDOP>,<VDOP>,<Reserved2>, <GNSS Satellites in View>,<GNSS Satellites Used>,<GLONASS Satellites Used>,
    //  <Reserved3>,<C/N0 max>,<HPA>,<VPA>
    public GeoPoint readBusMessage(MqttMessage mqttMessage){
        final String[] splitMessage = mqttMessage.toString().split(",");
        double latitude = Double.parseDouble(splitMessage[3]);
        double longitude = Double.parseDouble(splitMessage[4]);

        return new GeoPoint(latitude,longitude);

    }

}
