package br.ufpa.smartufpa.activities

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.PrefManager
import br.ufpa.smartufpa.utils.SharedPrefsHelper
import br.ufpa.smartufpa.utils.SystemServicesManager

class RedirectActivity : AppCompatActivity() {
    private var manager: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = PrefManager(this)

        if(!hasPermissionConditions()){
            goToPermissionActivity()
            return
        }

        if (isGPSEnabled()) {
            if (!hasOsmLogin()) goToOsmLoginActivity()
            else goToMainActivity()

        } else {
            goToNoGPSActivity()
        }
        finish()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun onDestroy() {
        Log.d(RedirectActivity::class.simpleName,"ondestroy")
        super.onDestroy()
    }

    /* Funções de fluxo */

    private fun goToPermissionActivity() {
        startActivity(Intent(this, PermissionCheckActivity::class.java))
    }

    private fun goToNoGPSActivity() {
        startActivity(Intent(this, NoGpsActivity::class.java))
    }

    private fun goToOsmLoginActivity() {
        startActivity(Intent(this, OsmLoginActivity::class.java))
    }

    /* Funções de checagem */

    private fun isGPSEnabled(): Boolean {
        return SystemServicesManager.isGPSEnabled(applicationContext)
    }


    private fun hasPermissionConditions(): Boolean {
        return !manager!!.isFirstTimeLaunch || isPermissionGranted()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun isPermissionGranted(): Boolean {
        return hasLocationPermission() && hasStoragePermission()
    }

    private fun hasOsmLogin(): Boolean {
        val hasUserSaved = SharedPrefsHelper.hasKeySaved(this, Constants.SharedPrefs.KEY_USER_ID)
        val hasTokenSaved = SharedPrefsHelper.hasKeySaved(this, Constants.SharedPrefs.KEY_ACCESS_TOKEN)
        return hasUserSaved && hasTokenSaved
    }

    private fun hasStoragePermission() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationPermission() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
