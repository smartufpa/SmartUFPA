package br.ufpa.smartufpa.fragments.forms

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RatingBar
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.utils.FormObject
import kotlinx.android.synthetic.main.fragment_food_place_form.view.*


class FormFoodPlace : Fragment() {
    private lateinit var form: View
    private val formObject : FormObject = FormObject

    companion object {
        @JvmStatic
        fun newInstance() = FormFoodPlace()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        form = inflater.inflate(R.layout.fragment_food_place_form, container, false)
        bindRatingPrice()
        bindRatingQuality()
        bindCategory()


        return form
    }

    private fun bindRatingQuality(){
        form.rbQuality.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, ratingValue, _ ->
            formObject.ratingQuality = ratingValue
        }
    }

    private fun bindRatingPrice(){
        form.rbPrice.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, ratingValue, _ ->
            formObject.ratingPrice - ratingValue
        }
    }

    private fun bindCategory(){
        form.spFoodPlaceCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                formObject.foodCategory = parent?.getChildAt(position).toString()
            }

        }
    }


}