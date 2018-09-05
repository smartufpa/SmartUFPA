package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.AddPlaceOptionAdapter;
import br.ufpa.smartufpa.fragments.AddPlaceInfoFragment;
import br.ufpa.smartufpa.fragments.SelectCategoryFragment;
import br.ufpa.smartufpa.models.PlaceCategory;

public class AddPlaceActivity extends AppCompatActivity {

    private SelectCategoryFragment selectCategoryFragment;
    public static final String LABEL_LATITUDE = "lat";
    public static final String LABEL_LONGITUDE = "long";

    private Toolbar tbAddPlace;
    private RecyclerView rvOptions;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_place);
        final Intent intent = getIntent();
        latitude = intent.getDoubleExtra(LABEL_LATITUDE, 0);
        longitude = intent.getDoubleExtra(LABEL_LONGITUDE, 0);

        tbAddPlace = findViewById(R.id.tb_add_place);
        rvOptions = findViewById(R.id.list_add_place_options);

        tbAddPlace.setTitle("Adicionar Novo Local");
        tbAddPlace.setSubtitle("Escolha uma categoria");

        // Create the adapter to the RecyclerView
        final AddPlaceOptionAdapter addPlaceOptionAdapter = new AddPlaceOptionAdapter(getApplicationContext());

        addPlaceOptionAdapter.setOnItemClickListener(new AddPlaceOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ArrayList<PlaceCategory> placeCategories = addPlaceOptionAdapter.getPlaceCategories();
                final PlaceCategory category = placeCategories.get(position);

                switch (position) {
                    case 0:
                        // Building
                        break;
                }

//                Toast.makeText(getContext(), placeCategories.get(position).getName()+ "\n " + latitude + "\n " + longitude, Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().findFragmentById()
            }
        });

        // Attach the adapter to the RecyclerView
        rvOptions.setAdapter(addPlaceOptionAdapter);

        // Create and attach a LayoutManager to the RecyclerView
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvOptions.setLayoutManager(llm);


    }


}
