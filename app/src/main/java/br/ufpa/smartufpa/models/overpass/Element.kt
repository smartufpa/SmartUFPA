package br.ufpa.smartufpa.models.overpass

import android.os.Parcel
import android.os.Parcelable
import br.ufpa.smartufpa.R
import com.google.gson.annotations.SerializedName

class Element() : Parcelable{

    @SerializedName("type")
    var type: String? = null

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lon")
    var lon: Double? = null

    @SerializedName("tags")
    var tags: Tags? = null

    @SerializedName("center")
    var center: Center? = null

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()
        id = parcel.readLong()
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        lon = parcel.readValue(Double::class.java.classLoader) as? Double
        tags = parcel.readParcelable(Tags::class.java.classLoader)
        center = parcel.readParcelable(Center::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeLong(id)
        parcel.writeValue(lat)
        parcel.writeValue(lon)
        parcel.writeParcelable(tags, flags)
        parcel.writeParcelable(center, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }


}
