package com.example.kaeuc.osmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuProfile extends AppCompatActivity {

    private Button btnAluno;
    private Button btnVisitante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_profile);

        btnAluno = (Button) findViewById(R.id.btn_Aluno);
        btnVisitante = (Button) findViewById(R.id.btn_Visitante);


        final View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == btnAluno.getId()) {
                    Intent intent = new Intent(LoginScreenActivity.ACTION_LOGIN);
                    intent.addCategory(LoginScreenActivity.CATEGORY_LOGIN);
                    startActivity(intent);
                } else if (v.getId() == btnVisitante.getId()) {
                    Intent intent = new Intent(MapActivity.ACTION_MAP);
                    intent.addCategory(MapActivity.CATEGORY_MAP);
                    startActivity(intent);
                }
            }
        };
        btnAluno.setOnClickListener(clickListener);
        btnVisitante.setOnClickListener(clickListener);
    }
}
