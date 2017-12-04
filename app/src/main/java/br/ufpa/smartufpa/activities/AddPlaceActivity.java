package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.AddPlaceOptionAdapter;

public class AddPlaceActivity extends AppCompatActivity {

    private RecyclerView rvOptions;

    public static final String LABEL_LATITUDE = "lat";
    public static final String LABEL_LONGITUDE = "long";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        rvOptions = findViewById(R.id.list_add_place_options);

        final Intent intent = getIntent();
        final double latitude = intent.getDoubleExtra(LABEL_LATITUDE, 0);
        final double longitude = intent.getDoubleExtra(LABEL_LONGITUDE, 0);

        // Create the adapter to the RecyclerView
        final AddPlaceOptionAdapter addPlaceOptionAdapter = new AddPlaceOptionAdapter(this);
        addPlaceOptionAdapter.setOnItemClickListener(new AddPlaceOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final String[] placeCategories = addPlaceOptionAdapter.getPlaceCategories();
                Toast.makeText(AddPlaceActivity.this, placeCategories[position]+ "\n " + latitude + "\n " + longitude, Toast.LENGTH_SHORT).show();
            }
        });

        // Attach the adapter to the RecyclerView
        rvOptions.setAdapter(addPlaceOptionAdapter);

        // Create and attach a LayoutManager to the RecyclerView
        RecyclerView.LayoutManager llm = new GridLayoutManager(this, 2);
        rvOptions.setLayoutManager(llm);

        //

    }



}
