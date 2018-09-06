package br.ufpa.smartufpa.activities;

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

import br.ufpa.smartufpa.R;

public class AddCourseActivity extends AppCompatActivity {

    private static final String TAG = AddCourseActivity.class.getName();
    private TextInputEditText edtxtName;
    private TextInputEditText edtxtCode;
    private TextInputEditText edtxtDuration;
    private TextInputEditText edtxtFaculty;
    private Spinner spinCourseLevel;

    private Button btnConfirm;
    private Button btnCancel;

    private final String keyName = "name";
    private final String keyCode = "code";
    private final String keyDuration= "duration";
    private final String keyLevel= "course_level";
    private final String keyFaculty= "faculty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);


        /*Spinner input*/
        spinCourseLevel = findViewById(R.id.spin_crs_level);
        edtxtName = findViewById(R.id.input_crs_name);
        edtxtCode = findViewById(R.id.input_crs_code);
        edtxtDuration = findViewById(R.id.input_crs_duration);
        edtxtFaculty = findViewById(R.id.input_crs_faculty);



        // TODO preencher o array com as informações vindas do banco de dados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_administrative_role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCourseLevel.setAdapter(adapter);

        //Confirm Button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put(keyName, edtxtName.getText().toString());
                    json.put(keyCode, edtxtCode.getText().toString());
                    json.put(keyDuration, edtxtDuration.getText().toString());
                    json.put(keyFaculty, edtxtFaculty.getText().toString());

                    if (spinCourseLevel.getSelectedItemId() == 0)
                        json.put(keyLevel,"");
                    else
                        json.put(keyLevel, spinCourseLevel.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "onClick: " + json.toString());
            }
        });
    }
}
