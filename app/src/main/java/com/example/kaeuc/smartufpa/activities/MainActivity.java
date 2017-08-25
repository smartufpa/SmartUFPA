package com.example.kaeuc.smartufpa.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.fragments.PlaceDetailsFragment;
import com.example.kaeuc.smartufpa.fragments.SearchResultFragment;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.server.OsmDataRequest;
import com.example.kaeuc.smartufpa.server.OverpassSearchRequest;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.Constants.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.Constants.OverlayTags;
import com.example.kaeuc.smartufpa.utils.Constants.OverpassFilters;
import com.example.kaeuc.smartufpa.utils.SystemServicesManager;
import com.example.kaeuc.smartufpa.utils.showcaseutils.AppTutorial;
import com.example.kaeuc.smartufpa.utils.showcaseutils.ShowcaseHolder;
import com.example.kaeuc.smartufpa.utils.showcaseutils.ToolbarActionItemTarget;
import com.example.kaeuc.smartufpa.utils.showcaseutils.ViewTargets;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OsmDataRequest.OnOsmDataListener,OverpassSearchRequest.OnOverpassListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ACTION_MAIN = "smartufpa.ACTION_MAIN";
    public static final String CATEGORY_MAIN = "smartufpa.CATEGORY_MAIN";
    private final int TUTORIAL_EXECUTED = 1;
    private final int TUTORIAL_NOT_EXECUTED = 0;

    // VIEWS
    private Toolbar mapToolbar;
    private DrawerLayout layoutDrawer;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private FrameLayout bottomSheetContainer;
    private BottomSheetBehavior bottomSheetBehavior;
    private MenuItem searchItem;

    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG,"onCreate()");

        // Encontra as views
        layoutDrawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        mapToolbar = (Toolbar) findViewById(R.id.tb_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        bottomSheetContainer = (FrameLayout) findViewById(R.id.bottom_sheet_container);

        setupToolbar();
        setupDrawer();
        setupMapFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    // REALIZA A BUSCA
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) && SystemServicesManager.isNetworkEnabled(this)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new OverpassSearchRequest(this).execute(query);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, getString(R.string.error_on_connection), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        setupBottomSheetContainer();
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
                this, layoutDrawer, mapToolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                final int state = bottomSheetBehavior.getState();
                if(state  == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        };
        layoutDrawer.addDrawerListener(drawerToggle);

        NavigationView.OnNavigationItemSelectedListener navigationItemListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // TODO: CHECK IF DATA IS IN MEMORY; IF IT'S NOT CHECK INTERNET TO DOWNLOAD
                final int id = item.getItemId();
                if(SystemServicesManager.isNetworkEnabled(getApplicationContext())) {
                    // Guarda a ID do botão clicado
                    OverpassFilters filter;
                    Context context = MainActivity.this;
                    switch (id) {
                        case R.id.nav_copy_service:
                            filter = OverpassFilters.XEROX;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_XEROX)) { // Caso a camada de filtro não esteja ativa, executar a busca
                                progressBar.setVisibility(View.VISIBLE);
                                new OsmDataRequest(context).execute(filter);
                            }
                            break;
                        case R.id.nav_restaurant:
                            filter = OverpassFilters.RESTAURANT;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_RESTAURANT)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new OsmDataRequest(context).execute(filter);
                            }
                            break;
                        case R.id.nav_restroom:
                            filter = OverpassFilters.RESTROOM;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_RESTROOM)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new OsmDataRequest(context).execute(filter);
                            }
                            break;
                        case R.id.nav_bus_route:
                            if (!mapFragment.isLayerEnabled(OverlayTags.BUS_ROUTE)) {
                                mapFragment.enableBusOverlay();
                            }
                            break;
                        case R.id.nav_auditorium:
                            filter = OverpassFilters.AUDITORIUMS;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_AUDITORIUMS)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new OsmDataRequest(context).execute(filter);
                            }
                            break;
                        case R.id.nav_library:
                            filter = OverpassFilters.LIBRARIES;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_LIBRARIES)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new OsmDataRequest(context).execute(filter);
                            }
                            break;
                        case R.id.nav_about:
                            final Intent intent = new Intent(AboutActivity.ACTION_ABOUT);
                            intent.addCategory(AboutActivity.CATEGORY_ABOUT);
                            startActivity(intent);
                            break;


                    }

                }else if(id == R.id.nav_about) {
                    final Intent intent = new Intent(AboutActivity.ACTION_ABOUT);
                    intent.addCategory(AboutActivity.CATEGORY_ABOUT);
                    startActivity(intent);
                }else if(id == R.id.nav_settings) {
                    Toast.makeText(MainActivity.this, "ainda a implementar", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.error_on_connection), Toast.LENGTH_SHORT).show();

                }
                layoutDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        };

        navigationView.setNavigationItemSelectedListener(navigationItemListener);
        drawerToggle.syncState();
    }

    private void setupBottomSheetContainer(){
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Fragment searchFrag = getSupportFragmentManager().findFragmentByTag(SearchResultFragment.FRAGMENT_TAG);
                        if(searchFrag != null){
                            getSupportFragmentManager().beginTransaction()
                                    .remove(searchFrag)
                                    .commit();
                        }else{
                            searchFrag = getSupportFragmentManager().findFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);
                            if(searchFrag != null){
                                getSupportFragmentManager().beginTransaction()
                                        .remove(searchFrag)
                                        .commit();
                            }
                        }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        //Restaura as preferencias gravadas para executar ou não o tutorial
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final int tutorialStatus = sharedPref.getInt(getString(R.string.tutorial_map_executed), TUTORIAL_NOT_EXECUTED);
        if(tutorialStatus == TUTORIAL_NOT_EXECUTED)
            runMapTutorial();


        return true;
    }

    @Override
    public void onBackPressed() {
        layoutDrawer = (DrawerLayout) findViewById(R.id.layout_drawer);
        if (layoutDrawer.isDrawerOpen(GravityCompat.START))
            layoutDrawer.closeDrawer(GravityCompat.START);

        PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) getSupportFragmentManager()
                .findFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);

        SearchResultFragment searchResultFragment = (SearchResultFragment) getSupportFragmentManager()
                .findFragmentByTag(SearchResultFragment.FRAGMENT_TAG);

        // Defines bottomshet behavior
        if(placeDetailsFragment != null && (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED))
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if((placeDetailsFragment != null && searchResultFragment == null ) && (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED))
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if (placeDetailsFragment != null && (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED))
            super.onBackPressed();
        else if(searchResultFragment != null && (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED))
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if (searchResultFragment != null && (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED))
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        if(!layoutDrawer.isDrawerOpen(GravityCompat.START) && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
            super.onBackPressed();



    }

    @Override
    public void onOsmDataResponse(final ArrayList<Place> places, MarkerTypes markersType, OverlayTags overlayTag, int taskStatus) {
        if(taskStatus == Constants.SERVER_RESPONSE_SUCCESS){
            mapFragment.addLayerToMap(places,markersType,overlayTag);
            Toast.makeText(this, getString(R.string.msg_click_marker), Toast.LENGTH_LONG).show();
        }else if(taskStatus == Constants.SERVER_RESPONSE_TIMEOUT){
            Toast.makeText(this, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onOverpassResponse(ArrayList<Place> places, int taskStatus) {
        progressBar.setVisibility(View.GONE);
        if(taskStatus == Constants.SERVER_RESPONSE_SUCCESS){

            MenuItemCompat.collapseActionView(searchItem);
            if(places.size() > 1){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // Open SearchResultFragment
                SearchResultFragment searchResultFrag = SearchResultFragment.newInstance(places);
                mapFragment.addLayerToMap(places,MarkerTypes.DEFAULT,OverlayTags.SEARCH);
                // TODO: CREATE A TRANSITION
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.bottom_sheet_container,searchResultFrag,SearchResultFragment.FRAGMENT_TAG)
                        .commit();
            }else{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                // Open PlaceDetailsFragment
                final MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.FRAGMENT_TAG);
                final Place place = places.get(0);
                PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(place,mapFragment.getUserLocation());
                // TODO: CREATE A TRANSITION
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.bottom_sheet_container,placeDetailsFragment,PlaceDetailsFragment.FRAGMENT_TAG)
                        .commit();
                mapFragment.getMapView().getController().setZoom(18);
                mapFragment.getMapView().getController().animateTo(place.getGeoPoint());
                mapFragment.getMapView().getController().setCenter(place.getGeoPoint());
                mapFragment.addLayerToMap(places, MarkerTypes.DEFAULT, OverlayTags.SEARCH);
            }

        }else{
            Toast.makeText(this, "PLACES IS EMPTY", Toast.LENGTH_SHORT).show();
        }

    }

    private void runMapTutorial(){
        ArrayList<ShowcaseHolder> holders = new ArrayList<>();

        try {
//            holders.add(new ShowcaseHolder(new ToolbarActionItemTarget(toolbar,R.id.action_add_location),
//                    getString(R.string.tutorial_msg_add_location)));
//            holders.add(new ShowcaseHolder(new ViewTarget(findViewById(R.id.fab_bus_location)),
//                    getString(R.string.tutorial_msg_bus_location),
//                    Constants.TUTORIAL_BTN_LEFT));

            final ShowcaseHolder toolbarHolder = new ShowcaseHolder(new ToolbarActionItemTarget(mapToolbar, R.id.action_search),
                    getString(R.string.tutorial_msg_search));
            final ShowcaseHolder drawerMenuHolder = new ShowcaseHolder(ViewTargets.navigationButtonViewTarget(mapToolbar),
                    getString(R.string.tutorial_msg_menu));
            final ShowcaseHolder fabMyLocationHolder = new ShowcaseHolder(new ViewTarget(findViewById(R.id.fab_my_location)),
                    getString(R.string.tutorial_msg_current_location),
                    Constants.TUTORIAL_BTN_LEFT);
            holders.add(toolbarHolder);
            holders.add(drawerMenuHolder);
            holders.add(fabMyLocationHolder);

            new AppTutorial(holders,MainActivity.this);
        } catch (ViewTargets.MissingViewException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.tutorial_map_executed),TUTORIAL_EXECUTED);
        final boolean commit = editor.commit();
        Log.i(TAG + ".tutorial()", String.valueOf(commit));

    }


}
