package com.example.kaeuc.osmapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kaeuc.osmapp.Database.LoginDAO;

/**
 * Created by kaeuc on 10/5/2016.
 */
public class LoginScreenActivity extends AppCompatActivity {
    public static final String CATEGORY_LOGIN = "osmapp.CATEGORY_LOGIN";
    public static final String ACTION_LOGIN = "osmapp.ACTION_LOGIN";

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnCreateProfile;
    private Button btnVisitante;
    private LoginDAO loginDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);

        // Views
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCreateProfile = (Button) findViewById(R.id.btn_signup);
        btnVisitante = (Button) findViewById(R.id.btn_Visitante);

        // Inicializa o banco de dados dos usuários em um thread para não comprometer a performance
        Runnable r1 = new Runnable() {                 //Thread code

            @Override
            public void run() {
                try {
                    loginDAO = new LoginDAO(LoginScreenActivity.this);
                    Runnable r2 = new Runnable() {
                        @Override
                        public void run() {
                            SQLiteDatabase db = SQLiteDatabase.create(new SQLiteDatabase.CursorFactory() {
                                @Override
                                public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                                    return null;
                                }
                            });
                            loginDAO.onCreate(db);
                        }
                    };
                    runOnUiThread(r2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Thread T = new Thread(r1); //new Thread(<runnable code>);
        T.start();

        // Cria um Listener para os tres botões, e ele identificará qual botão foi clicado pela sua id
        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getId() == btnLogin.getId()){
                    String username = edtUsername.getText().toString();
                    String password = edtPassword.getText().toString();
                    final int checkLogin = loginDAO.checkLogin(username, password);
                    if (checkLogin == 1){
                        Intent intent = new Intent(MapActivity.ACTION_MAP);
                        intent.addCategory(MapActivity.CATEGORY_MAP);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }else if(checkLogin == -1){
                        Toast.makeText(LoginScreenActivity.this, R.string.login_error_user, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginScreenActivity.this, R.string.login_error_noprofile, Toast.LENGTH_LONG).show();
                    }

                }else if(v.getId() == btnCreateProfile.getId()){
                    Intent intent = new Intent(CreateProfileActivity.ACTION_CREATEPROFILE);
                    intent.addCategory(CreateProfileActivity.CATEGORY_CREATEPROFILE);
                    startActivity(intent);
                }
                else if (v.getId() == btnVisitante.getId()) {
                    Intent intent = new Intent(MapActivity.ACTION_MAP);
                    intent.addCategory(MapActivity.CATEGORY_MAP);
                    startActivity(intent);
                }
            }
        };
        btnCreateProfile.setOnClickListener(clickListener);
        btnLogin.setOnClickListener(clickListener);
        btnVisitante.setOnClickListener(clickListener);





    }
}
