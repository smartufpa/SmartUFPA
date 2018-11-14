package br.ufpa.smartufpa.activities

import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken

class OAuthHelper {
    interface RequestTokenListener{
        fun onRequestTokenResponse(requestToken: OAuth1RequestToken?, authUrl : String?)
    }

    interface AccessTokenListener{
        fun onAccessTokenResponse(accessToken: OAuth1AccessToken?)
    }
}