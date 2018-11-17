package br.ufpa.smartufpa.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.asynctasks.osmapi.CloseChangeSetTask
import br.ufpa.smartufpa.asynctasks.osmapi.CreateChangeSetTask
import br.ufpa.smartufpa.asynctasks.osmapi.GetElementVersionTask
import br.ufpa.smartufpa.asynctasks.osmapi.UploadChangeSetTask
import br.ufpa.smartufpa.dialogs.CommentDialog
import br.ufpa.smartufpa.fragments.ElementBasicDataForm
import br.ufpa.smartufpa.fragments.PlaceDetailsFragment
import br.ufpa.smartufpa.interfaces.CreateChangeSetListener
import br.ufpa.smartufpa.interfaces.UploadChangeSetListener
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.Constants
import br.ufpa.smartufpa.utils.osm.ElementParser
import br.ufpa.smartufpa.utils.UIHelper
import br.ufpa.smartufpa.utils.osm.OsmXmlBuilder
import kotlinx.android.synthetic.main.activity_edit_element.*



class EditElementActivity : AppCompatActivity(), CommentDialog.CommentDelegate, CreateChangeSetListener, UploadChangeSetListener{


    private val elementParser : ElementParser = ElementParser
    private lateinit var elementBasicDataForm : ElementBasicDataForm
    private lateinit var element : Element

    companion object {
        private val TAG = EditElementActivity::class.simpleName
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
//            val foodPlaceForm = FoodPlaceForm.newInstance("", "")
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.containerEditForm,foodPlaceForm,foodPlaceForm.tag)
//                    .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
//                    .addToBackStack(foodPlaceForm.tag)
//                    .commit()
            elementBasicDataForm.updateElementData()
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
            txtEditTitle.setTextColor(ContextCompat.getColor(this,android.R.color.darker_gray))
        }
        txtEditTitle.text = name
    }

    private fun setActivitySubtitle() {
        val localName: String? = elementParser.getLocalName(element)
        if (localName != null) {
            txtEditSubtitle.text = localName
        } else {
            txtEditSubtitle.text = ""
        }
    }

    private fun setActivityExtraInfo() {
        val shortName = elementParser.getShortName(element)
        if (shortName != null) {
            txtEditExtraInfo.text = String.format("(%s)", shortName)
        } else {
            txtEditExtraInfo.text = ""
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
        UIHelper.showToastShort(this,getString(R.string.msg_edit_sent))
        makeCreateChangeSetRequest(OsmXmlBuilder.createChangeSetXml(commentText))
//        makeGetElementVersionRequest(element.id.toString(),element.type.toString())
    }

    private fun makeCreateChangeSetRequest(payload: String){
        CreateChangeSetTask(this).execute(payload)
    }

    private fun makeUploadChangeSetRequest(payload: String, changesetId: String){
        UploadChangeSetTask(this).execute(payload,changesetId)
    }

    private fun makeCloseChangeSetRequest(changesetId: String){
        CloseChangeSetTask(this).execute(changesetId)
    }

    private fun makeGetElementVersionRequest(elementId: String, elementType: String): String? {
        return GetElementVersionTask(this).execute(elementId, elementType).get()


    }

    override fun onChangeSetCreated(changesetId: String) {
        if(changesetId == Constants.ErrorCodes.ERROR_CREATE_CHANGESET){
            UIHelper.showToastShort(this,"Erro ao criar o changeset")
        }else{
            val elementVersion = makeGetElementVersionRequest(element.id.toString(), element.type.toString())
            makeUploadChangeSetRequest(OsmXmlBuilder.uploadChangeSetXml(element, changesetId,elementVersion),changesetId)
        }
    }

    override fun onChangesetUploaded(changesetId: String) {
        makeCloseChangeSetRequest(changesetId)
    }
}