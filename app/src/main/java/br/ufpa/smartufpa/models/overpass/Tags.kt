package br.ufpa.smartufpa.models.overpass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tags {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("short_name")
    var shortName: String? = null

    @SerializedName("loc_name")
    var locName: String? = null

    @SerializedName("shop")
    var shop: String? = null


    @SerializedName("toilets")
    val toilets: String? = null

    //Amenity reúne os tipos: food_court, restaurant, library, toilets, exhibition centre
    @SerializedName("amenity")
    var amenity: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("website")
    val website: String? = null

    @SerializedName("building")
    val building: String? = null

    @SerializedName("indoor")
    val indoor: String? = null

    @SerializedName("opening_hours")
    val openingHours: String? = null





    override fun toString(): String {
        return "[name=$name,short_name=$shortName,shop=$shop,amenity=$amenity,loc_name=$locName,description=$description]"
    }
}
