package br.ufpa.smartufpa.activities.about;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import br.ufpa.smartufpa.R;

public class AboutCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_code);
        final TextView txtOsmDroid = findViewById(R.id.txt_about_osmdroid);
        txtOsmDroid.setClickable(true);
        txtOsmDroid.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView txtOsmBonusPack = findViewById(R.id.about_txt_osmbonuspack);
        txtOsmBonusPack.setClickable(true);
        txtOsmBonusPack.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView txtShowcase = findViewById(R.id.about_txt_showcaseview);
        txtShowcase.setClickable(true);
        txtShowcase.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView txtGson = findViewById(R.id.about_txt_gson);
        txtGson.setClickable(true);
        txtGson.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView txtMaterialDialogs = findViewById(R.id.about_txt_materialdialogs);
        txtMaterialDialogs.setClickable(true);
        txtMaterialDialogs.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
