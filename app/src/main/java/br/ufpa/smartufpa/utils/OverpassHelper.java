package br.ufpa.smartufpa.utils;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.models.overpass.OverpassModel;
import br.ufpa.smartufpa.utils.enums.OverpassFilters;
import retrofit2.Response;

/**
 * Created by kaeuc on 19/02/2018.
 */

public class OverpassHelper {

    public static final String LOG_TAG = OverpassHelper.class.getSimpleName();

    private static OverpassHelper instance;

    private Context context;
    private Double northCoordinate;
    private Double eastCoordinate;
    private Double southCoordinate;
    private Double westCoordinate;
    private String mapRegionName;
    private String mapBusRouteName;
    private String mapBusOperator;

    private OverpassHelper(Context applicationContext) {
        this.context = applicationContext;

        // Get map configuration from location_config.properties
        final String westLimit = ConfigHelper.getConfigValue(applicationContext, Constants.CONFIG_WEST_LIMIT);
        final String southLimit = ConfigHelper.getConfigValue(applicationContext, Constants.CONFIG_SOUTH_LIMIT);
        final String eastLimit = ConfigHelper.getConfigValue(applicationContext, Constants.CONFIG_EAST_LIMIT);
        final String northLimit = ConfigHelper.getConfigValue(applicationContext, Constants.CONFIG_NORTH_LIMIT);

        this.westCoordinate = Double.valueOf(westLimit);
        this.southCoordinate = Double.valueOf(southLimit);
        this.eastCoordinate = Double.valueOf(eastLimit);
        this.northCoordinate = Double.valueOf(northLimit);
        this.mapRegionName = ConfigHelper.getConfigValue(applicationContext, Constants.CONFIG_MAP_REGION_NAME);
        this.mapBusOperator = ConfigHelper.getConfigValue(applicationContext, Constants.CONFIG_MAP_BUS_OPERATOR);

    }

    public static synchronized OverpassHelper getInstance(Context applicationContext) {
        if (instance == null)
            return new OverpassHelper(applicationContext);
        return instance;
    }

    public String getOverpassQuery(OverpassFilters filter) {
        String queryString = null;
        switch (filter) {
            case XEROX:
                queryString = this.context.getString(R.string.query_xerox);
                break;
            case FOOD:
                queryString = this.context.getString(R.string.query_food);
                break;
            case RESTROOM:
                queryString = this.context.getString(R.string.query_restroom);
                break;
            case LIBRARIES:
                queryString = this.context.getString(R.string.query_libraries);
                break;
            case AUDITORIUMS:
                queryString = this.context.getString(R.string.query_auditoriums);
                break;
        }
        return String.format(Locale.US, queryString, southCoordinate, westCoordinate, northCoordinate, eastCoordinate);
    }


    public URL getSearchURL(String userQuery) {
        // Cleans all the white spaces on the query
        userQuery = userQuery.replaceAll("\\s+", " ");
        if (Character.isWhitespace(userQuery.charAt(userQuery.length() - 1))) {
            userQuery = userQuery.substring(0, userQuery.length() - 1);
        }

        final String searchNameQuery = this.context.getString(R.string.query_name_search);
        final String formattedQuery = String.format(Locale.US, searchNameQuery, mapRegionName, userQuery);
        final String overpassURL = this.context.getString(R.string.overpass_url);

        URL searchURL = null;
        try {
            searchURL = new URL(overpassURL + URLEncoder.encode(formattedQuery, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return searchURL;
    }

    public URL getBusRouteByNameURL() {
        final String queryBusRoute = this.context.getString(R.string.query_bus_route);

        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String formattedQuery = String.format(Locale.US, queryBusRoute,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate, mapBusOperator);

        final String overpassURL = this.context.getString(R.string.overpass_url);
        URL busRouteURL = null;
        try {
            busRouteURL = new URL(overpassURL + URLEncoder.encode(formattedQuery, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return busRouteURL;
    }
}
