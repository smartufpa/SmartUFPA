package br.ufpa.smartufpa.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.osm.ElementParser
import kotlinx.android.synthetic.main.fragment_element_basic_data_form.view.*

private const val ARG_ELEMENT = "element"

class ElementBasicDataForm : Fragment() {

    private lateinit var element: Element
    private val elementParser: ElementParser = ElementParser
    private lateinit var form: View

    companion object {
        @JvmStatic
        fun newInstance(element: Element) = ElementBasicDataForm().apply {
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
        form = inflater.inflate(R.layout.fragment_element_basic_data_form, container, false)

        initFormName()
        initFormShortName()
        initFormLocalName()
        initFormDescription()
        initFormWebsite()

        return form
    }

    fun updateElementData() {
        // TODO: verificar necessidade do element parser
        elementParser.let {
            with(element) {
                it.setName(this, getFormName())
                it.setShortName(this, getFormShortName())
                it.setDescription(this, getFormDescription())
                it.setLocalName(this, getFormLocalName())
                it.setWebSite(this, getFormWebsite())
            }
        }

    }

    private fun initFormWebsite() {
        form.inputWebsite.setText(elementParser.getWebsite(element))
    }

    private fun initFormDescription() {
        form.inputDescription.setText(elementParser.getDescription(element))
    }

    private fun initFormLocalName() {
        form.inputLocalName.setText(elementParser.getLocalName(element))
    }
    private fun initFormName() {
        form.inputName.setText(elementParser.getName(element))
    }

    private fun initFormShortName() {
        form.inputShortName.setText(elementParser.getShortName(element))
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
