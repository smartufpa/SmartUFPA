package br.ufpa.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.ViewPagerAdapter;
import br.ufpa.smartufpa.fragments.AddPlaceInfoFragment;
import br.ufpa.smartufpa.fragments.SelectCategoryFragment;

public class AddPlaceActivity extends AppCompatActivity {

    private SelectCategoryFragment selectCategoryFragment;
    private AddPlaceInfoFragment addPlaceInfoFragment;
    public static final String LABEL_LATITUDE = "lat";
    public static final String LABEL_LONGITUDE = "long";

    private Toolbar tbAddPlace;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_place);

        final Intent intent = getIntent();
        selectCategoryFragment = SelectCategoryFragment.newInstance(intent.getDoubleExtra(LABEL_LATITUDE,0),
                                                            intent.getDoubleExtra(LABEL_LONGITUDE,0));
        addPlaceInfoFragment = new AddPlaceInfoFragment();
        tbAddPlace = findViewById(R.id.tb_add_place);
        tbAddPlace.setTitle("Adicionar Novo Local");

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(selectCategoryFragment, "Categoria");
        adapter.addFragment(addPlaceInfoFragment, "Detalhes");
        viewPager.setAdapter(adapter);
    }

}
