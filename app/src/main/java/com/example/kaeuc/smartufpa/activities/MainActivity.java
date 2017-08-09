package com.example.kaeuc.smartufpa.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.customviews.PlaceDetailsBottomSheet;
import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.server.OsmDataRequest;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.MapUtils;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OsmDataRequest.OnOsmDataListener {

    // VIEWS
    private Toolbar mapToolbar;
    private DrawerLayout layoutDrawer;
    private NavigationView navigationView;
    private ProgressBar progressBar;

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Encontra as views
        layoutDrawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        mapToolbar= (Toolbar) findViewById(R.id.tb_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        setupToolbar();
        setupDrawer();
        setupMapFragment();

    }

    private void setupToolbar(){
        mapToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mapToolbar);

    }

    private void setupMapFragment(){
       mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.FRAGMENT_TAG);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
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
                final int id = item.getItemId();
                String filter;
                Context context = MainActivity.this;
                switch (id){
                    case R.id.nav_copy_service:
                        filter = Constants.FILTER_XEROX;
                        if (!mapFragment.isLayerEnabled(filter)) { // Caso a camada de filtro não esteja ativa, executar a busca
                            progressBar.setVisibility(View.VISIBLE);
                            new OsmDataRequest(context).execute(filter);
                        }
                        break;
                    case  R.id.nav_restaurant:
                        filter = Constants.FILTER_RESTAURANT;
                        if (!mapFragment.isLayerEnabled(filter)) {
                            progressBar.setVisibility(View.VISIBLE);
                            new OsmDataRequest(context).execute(filter);
                        }
                        break;
                    case R.id.nav_restroom:
                        filter = Constants.FILTER_RESTROOM;
                        if (!mapFragment.isLayerEnabled(filter)) {
                            progressBar.setVisibility(View.VISIBLE);
                            new OsmDataRequest(context).execute(filter);
                        }
                        break;
                    case R.id.nav_bus_route:
                        filter = Constants.LAYER_BUS_ROUTE;
                        if (!mapFragment.isLayerEnabled(filter)) {
                            mapFragment.enableBusOverlay();
                        }
                        break;
                    case R.id.nav_auditorium:
                        filter = Constants.FILTER_AUDITORIUMS;
                        if(!mapFragment.isLayerEnabled(filter)){
                            progressBar.setVisibility(View.VISIBLE);
                            new OsmDataRequest(context).execute(filter);
                        }
                        break;
                    case R.id.nav_library:
                        filter = Constants.FILTER_LIBRARIES;
                        if (!mapFragment.isLayerEnabled(filter)){
                            progressBar.setVisibility(View.VISIBLE);
                            new OsmDataRequest(context).execute(filter);
                        }
                        break;
                    case R.id.nav_about:
                        final Intent intent = new Intent(AboutActivity.ACTION_ABOUT);
                        intent.addCategory(AboutActivity.CATEGORY_ABOUT);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        break;

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
        layoutDrawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        if (layoutDrawer.isDrawerOpen(GravityCompat.START))
            layoutDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void onOsmDataResponse(List<Place> places, String filter, int taskStatus) {
        if(taskStatus == Constants.SERVER_RESPONSE_SUCCESS){
            mapFragment.addMarkersToMap(places,filter);
        }else if(taskStatus == Constants.SERVER_RESPONSE_TIMEOUT){
            Toast.makeText(this, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
    }
}
