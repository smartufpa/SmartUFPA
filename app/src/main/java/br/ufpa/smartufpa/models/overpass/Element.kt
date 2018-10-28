package br.ufpa.smartufpa.models.overpass

import android.os.Parcel
import android.os.Parcelable
import br.ufpa.smartufpa.models.smartufpa.POI
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Element() : Parcelable{

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

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
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
