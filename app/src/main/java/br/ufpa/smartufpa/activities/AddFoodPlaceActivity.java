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

public class AddFoodPlaceActivity extends AddPlaceParent {

    public static final String TAG = AddFoodPlaceActivity.class.getName();

    private TextInputEditText edtxtName;
    private TextInputEditText edtxtOpening;
    private TextInputEditText edtxtClosing;
    private TextInputEditText edtxtOwner;
    private Spinner spinCategory;

    private Button btnConfirm;
    private Button btnCancel;

    private String keyName = "name";
    private String keyOpening= "opening_time";
    private String keyClosing= "closing_time";
    private String keyOwner= "owner";
    private String keyCategory= "category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_place);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ARG_LATITUDE,0);
        longitude = intent.getDoubleExtra(ARG_LONGITUDE,0);


        edtxtName = findViewById(R.id.input_food_name);
        edtxtOpening = findViewById(R.id.input_food_openingtime);
        edtxtClosing = findViewById(R.id.input_food_closingtime);
        edtxtOwner = findViewById(R.id.input_food_owner);

        btnConfirm = findViewById(R.id.btn_food_confirm);
        btnCancel = findViewById(R.id.btn_food_cancel);

        /*Spinner input*/
        spinCategory = findViewById(R.id.spin_food_category);
        // TODO alterar para receber dados do banco
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_administrative_role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put(keyName, edtxtName.getText().toString());
                    json.put(ARG_LATITUDE, latitude);
                    json.put(ARG_LONGITUDE, longitude);

                    json.put(keyOwner, edtxtOwner.getText().toString());
                    json.put(keyOpening, edtxtOpening.getText().toString());
                    json.put(keyClosing, edtxtClosing.getText().toString());

                    if (spinCategory.getSelectedItemId() == 0)
                        json.put(keyCategory,"");
                    else
                        json.put(keyCategory, spinCategory.getSelectedItem().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "onClick: " + json.toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFoodPlaceActivity.this.onBackPressed();
            }
        });

    }
}
