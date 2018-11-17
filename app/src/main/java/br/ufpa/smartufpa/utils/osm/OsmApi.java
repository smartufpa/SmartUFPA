package br.ufpa.smartufpa.utils.osm;

import com.github.scribejava.core.builder.api.DefaultApi10a;

import br.ufpa.smartufpa.utils.Constants;

public class OsmApi extends DefaultApi10a {

    private static  final String REQUEST_TOKEN_URL = Constants.OsmApiUrl.REQUEST_TOKEN;
    private static final String AUTHORIZE_URL = Constants.OsmApiUrl.AUTHORIZE;
    private static  final String ACCESS_TOKEN_URL = Constants.OsmApiUrl.ACCESS_TOKEN;

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
        return ACCESS_TOKEN_URL;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

}
