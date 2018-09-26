package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.SelectCategoryAdapter;
import br.ufpa.smartufpa.models.PlaceCategory;

public class SelectCategoryActivity extends AppCompatActivity {
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_LONGITUDE = "long";
    public static final String KEY_CATEGORY = "category";
    public static final String TAG = SelectCategoryActivity.class.getName();

    private RecyclerView rvOptions;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_category);
        final Intent intent = getIntent();
        latitude = intent.getDoubleExtra(KEY_LATITUDE, 0);
        longitude = intent.getDoubleExtra(KEY_LONGITUDE, 0);

        rvOptions = findViewById(R.id.list_add_place_options);

        // Create the adapter to the RecyclerView
        final SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(getApplicationContext());

        selectCategoryAdapter.setOnItemClickListener(new SelectCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ArrayList<PlaceCategory> placeCategories = selectCategoryAdapter.getPlaceCategories();
                final PlaceCategory category = placeCategories.get(position);

                Log.i(TAG, "Categoria escolhida: " + category.getName());

                Intent intent = new Intent(getApplicationContext(), NewPlaceActivity.class);
                intent.putExtra(SelectCategoryActivity.KEY_LATITUDE,latitude);
                intent.putExtra(SelectCategoryActivity.KEY_LONGITUDE,longitude);
                intent.putExtra(SelectCategoryActivity.KEY_CATEGORY, category.getCategory());
                startActivity(intent);

            }
        });

        // Attach the adapter to the RecyclerView
        rvOptions.setAdapter(selectCategoryAdapter);

        // Create and attach a LayoutManager to the RecyclerView
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvOptions.setLayoutManager(llm);


    }


}
