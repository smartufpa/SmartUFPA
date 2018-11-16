package br.ufpa.smartufpa.utils

import android.content.Context
import br.ufpa.smartufpa.utils.osm.OsmApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService

class OAuthHelper(private val context: Context){


    //TODO guardar chaves em um local apropriado
    private val dev_consumerKey = "IY3GmCJIaUxSeSlceMf8FrXihe0Km2bU9zrUCD9n"
    private val consumerKey = "dZG58UbBiP2F3Pi995CC7YY0FRnCxEHr2AvpHOnG"
    private val dev_consumerSecret = "IvAJZJSOL6Eeb6ra9BUy1QlPVz3OVczbDQ27jr5R"
    private val consumerSecret = "tpyAHG2yInll2IJkNfZNe6T3oWOd8QIUmPZQp55y"
    private val callback = "br.ufpa.smartufpa://callback"

    val service: OAuth10aService = ServiceBuilder(dev_consumerKey)
            .apiSecret(dev_consumerSecret)
            .callback(callback)
            .build(OsmApi.instance())


    private fun getAccessToken(): OAuth1AccessToken {
        val stringAccessToken = SharedPrefsHelper.getStringByKey(context, Constants.SharedPrefs.KEY_ACCESS_TOKEN)
        val stringAccessSecret = SharedPrefsHelper.getStringByKey(context, Constants.SharedPrefs.KEY_ACCESS_SECRET)

        return OAuth1AccessToken(stringAccessToken, stringAccessSecret)
    }


    fun makeRequest(requestVerb: Verb, url: String, payload : String?): Response? {
        val request = OAuthRequest(requestVerb, url)
        if(requestVerb == Verb.PUT){
            request.setPayload(payload)
            request.addHeader("Accept-Charset", "UTF-8")
            request.addHeader("Accept", "application/xml")
            request.addHeader("Content-type", "application/xml")
        }

        val newAccessToken = getAccessToken()
        service.signRequest(newAccessToken, request)
        return service.execute(request)
    }

}