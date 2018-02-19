package br.ufpa.smartufpa.activities;

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

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.about.AboutActivity;
import br.ufpa.smartufpa.asynctasks.FilterSearchTask;
import br.ufpa.smartufpa.asynctasks.SearchQueryTask;
import br.ufpa.smartufpa.asynctasks.interfaces.OnFilterSearchListener;
import br.ufpa.smartufpa.fragments.PlaceDetailsFragment;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.apptutorial.AppTutorial;
import br.ufpa.smartufpa.utils.apptutorial.ShowcaseHolder;
import br.ufpa.smartufpa.utils.apptutorial.ToolbarActionItemTarget;
import br.ufpa.smartufpa.utils.apptutorial.ViewTargets;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.ServerResponse;
import br.ufpa.smartufpa.fragments.MapFragment;
import br.ufpa.smartufpa.fragments.SearchResultFragment;
import br.ufpa.smartufpa.asynctasks.interfaces.OnSearchQueryListener;
import br.ufpa.smartufpa.models.Place;
import br.ufpa.smartufpa.utils.ConfigHelper;
import br.ufpa.smartufpa.utils.SystemServicesManager;
import br.ufpa.smartufpa.utils.enums.OverlayTags;
import br.ufpa.smartufpa.utils.enums.OverpassFilters;

import com.github.amlcurran.showcaseview.targets.ViewTarget;

import com.afollestad.materialdialogs.MaterialDialog;

import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;


