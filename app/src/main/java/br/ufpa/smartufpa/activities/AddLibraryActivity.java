package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufpa.smartufpa.AddPlaceParent;
import br.ufpa.smartufpa.R;

public class AddLibraryActivity extends AddPlaceParent {

    private TextInputEditText edtxtName;
    private TextInputEditText edtxtOpening;
    private TextInputEditText edtxtClosing;
    private TextInputEditText edtxtBuilding;

    private Button btnConfirm;
    private Button btnCancel;


    private static final String TAG = AddLibraryActivity.class.getName();


    private final String keyName = "name";
    private final String keyOpeningTime = "opening_time";
    private final String keyClosingTime = "closing_time";
    private final String keyBuildingId = "building_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_library);


        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ARG_LATITUDE, 0);
        longitude = intent.getDoubleExtra(ARG_LONGITUDE, 0);

        /*EditText input name openning*/
        edtxtName = findViewById(R.id.input_lib_name);
        /*EditText input OperationHours openning*/
        edtxtOpening = findViewById(R.id.input_lib_openingtime);
        /*EditText input OperationHours openning*/
        edtxtClosing = findViewById(R.id.input_lib_closingtime);
        /*EditText input building openning*/
        edtxtBuilding = findViewById(R.id.input_lib_building);

        btnConfirm = findViewById(R.id.btn_lib_confirm);
        btnCancel = findViewById(R.id.btn_lib_cancel);

        //Confirm Button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put(keyName, edtxtName.getText().toString());
                    json.put(ARG_LATITUDE, latitude);
                    json.put(ARG_LONGITUDE, longitude);


                    json.put(keyOpeningTime, edtxtOpening.getText().toString());
                    json.put(keyClosingTime, edtxtClosing.getText().toString());
                    json.put(keyBuildingId, edtxtBuilding.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "onClick: " + json.toString());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLibraryActivity.this.onBackPressed();
            }
        });


    }

}
