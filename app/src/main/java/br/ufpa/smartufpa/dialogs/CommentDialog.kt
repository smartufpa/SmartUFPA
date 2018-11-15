package br.ufpa.smartufpa.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import br.ufpa.smartufpa.R
import kotlinx.android.synthetic.main.dialog_comment.view.*

class CommentDialog : DialogFragment(){

    companion object {
        val DIALOG_TAG : String? = CommentDialog::class.simpleName
    }

    interface CommentDelegate{
        fun delegateComment(commentText : String)
    }

    lateinit var commentDelegate : CommentDelegate

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            commentDelegate = activity as CommentDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement CommentDelegate")
        }

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder:  AlertDialog.Builder = AlertDialog.Builder(activity)
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_comment,null)
        builder.setView(view)
                .setPositiveButton(R.string.label_btn_add_comment) { _, _ ->
                    val commentText = view.edtCommentText.text.toString()
                    commentDelegate.delegateComment(commentText)
                }
                .setNegativeButton(R.string.label_btn_cancel){ _, _ ->
                    dismiss()
                }


        return builder.create()
    }


}