package com.example.kaeuc.smartufpa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {
    public static final String ACTION_ABOUT = "osmapp.ACTION_ABOUT";
    public static final String CATEGORY_ABOUT = "osmapp.CATEGORY_ABOUT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
