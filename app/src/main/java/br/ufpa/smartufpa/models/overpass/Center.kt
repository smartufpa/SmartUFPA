package br.ufpa.smartufpa.models.overpass

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Center() : Parcelable {

    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lon")
    var lon: Double? = null

    constructor(parcel: Parcel) : this() {
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        lon = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(lat)
        parcel.writeValue(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Center> {
        override fun createFromParcel(parcel: Parcel): Center {
            return Center(parcel)
        }

        override fun newArray(size: Int): Array<Center?> {
            return arrayOfNulls(size)
        }
    }


}
