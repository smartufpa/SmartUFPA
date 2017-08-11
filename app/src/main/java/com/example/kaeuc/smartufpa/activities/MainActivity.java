package com.example.kaeuc.smartufpa.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.fragments.SearchResultFragment;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.server.OsmDataRequest;
import com.example.kaeuc.smartufpa.server.OverpassSearchRequest;
import com.example.kaeuc.smartufpa.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OsmDataRequest.OnOsmDataListener,OverpassSearchRequest.OnOverpassListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String ACTION_MAIN = "smartufpa.ACTION_MAIN";
    public static final String CATEGORY_MAIN = "smartufpa.CATEGORY_MAIN";
    // VIEWS
    private Toolbar mapToolbar;
    private DrawerLayout layoutDrawer;
    private NavigationView navigationView;
    private ProgressBar progressBar;

    private View bottomSheetContainer;

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG,"onCreate()");

        // Encontra as views
        layoutDrawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        mapToolbar= (Toolbar) findViewById(R.id.tb_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        bottomSheetContainer = findViewById(R.id.bottom_sheet);
        setupToolbar();
        setupDrawer();
        setupMapFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new OverpassSearchRequest(this).execute(query);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
        setupSearchResultBottomSheet();
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

    private void setupSearchResultBottomSheet(){
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetContainer);
        // ADICIONAR LISTENERS


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        return true;
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

    @Override
    public void onOverpassResponse(ArrayList<Place> places, int taskStatus) {
        progressBar.setVisibility(View.GONE);
        if(taskStatus == Constants.SERVER_RESPONSE_SUCCESS){
            bottomSheetContainer.setVisibility(View.VISIBLE);
            SearchResultFragment searchResultFrag = (SearchResultFragment) getSupportFragmentManager().findFragmentByTag(SearchResultFragment.FRAGMENT_TAG);
            if(searchResultFrag == null){
                searchResultFrag = SearchResultFragment.newInstance(places,null);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.bottom_sheet_container,searchResultFrag,SearchResultFragment.FRAGMENT_TAG);
                ft.commit();
            }
        }

    }


}
