package br.ufpa.smartufpa.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import br.ufpa.smartufpa.R
import kotlinx.android.synthetic.main.activity_osmlogin.*

class OSMLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_osmlogin)

        val url  = getString(R.string.url_login);
        if (url.isEmpty())
            finish();
        webview_login.settings.javaScriptEnabled = true
        webview_login.webViewClient = WebViewClient()
        webview_login.loadUrl(url);
    }
}
