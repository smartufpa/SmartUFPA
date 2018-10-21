package br.ufpa.smartufpa.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.about.AboutActivity;
import br.ufpa.smartufpa.asynctasks.FilterSearchTask;
import br.ufpa.smartufpa.asynctasks.SearchQueryTask;
import br.ufpa.smartufpa.asynctasks.interfaces.OnFilterSearchListener;
import br.ufpa.smartufpa.asynctasks.interfaces.OnSearchQueryListener;
import br.ufpa.smartufpa.fragments.MapFragment;
import br.ufpa.smartufpa.fragments.PlaceDetailsFragment;
import br.ufpa.smartufpa.fragments.SearchResultFragment;
import br.ufpa.smartufpa.models.smartufpa.POI;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.FragmentHelper;
import br.ufpa.smartufpa.utils.SystemServicesManager;
import br.ufpa.smartufpa.utils.UIHelper;
import br.ufpa.smartufpa.utils.apptutorial.AppTutorial;
import br.ufpa.smartufpa.utils.apptutorial.ShowcaseHolder;
import br.ufpa.smartufpa.utils.apptutorial.ToolbarActionItemTarget;
import br.ufpa.smartufpa.utils.apptutorial.ViewTargets;
import br.ufpa.smartufpa.utils.enums.MarkerTypes;
import br.ufpa.smartufpa.utils.enums.OverlayTags;
import br.ufpa.smartufpa.utils.enums.OverpassFilters;
import br.ufpa.smartufpa.utils.enums.ServerResponse;


/**
 * Main Activity that holds core functions of the app
 *
 * @author kaeuchoa
 */

