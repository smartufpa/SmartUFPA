package com.example.kaeuc.smartufpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.utils.Constants;

public class MainActivity extends AppCompatActivity {

    // VIEWS
    private Toolbar mapToolbar;
    private DrawerLayout layoutDrawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Encontra as views
        mapToolbar= (Toolbar) findViewById(R.id.tb_main);
        layoutDrawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
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

    private void setupDrawer(){
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, layoutDrawer, mapToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        layoutDrawer.addDrawerListener(drawerToggle);
        NavigationView.OnNavigationItemSelectedListener navigationItemListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Guarda a ID do botão clicado
                int id = item.getItemId();
                String filter;
                if (id == R.id.nav_copy_service) {
                    filter = Constants.FILTER_XEROX;
                    // Caso a camada de filtro não esteja ativa, executar a busca e adicionar marcadores
                    if (!MapFragment.isLayerEnabled(filter)) {
//                new OsmDataRequest(this, progressBar).execute(filter);

                    }
                } else if (id == R.id.nav_restaurant) {
                    filter = Constants.FILTER_RESTAURANT;
                    if (!MapFragment.isLayerEnabled(filter)) {
//                new OsmDataRequest(this, progressBar).execute(filter);

                    }
                } else if (id == R.id.nav_restroom) {
                    filter = Constants.FILTER_RESTROOM;
                    if (!MapFragment.isLayerEnabled(filter)) {
//                new OsmDataRequest(this, progressBar).execute(filter);

                    }
                }else if(id == R.id.nav_bus_route){
//            activeBusRouteLayer();
                }else if(id == R.id.nav_auditorium){
                    filter = Constants.FILTER_AUDITORIUMS;
                    if(!MapFragment.isLayerEnabled(filter)){
//                new OsmDataRequest(this, progressBar).execute(filter);

                    }
                }else if(id == R.id.nav_library){
                    filter = Constants.FILTER_LIBRARIES;
                    if (!MapFragment.isLayerEnabled(filter)){
//                new OsmDataRequest(this, progressBar).execute(filter);
                    }
                }else if(id == R.id.nav_about){
                    final Intent intent = new Intent(AboutActivity.ACTION_ABOUT);
                    intent.addCategory(AboutActivity.CATEGORY_ABOUT);
                    startActivity(intent);
                } else if(id == R.id.nav_settings){

                }
                layoutDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        };
        navigationView.setNavigationItemSelectedListener(navigationItemListener);

        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        layoutDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (layoutDrawer.isDrawerOpen(GravityCompat.START))
            layoutDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

    }

}
