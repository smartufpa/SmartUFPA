package br.ufpa.smartufpa.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.ufpa.smartufpa.R;

public class AddBuildingActivity extends AppCompatActivity {

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String TAG = AddBuildingActivity.class.getName();

    private double latitude;
    private double longitude;

    private TextInputEditText edtxtName;
    private TextInputEditText edtxtDescription;
    private TextInputEditText edtxtWebsite;
    private TextInputEditText edtxtOpeningTime;
    private Spinner spinner;
    private TextInputEditText edtxtClosingTime;
    private Button btnConfirm;


    private final String keyName = "name";
    private final String keyDescription = "description";
    private final String keyWebsite = "website";
    private final String keyOpeningTime = "opening_time";
    private final String keyClosingTime = "closing_time";
    private final String keyAdministrativeRole = "administrative_role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ARG_LATITUDE, 0);
        longitude = intent.getDoubleExtra(ARG_LONGITUDE, 0);

        /*EditText input Name*/
        edtxtName = findViewById(R.id.input_name);
        /*EditText input Description*/
        edtxtDescription = findViewById(R.id.input_description);
        /*EditText input Website*/
        edtxtWebsite = findViewById(R.id.input_website);
        /*EditText input OperationHours openning*/
        edtxtOpeningTime = findViewById(R.id.input_openingtime);
        /*EditText input OperationHours openning*/
        edtxtClosingTime = findViewById(R.id.input_closingtime);

        btnConfirm = findViewById(R.id.btn_add_place_confirm);

        /*Spinner input*/
        spinner = findViewById(R.id.spin_administrative_role);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_administrative_role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        //Confirm Button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put(keyName, edtxtName.getText().toString());
                    json.put(ARG_LATITUDE, latitude);
                    json.put(ARG_LONGITUDE, longitude);
                    json.put(keyDescription, edtxtDescription.getText().toString());
                    json.put(keyWebsite, edtxtWebsite.getText().toString());
                    json.put(keyOpeningTime, edtxtOpeningTime.getText().toString());
                    json.put(keyClosingTime, edtxtClosingTime.getText().toString());

                    if (spinner.getSelectedItemId() == 0)
                        json.put(keyAdministrativeRole,"");
                    else
                        json.put(keyAdministrativeRole, spinner.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "onClick: " + json.toString());
            }
        });


    }
}
