package br.ufpa.smartufpa.asynctasks.osmapi

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import br.ufpa.smartufpa.interfaces.OsmUploader
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.OAuthHelper
import com.github.scribejava.core.model.Verb

class CloseChangeSetTask(context: Context) : AsyncTask<String,Unit, String>() {

    private val urlCloseChangeSet = Constants.OsmApiUrl.CLOSE_CHANGESET
    private val oAuthHelper = OAuthHelper(context)

    private val callback: OsmUploader = context as OsmUploader

    companion object {
        val LOG_TAG = CloseChangeSetTask::class.simpleName
    }

    override fun doInBackground(vararg args: String?) : String? {
        Log.d(LOG_TAG, "Started Request.")
        val changesetId = args[0]
        val formatedUrl = String.format(urlCloseChangeSet,changesetId)
        val response = oAuthHelper.makeRequest(Verb.PUT, formatedUrl, null)
        if (response != null){
            if(response.isSuccessful){
                Log.d(LOG_TAG, response.toString())
                return response.body
            }else{
                Log.e(LOG_TAG,"Erro na criação do changeset=$response ")
            }
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        callback.onCloseChangeSetResponse()
    }


}