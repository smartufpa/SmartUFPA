package com.example.kaeuc.smartufpa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kaeuc.smartufpa.R;

public class AboutActivity extends AppCompatActivity {
    public static final String ACTION_ABOUT = "smartufpa.ACTION_ABOUT";
    public static final String CATEGORY_ABOUT = "smartufpa.CATEGORY_ABOUT";
    public static final String TAG = AboutActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
