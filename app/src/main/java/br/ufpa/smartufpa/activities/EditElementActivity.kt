package br.ufpa.smartufpa.activities

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.fragments.ElementBasicDataForm
import br.ufpa.smartufpa.fragments.FoodPlaceForm
import br.ufpa.smartufpa.fragments.PlaceDetailsFragment
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.ElementParser
import kotlinx.android.synthetic.main.activity_edit_element.*



class EditElementActivity : AppCompatActivity() {
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
            // TODO: enviar o element para o servidor?

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
        ft.add(R.id.containerEditForm,elementBasicDataForm, elementBasicDataForm.tag)
                .commit()
    }
}