package br.ufpa.smartufpa.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.R.id.txtEditExtraInfo
import br.ufpa.smartufpa.dialogs.CommentDialog
import br.ufpa.smartufpa.fragments.ElementBasicDataForm
import br.ufpa.smartufpa.interfaces.CloseChangeSetListener
import br.ufpa.smartufpa.interfaces.CreateChangeSetListener
import br.ufpa.smartufpa.interfaces.UploadChangeSetListener
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.osm.ElementParser
import br.ufpa.smartufpa.utils.osm.OsmUploadHelper
import kotlinx.android.synthetic.main.activity_edit_element.*

class CreateElementActivity : AppCompatActivity(), CommentDialog.CommentDelegate,
        CreateChangeSetListener, UploadChangeSetListener, CloseChangeSetListener {

    private val elementParser: ElementParser = ElementParser
    private lateinit var elementBasicDataForm: ElementBasicDataForm
    private val osmUploadHelper = OsmUploadHelper(this)

    companion object {
        @JvmStatic
        public val ARG_LATITUDE = "latitude"
        @JvmStatic
        val ARG_LONGITUDE = "longitude"
        @JvmStatic
        val ARG_CATEGORY = "category"

        val TAG = CreateElementActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_element)
        setActivityTitle()
        setActivitySubtitle()
        setActivityExtraInfo()

        initFormFragment()

        btnEditNext.setOnClickListener {
            elementBasicDataForm.updateElementData()
            openCommentDialog()

        }

        btnEditBack.setOnClickListener {
            super.onBackPressed()
        }
    }


    private fun setActivityTitle() {
        txtEditTitle.text = "Novo Local"
    }

    private fun setActivitySubtitle() {
        txtEditSubtitle.text = "Insira as informações sobre o local"
    }

    private fun setActivityExtraInfo() {
        txtEditExtraInfo.text = String.format("(%s)", intent.getStringExtra(ARG_CATEGORY))


    }

    private fun initFormFragment() {
        elementBasicDataForm = ElementBasicDataForm.newInstance(null)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.containerCreateForm, elementBasicDataForm, elementBasicDataForm.tag).commit()
    }

    private fun openCommentDialog() {
        val commentDialog = CommentDialog()
        commentDialog.show(supportFragmentManager, CommentDialog.DIALOG_TAG)
    }

    override fun delegateComment(commentText: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateChangeSetResponse(changesetId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUploadChangesetResponse(changesetId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCloseChangeSetResponse() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
