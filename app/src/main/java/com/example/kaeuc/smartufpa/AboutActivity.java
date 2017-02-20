package com.example.kaeuc.smartufpa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AboutActivity extends AppCompatActivity {
    public static final String ACTION_ABOUT = "osmapp.ACTION_ABOUT";
    public static final String CATEGORY_ABOUT = "osmapp.CATEGORY_ABOUT";
    public static final String TAG = "About";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy called");
    }
}
