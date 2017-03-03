package com.example.kaeuc.smartufpa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.kaeuc.smartufpa.jsonParserTest.OverpassModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class JsonParserTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_parser_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final String readfile = readfile();
        TextView textView = (TextView) findViewById(R.id.asd);

        Gson gson = new Gson();
        OverpassModel overpassModel = gson.fromJson(readfile, OverpassModel.class);
        textView.setText(overpassModel.getElements().get(0).getTags().toString());
        textView.append("\n" + String.valueOf(overpassModel.getElements().get(0).isCenterEmpty()));
        textView.append("\n" + String.valueOf(overpassModel.getElements().get(0).getLat()));
        textView.append("\n" + String.valueOf(overpassModel.getElements().get(0).getLon()));



    }

    private String readfile(){
        String filename = "jsontest";
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = getResources().openRawResource(R.raw.jsonteste);
        int character;

        try {
            while((character= inputStream.read())!=-1){
                stringBuffer.append((char)character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(stringBuffer);
    }

}
