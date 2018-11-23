package br.ufpa.smartufpa.fragments.forms

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.activities.api.CreateElementActivity
import br.ufpa.smartufpa.utils.FormObject
import kotlinx.android.synthetic.main.fragment_form_extra_info.view.*


class FormExtraInfo : Fragment() {
    private lateinit var form: View
    private val formObject : FormObject = FormObject

    companion object {
        @JvmStatic
        fun newInstance() = FormExtraInfo()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        form = inflater.inflate(R.layout.fragment_form_extra_info, container, false)
        bindLocalName()
        bindShortName()
        bindWebSite()
        return form
    }

    private fun bindLocalName() {
        form.inputLocalName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                    formObject.localName = text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun bindShortName(){
        form.inputShortName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                formObject.shortName = text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun bindWebSite(){
        form.inputWebsite.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                formObject.website = text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

}
