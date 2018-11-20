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
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;

import java.util.ArrayList;
import java.util.List;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.activities.about.AboutActivity;
import br.ufpa.smartufpa.activities.ui.BottomSheetController;
import br.ufpa.smartufpa.fragments.MapFragment;
import br.ufpa.smartufpa.fragments.PlaceDetailsFragment;
import br.ufpa.smartufpa.fragments.SearchResultFragment;
import br.ufpa.smartufpa.interfaces.PlaceDetailsDelegate;
import br.ufpa.smartufpa.models.overpass.Element;
import br.ufpa.smartufpa.models.overpass.OverpassModel;
import br.ufpa.smartufpa.models.retrofit.OverpassApi;
import br.ufpa.smartufpa.models.smartufpa.POI;
import br.ufpa.smartufpa.utils.Constants;
import br.ufpa.smartufpa.utils.osm.ElementParser;
import br.ufpa.smartufpa.utils.FragmentHelper;
import br.ufpa.smartufpa.utils.osm.OverpassQueryHelper;
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
import br.ufpa.smartufpa.utils.retrofit.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Main Activity that holds core functions of the app
 *
 * @author kaeuchoa
 */

public class MainActivity extends AppCompatActivity implements Callback<OverpassModel>, PlaceDetailsDelegate {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ACTION_MAIN = "smartufpa.ACTION_MAIN";
    public static final String CATEGORY_MAIN = "smartufpa.CATEGORY_MAIN";


    // VIEWS
    private Toolbar mapToolbar;
    private DrawerLayout drawerMenu;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private MenuItem searchItem;
    private Button btnClear;

    private MapFragment mapFragment;
    private IMapController mapCamera;
    private OverpassApi overpassApi;
    private BottomSheetController bottomSheetController;

    private OverpassQueryHelper overpassQueryHelper;
    private FragmentHelper fragmentHelper;
    private final ElementParser elementParser = ElementParser.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // views
        drawerMenu = findViewById(R.id.layout_drawer);
        mapToolbar = findViewById(R.id.tb_main);
        navigationView = findViewById(R.id.nav_view);
        progressBar = findViewById(R.id.progress_bar);
        btnClear = findViewById(R.id.btn_clear_map);

