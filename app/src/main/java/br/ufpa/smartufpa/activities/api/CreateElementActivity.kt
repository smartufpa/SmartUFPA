package br.ufpa.smartufpa.activities.api

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.adapters.CreateElementTabsAdapter
import br.ufpa.smartufpa.dialogs.CommentDialog
import br.ufpa.smartufpa.interfaces.CloseChangeSetListener
import br.ufpa.smartufpa.interfaces.CreateChangeSetListener
import br.ufpa.smartufpa.interfaces.UploadChangeSetListener
import br.ufpa.smartufpa.models.PlaceCategory
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.FormObject
import br.ufpa.smartufpa.utils.UIHelper
import br.ufpa.smartufpa.utils.enums.FormFlag
import br.ufpa.smartufpa.utils.osm.OsmUploadHelper
import br.ufpa.smartufpa.utils.osm.OsmXmlBuilder
import kotlinx.android.synthetic.main.activity_create_element.*

class CreateElementActivity : AppCompatActivity(),  CommentDialog.CommentDelegate,
        CreateChangeSetListener, UploadChangeSetListener, CloseChangeSetListener {


    private var tabsAdapter: CreateElementTabsAdapter? = null
    private val element : Element = Element()
    private val osmUploadHelper: OsmUploadHelper = OsmUploadHelper(this)
    private val formObject : FormObject = FormObject
    private lateinit var category : String
    private lateinit var categoryName : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_element)
        category = intent.getStringExtra(old_CreateElementActivity.ARG_CATEGORY)
        categoryName = intent.getStringExtra(old_CreateElementActivity.ARG_CATEGORY_NAME)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = categoryName
        tabsAdapter = CreateElementTabsAdapter(supportFragmentManager, category)

        // Set up the ViewPager with the sections adapter.
        container.adapter = tabsAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))


        fabSend.setOnClickListener {
            bindFormToElement()
            openCommentDialog()
        }

    }

    private fun bindFormToElement(){
        element.lat = intent.getDoubleExtra(old_CreateElementActivity.ARG_LATITUDE, 0.0)
        element.lon = intent.getDoubleExtra(old_CreateElementActivity.ARG_LONGITUDE, 0.0)
        var amenity : String? = null
        when(category){
            PlaceCategory.Categories.FOODPLACE.toString() -> {
                val foodCategory = formObject.foodCategory
                if(foodCategory != null){
                     amenity = if(foodCategory.toLowerCase() == "restaurante") "restaurant" else "fast_food"
                 }
            }
        }

        if(amenity != null) element.setAmenity(amenity)

        // Basic Data
        if(formObject.name != null) element.setName(formObject.name)
        if(formObject.description != null) element.setDescription(formObject.description)
        element.setIndoor(formObject.indoor)

        // FoodPlace

        // ExtraInfo
        if(formObject.localName != null) element.setLocalName(formObject.localName)
        if(formObject.shortName != null) element.setShortName(formObject.shortName)
        if(formObject.website != null) element.setWebSite(formObject.website)

    }

    private fun openCommentDialog() {
        val commentDialog = CommentDialog()
        commentDialog.show(supportFragmentManager, CommentDialog.DIALOG_TAG)
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
        val payload = OsmXmlBuilder.uploadChangeSetXml(element, changesetId, element.version.toString(), FormFlag.CREATE)
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
