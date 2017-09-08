package com.example.kaeuc.smartufpa.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.R;
import com.example.kaeuc.smartufpa.fragments.MapFragment;
import com.example.kaeuc.smartufpa.fragments.PlaceDetailsFragment;
import com.example.kaeuc.smartufpa.fragments.SearchResultFragment;
import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnFilterSearchListener;
import com.example.kaeuc.smartufpa.asynctasks.interfaces.OnSearchQueryListener;
import com.example.kaeuc.smartufpa.models.Place;
import com.example.kaeuc.smartufpa.asynctasks.FilterSearchTask;
import com.example.kaeuc.smartufpa.asynctasks.SearchQueryTask;
import com.example.kaeuc.smartufpa.utils.ConfigHelper;
import com.example.kaeuc.smartufpa.utils.Constants;
import com.example.kaeuc.smartufpa.utils.SystemServicesManager;
import com.example.kaeuc.smartufpa.utils.enums.MarkerTypes;
import com.example.kaeuc.smartufpa.utils.enums.OverlayTags;
import com.example.kaeuc.smartufpa.utils.enums.OverpassFilters;
import com.example.kaeuc.smartufpa.utils.enums.ServerResponse;
import com.example.kaeuc.smartufpa.utils.apptutorial.AppTutorial;
import com.example.kaeuc.smartufpa.utils.apptutorial.ShowcaseHolder;
import com.example.kaeuc.smartufpa.utils.apptutorial.ToolbarActionItemTarget;
import com.example.kaeuc.smartufpa.utils.apptutorial.ViewTargets;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import com.afollestad.materialdialogs.MaterialDialog;

