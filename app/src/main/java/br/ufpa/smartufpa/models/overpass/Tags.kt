package br.ufpa.smartufpa.models.overpass

import android.os.Parcel
import android.os.Parcelable
import br.ufpa.smartufpa.R
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Tags() : Parcelable {

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
    var website: String? = null

    @SerializedName("building")
    var building: String? = null

    @SerializedName("indoor")
    var indoor: String? = null

    var iconResourceID : Int? = null
        get() {
        if((toilets != null && toilets.equals("yes")) or amenity?.equals("toilets")!!){
            return R.drawable.ic_restroom
        }
        return null
    }


    @SerializedName("opening_hours")
    var openingHours: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        shortName = parcel.readString()
        locName = parcel.readString()
        shop = parcel.readString()
        amenity = parcel.readString()
        description = parcel.readString()
        website = parcel.readString()
        building = parcel.readString()
        indoor = parcel.readString()
        openingHours = parcel.readString()
    }

    override fun toString(): String {
        return "[name=$name,short_name=$shortName,shop=$shop,amenity=$amenity,loc_name=$locName,description=$description]"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(shortName)
        parcel.writeString(locName)
        parcel.writeString(shop)
        parcel.writeString(amenity)
        parcel.writeString(description)
        parcel.writeString(website)
        parcel.writeString(building)
        parcel.writeString(indoor)
        parcel.writeString(openingHours)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tags> {
        override fun createFromParcel(parcel: Parcel): Tags {
            return Tags(parcel)
        }

        override fun newArray(size: Int): Array<Tags?> {
            return arrayOfNulls(size)
        }
    }


}
