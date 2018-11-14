package br.ufpa.smartufpa.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import br.ufpa.smartufpa.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(
                {
                    startActivity(Intent(this@SplashActivity, RedirectActivity::class.java))
                    finish()
                }, 2000)
    }


}
