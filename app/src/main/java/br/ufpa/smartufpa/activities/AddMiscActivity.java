package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufpa.smartufpa.AddPlaceParent;
import br.ufpa.smartufpa.R;

public class AddMiscActivity extends AddPlaceParent {

    public static final String TAG = AddMiscActivity.class.getName();

    private TextInputEditText edtxtName;
    private TextInputEditText edtxtContact;
    private TextInputEditText edtxtBuilding;
    private Spinner spinServices;

    private Button btnConfirm;
    private Button btnCancel;

    private String keyName = "name";
    private String keyContact = "contact";
    private String keyBuilding = "building";
    private String keyServices = "services";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_misc);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ARG_LATITUDE,0);
        longitude = intent.getDoubleExtra(ARG_LONGITUDE,0);


        edtxtName = findViewById(R.id.input_misc_name);
        edtxtContact = findViewById(R.id.input_misc_contact);
        edtxtBuilding = findViewById(R.id.input_misc_building);


        btnCancel = findViewById(R.id.btn_misc_cancel);
        btnConfirm = findViewById(R.id.btn_misc_confirm);

        /*Spinner input*/
        spinServices = findViewById(R.id.spin_misc_services);
        // TODO alterar esse adapter para receber dados do banco
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_administrative_role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinServices.setAdapter(adapter);

        //Confirm Button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put(keyName, edtxtName.getText().toString());
                    json.put(ARG_LATITUDE, latitude);
                    json.put(ARG_LONGITUDE, longitude);
                    json.put(keyContact, edtxtContact.getText().toString());
                    json.put(keyBuilding, edtxtBuilding.getText().toString());

                    if (spinServices.getSelectedItemId() == 0)
                        json.put(keyServices,"");
                    else
                        json.put(keyServices, spinServices.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "onClick: " + json.toString());
            }
        });





    }
}
