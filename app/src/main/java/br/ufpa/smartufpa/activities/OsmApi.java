package br.ufpa.smartufpa.activities;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class OsmApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://www.openstreetmap.org/oauth/authorize";
    private static  final String requestTokenURL = "https://www.openstreetmap.org/oauth/request_token";
    private static  final String accessTokenURL = "https://www.openstreetmap.org/oauth/access_token";

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
        return accessTokenURL;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return requestTokenURL;
    }

}
