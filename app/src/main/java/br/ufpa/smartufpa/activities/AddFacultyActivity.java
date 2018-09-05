package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufpa.smartufpa.AddPlaceParent;
import br.ufpa.smartufpa.R;

public class AddFacultyActivity extends AddPlaceParent {


    public static final String TAG = AddFacultyActivity.class.getName();


    private TextInputEditText edtxtName;
    private TextInputEditText edtxtShortName;
    private TextInputEditText edtxtPrincipal;
    private TextInputEditText edtxtWebsite;
    private TextInputEditText edtxtBuilding;

    private Button btnConfirm;
    private Button btnCancel;

    private final String keyName = "name";
    private final String keyShortName = "short_name";
    private final String keyPrincipal = "principal";
    private final String keyWebsite = "website";
    private final String keyBuildingId = "building_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty_activity);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ARG_LATITUDE, 0);
        longitude = intent.getDoubleExtra(ARG_LONGITUDE, 0);

        edtxtName = findViewById(R.id.input_fac_name);
        edtxtShortName = findViewById(R.id.input_fac_short_name);
        edtxtPrincipal = findViewById(R.id.input_fac_principal);
        edtxtWebsite = findViewById(R.id.input_fac_website);
        edtxtBuilding = findViewById(R.id.input_fac_building);

        btnConfirm = findViewById(R.id.btn_fac_confirm);
        btnCancel= findViewById(R.id.btn_fac_cancel);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put(keyName, edtxtName.getText().toString());
                    json.put(ARG_LATITUDE, latitude);
                    json.put(ARG_LONGITUDE, longitude);
                    json.put(keyShortName, edtxtShortName.getText().toString());
                    json.put(keyWebsite, edtxtWebsite.getText().toString());
                    json.put(keyPrincipal, edtxtPrincipal.getText().toString());
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
                AddFacultyActivity.this.onBackPressed();
            }
        });
    }




}
