package br.ufpa.smartufpa.utils

import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View

object FormObject {
    var name : String? =  null
    var description: String? = null
    var indoor: Boolean = false

    // Food
    var ratingQuality: Float = 1F
    var ratingPrice: Int = 1
    var foodCategory : String? = null

    // Extra Info
    var website: String? = null
    var localName: String? = null
    var shortName: String? = null
}