/**
 * Stable Commit (20/09)
 * Main Activity that holds core functions of the app
 * @author kaeuchoa
 */

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

        // views
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
        handleSearchIntent(intent);
    }

    /**
     * Method that handles the search intent
     * @param searchIntent
     */
    private void handleSearchIntent(Intent searchIntent) {
        if (Intent.ACTION_SEARCH.equals(searchIntent.getAction()) && SystemServicesManager.isNetworkEnabled(this)) {
            final String query = searchIntent.getStringExtra(SearchManager.QUERY);
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

    /**
     * Configuration method for the app toolbar
     */
    private void setupToolbar(){
        mapToolbar.setTitle(getString(R.string.app_name));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mapToolbar.setBackgroundResource(R.drawable.toolbar_shape);
        }
        setSupportActionBar(mapToolbar);

    }

    /**
     * Configuration method for the MapFragment.
     * It will pass up to the fragment the location where the map should be positioned.
     */

    // TODO: desenvolver função universal de configuração através de um arquivo somente
    private void setupMapFragment(){
       mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.FRAGMENT_TAG);
        if(mapFragment == null){
            try {
                // Get location configs from file
                final Context context = getApplicationContext();
                final String[] defaultPlaceCoord = ConfigHelper.getConfigValue(context, Constants.DEFAULT_PLACE_COORDINATES).split(",");
                final String[] mapRegionBounds = ConfigHelper.getConfigValue(context, Constants.MAP_REGION_BOUNDS).split(",");
                final String defaultPlaceName = ConfigHelper.getConfigValue(context, Constants.MAP_REGION_NAME);

                // Parse information about place
                double lat = Double.valueOf(defaultPlaceCoord[0]);
                double longtd = Double.valueOf(defaultPlaceCoord[1]);
                // Parse information about map bounds
                double north = Double.valueOf(mapRegionBounds[0]);
                double east = Double.valueOf(mapRegionBounds[1]);
                double south = Double.valueOf(mapRegionBounds[2]);
                double west = Double.valueOf(mapRegionBounds[3]);

                // Define variables to pass to the MapFragment
                final BoundingBox mapBounds = new BoundingBox(north,east,south,west);
                final Place chosenLocation = new Place(lat,longtd,defaultPlaceName);
                mapFragment = MapFragment.newInstance(chosenLocation,mapBounds);

                // Loads the fragment
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_map_container,mapFragment,MapFragment.FRAGMENT_TAG);
                ft.commit();

            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }
    }

    /**
     * Configuration method for the app left drawer
     */
    private void setupDrawer(){
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, layoutDrawer, mapToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                // Called when a drawer has settled in a completely open state.
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // Check the state of the bottom sheet so they don't overlap each other on the view
                    final int state = bottomSheetBehavior.getState();
                    if(state  == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_COLLAPSED){
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                }
        };
        layoutDrawer.addDrawerListener(drawerToggle);

        // Defines the actions for each item on the list
        NavigationView.OnNavigationItemSelectedListener navigationItemListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // TODO (OFFLINE FUNCTIONS): CHECK IF DATA IS IN MEMORY; IF IT'S NOT CHECK INTERNET TO DOWNLOAD
                final int id = item.getItemId();
                if(SystemServicesManager.isNetworkEnabled(getApplicationContext())) {
                    OverpassFilters filter;
                    Context context = MainActivity.this;
                    switch (id) {
                        case R.id.nav_copy_service:
                            filter = OverpassFilters.XEROX;
                            // If the layer is enabled it means the search was performed and there is no need to do it again
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_XEROX)) {
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


    /**
     * Configuration method for the app bottom sheet which is responsible for
     * showing information about places searched and their details.
     * Serves as a container.
     */
    private void setupBottomSheetContainer(){
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        // The clear button should not be visible when the bottom sheet is shown
        final Button btnClear = findViewById(R.id.btn_clear_map);
        // Defines behaviors for the bottom sheet
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
                // Nothing to do here
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        // Defines what happens when the search icon is clicked
        searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        // Restore the preferes stored and decide on whether executing or not the app tutorial
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final int tutorialStatus = sharedPref.getInt(getString(R.string.tutorial_map_executed), Constants.TUTORIAL_NOT_EXECUTED);
        if(tutorialStatus == Constants.TUTORIAL_NOT_EXECUTED)
            runAppTutorial();


        return true;
    }

    @Override
    public void onBackPressed() {
        // Defines left drawer behavior when back button is pressed
        layoutDrawer = findViewById(R.id.layout_drawer);
        if (layoutDrawer.isDrawerOpen(GravityCompat.START))
            layoutDrawer.closeDrawer(GravityCompat.START);

        PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) getSupportFragmentManager()
                .findFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);

        SearchResultFragment searchResultFragment = (SearchResultFragment) getSupportFragmentManager()
                .findFragmentByTag(SearchResultFragment.FRAGMENT_TAG);

        // Defines bottom sheet behavior when back button is pressed
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

        // Defines the specific scenario where the back button should take back to the previous activity
        if(!layoutDrawer.isDrawerOpen(GravityCompat.START) && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
            super.onBackPressed();

    }


    @Override
    public void onFilterSearchResponse(final ArrayList<Place> places, final MarkerTypes markersType, final OverlayTags overlayTag, final ServerResponse taskStatus) {
        progressBar.setVisibility(View.INVISIBLE);
        if(taskStatus == ServerResponse.SUCCESS){
            mapFragment.createOverlay(places,markersType,overlayTag);
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
                mapFragment.createOverlay(places,MarkerTypes.DEFAULT,OverlayTags.SEARCH);
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

                // Open PlaceDetailsFragment
                PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(currentPlace);
                // TODO (VISUAL ADJUSTMENTS): CREATE A TRANSITION
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.bottom_sheet_container,placeDetailsFragment,PlaceDetailsFragment.FRAGMENT_TAG)
                        .commit();

                // Uses mapFragment methods to utilize map functions
                final MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.FRAGMENT_TAG);
                mapFragment.zoomToGeoPoint(currentPlace.getGeoPoint(),18);
                mapFragment.createOverlay(places, MarkerTypes.DEFAULT, OverlayTags.SEARCH);
            }

        }else if(taskStatus.equals(ServerResponse.EMPTY_RESPONSE)){
            // Poup up dialog
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


    /**
     * Method to run a tutorial for users on the first time they open the app
     * @see AppTutorial
     */

    // TODO: Adicionar botão para rodar o tutorial no drawer lateral
    private void runAppTutorial(){
        ArrayList<ShowcaseHolder> holders = new ArrayList<>();

        try {
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
