package br.ufpa.smartufpa.utils.osm

import android.content.Context
import br.ufpa.smartufpa.asynctasks.osmapi.CloseChangeSetTask
import br.ufpa.smartufpa.asynctasks.osmapi.CreateChangeSetTask
import br.ufpa.smartufpa.asynctasks.osmapi.GetElementVersionTask
import br.ufpa.smartufpa.asynctasks.osmapi.UploadChangeSetTask

class OsmUploadHelper (private val context: Context){

    fun makeCreateChangeSetRequest(commentText : String){
         CreateChangeSetTask(context).execute(commentText)
    }

    fun makeUploadChangeSetRequest(payload: String, changesetId: String){
        UploadChangeSetTask(context).execute(payload,changesetId)
    }

    fun makeCloseChangeSetRequest(changesetId: String){
        CloseChangeSetTask(context).execute(changesetId)
    }

    fun makeGetElementVersionRequest(elementId: String, elementType: String): String? {
        return GetElementVersionTask(context).execute(elementId, elementType).get()
    }


}