public class MainActivity extends AppCompatActivity
        implements OnFilterSearchListener, OnSearchQueryListener {

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
    private FragmentHelper fragmentHelper;
    private Button btnClear;

    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
        layoutDrawer = findViewById(R.id.layout_drawer);
        mapToolbar = findViewById(R.id.tb_main);
        navigationView = findViewById(R.id.nav_view);
        progressBar = findViewById(R.id.progress_bar);
        bottomSheetContainer = findViewById(R.id.bottom_sheet_container);
        btnClear = findViewById(R.id.btn_clear_map);

        fragmentHelper = new FragmentHelper(getSupportFragmentManager());
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
     *
     * @param searchIntent
     */
    private void handleSearchIntent(Intent searchIntent) {
        if (Intent.ACTION_SEARCH.equals(searchIntent.getAction()) && SystemServicesManager.isNetworkEnabled(this)) {
            final String query = searchIntent.getStringExtra(SearchManager.QUERY);
            new SearchQueryTask(this).execute(query);
            showProgressBar();
        } else {
            Toast.makeText(this, getString(R.string.error_no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Requests permissions on versions from Marshmallow and over
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isPermissionGranted()) {
                startActivity(new Intent(MainActivity.this, PermissionCheckActivity.class));
                finish();
            }
        }
        if (!SystemServicesManager.isGPSEnabled(getApplicationContext())) {
            // Doesn't start the main activity, instead asks user to turn on the GPS
            Intent intent = new Intent(NoGpsActivity.ACTION_NO_GPS);
            intent.addCategory(NoGpsActivity.CATEGORY_NO_GPS);
            startActivity(intent);
        }
        setupBottomSheetContainer();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Configuration method for the app toolbar
     */
    private void setupToolbar() {
        mapToolbar.setTitle(getString(R.string.app_name));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mapToolbar.setBackgroundResource(R.drawable.toolbar_shape);
        }
        setSupportActionBar(mapToolbar);
    }

    /**
     * Configuration method for the MapFragment.
     * It will pass up to the fragment the location where the map should be positioned.
     */
    private void setupMapFragment() {
        mapFragment = (MapFragment) fragmentHelper.findFragmentByTag(MapFragment.FRAGMENT_TAG);
        if (mapFragment == null) {
            try {
                mapFragment = MapFragment.newInstance();
                fragmentHelper.loadWithReplace(R.id.frame_map_container, mapFragment, MapFragment.FRAGMENT_TAG);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Configuration method for the app left drawer
     * TODO: refactor
     */
    private void setupDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, layoutDrawer, mapToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Check the state of the bottom sheet so they don't overlap each other on the view
                final int state = bottomSheetBehavior.getState();
                if (state == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_COLLAPSED) {
                    hideBottomSheet();
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
                final boolean networkEnabled = SystemServicesManager.isNetworkEnabled(getApplicationContext());
                if (networkEnabled) {
                    OverpassFilters filter;
                    Context context = MainActivity.this;
                    switch (id) {
                        case R.id.nav_copy_service:
                            filter = OverpassFilters.XEROX;
                            // If the layer is enabled it means the search was performed and there is no need to do it again
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_XEROX)) {
                                showProgressBar();
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_food:
                            filter = OverpassFilters.FOOD;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_FOOD)) {
                                showProgressBar();
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_restroom:
                            filter = OverpassFilters.RESTROOM;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_RESTROOM)) {
                                showProgressBar();
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_bus_route:
                            if (!mapFragment.isLayerEnabled(OverlayTags.BUS_ROUTE)) {
                                showProgressBar();
                                mapFragment.enableBusOverlay();
                            }
                            break;
                        case R.id.nav_auditorium:
                            filter = OverpassFilters.AUDITORIUMS;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_AUDITORIUMS)) {
                                showProgressBar();
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_library:
                            filter = OverpassFilters.LIBRARIES;
                            if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_LIBRARIES)) {
                                showProgressBar();
                                new FilterSearchTask(context).execute(filter);
                            }
                            break;
                        case R.id.nav_about:
                            startAboutActivity();
                            break;
                    }

                } else if (id == R.id.nav_about) {
                    startAboutActivity();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();

                }
                layoutDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        };

        navigationView.setNavigationItemSelectedListener(navigationItemListener);
        drawerToggle.syncState();
    }

    private void startAboutActivity() {
        final Intent intent = new Intent(AboutActivity.ACTION_ABOUT);
        intent.addCategory(AboutActivity.CATEGORY_ABOUT);
        startActivity(intent);
    }


    /**
     * Configuration method for the app bottom sheet which is responsible for
     * showing information about places searched and their details.
     * Serves as a container.
     */
    private void setupBottomSheetContainer() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer);
        hideBottomSheet();

        // Defines behaviors for the bottom sheet
        final BottomSheetCallback sheetCallback = new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        clearBottomSheetFragment();
                        showBtnClear();
                        break;
                    case (BottomSheetBehavior.STATE_EXPANDED):
                        hideBtnClear();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Nothing to do here
            }
        };
        bottomSheetBehavior.setBottomSheetCallback(sheetCallback);
    }

    private void clearBottomSheetFragment() {
        final boolean searchRemoved = fragmentHelper.removeFragmentByTag(SearchResultFragment.FRAGMENT_TAG);
        if (!searchRemoved) {
            fragmentHelper.removeFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);
        }
    }

    private void hideBtnClear() {
        if (btnClear.getVisibility() == View.VISIBLE) {
            btnClear.setVisibility(View.GONE);
        }
    }

    private void showBtnClear() {
        final int btnClearVisibility = btnClear.getVisibility();
        if (btnClearVisibility == View.GONE || btnClearVisibility == View.INVISIBLE) {
            btnClear.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflateMenu(menu);
        // Defines what happens when the search icon is clicked
        setupSearchItem(menu);
        // Restore the preferences stored and decide on whether executing or not the app tutorial
        final int tutorialStatus = checkTutorialStatus();
        if (tutorialStatus == Constants.TUTORIAL_NOT_EXECUTED)
            runAppTutorial();

        return true;
    }

    private int checkTutorialStatus() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(getString(R.string.tutorial_map_executed), Constants.TUTORIAL_NOT_EXECUTED);
    }

    private void setupSearchItem(Menu menu) {
        searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
    }

    private void inflateMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_location:
                mapFragment.addLocationToMap();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        // Defines left drawer behavior when back button is pressed
        closeLeftDrawer();

        /*TODO: refactor*/
        PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) getSupportFragmentManager()
                .findFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);

        SearchResultFragment searchResultFragment = (SearchResultFragment) getSupportFragmentManager()
                .findFragmentByTag(SearchResultFragment.FRAGMENT_TAG);

        // Defines bottom sheet behavior when back button is pressed
        if (placeDetailsFragment != null && isBottomSheetCollapsed())
            hideBottomSheet();
        else {
            if ((placeDetailsFragment != null && searchResultFragment == null) && isBottomSheetExpanded())
                hideBottomSheet();
            else if (placeDetailsFragment != null && isBottomSheetExpanded())
                super.onBackPressed();
            else if (searchResultFragment != null && isBottomSheetCollapsed()) {
                hideBottomSheet();
            } else if (searchResultFragment != null && isBottomSheetExpanded())
                collapseBottomSheet();
        }

        // Defines the specific scenario where the back button should take back to the previous activity

        if (!layoutDrawer.isDrawerOpen(GravityCompat.START) && isBottomSheetHidden())
            super.onBackPressed();

    }

    private boolean isBottomSheetCollapsed() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED;
    }

    private boolean isBottomSheetExpanded() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    private boolean isBottomSheetHidden() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN;
    }

    private void hideBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void closeLeftDrawer() {
        if (layoutDrawer.isDrawerOpen(GravityCompat.START))
            layoutDrawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onFilterSearchResponse(final ArrayList<POI> POIS, final MarkerTypes markersType, final OverlayTags overlayTag, final ServerResponse taskStatus) {
        hideProgressBar();
        if (taskStatus == ServerResponse.SUCCESS) {
            mapFragment.createOverlay(POIS, markersType, overlayTag);
            UIHelper.showToastLong(this, getString(R.string.msg_click_marker));
        } else if (taskStatus == ServerResponse.TIMEOUT) {
            UIHelper.showToastShort(this, getString(R.string.error_server_timeout));
        } else if (taskStatus == ServerResponse.CONNECTION_FAILED) {
            UIHelper.showToastShort(this, getString(R.string.error_connection_failed));
        } else if (taskStatus.equals(ServerResponse.EMPTY_RESPONSE)) {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.dialog_title))
                    .content(R.string.msg_no_filter_results)
                    .positiveText("OK")
                    .show();
        }

    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSearchQueryResponse(ArrayList<POI> POIs, final ServerResponse taskStatus) {
        hideProgressBar();

        if (taskStatus == ServerResponse.SUCCESS) {
            searchItem.collapseActionView();
            expandBottomSheet();
            // The query returned multiple results
            if (POIs.size() > 1) {
                showMultipleResultsFragment(POIs);
            } else {  // The query return a single result
                // Places to pass over PlaceDetailsFragment to present the data
                showSingleResultFragment(POIs.get(0));
            }
            mapFragment.createOverlay(POIs, MarkerTypes.DEFAULT, OverlayTags.SEARCH);

        } else if (taskStatus.equals(ServerResponse.EMPTY_RESPONSE)) {
            // Poup up dialog
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.dialog_title))
                    .content(R.string.msg_no_results_found)
                    .positiveText("OK")
                    .show();

        } else if (taskStatus.equals(ServerResponse.TIMEOUT)) {
            UIHelper.showToastLong(this, getString(R.string.error_server_timeout));
        } else if (taskStatus == ServerResponse.CONNECTION_FAILED) {
            UIHelper.showToastShort(this, getString(R.string.error_connection_failed));
        }

    }

    private void showSingleResultFragment(final POI currentPOI) {
        PlaceDetailsFragment placeDetailsFragment = PlaceDetailsFragment.newInstance(currentPOI);
        fragmentHelper.loadWithReplace(R.id.bottom_sheet_container, placeDetailsFragment, PlaceDetailsFragment.FRAGMENT_TAG);

        // Uses mapFragment methods to utilize map functions
        final MapFragment mapFragment = (MapFragment) fragmentHelper.findFragmentByTag(MapFragment.FRAGMENT_TAG);
        mapFragment.zoomToGeoPoint(currentPOI.getGeoPoint(), 18);
    }

    private void showMultipleResultsFragment(ArrayList<POI> POIS) {
        Log.i(TAG, POIS.toString());
        // Open SearchResultFragment
        SearchResultFragment searchResultFrag = SearchResultFragment.newInstance(POIS);
        fragmentHelper.loadWithReplace(R.id.bottom_sheet_container, searchResultFrag, SearchResultFragment.FRAGMENT_TAG);
    }

    private void collapseBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void expandBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    /**
     * Method to run a tutorial for users on the first time they open the app
     *
     * @see AppTutorial
     */

    // TODO: Adicionar botão para rodar o tutorial no drawer lateral
    private void runAppTutorial() {
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

            new AppTutorial(holders, MainActivity.this);
        } catch (ViewTargets.MissingViewException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.tutorial_map_executed), Constants.TUTORIAL_EXECUTED);
        editor.apply();

    }


}
