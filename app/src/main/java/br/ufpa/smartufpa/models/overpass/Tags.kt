package br.ufpa.smartufpa.models.overpass

import android.os.Parcel
import android.os.Parcelable
import br.ufpa.smartufpa.R
import com.google.gson.annotations.SerializedName
import br.ufpa.smartufpa.utils.Constants.ElementTags.*
class Tags() : Parcelable {

    @SerializedName(TAG_NAME)
    var name: String? = null

    @SerializedName(TAG_SHORT_NAME)
    var shortName: String? = null

    @SerializedName(TAG_LOC_NAME)
    var locName: String? = null

    @SerializedName(TAG_SHOP)
    var shop: String? = null

    @SerializedName(TAG_TOILETS)
    val toilets: String? = null

    //Amenity reúne os tipos: food_court, restaurant, library, toilets, exhibition centre
    @SerializedName(TAG_AMENITY)
    var amenity: String? = null

    @SerializedName(TAG_DESCRIPTION)
    var description: String? = null

    @SerializedName(TAG_WEBSITE)
    var website: String? = null

    @SerializedName(TAG_BUILDING)
    var building: String? = null

    @SerializedName(TAG_INDOOR)
    var indoor: String? = null

    var iconResourceID : Int? = null
        get() {
        if((toilets != null && toilets.equals("yes")) or amenity?.equals("toilets")!!){
            return R.drawable.ic_restroom
        }
        return null
    }

    fun getTags(): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        if(name != null) hashMap[TAG_NAME] = this.name!!

        if (shortName != null) hashMap[TAG_SHORT_NAME] = this.shortName!!

        if (locName != null) hashMap[TAG_LOC_NAME] = this.locName!!

        if (shop != null) hashMap[TAG_SHOP] = this.shop!!

        if (toilets != null) hashMap[TAG_TOILETS] = this.toilets

        if (amenity != null) hashMap[TAG_AMENITY] = this.amenity!!

        if (description != null) hashMap[TAG_DESCRIPTION] = this.description!!

        if (website != null) hashMap[TAG_WEBSITE] = this.website!!

        if (building != null ) hashMap[TAG_BUILDING] = this.building!!

        if (indoor != null) hashMap[TAG_INDOOR] = this.indoor!!

        return hashMap

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
