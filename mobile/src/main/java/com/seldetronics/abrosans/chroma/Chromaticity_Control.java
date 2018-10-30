package com.seldetronics.abrosans.chroma;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;
import java.net.Socket;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class Chromaticity_Control extends Activity {

    SeekBar seekbarRED;
    int RED;
    SeekBar seekbarGREEN;
    int GREEN;
    SeekBar seekbarBLUE;
    int BLUE;
    SVBar svBar;
    ColorPicker colourpicker;
    ToggleButton OnOffSwitch;
    String TAG;
    TextView debug_display;
    String clientId;
    MqttAndroidClient client;
    byte[] encodedPayload = new byte[0];
    String payload;
    String topic = "colour";
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chromaticity__control);

        final TextView debugdiplay = (TextView) findViewById(R.id.tvLabel1);
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://192.168.1.76:1883", clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        seekbarRED = (SeekBar) findViewById(R.id.sliderR);
        seekbarGREEN = (SeekBar) findViewById(R.id.sliderG);
        seekbarBLUE = (SeekBar) findViewById(R.id.sliderB);
        colourpicker = (ColorPicker) findViewById(R.id.Colour_picker);
        svBar = (SVBar) findViewById(R.id.svbar);
        OnOffSwitch = (ToggleButton) findViewById(R.id.toggleButton);
        colourpicker.addSVBar(svBar);
        colourpicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            public void onColorChanged(int color) {
                OnOffSwitch.setChecked(true);
                int RED = (color >> 16) & 0xFF;
                int GREEN = (color >> 8) & 0xFF;
                int BLUE = color & 0xFF;
                seekbarRED.setProgress(RED);
                seekbarGREEN.setProgress(GREEN);
                seekbarBLUE.setProgress(BLUE);
                payload = "#" + Integer.toHexString(0x100 | RED).substring(1) + Integer.toHexString(0x100 | GREEN).substring(1) + Integer.toHexString(0x100 | BLUE).substring(1) + "\r";
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void onClickcolour(View view) {
        OnOffSwitch.setChecked(true);
        RED = seekbarRED.getProgress();
        GREEN = seekbarGREEN.getProgress();
        BLUE = seekbarBLUE.getProgress();
        colourpicker.setOldCenterColor(Color.rgb(RED, GREEN, BLUE));
        payload = "#" + Integer.toHexString(0x100 | RED).substring(1) + Integer.toHexString(0x100 | GREEN).substring(1) + Integer.toHexString(0x100 | BLUE).substring(1) + "\r";
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void onOnOffClick(View view) {
        if (OnOffSwitch.isChecked()) {
            payload = "#FFFFFF\r";
            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                client.publish(topic, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        } else {
            payload = "#000000\r";
            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                client.publish(topic, message);

            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
