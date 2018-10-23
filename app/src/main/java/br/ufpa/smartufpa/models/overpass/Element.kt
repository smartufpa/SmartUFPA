package br.ufpa.smartufpa.models.overpass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Element {

    @SerializedName("type")
    val type: String? = null

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("lat")
    val lat: Double? = null

    @SerializedName("lon")
    val lon: Double? = null

    @SerializedName("tags")
    val tags: Tags? = null

    @SerializedName("center")
    var center: Center? = null



}
