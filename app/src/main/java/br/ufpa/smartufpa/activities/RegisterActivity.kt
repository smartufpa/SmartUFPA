package br.ufpa.smartufpa.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebViewClient
import br.ufpa.smartufpa.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = getString(R.string.register_url)
        if (url.isEmpty())
            finish()
        setContentView(R.layout.activity_register)

        webview_register!!.settings.javaScriptEnabled = true
        webview_register!!.webViewClient = WebViewClient()
        webview_register!!.loadUrl(url)


    }
}
