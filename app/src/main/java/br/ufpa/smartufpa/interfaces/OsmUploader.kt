package br.ufpa.smartufpa.interfaces

interface OsmUploader {
    fun openCommentDialog()
    fun onCommentResponse(commentText : String)
    fun onCreateChangeSetResponse(changesetId : String)
    fun onUploadChangeSetResponse(changesetId: String)
    fun onCloseChangeSetResponse()
}