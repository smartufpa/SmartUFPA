package br.ufpa.smartufpa.activities;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class OsmApi extends DefaultApi10a {
    private static final String DEV_AUTHORIZE_URL = "https://master.apis.dev.openstreetmap.org/oauth/authorize";
    private static  final String DEV_REQUEST_TOKEN_URL = "https://master.apis.dev.openstreetmap.org/oauth/request_token";
    private static  final String DEV_ACCESS_TOKEN_URL = "https://master.apis.dev.openstreetmap.org/oauth/access_token";

    private static  final String REQUEST_TOKEN_URL = "https://www.openstreetmap.org/oauth/request_token";
    private static final String AUTHORIZE_URL = "https://www.openstreetmap.org/oauth/authorize";
    private static  final String ACCESS_TOKEN_URL = "https://www.openstreetmap.org/oauth/access_token";

    protected OsmApi() {
    }

    private static class InstanceHolder {
        private static final OsmApi INSTANCE = new OsmApi();
    }

    public static OsmApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return DEV_ACCESS_TOKEN_URL;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return DEV_AUTHORIZE_URL;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return DEV_REQUEST_TOKEN_URL;
    }

}
