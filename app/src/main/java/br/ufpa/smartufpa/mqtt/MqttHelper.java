package br.ufpa.smartufpa.mqtt;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * code from: https://wildanmsyah.wordpress.com/2017/05/11/mqtt-android-client-tutorial/
 */

public class MqttHelper {

    private MqttAndroidClient mqttAndroidClient;

    public static final String TAG = MqttHelper.class.getSimpleName();


    // LASSE server
    //final String serverUri = "tcp://iot.eclipse.org:1883";
    // LASSE CREDENTIALS
    // final String username = "alberto";
    // final String password = "null";
//    final String subscriptionTopic = "/ufpa/circular/loc/";

    // Test server
    final String serverUri = "tcp://m13.cloudmqtt.com:13687";
    final String clientId = "ExampleAndroid";
    final String subscriptionTopic = "/ufpa/circular/loc/+";

     final String username = "rrlampnt";
     final String password = "2ZBKsPgOJxdp";
    public MqttHelper(Context context){
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("mqtt", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                //Data structure:
                // +CGNSINF: <GNSS run status>,<Fix status>,
                // <UTC date & Time>,<Latitude>,<Longitude>,
                // <MSL Altitude>,<Speed Over Ground>,<Course Over Ground>,
                // <Fix Mode>,<Reserved1>,<HDOP>,
                // <PDOP>,<VDOP>,<Reserved2>,
                // <GNSS Satellites in View>,<GNSS Satellites Used>,<GLONASS Satellites Used>,
                // <Reserved3>,<C/N0 max>,<HPA>,<VPA>
                Log.w(TAG + "Message Received: ",  mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    private void connect(){
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
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }


    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exceptionst subscribing");
            ex.printStackTrace();
        }
    }

}
