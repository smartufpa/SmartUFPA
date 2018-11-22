package br.ufpa.smartufpa.fragments.forms

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.enums.FormFlag
import br.ufpa.smartufpa.utils.osm.ElementParser
import kotlinx.android.synthetic.main.fragment_form_basic_data.view.*
import kotlinx.android.synthetic.main.fragment_form_extra_info.view.*

private const val ARG_ELEMENT = "element"

class FormBasicData : Fragment() {

    private var element: Element? = null
    private val elementParser: ElementParser = ElementParser
    private lateinit var form: View

    companion object {
        val LOG_TAG = FormBasicData::class.simpleName

        @JvmStatic
        fun newInstance(element: Element?) = FormBasicData().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ELEMENT, element)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            element = it.getParcelable(ARG_ELEMENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        form = inflater.inflate(R.layout.fragment_form_basic_data, container, false)
        if (element != null) {
            initFormName()
            initFormShortName()
            initFormLocalName()
            initFormDescription()
            initFormWebsite()
        }

        return form
    }

    fun setElementData(formFlag: FormFlag) : Element? {
        when (formFlag) {
            FormFlag.CREATE -> {
                val newElement = Element()
                updateElementFromForm(newElement)
                return newElement
            }

            FormFlag.EDIT -> {
                updateElementFromForm(element!!)
            }
        }
        return element
    }

    private fun updateElementFromForm(element: Element) {
        with(element) {
            this.setName(getFormName())
            this.setShortName(getFormShortName())
            this.setDescription(getFormDescription())
            this.setLocalName(getFormLocalName())
            this.setWebSite(getFormWebsite())
        }
    }

    private fun initFormWebsite() {
        try {
//            form.inputWebsite.setText(element!!.getWebsite())
        } catch (e: KotlinNullPointerException) {
            Log.e(LOG_TAG, "Element Website is null", e)
        }

    }

    private fun initFormDescription() {
        try {
            form.inputDescription.setText(element!!.getDescription())
        } catch (e: KotlinNullPointerException) {
            Log.e(LOG_TAG, "Element Description is null", e)
        }
    }

    private fun initFormLocalName() {
        try {
//            form.inputLocalName.setText(element!!.getLocalName())
        } catch (e: KotlinNullPointerException) {
            Log.e(LOG_TAG, "Element LocalName is null", e)
        }
    }

    private fun initFormName() {
        try {
            form.inputName.setText(element!!.getName())
        } catch (e: KotlinNullPointerException) {
            Log.e(LOG_TAG, "Element Name is null", e)
        }
    }

    private fun initFormShortName() {
        try {
//            form.inputShortName.setText(element!!.getShortName())
        } catch (e: KotlinNullPointerException) {
            Log.e(LOG_TAG, "Element ShortName is null", e)
        }
    }

    private fun getFormWebsite(): String {
        return form.inputLocalName.text.toString()
    }

    private fun getFormLocalName(): String {
        return form.inputLocalName.text.toString()
    }

    private fun getFormDescription(): String {
        return form.inputDescription.text.toString()
    }

    private fun getFormShortName(): String {
        return form.inputShortName.text.toString()
    }

    private fun getFormName(): String {
        return form.inputName.text.toString()
    }


}
