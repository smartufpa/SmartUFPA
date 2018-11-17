package br.ufpa.smartufpa.asynctasks.osmapi

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.OAuthHelper
import br.ufpa.smartufpa.utils.osm.OsmApiXmlParser
import com.github.scribejava.core.model.Verb
import java.io.ByteArrayInputStream

class GetElementVersionTask (context: Context) : AsyncTask<String, Unit, String>() {

    private val urlGetElement = Constants.OsmApiUrl.GET_ELEMENT_VERSION
    private val oAuthHelper = OAuthHelper(context)
    private val osmApiXmlParser = OsmApiXmlParser()

    companion object {
        val TAG = GetElementVersionTask::class.simpleName
    }

    override fun doInBackground(vararg args: String): String? {
        val elementId = args[0]
        val elementType = args[1]

        val formattedUrl = String.format(urlGetElement,elementType,elementId)
        val response = oAuthHelper.makeRequest(Verb.GET, formattedUrl, null)
        if (response != null) {
            val stream = ByteArrayInputStream(response.body.toByteArray())
            val elementVersion = osmApiXmlParser.parseElementVersion(stream)
            Log.d(TAG, elementVersion)
            return elementVersion
        }
        return null
    }
}