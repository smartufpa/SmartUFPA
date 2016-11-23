package com.example.kaeuc.osmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaIntermediaria extends AppCompatActivity {
    public static final String ACTION_MAP = "osmapp.ACTION_MAP";
    public static final String CATEGORY_MAP = "osmapp.CATEGORY_MAP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_intermediaria);

        Button mobilidade = (Button) findViewById(R.id.mobilidade);

        mobilidade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TelaIntermediaria.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
