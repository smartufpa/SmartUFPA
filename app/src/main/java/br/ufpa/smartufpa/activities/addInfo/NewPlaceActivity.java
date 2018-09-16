package br.ufpa.smartufpa.activities.addInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.SelectCategoryActivity;
import br.ufpa.smartufpa.models.PlaceCategory;

public class NewPlaceActivity extends AppCompatActivity {
    public static final String TAG = NewPlaceActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();

        final Bundle bundle = intent.getExtras();
        final PlaceCategory.Categories category = (PlaceCategory.Categories) (bundle != null ? bundle.get(SelectCategoryActivity.KEY_CATEGORY) : null);
        final double latitude = bundle != null ? bundle.getDouble(SelectCategoryActivity.KEY_LATITUDE) : 0;
        final double lontitude = bundle != null ? bundle.getDouble(SelectCategoryActivity.KEY_LONGITUDE) : 0;

        if (category != null) {
            switch (category) {
                case BUILDING:// TODO carregar fragmento
                    Toast.makeText(this, "Fragmento Building", Toast.LENGTH_SHORT).show();
                    break;
                case FOODPLACE:
                    Toast.makeText(this, "Fragmento Foodplace", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else{
            setContentView(R.layout.no_category_layout);
        }


    }

}
