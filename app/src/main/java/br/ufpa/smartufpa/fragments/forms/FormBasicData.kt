package br.ufpa.smartufpa.fragments.forms

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.FormObject
import br.ufpa.smartufpa.utils.enums.FormFlag
import br.ufpa.smartufpa.utils.osm.ElementParser
import kotlinx.android.synthetic.main.fragment_form_basic_data.view.*
import kotlinx.android.synthetic.main.fragment_form_extra_info.view.*

private const val ARG_ELEMENT = "element"

class FormBasicData : Fragment() {

    private var element: Element? = null
    private lateinit var form: View

    private val formObject : FormObject = FormObject

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
        bindInputName()
        bindInputDescription()
        bindInputIndoor()

//        if (element != null) {
//            initFormName()
//            initFormDescription()
//        }

        return form
    }

    private fun bindInputName() {
        form.inputName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                formObject.name = editable.toString()
            }
        })
    }

    private fun bindInputDescription() {
        form.inputDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                formObject.description = editable?.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun bindInputIndoor(){
        form.cbIndoor.setOnCheckedChangeListener { _, isChecked ->
            formObject.indoor = isChecked
        }
    }

//    fun setElementData(formFlag: FormFlag) : Element? {
//        when (formFlag) {
//            FormFlag.CREATE -> {
//                val newElement = Element()
//                updateElementFromForm(newElement)
//                return newElement
//            }
//
//            FormFlag.EDIT -> {
//                updateElementFromForm(element!!)
//            }
//        }
//        return element
//    }
//
//    private fun updateElementFromForm(element: Element) {
//        with(element) {
//            this.setName(formObject.name)
//            this.setDescription(formObject.description)
//            this.setIndoor(formObject.indoor)
//        }
//    }
//
//    private fun initFormDescription() {
//        try {
//            form.inputDescription.setText(element!!.getDescription())
//        } catch (e: KotlinNullPointerException) {
//            Log.e(LOG_TAG, "Element Description is null", e)
//        }
//    }
//
//    private fun initFormName() {
//        try {
//            form.inputName.setText(element!!.getName())
//        } catch (e: KotlinNullPointerException) {
//            Log.e(LOG_TAG, "Element Name is null", e)
//        }
//    }




}
