package br.ufpa.smartufpa.models.smartufpa

import android.os.Parcel
import android.os.Parcelable
import org.osmdroid.util.GeoPoint

open class POI(var latitude: Double,
               var longitude: Double,
               var name: String?,
               var shortName: String? = "",
               var localName: String? = "",
               var description: String? = "") : Parcelable {

    val geoPoint: GeoPoint
        get() {
            return GeoPoint(latitude, longitude)
        }

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(name)
        parcel.writeString(shortName)
        parcel.writeString(localName)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<POI> {
        override fun createFromParcel(parcel: Parcel): POI {
            return POI(parcel)
        }

        override fun newArray(size: Int): Array<POI?> {
            return arrayOfNulls(size)
        }
    }
}