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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufpa.smartufpa.AddPlaceParent;
import br.ufpa.smartufpa.R;

public class AddLaboratoryActivity extends AddPlaceParent {

    public static final String TAG = AddLaboratoryActivity.class.getName();

    private TextInputEditText edtxtName;
    private Spinner spinAccess;
    private Spinner spinCategory;

    private Button btnConfirm;
    private Button btnCancel;

    private final String keyName = "name";
    private final String keyAccess = "access";
    private final String keyCategory = "category_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_laboratory);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(ARG_LATITUDE,0);
        longitude = intent.getDoubleExtra(ARG_LONGITUDE,0);

        edtxtName = findViewById(R.id.input_lab_name);
        spinAccess = findViewById(R.id.spin_lab_access);
        spinCategory = findViewById(R.id.spin_lab_category);

        btnConfirm = findViewById(R.id.btn_lab_confirm);
        btnCancel = findViewById(R.id.btn_lab_cancel);


        /*Spinner input*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_administrative_role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // TODO alterar esse adapter para recuperar informações do banco
        spinAccess.setAdapter(adapter);
        spinCategory.setAdapter(adapter);



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put(keyName,edtxtName.getText());
                    json.put(ARG_LATITUDE, latitude);
                    json.put(ARG_LONGITUDE,longitude);
                    if (spinAccess.getSelectedItemId() == 0)
                        json.put(keyAccess,"");
                    else
                        json.put(keyAccess, spinAccess.getSelectedItem().toString());

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
                AddLaboratoryActivity.this.onBackPressed();
            }
        });




    }

}
