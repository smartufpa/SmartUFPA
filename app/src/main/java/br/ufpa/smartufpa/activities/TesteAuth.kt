package br.ufpa.smartufpa.activities

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import br.ufpa.smartufpa.R
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.android.synthetic.main.activity_teste_auth.*
import java.util.concurrent.ExecutionException


class TesteAuth : AppCompatActivity(), OAuthHelper.RequestTokenListener, OAuthHelper.AccessTokenListener {

    private val consumerKey = "dZG58UbBiP2F3Pi995CC7YY0FRnCxEHr2AvpHOnG"
    private val consumerSecret = "tpyAHG2yInll2IJkNfZNe6T3oWOd8QIUmPZQp55y"
    private val callback = "br.ufpa.smartufpa://callback"
    val service: OAuth10aService = ServiceBuilder(consumerKey)
            .debug()
            .apiSecret(consumerSecret)
            .callback(callback)
            .build(OsmApi.instance())

    private lateinit var authUrl: String
    companion object {
        val LOG_TAG = TesteAuth::class.simpleName
        var requestToken: OAuth1RequestToken? = null
        var accessToken: OAuth1AccessToken? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teste_auth)
        btnAuth.setOnClickListener {
            try {
                authUrl = RequestTokenTask().execute().get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            startCustomTabIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        val data = intent.data
        if (data != null && !TextUtils.isEmpty(data.scheme)) {
//            val oauthToken: String = data.getQueryParameter("oauth_token")
            val oauthVerifier: String = data.getQueryParameter("oauth_verifier")
            if (!oauthVerifier.isEmpty()) {
                AccessTokenTask().execute(oauthVerifier)
            }

        }

    }
    override fun onAccessTokenResponse(accessTokenServer: OAuth1AccessToken?) {
        accessToken = accessTokenServer
        if (accessTokenServer != null) {
            btnAuth.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
        }
    }

    override fun onRequestTokenResponse(requestTokenServer: OAuth1RequestToken?, authUrl: String?) {

    }

    private fun startCustomTabIntent(){

        val uri = Uri.parse(authUrl)

        // create an intent builder
        val intentBuilder = CustomTabsIntent.Builder()

        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))


        // build custom tabs intent
        val customTabsIntent = intentBuilder.build()

        // launch the url
        customTabsIntent.launchUrl(this, uri)
    }


    private inner class RequestTokenTask : AsyncTask<Unit, Void, String>() {
        override fun doInBackground(vararg p0: Unit?): String {
            requestToken = service.requestToken
            Log.d(LOG_TAG, requestToken?.rawResponse)
            return service.getAuthorizationUrl(requestToken)
        }
    }

    private inner class AccessTokenTask : AsyncTask<String, Unit, OAuth1AccessToken>() {

        override fun doInBackground(vararg args: String?): OAuth1AccessToken {
            val verifier: String? = args[0]
            Log.d(LOG_TAG, requestToken?.rawResponse)
            return service.getAccessToken(requestToken, verifier)
        }

    }
}
