package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.User;
import br.ufpa.smartufpa.models.retrofit.UsersList;
import br.ufpa.smartufpa.utils.retrofit.RetrofitClient;
import br.ufpa.smartufpa.utils.retrofit.UserAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = LoginActivity.class.getName();
    private TextInputEditText txtinpEmail;
    private TextInputEditText txtinpPassword;

    private UsersList listOfUsers;

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
//                        Toast.makeText(LoginActivity.this, "Clique", Toast.LENGTH_SHORT).show();
//                        getUsers();
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

    private void attemptLogin(final String email,final String password) {

        Call<UsersList> call = retrofitUserByEmail(email);
        call.enqueue(new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                boolean status = response.isSuccessful();
                List<User> usersList = response.body().getUsersList();
                if(status && usersList.size() == 1){
                    Log.i(TAG, "onResponse: "+ response.body());
                    if(passwordMatches(usersList, password)){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Call<UsersList> retrofitUserByEmail(String email) {
        Retrofit retrofitClient = RetrofitClient.getInstance(this);
        UserAPI userAPI = retrofitClient.create(UserAPI.class);
        return userAPI.userByEmail(email);
    }

    private boolean passwordMatches(List<User> usersList, String password) {
        return usersList.get(0).getPassword().matches(password);
    }
}
