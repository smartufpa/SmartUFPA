package br.ufpa.smartufpa.asynctasks.osmapi

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import br.ufpa.smartufpa.interfaces.CreateChangeSetListener
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.OAuthHelper
import com.github.scribejava.core.model.Verb

class CreateChangeSetTask(context: Context) : AsyncTask<String,Unit,String>() {

    private val urlCreateChangeSet = Constants.OsmApiUrl.CREATE_CHANGESET
    private val oAuthHelper = OAuthHelper(context)

    private val callback: CreateChangeSetListener = context as CreateChangeSetListener

    companion object {
        val LOG_TAG = CreateChangeSetTask::class.simpleName
    }

    override fun doInBackground(vararg args: String?): String? {
        Log.d(LOG_TAG, "Started Request.")
        val payloadXml = args[0]
        val response = oAuthHelper.makeRequest(Verb.PUT, urlCreateChangeSet, payloadXml)
        if (response != null){
            if(response.isSuccessful){
                Log.d(LOG_TAG, response.toString())
                return response.body
            }else{
                Log.e(LOG_TAG,"Erro na criação do changeset=${response} ")
            }
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d(LOG_TAG, "Finished Request")
        if(result != null)
            callback.onCreateChangeSetResponse(result)
        else
            callback.onCreateChangeSetResponse("-1")
    }
}