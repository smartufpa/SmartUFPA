package br.ufpa.smartufpa.asynctasks.osmapi

import android.content.Context
import android.os.AsyncTask
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.OAuthHelper
import com.github.scribejava.core.model.Verb

class GetElementVersionTask (context: Context) : AsyncTask<String, Unit, String>() {

    private val urlGetElement = Constants.OsmApiUrl.GET_ELEMENT_VERSION
    private val oAuthHelper = OAuthHelper(context)


    override fun doInBackground(vararg args: String): String? {
        val elementId = args[0]
        val elementType = args[1]

        val formatedUrl = String.format(urlGetElement,elementType,elementId)
        val response = oAuthHelper.makeRequest(Verb.GET, formatedUrl, null)
        if (response != null) {

        }
        return null
    }
}