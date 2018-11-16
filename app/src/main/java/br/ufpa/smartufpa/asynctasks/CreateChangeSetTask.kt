package br.ufpa.smartufpa.asynctasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import br.ufpa.smartufpa.interfaces.CreateChangeSetListener
import br.ufpa.smartufpa.utils.OAuthHelper
import com.github.scribejava.core.model.Verb

class CreateChangeSetTask(private val context: Context) : AsyncTask<String,Unit,String>() {

    private val urlCreateChangeSet = "https://master.apis.dev.openstreetmap.org/api/0.6/changeset/create"
    private val oAuthHelper = OAuthHelper(context)

    private val callback: CreateChangeSetListener = context as CreateChangeSetListener

    companion object {
        val LOG_TAG = CreateChangeSetTask::class.simpleName
    }

    override fun doInBackground(vararg args: String?): String? {
        val payloadXml = args[0]
        val response = oAuthHelper.makeRequest(Verb.PUT, urlCreateChangeSet, payloadXml)
        if (response != null){
            if(response.isSuccessful){
                Log.d(LOG_TAG, response.toString())
                return response.body
            }else{
                Log.e(LOG_TAG,"Erro na criação do changeset=${response.toString()} ")
            }
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(result != null)
            callback.onChangeSetCreated(result)
        else
            callback.onChangeSetCreated("-1")
    }
}