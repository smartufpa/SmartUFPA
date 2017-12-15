package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.fragments.SelectCategoryFragment;

public class AddPlaceActivity extends AppCompatActivity {

    private SelectCategoryFragment selectCategoryFragment;
    public static final String LABEL_LATITUDE = "lat";
    public static final String LABEL_LONGITUDE = "long";

    private Toolbar tbAddPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_place);
        final Intent intent = getIntent();
        selectCategoryFragment = SelectCategoryFragment.newInstance(intent.getDoubleExtra(LABEL_LATITUDE,0),
                                                            intent.getDoubleExtra(LABEL_LONGITUDE,0));
        tbAddPlace = findViewById(R.id.tb_add_place);
        if(selectCategoryFragment != null){
            tbAddPlace.setTitle("Adicionar Novo Local");
            tbAddPlace.setSubtitle("Escolha uma categoria");
        }

        // Loads the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_add_place_container, selectCategoryFragment, SelectCategoryFragment.FRAGMENT_TAG);
        ft.commit();


    }



}
