package br.ufpa.smartufpa.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.models.overpass.OsmUser
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.Constants.SharedPrefs.*
import br.ufpa.smartufpa.utils.OsmApiXmlParser
import br.ufpa.smartufpa.utils.SharedPrefsHelper
import br.ufpa.smartufpa.utils.UIHelper
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.android.synthetic.main.activity_teste_auth.*
import java.io.ByteArrayInputStream
import java.util.concurrent.ExecutionException


class TesteAuth : AppCompatActivity() {

    private val dev_consumerKey = "IY3GmCJIaUxSeSlceMf8FrXihe0Km2bU9zrUCD9n"
    private val consumerKey = "dZG58UbBiP2F3Pi995CC7YY0FRnCxEHr2AvpHOnG"
    private val dev_consumerSecret = "IvAJZJSOL6Eeb6ra9BUy1QlPVz3OVczbDQ27jr5R"
    private val consumerSecret = "tpyAHG2yInll2IJkNfZNe6T3oWOd8QIUmPZQp55y"
    private val callback = "br.ufpa.smartufpa://callback"

    val service: OAuth10aService = ServiceBuilder(dev_consumerKey)
            .debug()
            .apiSecret(dev_consumerSecret)
            .callback(callback)
            .build(OsmApi.instance())

    private lateinit var authUrl: String

    companion object {
        val LOG_TAG = TesteAuth::class.simpleName
        val KEY_QUERY_OAUTH_VERIFIER = "oauth_verifier"

        var requestToken: OAuth1RequestToken? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teste_auth)
        btnAuth.setOnClickListener {
            initAuthUrl()
            startCustomTabIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        val uri: Uri? = intent.data
        if (!isUserDetailSaved()) {
            if (!isAccessTokenSaved() && !isAccessSecretSaved()) {
                startOAuthFlow(uri)
            }
            saveUserDetails(getUserDetails())
        }
        goToMainActivity()
    }

    private fun saveUserDetails(userDetails: String?) {
        val osmApiXmlParser = OsmApiXmlParser()
        val stream = ByteArrayInputStream(userDetails?.toByteArray())
        val user: OsmUser? = osmApiXmlParser.parseUser(stream)
        if (user != null) {
            val editor = SharedPrefsHelper.getEditor(this)
            editor?.let {
                it.putString(Constants.SharedPrefs.KEY_USER_ID, user.userId)
                it.putString(Constants.SharedPrefs.KEY_USER_DISPLAY_NAME, user.displayName)
                it.putString(Constants.SharedPrefs.KEY_USER_ACCOUNT_CREATED, user.accountCreated)
                it.commit()
            }
        }
    }

    private fun goToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }

    private fun getUserDetails(): String? {
        try {
            val xmlResponse = GetUserDetailsTask(this).execute().get()
            return xmlResponse;
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }

    private fun initAuthUrl() {
        try {
            authUrl = RequestTokenTask().execute().get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
    }

    private fun startOAuthFlow(uri: Uri?) {
        if (uri != null) {
            val oauthVerifier = uri.getQueryParameter(KEY_QUERY_OAUTH_VERIFIER)
            try {
                if (!oauthVerifier!!.isEmpty()) {
                    AccessTokenTask(this).execute(oauthVerifier)
                }
            } catch (e: NullPointerException) {
                UIHelper.showToastLong(this, "Não foi possível recuperar o código verificador.")
            }
        }
    }

    private fun isAccessTokenSaved(): Boolean {
        return SharedPrefsHelper.hasKeySaved(this, Constants.SharedPrefs.KEY_ACCESS_TOKEN)
    }


    private fun isAccessSecretSaved(): Boolean {
        return SharedPrefsHelper.hasKeySaved(this, Constants.SharedPrefs.KEY_ACCESS_SECRET)
    }

    private fun isUserDetailSaved(): Boolean {
        val isUserIdSet = SharedPrefsHelper
                .hasKeySaved(this, Constants.SharedPrefs.KEY_USER_ID)
        val isUserDisplayNameSet = SharedPrefsHelper
                .hasKeySaved(this, Constants.SharedPrefs.KEY_USER_DISPLAY_NAME)
        val isUserAccountCreatedSet = SharedPrefsHelper
                .hasKeySaved(this, Constants.SharedPrefs.KEY_USER_ACCOUNT_CREATED)
        return isUserIdSet && isUserDisplayNameSet && isUserAccountCreatedSet
    }

    private fun startCustomTabIntent() {
        val uri = Uri.parse(authUrl)
        val intentBuilder = CustomTabsIntent.Builder()
        with(intentBuilder) {
            setToolbarColor(ContextCompat.getColor(this@TesteAuth, R.color.colorPrimary))
            setSecondaryToolbarColor(ContextCompat.getColor(this@TesteAuth, R.color.colorPrimaryDark))
        }
        val customTabsIntent = intentBuilder.build()
        customTabsIntent.launchUrl(this, uri)
    }


    private inner class RequestTokenTask : AsyncTask<Unit, Void, String>() {
        override fun doInBackground(vararg p0: Unit?): String {
            requestToken = service.requestToken
            Log.d(LOG_TAG, requestToken?.rawResponse)
            return service.getAuthorizationUrl(requestToken)
        }
    }

    private inner class AccessTokenTask(context: Context) : AsyncTask<String, Unit, Unit>() {

        val sharedPreferencesEditor = SharedPrefsHelper.getEditor(context)

        override fun doInBackground(vararg args: String?) {
            Log.d(LOG_TAG, requestToken?.rawResponse)
            val verifier: String? = args[0]
            val accessToken: OAuth1AccessToken = service.getAccessToken(requestToken, verifier)
            saveTokenDetails(accessToken)
        }

        private fun saveTokenDetails(accessToken: OAuth1AccessToken) {
            val androidId = Settings.Secure.getString(applicationContext.contentResolver,
                    Settings.Secure.ANDROID_ID)
            sharedPreferencesEditor?.let {
                it.putString(KEY_ACCESS_TOKEN, accessToken.token)
                it.putString(KEY_ACCESS_SECRET, accessToken.tokenSecret)
                it.putString(KEY_ANDROID_ID, androidId)
                it.commit()
            }
        }

    }

    private inner class GetUserDetailsTask(private val context: Context) : AsyncTask<Unit, Unit, String?>() {

        private val urlUserDetails = "https://master.apis.dev.openstreetmap.org/api/0.6/user/details"

        override fun doInBackground(vararg p0: Unit?): String? {
            val newAccessToken = getAccessToken()
            val request = OAuthRequest(Verb.GET, urlUserDetails)
            service.signRequest(newAccessToken, request)
            val response = service.execute(request)
            Log.d(LOG_TAG, response.body)
            return response.body
        }

        private fun getAccessToken(): OAuth1AccessToken {
            val stringAccessToken = SharedPrefsHelper.getStringByKey(context, Constants.SharedPrefs.KEY_ACCESS_TOKEN)
            val stringAccessSecret = SharedPrefsHelper.getStringByKey(context, Constants.SharedPrefs.KEY_ACCESS_SECRET)
            val newAccessToken = OAuth1AccessToken(stringAccessToken, stringAccessSecret)
            return newAccessToken
        }

    }
}
