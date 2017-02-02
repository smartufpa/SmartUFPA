package com.example.kaeuc.smartufpa;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AppMenuActivity extends AppCompatActivity {
    public static final String ACTION_APP_MENU = "osmapp.ACTION_APP_MENU";
    public static final String CATEGORY_APP_MENU = "osmapp.CATEGORY_APP_MENU";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_menu);

        Button mobilidade = (Button) findViewById(R.id.btn_mobilidade);


        mobilidade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AppMenuActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

       }
}
