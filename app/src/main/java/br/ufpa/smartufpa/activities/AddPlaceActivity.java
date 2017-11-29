package br.ufpa.smartufpa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.AddPlaceOptionAdapter;

public class AddPlaceActivity extends AppCompatActivity {

    private RecyclerView rvOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        rvOptions = findViewById(R.id.list_add_place_options);

        // Create the adapter to the RecyclerView
        AddPlaceOptionAdapter addPlaceOptionAdapter = new AddPlaceOptionAdapter(this);
        addPlaceOptionAdapter.setOnItemClickListener(new AddPlaceOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        // Attach the adapter to the RecyclerView
        rvOptions.setAdapter(addPlaceOptionAdapter);

        // Create and attach a LayoutManager to the RecyclerView
        RecyclerView.LayoutManager llm = new GridLayoutManager(this, 2);
        rvOptions.setLayoutManager(llm);

    }



}
