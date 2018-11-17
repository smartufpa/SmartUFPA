package br.ufpa.smartufpa.utils

import android.content.Context
import android.util.Log
import br.ufpa.smartufpa.utils.osm.OsmApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService

class OAuthHelper(private val context: Context) {

    private val consumerKey = Constants.Oauth.CONSUMER_KEY
    private val consumerSecret = Constants.Oauth.CONSUMER_SECRET
    private val callback = Constants.Oauth.CALLBACK

    companion object {
        private val LOG_TAG = OAuthHelper::class.simpleName
    }

    val service: OAuth10aService = ServiceBuilder(consumerKey)
            .apiSecret(consumerSecret)
            .callback(callback)
            .build(OsmApi.instance())


    private fun getAccessToken(): OAuth1AccessToken {
        val stringAccessToken = SharedPrefsHelper.getStringByKey(context, Constants.SharedPrefs.KEY_ACCESS_TOKEN)
        val stringAccessSecret = SharedPrefsHelper.getStringByKey(context, Constants.SharedPrefs.KEY_ACCESS_SECRET)

        return OAuth1AccessToken(stringAccessToken, stringAccessSecret)
    }


    fun makeRequest(requestVerb: Verb, url: String, payload: String?): Response? {
        val request = OAuthRequest(requestVerb, url)
        if (requestVerb == Verb.PUT) {
            addPutHeader(request, payload)
        }

        if (requestVerb == Verb.POST) {
            addPostHeader(request, payload)
        }

        val newAccessToken = getAccessToken()
        service.signRequest(newAccessToken, request)
        Log.d("${LOG_TAG}:/Request", request.toString())
        val response = service.execute(request)
        Log.d("${LOG_TAG}:/Response", response.body)
        return response
    }

    private fun addPutHeader(request: OAuthRequest, payload: String?) {
        if (payload != null)
            request.setPayload(payload)
        request.addHeader("Accept-Charset", "UTF-8")
        request.addHeader("Accept", "application/xml")
        request.addHeader("Content-type", "application/xml")
    }

    private fun addPostHeader(request: OAuthRequest, payload: String?) {
        request.setPayload(payload)
        request.addHeader("Accept-Charset", "UTF-8")
        request.addHeader("Accept", "*/*")
        request.addHeader("Content-type", "text/xml")
    }

}