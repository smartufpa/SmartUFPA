package com.example.kaeuc.smartufpa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.database.PlaceDAO;

import java.net.SocketTimeoutException;


public class SQLTestActivity extends AppCompatActivity {

    public static final String SQL_ACTION = "smartufpa.SQL_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqltest);
        final Button btn = (Button) findViewById(R.id.btn_sqltest);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceDAO placeDAO = PlaceDAO.getInstance();
                placeDAO.getPlaceByName("Biblioteca Central");
            }
        });


    }


}