        fragmentHelper = new FragmentHelper(getSupportFragmentManager());
        setupToolbar();
        setupDrawer();
        setupMapFragment();
        overpassApi = RetrofitHelper.INSTANCE.getOverpassApi();
        overpassQueryHelper = OverpassQueryHelper.getInstance(this);
        bottomSheetController = new BottomSheetController(getWindow().getDecorView(), fragmentHelper);

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
            final String userQuery = searchIntent.getStringExtra(SearchManager.QUERY);
            final String overpassQuery = overpassQueryHelper.getSearchQuery(userQuery);
            if(overpassQuery != null){
                Call<OverpassModel> call = overpassApi.getData(overpassQuery);
                call.enqueue(this);
            }
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
        mapCamera = mapFragment.getMapCamera();

    }

    /**
     * Configuration method for the app left drawer
     */
    private void setupDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerMenu, mapToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Check the state of the bottom sheet so they don't overlap each other on the view
                if (bottomSheetController.isVisible()) {
                    bottomSheetController.hide();
                }
            }
        };
        drawerMenu.addDrawerListener(drawerToggle);

        // Defines the actions for each item on the list
        NavigationView.OnNavigationItemSelectedListener navigationItemListener = initNavigationListener();

        navigationView.setNavigationItemSelectedListener(navigationItemListener);
        drawerToggle.syncState();
    }

    @NonNull
    private NavigationView.OnNavigationItemSelectedListener initNavigationListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // TODO (OFFLINE FUNCTIONS): CHECK IF DATA IS IN MEMORY; IF IT'S NOT CHECK INTERNET TO DOWNLOAD
                    final int id = item.getItemId();
                    Call<OverpassModel> call = null;
                    String query = null;
                    final boolean networkEnabled = SystemServicesManager.isNetworkEnabled(getApplicationContext());
                    if (networkEnabled) {
                        showProgressBar();
                        if (id == R.id.nav_about) {
                            startAboutActivity();
                        } else if (id == R.id.nav_bus_route) {
                            if (!mapFragment.isLayerEnabled(OverlayTags.BUS_ROUTE))
                                mapFragment.enableBusOverlay();
                        } else {
                            query = getFilterQuery(id);
                            if (query != null) {
                                call = overpassApi.getData(query);
                                call.enqueue(MainActivity.this);
                            }
                        }
                    } else {
                        UIHelper.showToastShort(MainActivity.this, getString(R.string.error_no_internet_connection));
                    }
                    drawerMenu.closeDrawer(GravityCompat.START);
                    return true;
                }
            };
    }

    private String getFilterQuery(int id) {
        // If the layer is enabled it means the search was performed return null
        switch (id) {
            case R.id.nav_copy_service:
                if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_XEROX))
                    return overpassQueryHelper.getOverpassQuery(OverpassFilters.XEROX);
            case R.id.nav_food:
                if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_FOOD))
                    return overpassQueryHelper.getOverpassQuery(OverpassFilters.FOOD);
            case R.id.nav_restroom:
                if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_RESTROOM))
                    return overpassQueryHelper.getOverpassQuery(OverpassFilters.RESTROOM);
            case R.id.nav_auditorium:
                if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_AUDITORIUMS))
                    return overpassQueryHelper.getOverpassQuery(OverpassFilters.AUDITORIUMS);
            case R.id.nav_library:
                if (!mapFragment.isLayerEnabled(OverlayTags.FILTER_LIBRARIES))
                    return overpassQueryHelper.getOverpassQuery(OverpassFilters.LIBRARIES);
        }
        return null;
    }

    private void startAboutActivity() {
        final Intent intent = new Intent(AboutActivity.ACTION_ABOUT);
        intent.addCategory(AboutActivity.CATEGORY_ABOUT);
        startActivity(intent);
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
        if (drawerMenu.isDrawerOpen(GravityCompat.START)) {
            drawerMenu.closeDrawer(GravityCompat.START);
            return;
        }

        // Defines bottomsheet behavior
        if (bottomSheetController.isVisible()) {
            // TODO
//            final FragmentManager fragmentManager = getSupportFragmentManager();
//            final PlaceDetailsFragment fragmentByTag = (PlaceDetailsFragment) fragmentManager.findFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);
//            if (fragmentByTag == null) {
//                bottomSheetController.hide();
//            }
//            return;
        }

        super.onBackPressed();
    }


    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
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


    @Override
    public void onResponse(Call<OverpassModel> call, Response<OverpassModel> response) {
        hideProgressBar();
        final int code = response.code();
        switch (code) {
            case 400:
                UIHelper.showToastShort(this, getString(R.string.error_server_internal_error));
                break;
            case 200:
                filterResponse(response.body());
        }

    }

    private void filterResponse(OverpassModel overpassModel) {
        if (overpassModel != null) {
            final List<Element> elements = overpassModel.getElements();
            final int numberOfResults = elements.size();
            switch (numberOfResults) {
                case 0:
                    UIHelper.showToastShort(this, getString(R.string.msg_no_filter_results));
                    break;
                default:
                    showSearchResultFragment(elements);
            }
        }
    }


    private void showSearchResultFragment(List<Element> elements) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        SearchResultFragment searchResultFragment = (SearchResultFragment) fragmentManager.findFragmentByTag(SearchResultFragment.FRAGMENT_TAG);
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if(searchResultFragment != null){
            searchResultFragment.updateResults(elements);
        }else{
            searchResultFragment = SearchResultFragment.newInstance(elements);
            ft.add(R.id.containerBottomsheet, searchResultFragment, SearchResultFragment.FRAGMENT_TAG)
                    .commit();
        }

        bottomSheetController.setTitle(getString(R.string.search_result_title));
        bottomSheetController.setSubTitle(getString(R.string.search_result_subtitle));
        bottomSheetController.setExtraInfo(String.format(getString(R.string.search_result_extra), elements.size()));
        bottomSheetController.expand();


    }

    @Override
    public void showPlaceDetailsFragment(@NotNull Element element) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) fragmentManager
                .findFragmentByTag(PlaceDetailsFragment.FRAGMENT_TAG);

        if (placeDetailsFragment == null)
            placeDetailsFragment = PlaceDetailsFragment.newInstance(element);

        final String name = elementParser.getName(element);
        final String localName = elementParser.getLocalName(element);
        final String shortName = elementParser.getShortName(element);

        initPlaceDetailsTitle(name);
        initPlaceDetailsSubtitle(localName);
        initPlaceDetailsExtra(shortName);

        fragmentManager.beginTransaction()
                .replace(R.id.containerBottomsheet, placeDetailsFragment, PlaceDetailsFragment.FRAGMENT_TAG)
                .addToBackStack(PlaceDetailsFragment.ARG_ELEMENT)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit();
        bottomSheetController.expand();

    }

    private void initPlaceDetailsExtra(String shortName) {
        if (shortName != null) {
            bottomSheetController.setExtraInfo(String.format("(%s)", shortName));
        } else {
            bottomSheetController.setExtraInfo("");
        }
    }

    private void initPlaceDetailsSubtitle(String localName) {
        if (localName != null) {
            bottomSheetController.setSubTitle(localName);
        } else {
            bottomSheetController.setSubTitle("");
        }
    }

    private void initPlaceDetailsTitle(String name) {
        if (name != null) {
            bottomSheetController.setTitle(name);
        } else {
            bottomSheetController.setTitle(getString(R.string.place_holder_no_name));
        }
    }


    @Override
    public void onFailure(Call<OverpassModel> call, Throwable t) {
        UIHelper.showToastShort(this, "Erro ao recuperar dados do servidor do OSM.");
        Log.e(TAG,"Erro ao recuperar dados", t);
        hideProgressBar();
    }


}
