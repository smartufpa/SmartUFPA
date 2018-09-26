package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.fragments.AddBuildingFragment;
import br.ufpa.smartufpa.fragments.NewPlaceFragment;
import br.ufpa.smartufpa.models.PlaceCategory;

public class NewPlaceActivity extends AppCompatActivity implements NewPlaceFragment.onNewPlaceListener {

    public static final String TAG = NewPlaceActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final PlaceCategory.Categories category = (PlaceCategory.Categories) (bundle != null ? bundle.get(SelectCategoryActivity.KEY_CATEGORY) : null);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (category != null) {
            setContentView(R.layout.activity_new_place);
            switch (category) {
                case BUILDING:
                    final AddBuildingFragment addBuildingFragment = new AddBuildingFragment();
                    addBuildingFragment.setArguments(bundle);
                    ft.replace(R.id.frame_new_place, addBuildingFragment, AddBuildingFragment.TAG);
                    break;
                case FOODPLACE:
                    Toast.makeText(this, getString(R.string.holder_implement), Toast.LENGTH_SHORT).show();
                    break;
            }
            ft.commit();
        } else {
            setContentView(R.layout.no_category_layout);
        }
    }

    @Override
    public void getFormJSON(JSONObject jsonObject) {
        // TODO enviar para o servidor
        Log.i(TAG, "getFormJSON: " + jsonObject);
    }
}
