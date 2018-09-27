package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.ufpa.smartufpa.R;

public class LoginActivity extends AppCompatActivity{

    private TextInputEditText txtinpEmail;
    private TextInputEditText txtinpPassword;

    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtinpEmail = findViewById(R.id.txtinp_email);
        txtinpPassword = findViewById(R.id.txtinp_password);

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_login:
                        attemptLogin(txtinpEmail.getText().toString(),txtinpPassword.getText().toString());
                        break;
                    case R.id.btn_register:
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        break;
                }
            }
        };

        btnLogin.setOnClickListener(onClickListener);
        btnRegister.setOnClickListener(onClickListener);

    }

    private void attemptLogin(String email, String password) {

    }
}