import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity
        implements OnFilterSearchListener,OnSearchQueryListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ACTION_MAIN = "smartufpa.ACTION_MAIN";
    public static final String CATEGORY_MAIN = "smartufpa.CATEGORY_MAIN";


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


        // Encontra as views
        layoutDrawer =  findViewById(R.id.layout_drawer);
        mapToolbar =  findViewById(R.id.tb_main);
        navigationView =  findViewById(R.id.nav_view);
        progressBar =  findViewById(R.id.progress_bar);
        bottomSheetContainer = findViewById(R.id.bottom_sheet_container);

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
            new SearchQueryTask(this).execute(query);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, getString(R.string.error_no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setupBottomSheetContainer();
    }

    private void setupToolbar(){
        mapToolbar.setTitle(getString(R.string.app_name));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mapToolbar.setBackgroundResource(R.drawable.toolbar_shape);
        }
        setSupportActionBar(mapToolbar);

    }

    private void setupMapFragment(){
       mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.FRAGMENT_TAG);
        if(mapFragment == null){
            try {
                // Get location configs from file
                final String[] defaultPlaceCoord = ConfigHelper.getConfigValue(this, Constants.DEFAULT_PLACE_COORDINATES).split(",");
                final String[] mapRegionBounds = ConfigHelper.getConfigValue(this, Constants.MAP_REGION_BOUNDS).split(",");
                final String defaultPlaceName = ConfigHelper.getConfigValue(this, Constants.DEFAULT_PLACE_NAME);


                // Parse information about place
                double lat = Double.valueOf(defaultPlaceCoord[0]);
                double longtd = Double.valueOf(defaultPlaceCoord[1]);
                // Parse information about map bounds
                double north = Double.valueOf(mapRegionBounds[0]);
                double east = Double.valueOf(mapRegionBounds[1]);
                double south = Double.valueOf(mapRegionBounds[2]);
                double west = Double.valueOf(mapRegionBounds[3]);

                // define variables to pass to the MapFragment
                final BoundingBox mapBounds = new BoundingBox(north,east,south,west);
                final Place chosenLocation = new Place(lat,longtd,defaultPlaceName);
                mapFragment = MapFragment.newInstance(chosenLocation,mapBounds);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_map_container,mapFragment,MapFragment.FRAGMENT_TAG);
                ft.commit();


            }catch (NullPointerException e){
                e.printStackTrace();
            }


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
                // TODO (OFFLINE FUNCTIONS): CHECK IF DATA IS IN MEMORY; IF IT'S NOT CHECK INTERNET TO DOWNLOAD
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
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_food:
                            filter = OverpassFilters.FOOD;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_FOOD)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_restroom:
                            filter = OverpassFilters.RESTROOM;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_RESTROOM)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_bus_route:
                            if (!mapFragment.isLayerEnabled(OverlayTags.BUS_ROUTE)) {
                                progressBar.setVisibility(View.VISIBLE);
                                mapFragment.enableBusOverlay();
                            }
                            break;
                        case R.id.nav_auditorium:
                            filter = OverpassFilters.AUDITORIUMS;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_AUDITORIUMS)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_library:
                            filter = OverpassFilters.LIBRARIES;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_LIBRARIES)) {
                                progressBar.setVisibility(View.VISIBLE);
                                new FilterSearchTask(context).execute(filter);
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
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();

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
        final Button btnClear = findViewById(R.id.btn_clear_map);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SearchResultFragment.FRAGMENT_TAG);
                        if(fragment != null){
                            btnClear.setVisibility(View.VISIBLE);
                            getSupportFragmentManager().beginTransaction()
                                    .remove(fragment)
                                    .commit();
                        }else{
                            fragment = getSupportFragmentManager().findFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);
                            if(fragment != null){
                                btnClear.setVisibility(View.VISIBLE);
                                getSupportFragmentManager().beginTransaction()
                                        .remove(fragment)
                                        .commit();
                            }
                        }
                        break;
                    case (BottomSheetBehavior.STATE_EXPANDED):
                        if(btnClear.getVisibility() == View.VISIBLE){
                            btnClear.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        // Restore the preferes stored and decide on executing or not the app tutorial
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final int tutorialStatus = sharedPref.getInt(getString(R.string.tutorial_map_executed), Constants.TUTORIAL_NOT_EXECUTED);
        if(tutorialStatus == Constants.TUTORIAL_NOT_EXECUTED)
            runAppTutorial();


        return true;
    }

    @Override
    public void onBackPressed() {
        layoutDrawer = findViewById(R.id.layout_drawer);
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
    public void onFilterSearchResponse(final ArrayList<Place> places, final MarkerTypes markersType, final OverlayTags overlayTag, final ServerResponse taskStatus) {
        if(taskStatus == ServerResponse.SUCCESS){
            mapFragment.createLayerToMap(places,markersType,overlayTag);
            Toast.makeText(this, getString(R.string.msg_click_marker), Toast.LENGTH_LONG).show();
        }else if(taskStatus == ServerResponse.TIMEOUT){
            Toast.makeText(this, getString(R.string.error_server_timeout), Toast.LENGTH_SHORT).show();
        }else if (taskStatus == ServerResponse.CONNECTION_FAILED){
            Toast.makeText(this, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        }else if(taskStatus.equals(ServerResponse.EMPTY_RESPONSE)) {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.dialog_title))
                    .content(R.string.msg_no_filter_results)
                    .positiveText("OK")
                    .show();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSearchQueryResponse(ArrayList<Place> places, final ServerResponse taskStatus) {
        progressBar.setVisibility(View.INVISIBLE);

        if(taskStatus == ServerResponse.SUCCESS){
            searchItem.collapseActionView();
            // The query returned multiple results
            if(places.size() > 1){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                // Open SearchResultFragment
                SearchResultFragment searchResultFrag = SearchResultFragment.newInstance(places);
                mapFragment.createLayerToMap(places,MarkerTypes.DEFAULT,OverlayTags.SEARCH);
                // TODO (VISUAL ADJUSTMENTS): CREATE A TRANSITION
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.bottom_sheet_container,searchResultFrag,SearchResultFragment.FRAGMENT_TAG)
                        .commit();
            // The query return a single result
            }else{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                // Places to pass over PlaceDetailsFragment to present the data
                final Place currentPlace = places.get(0);
                final Place userLocation = mapFragment.getUserLocation();

                // Open PlaceDetailsFragment
                PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(currentPlace, userLocation);
                // TODO (VISUAL ADJUSTMENTS): CREATE A TRANSITION
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.bottom_sheet_container,placeDetailsFragment,PlaceDetailsFragment.FRAGMENT_TAG)
                        .commit();

                // Uses mapFragment methods to utilize map functions
                final MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.FRAGMENT_TAG);
                mapFragment.zoomToGeoPoint(currentPlace.getGeoPoint(),18);
                mapFragment.createLayerToMap(places, MarkerTypes.DEFAULT, OverlayTags.SEARCH);
            }

        }else if(taskStatus.equals(ServerResponse.EMPTY_RESPONSE)){
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.dialog_title))
                    .content(R.string.msg_no_results_found)
                    .positiveText("OK")
                    .show();
        }else if(taskStatus.equals(ServerResponse.TIMEOUT)){
            Toast.makeText(this, getString(R.string.error_server_timeout), Toast.LENGTH_LONG).show();
        } else if (taskStatus == ServerResponse.CONNECTION_FAILED){
            Toast.makeText(this, R.string.error_connection_failed, Toast.LENGTH_SHORT).show();
        }

    }



    private void runAppTutorial(){
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
        editor.putInt(getString(R.string.tutorial_map_executed),Constants.TUTORIAL_EXECUTED);
        editor.apply();

    }



}
