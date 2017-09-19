package br.ufpa.smartufpa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.ufpa.smartufpa.R;

/**
 * Simple Activity to show pertinent information about the app
 * @author kaeuchoa
 */

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
