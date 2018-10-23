package br.ufpa.smartufpa.models.overpass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Center {

    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lon")
    var lon: Double? = null


}
