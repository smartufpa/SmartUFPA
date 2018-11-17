package br.ufpa.smartufpa.asynctasks.osmapi

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import br.ufpa.smartufpa.interfaces.UploadChangeSetListener
import br.ufpa.smartufpa.utils.OAuthHelper
import com.github.scribejava.core.model.Verb

class CloseChangeSetTask(private val context: Context) : AsyncTask<String,Unit, String>() {

    private val urlUploadChangeSet = "https://master.apis.dev.openstreetmap.org/api/0.6/changeset/%s/close"
    private val oAuthHelper = OAuthHelper(context)

//    private val callback: UploadChangeSetListener = context as UploadChangeSetListener

    companion object {
        val LOG_TAG = CloseChangeSetTask::class.simpleName
    }

    override fun doInBackground(vararg args: String?) : String? {
        Log.d(LOG_TAG, "Started Request.")
        val changesetId = args[0]
        val formatedUrl = String.format(urlUploadChangeSet,changesetId)
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


}