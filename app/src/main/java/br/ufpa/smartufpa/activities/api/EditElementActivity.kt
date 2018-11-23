package br.ufpa.smartufpa.activities.api

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.dialogs.CommentDialog
import br.ufpa.smartufpa.fragments.ElementDetailsFragment
import br.ufpa.smartufpa.fragments.forms.FormBasicData
import br.ufpa.smartufpa.interfaces.OsmUploader
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.UIHelper
import br.ufpa.smartufpa.utils.enums.FormFlag
import br.ufpa.smartufpa.utils.osm.OsmUploadHelper
import br.ufpa.smartufpa.utils.osm.OsmXmlBuilder
import kotlinx.android.synthetic.main.activity_edit_element.*
import kotlinx.android.synthetic.main.custom_header.*


class EditElementActivity : AppCompatActivity(),OsmUploader{


    private lateinit var formBasicData : FormBasicData
    private lateinit var element : Element
    private val osmUploadHelper = OsmUploadHelper(this)

    companion object {
        private val LOG_TAG = EditElementActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_element)
        element = intent.getParcelableExtra(ElementDetailsFragment.ARG_ELEMENT)
        setActivityTitle()
        setActivitySubtitle()
        setActivityExtraInfo()

        initFormFragment()

        btnEditNext.setOnClickListener {
//            formBasicData.setElementData(FormFlag.EDIT)
            openCommentDialog()
        }

        btnEditBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setActivityTitle() {
        var name : String? = element.getName()
        if(name == null) {
            name = getString(R.string.place_holder_no_name)
            txtHeaderTitle.setTextColor(ContextCompat.getColor(this,android.R.color.darker_gray))
        }
        txtHeaderTitle.text = name
    }

    private fun setActivitySubtitle() {
        val localName: String? = element.getLocalName()
        if (localName != null) {
            txtHeaderSubtitle.text = localName
        } else {
            txtHeaderSubtitle.text = ""
        }
    }

    private fun setActivityExtraInfo() {
        val shortName = element.getShortName()
        if (shortName != null) {
            txtHeaderExtraInfo.text = String.format("(%s)", shortName)
        } else {
            txtHeaderExtraInfo.text = ""
        }

    }

    private fun initFormFragment() {
        formBasicData = FormBasicData.newInstance(element)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.containerEditForm,formBasicData, formBasicData.tag).commit()
    }

    override fun openCommentDialog(){
        val commentDialog = CommentDialog()
        commentDialog.show(supportFragmentManager,CommentDialog.DIALOG_TAG)
    }

    override fun onCommentResponse(commentText: String) {
        startUploadFlow(commentText)
    }

    private fun startUploadFlow(commentText: String) {
        UIHelper.showToastShort(this,"Upload Iniciado")
        val payload = OsmXmlBuilder.createChangeSetXml(commentText)
        osmUploadHelper.makeCreateChangeSetRequest(payload)
    }

    override fun onCreateChangeSetResponse(changesetId: String) {
        val elementVersion = osmUploadHelper.makeGetElementVersionRequest(element.id.toString(), element.type.toString())
        val payload = OsmXmlBuilder.uploadChangeSetXml(element, changesetId, elementVersion, FormFlag.EDIT)
        osmUploadHelper.makeUploadChangeSetRequest(payload,changesetId)
    }
    override fun onUploadChangeSetResponse(changesetId: String) {
        osmUploadHelper.makeCloseChangeSetRequest(changesetId)
    }

    override fun onCloseChangeSetResponse() {
        UIHelper.showToastShort(this,"Upload Concluído")
        finish()
    }


}