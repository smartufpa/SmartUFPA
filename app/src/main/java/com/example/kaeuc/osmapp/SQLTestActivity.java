package com.example.kaeuc.osmapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import com.example.kaeuc.osmapp.Server.LocalHostRequest;
import com.example.kaeuc.osmapp.Server.LocalHostRequestResponse;

public class SQLTestActivity extends AppCompatActivity implements LocalHostRequestResponse {

    public static final String SQL_ACTION = "osmapp.SQL_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqltest);


        new LocalHostRequest(this).execute();


    }

    @Override
    public void LocalHostTaskResponse(String response) {
        ((AppCompatTextView)findViewById(R.id.txt_localhost)).setText(response);
    }
}
