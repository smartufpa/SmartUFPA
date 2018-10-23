package br.ufpa.smartufpa.models.overpass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OverpassModel {

    @SerializedName("elements")
    val elements: List<Element>? = null

    val isEmpty: Boolean
        get() = elements!!.isEmpty()


}
