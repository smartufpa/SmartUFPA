package com.example.kaeuc.smartufpa.testes_layout;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kaeuc.smartufpa.R;

public class TesteMainActivity extends AppCompatActivity {
    private Toolbar mapToolbar;
    private DrawerLayout drawerMap;
    private ListView drawerListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remake_drawer_layout);

        setupToolbar();
//        setupMap();
        setupDrawer();


    }


    private void setupToolbar(){
        mapToolbar= (Toolbar) findViewById(R.id.toolbar_map);
        drawerMap = (DrawerLayout) findViewById(R.id.drawer_map);
        mapToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mapToolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerMap, mapToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerMap.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void setupDrawer(){
        String[] drawerOptions = getResources().getStringArray(R.array.left_drawer_options);
        drawerListView = (ListView) findViewById(R.id.left_drawer_list);
        drawerListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, drawerOptions));
    }


}
