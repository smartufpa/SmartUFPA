package br.ufpa.smartufpa.activities.api

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.dialogs.CommentDialog
import br.ufpa.smartufpa.fragments.forms.ElementBasicDataForm
import br.ufpa.smartufpa.fragments.PlaceDetailsFragment
import br.ufpa.smartufpa.interfaces.CloseChangeSetListener
import br.ufpa.smartufpa.interfaces.CreateChangeSetListener
import br.ufpa.smartufpa.interfaces.UploadChangeSetListener
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.osm.ElementParser
import br.ufpa.smartufpa.utils.UIHelper
import br.ufpa.smartufpa.utils.enums.FormFlag
import br.ufpa.smartufpa.utils.osm.OsmUploadHelper
import br.ufpa.smartufpa.utils.osm.OsmXmlBuilder
import kotlinx.android.synthetic.main.activity_edit_element.*
import kotlinx.android.synthetic.main.custom_header.*


class EditElementActivity : AppCompatActivity(), CommentDialog.CommentDelegate,
        CreateChangeSetListener, UploadChangeSetListener, CloseChangeSetListener{


    private val elementParser : ElementParser = ElementParser
    private lateinit var elementBasicDataForm : ElementBasicDataForm
    private lateinit var element : Element
    private val osmUploadHelper = OsmUploadHelper(this)

    companion object {
        private val LOG_TAG = EditElementActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_element)
        element = intent.getParcelableExtra(PlaceDetailsFragment.ARG_ELEMENT)
        setActivityTitle()
        setActivitySubtitle()
        setActivityExtraInfo()

        initFormFragment()

        btnEditNext.setOnClickListener {
            elementBasicDataForm.setElementData(FormFlag.EDIT)
            openCommentDialog()
        }

        btnEditBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setActivityTitle() {
        var name : String? = elementParser.getName(element)
        if(name == null) {
            name = getString(R.string.place_holder_no_name)
            txtHeaderTitle.setTextColor(ContextCompat.getColor(this,android.R.color.darker_gray))
        }
        txtHeaderTitle.text = name
    }

    private fun setActivitySubtitle() {
        val localName: String? = elementParser.getLocalName(element)
        if (localName != null) {
            txtHeaderSubtitle.text = localName
        } else {
            txtHeaderSubtitle.text = ""
        }
    }

    private fun setActivityExtraInfo() {
        val shortName = elementParser.getShortName(element)
        if (shortName != null) {
            txtHeaderExtraInfo.text = String.format("(%s)", shortName)
        } else {
            txtHeaderExtraInfo.text = ""
        }

    }

    private fun initFormFragment() {
        elementBasicDataForm = ElementBasicDataForm.newInstance(element)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.containerEditForm,elementBasicDataForm, elementBasicDataForm.tag).commit()
    }

    private fun openCommentDialog(){
        val commentDialog = CommentDialog()
        commentDialog.show(supportFragmentManager,CommentDialog.DIALOG_TAG)
    }

    // Btn Enviar foi pressionado
    override fun delegateComment(commentText: String) {
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

    override fun onUploadChangesetResponse(changesetId: String) {
        osmUploadHelper.makeCloseChangeSetRequest(changesetId)
    }

    override fun onCloseChangeSetResponse() {
        UIHelper.showToastShort(this,"Upload Concluído")
        finish()
    }


}