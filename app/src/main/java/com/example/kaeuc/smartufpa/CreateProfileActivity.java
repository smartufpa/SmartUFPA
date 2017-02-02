package com.example.kaeuc.smartufpa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kaeuc.smartufpa.database.LoginDAO;

/**
 * Created by kaeuc on 10/5/2016.
 */
public class CreateProfileActivity extends AppCompatActivity{

    public static final String CATEGORY_CREATEPROFILE = "osmapp.CATEGORY_CREATEPROFILE";
    public static final String ACTION_CREATEPROFILE = "osmapp.ACTION_CREATEPROFILE";

    private EditText edtCreateUsername;
    private EditText edtCreatePassword;
    private Spinner spnProfile;
    private Button btnCreate;

    private LoginDAO helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        helper = new LoginDAO(this);

        // Views
        edtCreateUsername = (EditText) findViewById(R.id.edt_new_username);
        edtCreatePassword = (EditText) findViewById(R.id.edt_new_password);
        spnProfile = (Spinner) findViewById(R.id.spn_profile);
        btnCreate = (Button) findViewById(R.id.btn_create);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.addUser(edtCreateUsername.getText().toString(),
                        edtCreatePassword.getText().toString(), String.valueOf(spnProfile.getSelectedItem()),
                        CreateProfileActivity.this);
                finish();
            }
        });
    }
}
