package br.ufpa.smartufpa.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper {

    companion object {
        private val prefName = Constants.SharedPrefs.PREFS_NAME
        @JvmStatic
        fun getEditor(context: Context): SharedPreferences.Editor?{
            val settings = context.getSharedPreferences(prefName, 0)
            return settings.edit()
        }
        @JvmStatic
        fun hasKeySaved(context: Context, key : String): Boolean {
            val settings = context.getSharedPreferences(prefName, 0)
            return settings.getString(key, null) != null
        }
        @JvmStatic
        fun getStringByKey(context: Context, key : String): String? {
            val settings = context.getSharedPreferences(prefName, 0)
            return settings.getString(key, null)
        }
    }

}