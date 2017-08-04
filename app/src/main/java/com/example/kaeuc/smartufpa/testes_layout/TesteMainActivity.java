package com.example.kaeuc.smartufpa.testes_layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.fragments.MapFragment;

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class TesteMainActivity extends AppCompatActivity {

    // VIEWS
    private Toolbar mapToolbar;
    private DrawerLayout layoutDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remake_drawer_layout);
        // Encontra as views
        mapToolbar= (Toolbar) findViewById(R.id.tb_main);
        layoutDrawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        setupToolbar();
        setupDrawer();
        setupMapFragment();

    }


    private void setupToolbar(){
        mapToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mapToolbar);

    }

    private void setupMapFragment(){
        Fragment mapFragment = getSupportFragmentManager().findFragmentByTag(MapFragment.FRAGMENT_TAG);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance(null,null);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_map_container,mapFragment,MapFragment.FRAGMENT_TAG);
            ft.commit();

        }

    }


    // TODO: Click events
    private void setupDrawer(){
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, layoutDrawer, mapToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        layoutDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


}
