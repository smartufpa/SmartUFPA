package br.ufpa.smartufpa.asynctasks.osmapi

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import br.ufpa.smartufpa.interfaces.UploadChangeSetListener
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.OAuthHelper
import com.github.scribejava.core.model.Verb

class UploadChangeSetTask(context: Context) : AsyncTask<String,Unit, String>() {

    private val urlUploadChangeSet = Constants.OsmApiUrl.UPLOAD_CHANGESET
    private val oAuthHelper = OAuthHelper(context)

    private val callback: UploadChangeSetListener = context as UploadChangeSetListener

    companion object {
        val LOG_TAG = UploadChangeSetTask::class.simpleName
    }

    override fun doInBackground(vararg args: String?) : String? {
        Log.d(LOG_TAG, "Started Request.")
        val payloadXml = args[0]
        val changesetId = args[1]
        val formatedUrl = String.format(urlUploadChangeSet,changesetId)
        val response = oAuthHelper.makeRequest(Verb.POST, formatedUrl, payloadXml)
        if (response != null){
            if(response.isSuccessful){
                Log.d(LOG_TAG, response.toString())
                return changesetId
            }else{
                Log.e(LOG_TAG,"Erro no upload do changeset=$response")
            }
        }

        return null
    }

    override fun onPostExecute(changesetId: String?) {
        super.onPostExecute(changesetId)
        Log.d(LOG_TAG, "Finished Request")
        if(changesetId != null)
            callback.onUploadChangesetResponse(changesetId)
        else
            callback.onUploadChangesetResponse("-1")
    }
}