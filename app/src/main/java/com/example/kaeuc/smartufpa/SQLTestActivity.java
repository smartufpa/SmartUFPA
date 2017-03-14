package com.example.kaeuc.smartufpa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.server.LocalHostRequest;
import com.example.kaeuc.smartufpa.server.LocalHostRequestResponse;

public class SQLTestActivity extends AppCompatActivity implements LocalHostRequestResponse {

    public static final String SQL_ACTION = "smartufpa.SQL_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqltest);
//        new LocalHostRequest(this).execute(new Place(123456,48.23,7.48,"nome qualquer","nome","descrição qualquer"));


    }

    @Override
    public void onLocalHostTaskResponse(String response) {
        ((AppCompatTextView)findViewById(R.id.txt_localhost)).setText(response);
    }
}
