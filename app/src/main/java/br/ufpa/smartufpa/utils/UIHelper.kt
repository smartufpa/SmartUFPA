package br.ufpa.smartufpa.utils

import android.content.Context
import android.widget.Toast
import br.ufpa.smartufpa.R

class UIHelper{
    companion object {
        @JvmStatic
        fun showToastLong(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
        @JvmStatic
        fun showToastShort(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}