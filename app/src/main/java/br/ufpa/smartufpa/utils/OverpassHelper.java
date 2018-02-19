package br.ufpa.smartufpa.utils;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import br.ufpa.smartufpa.R;

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

    private OverpassHelper(Context context) {
        this.context = context;
        String[] mapRegionBounds = ConfigHelper.getConfigValue(context,Constants.MAP_REGION_BOUNDS).split(",");
        this.northCoordinate = Double.valueOf(mapRegionBounds[0]);
        this.eastCoordinate = Double.valueOf(mapRegionBounds[1]);
        this.southCoordinate = Double.valueOf(mapRegionBounds[2]);
        this.westCoordinate = Double.valueOf(mapRegionBounds[3]);
        this.mapRegionName = ConfigHelper.getConfigValue(context,Constants.MAP_REGION_NAME);

    }

    public static synchronized OverpassHelper getInstance(Context context){
        if(instance == null)
            return new OverpassHelper(context);
        return instance;
    }

    public URL getXeroxURL(){
        final String queryXerox = this.context.getString(R.string.query_xerox);

        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String formattedQuery = String.format(Locale.US, queryXerox,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate);

        final String overpassURL = this.context.getString(R.string.overpass_url);
        URL restroomURL = null;
        try {
            restroomURL = new URL(overpassURL + URLEncoder.encode(formattedQuery, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  restroomURL;
    }

    public URL getAuditoriumsURL(){
        final String queryAuditoriums = this.context.getString(R.string.query_auditoriums);

        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String formattedQuery = String.format(Locale.US, queryAuditoriums,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate);

        final String overpassURL = this.context.getString(R.string.overpass_url);
        URL restroomURL = null;
        try {
            restroomURL = new URL(overpassURL + URLEncoder.encode(formattedQuery, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  restroomURL;
    }

    public URL getLibrariesURL(){
        final String queryLibraries = this.context.getString(R.string.query_libraries);

        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String formattedQuery = String.format(Locale.US, queryLibraries,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate);

        final String overpassURL = this.context.getString(R.string.overpass_url);
        URL restroomURL = null;
        try {
            restroomURL = new URL(overpassURL + URLEncoder.encode(formattedQuery, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  restroomURL;
    }

    public URL getFoodURL(){
        final String queryFood = this.context.getString(R.string.query_food);

        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String formattedQuery = String.format(Locale.US, queryFood,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate);

        final String overpassURL = this.context.getString(R.string.overpass_url);
        URL restroomURL = null;
        try {
            restroomURL = new URL(overpassURL + URLEncoder.encode(formattedQuery, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  restroomURL;
    }

    public URL getRestroomURL(){
        final String queryRestroom = this.context.getString(R.string.query_restroom);

        // For overpass queries, use the following order of coordinates: (south,west,north,east)
        final String formattedQuery = String.format(Locale.US, queryRestroom,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate,
                southCoordinate, westCoordinate, northCoordinate, eastCoordinate);

        final String overpassURL = this.context.getString(R.string.overpass_url);
        URL restroomURL = null;
        try {
            restroomURL = new URL(overpassURL + URLEncoder.encode(formattedQuery, "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  restroomURL;
    }

    public URL getSearchURL(String userQuery){
        // Cleans all the white spaces on the query
        userQuery = userQuery.replaceAll("\\s+", " ");
        if (Character.isWhitespace(userQuery.charAt(userQuery.length()-1))){
            userQuery = userQuery.substring(0,userQuery.length()-1);
        }

        final String searchNameQuery = this.context.getString(R.string.query_name_search);
        final String formattedQuery = String.format(Locale.US, searchNameQuery, mapRegionName,
                userQuery, userQuery, userQuery, userQuery, userQuery, userQuery);
        final String overpassURL = this.context.getString(R.string.overpass_url);

        URL searchURL = null;
        try {
            searchURL = new URL (overpassURL + URLEncoder.encode(formattedQuery,"UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return searchURL;
    }
}
