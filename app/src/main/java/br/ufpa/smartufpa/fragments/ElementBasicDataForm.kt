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
        return form
    }

    fun updateElementData() {
        elementParser.let {
            with(element) {
                it.setName(this, form.input_name.text.toString())
                it.setShortName(this, form.inputShortName.text.toString())
                it.setDescription(this, form.input_description.text.toString())
                it.setLocalName(this, form.inputLocalName.text.toString())
                it.setWebSite(this, form.inputLocalName.text.toString())
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(element: Element) = ElementBasicDataForm().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ELEMENT, element)
            }
        }
    }
}
